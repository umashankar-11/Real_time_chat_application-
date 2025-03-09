import java.util.*;

abstract class User {
    protected String username;
    protected String password;
    protected String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "user";
    }

    public abstract void displayInfo();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
        this.role = "admin";
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + username + " (Role: " + role + ")");
    }

    public void manageUsers(List<User> users) {
        System.out.println("Managing Users:");
        for (User user : users) {
            System.out.println("User: " + user.getUsername() + ", Role: " + user.getRole());
        }
    }

    public void changeUserRole(User user, String newRole) {
        user.role = newRole;
        System.out.println("User " + user.getUsername() + " role updated to " + newRole);
    }

    public void removeUser(List<User> users, String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        System.out.println("User " + username + " has been removed.");
    }
}

class RegularUser extends User {
    public RegularUser(String username, String password) {
        super(username, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Regular User: " + username + " (Role: " + role + ")");
    }
}

class GuestUser extends User {
    public GuestUser(String username) {
        super(username, "guest123"); 
        this.role = "guest";
    }

    @Override
    public void displayInfo() {
        System.out.println("Guest User: " + username + " (Role: " + role + ")");
    }
}

class AdminControlPanel {
    private List<User> users;
    private Admin admin;

    public AdminControlPanel(Admin admin) {
        this.admin = admin;
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
        System.out.println("User " + user.getUsername() + " added.");
    }

    public void displayUsers() {
        System.out.println("\nUser List:");
        for (User user : users) {
            user.displayInfo();
        }
    }

    public void performAdminActions() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nAdmin Control Panel");
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Manage Users");
            System.out.println("4. Remove User");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();  

            switch (choice) {
                case 1:
                    addNewUser(sc);
                    break;
                case 2:
                    displayUsers();
                    break;
                case 3:
                    manageUsers(sc);
                    break;
                case 4:
                    removeUser(sc);
                    break;
                case 5:
                    System.out.println("Exiting Admin Panel.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    private void addNewUser(Scanner sc) {
        System.out.println("\nAdd New User");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        System.out.println("Select role: ");
        System.out.println("1. Regular User");
        System.out.println("2. Guest User");
        System.out.print("Enter your choice: ");
        int roleChoice = sc.nextInt();
        sc.nextLine(); 

        User user;
        if (roleChoice == 1) {
            user = new RegularUser(username, password);
        } else if (roleChoice == 2) {
            user = new GuestUser(username);
        } else {
            System.out.println("Invalid choice.");
            return;
        }
        addUser(user);
    }

    private void manageUsers(Scanner sc) {
        System.out.print("\nEnter username to manage: ");
        String username = sc.nextLine();
        Optional<User> userOptional = users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User found: " + user.getUsername());
            System.out.println("1. Change role");
            System.out.println("2. Cancel");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            if (choice == 1) {
                System.out.print("Enter new role (admin/regular/guest): ");
                String newRole = sc.nextLine();
                admin.changeUserRole(user, newRole);
            } else {
                System.out.println("Cancelled.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    private void removeUser(Scanner sc) {
        System.out.print("\nEnter username to remove: ");
        String username = sc.nextLine();
        Optional<User> userOptional = users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
        
        if (userOptional.isPresent()) {
            admin.removeUser(users, username);
        } else {
            System.out.println("User not found.");
        }
    }
}

public class AdminPanelDemo {
    public static void main(String[] args) {
        Admin admin = new Admin("admin1", "adminpass");
        AdminControlPanel controlPanel = new AdminControlPanel(admin);

        
        controlPanel.addUser(new RegularUser("user1", "password123"));
        controlPanel.addUser(new GuestUser("guest1"));

        
        controlPanel.performAdminActions();
    }
}
