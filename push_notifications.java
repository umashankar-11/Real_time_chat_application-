import java.util.*;

abstract class Notification {
    protected String message;
    protected String user;

    public Notification(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public abstract void sendNotification();

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}

class PushNotification extends Notification {
    public PushNotification(String user, String message) {
        super(user, message);
    }

    @Override
    public void sendNotification() {
        System.out.println("Sending push notification to " + user + ": " + message);
    }
}

class EmailNotification extends Notification {
    public EmailNotification(String user, String message) {
        super(user, message);
    }

    @Override
    public void sendNotification() {
        System.out.println("Sending email notification to " + user + ": " + message);
    }
}

class SMSNotification extends Notification {
    public SMSNotification(String user, String message) {
        super(user, message);
    }

    @Override
    public void sendNotification() {
        System.out.println("Sending SMS notification to " + user + ": " + message);
    }
}

class NotificationManager {
    private List<Notification> notifications;

    public NotificationManager() {
        notifications = new ArrayList<>();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void sendAllNotifications() {
        if (notifications.isEmpty()) {
            System.out.println("No notifications to send.");
        } else {
            for (Notification notification : notifications) {
                notification.sendNotification();
                System.out.println("-------------------------------");
            }
        }
    }
}

class ChatApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NotificationManager notificationManager = new NotificationManager();

        int choice;
        do {
            System.out.println("\n1. Send Push Notification");
            System.out.println("2. Send Email Notification");
            System.out.println("3. Send SMS Notification");
            System.out.println("4. Send All Notifications");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter user name for Push Notification: ");
                    String userPush = scanner.nextLine();
                    System.out.print("Enter message for Push Notification: ");
                    String pushMessage = scanner.nextLine();
                    notificationManager.addNotification(new PushNotification(userPush, pushMessage));
                    break;

                case 2:
                    System.out.print("Enter user name for Email Notification: ");
                    String userEmail = scanner.nextLine();
                    System.out.print("Enter message for Email Notification: ");
                    String emailMessage = scanner.nextLine();
                    notificationManager.addNotification(new EmailNotification(userEmail, emailMessage));
                    break;

                case 3:
                    System.out.print("Enter user name for SMS Notification: ");
                    String userSMS = scanner.nextLine();
                    System.out.print("Enter message for SMS Notification: ");
                    String smsMessage = scanner.nextLine();
                    notificationManager.addNotification(new SMSNotification(userSMS, smsMessage));
                    break;

                case 4:
                    notificationManager.sendAllNotifications();
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
