public import java.util.*;

class CircularQueue {
    private String[] queue;
    private int front, rear, size, capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        queue = new String[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(String message) {
        if (isFull()) {
            front = (front + 1) % capacity;  // Overwrite the oldest message
            size--;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = message;
        size++;
    }

    public String dequeue() {
        if (isEmpty()) {
            return null;
        }
        String message = queue[front];
        front = (front + 1) % capacity;
        size--;
        return message;
    }

    public void displayQueue() {
        int i = front;
        int count = 0;
        while (count < size) {
            System.out.println(queue[i]);
            i = (i + 1) % capacity;
            count++;
        }
    }
}

class User {
    String username;
    String notification;

    public User(String username) {
        this.username = username;
        this.notification = "";
    }

    public String getUsername() {
        return username;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }
}

class ChatApp {
    private CircularQueue messagesQueue;
    private List<User> users;

    public ChatApp(int queueSize) {
        messagesQueue = new CircularQueue(queueSize);
        users = new ArrayList<>();
    }

    public void addUser(String username) {
        users.add(new User(username));
    }

    public void sendMessage(String username, String message) {
        messagesQueue.enqueue(username + ": " + message);
        checkMentions(username, message);
    }

    private void checkMentions(String sender, String message) {
        for (User user : users) {
            if (message.contains("@" + user.getUsername())) {
                user.setNotification("You were mentioned in a message from " + sender);
            }
        }
    }

    public void viewMessages() {
        messagesQueue.displayQueue();
    }

    public void viewNotifications(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println(user.getNotification());
            }
        }
    }

    public void displayUsers() {
        System.out.println("\nUsers:");
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ChatApp chatApp = new ChatApp(5);  // Circular queue size of 5 messages
        Scanner scanner = new Scanner(System.in);

        chatApp.addUser("alice");
        chatApp.addUser("bob");
        chatApp.addUser("charlie");

        int choice;
        do {
            System.out.println("\n1. Send Message");
            System.out.println("2. View Messages");
            System.out.println("3. View Notifications");
            System.out.println("4. Display Users");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("\nEnter your username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter your message: ");
                    String message = scanner.nextLine();
                    chatApp.sendMessage(username, message);
                    break;

                case 2:
                    System.out.println("\nChat Messages:");
                    chatApp.viewMessages();
                    break;

                case 3:
                    System.out.print("\nEnter your username to view notifications: ");
                    String userNotification = scanner.nextLine();
                    chatApp.viewNotifications(userNotification);
                    break;

                case 4:
                    chatApp.displayUsers();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (choice != 5);
    }
} 
    

