import java.io.*;
import java.net.*;
import java.util.*;

public class ChatApp {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")) {
           
            new Server().start();
        } else if (args.length > 0 && args[0].equals("client")) {
            
            new Client().start();
        } else {
            System.out.println("Usage: java ChatApp server|client");
        }
    }

  
    static class Server {
        private static final int PORT = 12345;
        private static Set<PrintWriter> clientWriters = new HashSet<>();

        public void start() {
            System.out.println("Chat server started...");

            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    new ClientHandler(serverSocket.accept()).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static class ClientHandler extends Thread {
            private Socket socket;
            private PrintWriter out;
            private BufferedReader in;

            public ClientHandler(Socket socket) {
                this.socket = socket;
            }

            public void run() {
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    synchronized (clientWriters) {
                        clientWriters.add(out);
                    }

                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received: " + message);
                        broadcast(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    synchronized (clientWriters) {
                        clientWriters.remove(out);
                    }
                }
            }

            private void broadcast(String message) {
                synchronized (clientWriters) {
                    for (PrintWriter writer : clientWriters) {
                        writer.println(message);
                    }
                }
            }
        }
    }

  
    static class Client {
        private static final String SERVER_ADDRESS = "localhost";
        private static final int SERVER_PORT = 12345;
        private static Scanner scanner = new Scanner(System.in);
        private static PrintWriter out;
        private static BufferedReader in;

        public void start() {
            try {
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                System.out.println("Connected to the chat server.");
                System.out.print("Enter your name: ");
                String username = scanner.nextLine();

               
                Thread readThread = new Thread(new ReadMessages());
                readThread.start();

        
                String message;
                while (true) {
                    message = scanner.nextLine();
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    out.println(username + ": " + message);
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static class ReadMessages implements Runnable {
            @Override
            public void run() {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
