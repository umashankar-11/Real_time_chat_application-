public import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;

public class VideoChatServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void broadcastImage(BufferedImage image) {
        for (ClientHandler client : clients) {
            client.sendImage(image);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private ObjectOutputStream objectOut;
        private ObjectInputStream objectIn;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    VideoChatServer.broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void sendImage(BufferedImage image) {
            try {
                objectOut.writeObject(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class VideoChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static JFrame frame;
    private static JLabel videoLabel;
    private static PrintWriter out;
    private static BufferedReader in;
    private static ObjectInputStream objectIn;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectIn = new ObjectInputStream(socket.getInputStream());

            frame = new JFrame("Video Chat");
            videoLabel = new JLabel();
            frame.add(videoLabel);
            frame.setSize(640, 480);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            new Thread(new IncomingMessageListener(in)).start();
            new Thread(new IncomingImageListener(objectIn)).start();

            System.out.println("Connected to chat server. Type your messages...");

            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class IncomingMessageListener implements Runnable {
        private BufferedReader in;

        public IncomingMessageListener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class IncomingImageListener implements Runnable {
        private ObjectInputStream objectIn;

        public IncomingImageListener(ObjectInputStream objectIn) {
            this.objectIn = objectIn;
        }

        @Override
        public void run() {
            BufferedImage image;
            try {
                while ((image = (BufferedImage) objectIn.readObject()) != null) {
                    ImageIcon icon = new ImageIcon(image);
                    videoLabel.setIcon(icon);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendImage(BufferedImage image) {
        try {
            objectIn.writeObject(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;

public class VideoCaptureClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());

            JFrame frame = new JFrame("Video Capture");
            frame.setSize(640, 480);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            JPanel panel = new JPanel();
            frame.add(panel);

            while (true) {
                BufferedImage image = captureImage();
                objectOut.writeObject(image);
                panel.getGraphics().drawImage(image, 0, 0, null);
                Thread.sleep(100); // Simulating frame rate
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage captureImage() throws IOException {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(screenRect);
    }
}

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class VideoCaptureServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastImage(BufferedImage image) {
        for (ClientHandler client : clients) {
            client.sendImage(image);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                objectIn = new ObjectInputStream(socket.getInputStream());
                objectOut = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            BufferedImage image;
            try {
                while ((image = (BufferedImage) objectIn.readObject()) != null) {
                    VideoCaptureServer.broadcastImage(image);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendImage(BufferedImage image) {
            try {
                objectOut.writeObject(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

    



