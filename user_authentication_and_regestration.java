public import java.util.*;

abstract class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public abstract boolean authenticate(String password);

    public String getUsername() {
        return username;
    }
}

class RegisteredUser extends User {

    public RegisteredUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}

class GuestUser extends User {

    public GuestUser(String username) {
        super(username, "");
    }

    @Override
    public boolean authenticate(String password) {
        return true;
    }
}

class UserManager {
    private List<User> registeredUsers;

    public UserManager() {
        registeredUsers = new ArrayList<>();
    }

    public void registerUser(String username, String password) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username)) {
                System.out.println("User already exists.");
                return;
            }
        }
        registeredUsers.add(new RegisteredUser(username, password));
        System.out.println("Registration successful.");
    }

    public User loginUser(String username, String password) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username)) {
                if (user.authenticate(password)) {
                    System.out.println("Login successful.");
                    return user;
                } else {
                    System.out.println("Incorrect password.");
                    return null;
                }
            }
        }
        System.out.println("User not found.");
        return null;
    }

    public void displayRegisteredUsers() {
        if (registeredUsers.isEmpty()) {
            System.out.println("No registered users.");
            return;
        }

        System.out.println("Registered Users: ");
        for (User user : registeredUsers) {
            System.out.println("Username: " + user.getUsername());
        }
    }
}

class ChatApplication {
    private UserManager userManager;
    private User currentUser;

    public ChatApplication() {
        userManager = new UserManager();
        currentUser = null;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Display Registered Users");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (currentUser == null) {
                        System.out.print("Enter username: ");
                        String regUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String regPassword = scanner.nextLine();
                        userManager.registerUser(regUsername, regPassword);
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;

                case 2:
                    if (currentUser == null) {
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();
                        currentUser = userManager.loginUser(loginUsername, loginPassword);
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;

                case 3:
                    if (currentUser != null) {
                        System.out.println("Logging out...");
                        currentUser = null;
                    } else {
                        System.out.println("No user is logged in.");
                    }
                    break;

                case 4:
                    userManager.displayRegisteredUsers();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}

public class Main {
    public static void main(String[] args) {
        ChatApplication chatApp = new ChatApplication();
        chatApp.start();
    }
}
 
    

