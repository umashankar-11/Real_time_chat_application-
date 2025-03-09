public import java.io.*;
import java.net.*;
import java.util.*;

abstract class User {
    protected String username;
    protected BufferedReader in;
    protected PrintWriter out;

    public User(String username, BufferedReader in, PrintWriter out) {
        this.username = username;
        this.in = in;
        this.out = out;
    }

    public abstract void processRequest();

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return username;
    }
}

class ChatUser extends User {

    public ChatUser(String username, BufferedReader in, PrintWriter out) {
        super(username, in, out);
    }

    @Override
    public void processRequest() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(username + ": " + message);
                ChatServer.broadcast(username + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class AdminUser extends User {

    public AdminUser(String username, BufferedReader in, PrintWriter out) {
        super(username, in, out);
    }

    @Override
    public void processRequest() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Admin " + username + ": " + message);
                ChatServer.broadcast("Admin " + username + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kickUser(String targetUsername) {
        ChatServer.kickUser(targetUsername);
        sendMessage("User " + targetUsername + " has been kicked out.");
    }
}

public class ChatServer {
    private static final int PORT = 12345;
    private static ServerSocket serverSocket;
    private static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        for (User user : users.values()) {
            user.sendMessage(message);
        }
    }

    public static void kickUser(String username) {
        users.remove(username);
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                out.println("Enter username: ");
                username = in.readLine();
                if (users.containsKey(username)) {
                    out.println("Username already taken. Please try again.");
                    socket.close();
                    return;
                }

                out.println("Enter password: ");
                String password = in.readLine();
                // Simulating password validation
                if (validateLogin(username, password)) {
                    User user;
                    if (username.equalsIgnoreCase("admin")) {
                        user = new AdminUser(username, in, out);
                    } else {
                        user = new ChatUser(username, in, out);
                    }
                    users.put(username, user);
                    user.sendMessage("Welcome " + username + "!");
                    user.processRequest();
                } else {
                    out.println("Invalid login credentials.");
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean validateLogin(String username, String password) {
            // For simplicity, assuming the following credentials:
            return (username.equals("admin") && password.equals("adminpass")) ||
                    (!username.equals("admin") && password.equals("userpass"));
        }
    }
}

class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static BufferedReader userInput;

    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readLine());  // Prompt for username
            String username = userInput.readLine();
            out.println(username);

            System.out.println(in.readLine());  // Prompt for password
            String password = userInput.readLine();
            out.println(password);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("Invalid login credentials.")) {
                    break;
                }
                startChat();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startChat() {
        try {
            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class AdminClient extends ChatClient {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader userInput;

    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readLine());
            String username = userInput.readLine();
            out.println(username);

            System.out.println(in.readLine());
            String password = userInput.readLine();
            out.println(password);

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.equals("Invalid login credentials.")) {
                    break;
                }
                startAdminChat();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startAdminChat() {
        try {
            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);
                if (message.equalsIgnoreCase("kick")) {
                    System.out.println("Enter username to kick: ");
                    String targetUser = userInput.readLine();
                    out.println("kick " + targetUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
    

