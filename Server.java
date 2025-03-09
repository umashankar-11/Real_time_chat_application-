public import java.io.*;
import java.net.*;

abstract class ClientHandler {
    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void processRequest();

    public void sendMessage(String message) {
        out.println(message);
    }
}

class TextClientHandler extends ClientHandler {

    public TextClientHandler(Socket socket) {
        super(socket);
    }

    @Override
    public void processRequest() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                sendMessage("Message received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileClientHandler extends ClientHandler {

    public FileClientHandler(Socket socket) {
        super(socket);
    }

    @Override
    public void processRequest() {
        try {
            String fileName = in.readLine();
            File file = new File(fileName);
            if (file.exists()) {
                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    sendMessage(line);
                }
                fileReader.close();
            } else {
                sendMessage("File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Server {
    private static final int PORT = 12345;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler handler;
                String requestType = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();

                if ("text".equalsIgnoreCase(requestType)) {
                    handler = new TextClientHandler(clientSocket);
                } else {
                    handler = new FileClientHandler(clientSocket);
                }

                new Thread(() -> handler.processRequest()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Socket socket;
    private static BufferedReader userInput;
    private static PrintWriter out;

    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            userInput = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Enter request type (text/file): ");
            String requestType = userInput.readLine();
            out.println(requestType);

            if ("text".equalsIgnoreCase(requestType)) {
                System.out.println("Enter your message: ");
                String message = userInput.readLine();
                out.println(message);
            } else if ("file".equalsIgnoreCase(requestType)) {
                System.out.println("Enter the file name: ");
                String fileName = userInput.readLine();
                out.println(fileName);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileClient extends Client {
    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            userInput = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("file");

            System.out.println("Enter the file name: ");
            String fileName = userInput.readLine();
            out.println(fileName);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TextClient extends Client {
    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            userInput = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("text");

            System.out.println("Enter your message: ");
            String message = userInput.readLine();
            out.println(message);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    

