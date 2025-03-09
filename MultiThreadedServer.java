public import java.io.*;
import java.net.*;
import java.util.*;

abstract class Task implements Runnable {
    protected Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    public abstract void processTask();

    @Override
    public void run() {
        processTask();
    }
}

class ClientTask extends Task {
    private BufferedReader in;
    private PrintWriter out;

    public ClientTask(Socket socket) {
        super(socket);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processTask() {
        try {
            String message;
            out.println("Enter your message: ");
            while ((message = in.readLine()) != null) {
                System.out.println("Received message: " + message);
                out.println("Message received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MathTask extends Task {
    private BufferedReader in;
    private PrintWriter out;

    public MathTask(Socket socket) {
        super(socket);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processTask() {
        try {
            String input;
            out.println("Enter a mathematical expression: ");
            while ((input = in.readLine()) != null) {
                try {
                    double result = evaluateExpression(input);
                    out.println("Result: " + result);
                } catch (Exception e) {
                    out.println("Invalid expression. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        double num1 = Double.parseDouble(tokens[0]);
        double num2 = Double.parseDouble(tokens[2]);
        String operator = tokens[1];

        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }
}

public class MultiThreadedServer {
    private static final int PORT = 12345;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println("Select a task: 1. Chat 2. Math");
                String choice = in.readLine();

                Task task;
                if ("1".equals(choice)) {
                    task = new ClientTask(clientSocket);
                } else if ("2".equals(choice)) {
                    task = new MathTask(clientSocket);
                } else {
                    out.println("Invalid choice. Closing connection.");
                    clientSocket.close();
                    continue;
                }

                new Thread(task).start();
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
    private static BufferedReader in;
    private static PrintWriter out;
    private static BufferedReader userInput;

    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.startsWith("Select a task")) {
                    String choice = userInput.readLine();
                    out.println(choice);
                    if ("1".equals(choice)) {
                        startChat();
                    } else if ("2".equals(choice)) {
                        startMathTask();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
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
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startMathTask() {
        try {
            String expression;
            while ((expression = userInput.readLine()) != null) {
                out.println(expression);
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MathClient extends Client {
    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.startsWith("Select a task")) {
                    out.println("2");
                    startMathTask();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ChatClient extends Client {
    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if (response.startsWith("Select a task")) {
                    out.println("1");
                    startChat();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
    

