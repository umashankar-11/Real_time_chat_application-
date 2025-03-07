import com.google.cloud.translate.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.logging.log4j.*;
import javax.sound.sampled.*;
import java.nio.charset.StandardCharsets;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Map<String, ClientHandler> clients = new HashMap<>();
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final Map<String, String> userCredentials = new HashMap<>();
    private static final Translate translate = TranslateOptions.getDefaultInstance().getService();
    private static final Logger logger = LogManager.getLogger(ChatServer.class);

    
    private static final String SECRET_KEY = "1234567890123456";
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) {
        
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");
        userCredentials.put("admin", "admin123");

        logger.info("Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            logger.error("Error starting server: ", e);
        }
    }

    
    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients.values()) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
        storeMessage(message); 
        storeMessageToDatabase(message); 
    }

    
    public static void broadcastAudio(File audioFile, ClientHandler sender) {
        for (ClientHandler client : clients.values()) {
            if (client != sender) {
                client.sendAudio(audioFile);
            }
        }
        storeMessage("Audio message sent."); 
        storeMessageToDatabase("Audio message sent."); 
    }

    
    public static String translateMessage(String message, String targetLanguage) {
        Translation translation = translate.translate(
            message,
            TranslateOption.targetLanguage(targetLanguage)
        );
        return translation.getTranslatedText();
    }

    
    public static void storeMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("chatHistory.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error writing message to file: ", e);
        }
    }

    
    public static void storeMessageToDatabase(String message) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "root")) {
            String sql = "INSERT INTO chat_history (message) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, message);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error storing message in database: ", e);
        }
    }

    
    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler.getUsername());
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private boolean isAuthenticated = false;
        private String status = "Online"; 
        private boolean isEncrypted = false; 

        private TargetDataLine targetDataLine;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                logger.error("Error setting up streams for client: ", e);
            }
        }

        @Override
        public void run() {
            try {
                
                while (!isAuthenticated) {
                    out.println("Enter username: ");
                    String usernameInput = in.readLine();
                    out.println("Enter password: ");
                    String passwordInput = in.readLine();

                    if (userCredentials.containsKey(usernameInput) && userCredentials.get(usernameInput).equals(passwordInput)) {
                        username = usernameInput;
                        isAuthenticated = true;
                        out.println("Authentication successful. Welcome " + username + "!");
                        clients.put(username, this);
                        broadcastMessage(username + " has joined the chat.", this);
                        logger.info(username + " has joined the chat.");
                        break;
                    } else {
                        out.println("Invalid credentials. Try again.");
                        logger.warn("Failed login attempt for username: " + usernameInput);
                    }
                }

                
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        break;
                    } else if (line.startsWith("/translate ")) {
                        String[] parts = line.split(" ", 3);
                        if (parts.length == 3) {
                            String translatedMessage = translateMessage(parts[2], parts[1]);
                            broadcastMessage(username + " (translated): " + translatedMessage, this);
                        } else {
                            sendMessage("Usage: /translate [target language] [message]");
                        }
                    } else if (line.startsWith("/private ")) {
                        String[] parts = line.split(" ", 3);
                        if (parts.length == 3) {
                            String recipientUsername = parts[1];
                            String privateMessage = parts[2];
                            sendPrivateMessage(privateMessage, recipientUsername);
                        } else {
                            sendMessage("Usage: /private [username] [message]");
                        }
                    } else if (line.startsWith("/status ")) {
                        status = line.split(" ")[1];
                        sendMessage("Status updated to: " + status);
                        logger.info(username + " updated status to: " + status);
                    } else if (line.startsWith("/encrypt ")) {
                        isEncrypted = !isEncrypted; 
                        sendMessage("Encryption toggled: " + (isEncrypted ? "Enabled" : "Disabled"));
                    } else if (line.startsWith("/audio")) {
                        
                        startAudioRecording();
                    } else {
                        String message = "[" + new Date() + "] " + username + " (" + status + "): " + line;
                        if (isEncrypted) {
                            message = encryptMessage(message);
                        }
                        broadcastMessage(message, this);
                    }
                }
            } catch (IOException e) {
                logger.error("Error in client communication: ", e);
            } finally {
                try {
                    socket.close();
                    removeClient(this);
                    broadcastMessage(username + " has left the chat.", this);
                    logger.info(username + " has left the chat.");
                } catch (IOException e) {
                    logger.error("Error closing socket: ", e);
                }
            }
        }

        
        private void startAudioRecording() {
            try {
                AudioFormat format = new AudioFormat(16000, 8, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                if (!AudioSystem.isLineSupported(info)) {
                    sendMessage("Audio recording not supported on this machine.");
                    return;
                }

                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                targetDataLine.open(format);
                targetDataLine.start();

                File audioFile = new File(username + "_audio.wav");
                AudioInputStream audioStream = new AudioInputStream(targetDataLine);
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);

                
                broadcastAudio(audioFile, this);
                logger.info("Audio message sent by " + username);

               
                performSoundAnalysis(audioFile);

               
                performVoiceRecognition(audioFile);

                
                performVoiceChangeover(audioFile);

            } catch (LineUnavailableException | IOException e) {
                logger.error("Error recording or broadcasting audio: ", e);
            }
        }

       
        private void performSoundAnalysis(File audioFile) {
                       try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioInputStream.getFormat();
                byte[] audioBytes = new byte[(int) audioFile.length()];
                audioInputStream.read(audioBytes);

               

                String detectedKeyword = analyzeAudioForKeywords(audioBytes);
                if (detectedKeyword != null) {
                    sendMessage("Detected keyword in audio: " + detectedKeyword);
                    logger.info("Detected keyword in audio: " + detectedKeyword);
                }

            } catch (UnsupportedAudioFileException | IOException e) {
                logger.error("Error performing sound analysis: ", e);
            }
        }

        
        private String analyzeAudioForKeywords(byte[] audioData) {
            
            String[] keywords = {"hello", "stop", "start", "bye"};

            
            for (String keyword : keywords) {
                if (Arrays.toString(audioData).contains(keyword)) {
                    return keyword;
                }
            }

            return null; 
        }

        
        private void performVoiceRecognition(File audioFile) {
            try {
                SpeechClient speechClient = SpeechClient.create();
                ByteString audioBytes = ByteString.readFrom(new FileInputStream(audioFile));
                RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                        .setContent(audioBytes)
                        .build();
                RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(16000)
                        .setLanguageCode("en-US")
                        .build();

                RecognizeRequest request = RecognizeRequest.newBuilder()
                        .setConfig(recognitionConfig)
                        .setAudio(recognitionAudio)
                        .build();

                RecognizeResponse response = speechClient.recognize(request);
                List<SpeechRecognitionResult> results = response.getResultsList();
                for (SpeechRecognitionResult result : results) {
                    String transcript = result.getAlternativesList().get(0).getTranscript();
                    sendMessage("Recognized text: " + transcript);
                    logger.info("Recognized text: " + transcript);
                }
            } catch (Exception e) {
                logger.error("Error performing voice recognition: ", e);
            }
        }

        
        private void performVoiceChangeover(File audioFile) {
            
            try {
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = inputStream.getFormat();
                byte[] audioData = inputStream.readAllBytes();

                
                for (int i = 0; i < audioData.length; i++) {
                    audioData[i] = (byte) (audioData[i] * 0.8);
                }

                File modifiedFile = new File("modified_" + audioFile.getName());
                try (FileOutputStream fos = new FileOutputStream(modifiedFile)) {
                    fos.write(audioData);
                    broadcastAudio(modifiedFile, this);
                    logger.info("Modified voice sent with changeover effect.");
                }
            } catch (Exception e) {
                logger.error("Error performing voice changeover: ", e);
            }
        }

        
        private String encryptMessage(String message) {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedMessage = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
                return new String(encryptedMessage, StandardCharsets.ISO_8859_1);
            } catch (Exception e) {
                logger.error("Error encrypting message: ", e);
                return message;
            }
        }

        
        private void sendPrivateMessage(String message, String recipientUsername) {
            ClientHandler recipient = clients.get(recipientUsername);
            if (recipient != null) {
                recipient.sendMessage("[Private from " + username + "]: " + message);
            } else {
                sendMessage("User " + recipientUsername + " not found.");
            }
        }

       
        public void sendMessage(String message) {
            out.println(message);
        }

        public void sendAudio(File audioFile) {
            try {
                
                byte[] byteArray = new byte[(int) audioFile.length()];
                FileInputStream fis = new FileInputStream(audioFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(byteArray, 0, byteArray.length);
                OutputStream os = socket.getOutputStream();
                os.write(byteArray, 0, byteArray.length);
                os.flush();
            } catch (IOException e) {
                logger.error("Error sending audio file: ", e);
            }
        }

        public String getUsername() {
            return username;
        }
    }

    
    public static class DatabaseHelper {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/chat_db";
        private static final String USER = "root";
        private static final String PASSWORD = "roo";
        public static void storeMessageToDatabase(String message) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                String sql = "INSERT INTO chat_history (message) VALUES (?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, message);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                logger.error("Error storing message in database: ", e);
            }
        }
    }
}
