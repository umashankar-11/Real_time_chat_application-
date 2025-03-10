public import java.util.*;

abstract class Device {
    protected String deviceName;
    protected String user;
    protected List<String> messages;

    public Device(String deviceName, String user) {
        this.deviceName = deviceName;
        this.user = user;
        this.messages = new ArrayList<>();
    }

    public abstract void syncMessages();

    public void sendMessage(String message) {
        messages.add(message);
        System.out.println(user + " sent message from " + deviceName + ": " + message);
    }

    public void displayMessages() {
        System.out.println("Messages on " + deviceName + " for " + user + ":");
        for (String message : messages) {
            System.out.println(message);
        }
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getUser() {
        return user;
    }
}

class MobileDevice extends Device {

    public MobileDevice(String deviceName, String user) {
        super(deviceName, user);
    }

    @Override
    public void syncMessages() {
        System.out.println("Syncing messages on Mobile for user: " + user);
        for (String message : messages) {
            System.out.println("Mobile: " + message);
        }
    }
}

class DesktopDevice extends Device {

    public DesktopDevice(String deviceName, String user) {
        super(deviceName, user);
    }

    @Override
    public void syncMessages() {
        System.out.println("Syncing messages on Desktop for user: " + user);
        for (String message : messages) {
            System.out.println("Desktop: " + message);
        }
    }
}

class TabletDevice extends Device {

    public TabletDevice(String deviceName, String user) {
        super(deviceName, user);
    }

    @Override
    public void syncMessages() {
        System.out.println("Syncing messages on Tablet for user: " + user);
        for (String message : messages) {
            System.out.println("Tablet: " + message);
        }
    }
}

class DeviceManager {
    private List<Device> devices;

    public DeviceManager() {
        devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void syncAllDevices() {
        for (Device device : devices) {
            device.syncMessages();
            System.out.println("-------------------------------");
        }
    }

    public void displayAllMessages() {
        for (Device device : devices) {
            device.displayMessages();
            System.out.println("-------------------------------");
        }
    }
}

public class MultiDeviceSyncApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DeviceManager deviceManager = new DeviceManager();

        int choice;
        do {
            System.out.println("\n1. Add Mobile Device");
            System.out.println("2. Add Desktop Device");
            System.out.println("3. Add Tablet Device");
            System.out.println("4. Send Message");
            System.out.println("5. Sync All Devices");
            System.out.println("6. Display Messages on All Devices");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter user name for Mobile Device: ");
                    String mobileUser = scanner.nextLine();
                    System.out.print("Enter device name for Mobile: ");
                    String mobileName = scanner.nextLine();
                    deviceManager.addDevice(new MobileDevice(mobileName, mobileUser));
                    break;

                case 2:
                    System.out.print("Enter user name for Desktop Device: ");
                    String desktopUser = scanner.nextLine();
                    System.out.print("Enter device name for Desktop: ");
                    String desktopName = scanner.nextLine();
                    deviceManager.addDevice(new DesktopDevice(desktopName, desktopUser));
                    break;

                case 3:
                    System.out.print("Enter user name for Tablet Device: ");
                    String tabletUser = scanner.nextLine();
                    System.out.print("Enter device name for Tablet: ");
                    String tabletName = scanner.nextLine();
                    deviceManager.addDevice(new TabletDevice(tabletName, tabletUser));
                    break;

                case 4:
                    System.out.print("Enter user name to send message: ");
                    String sender = scanner.nextLine();
                    System.out.print("Enter message: ");
                    String message = scanner.nextLine();

                    for (Device device : deviceManager.devices) {
                        if (device.getUser().equals(sender)) {
                            device.sendMessage(message);
                            break;
                        }
                    }
                    break;

                case 5:
                    deviceManager.syncAllDevices();
                    break;

                case 6:
                    deviceManager.displayAllMessages();
                    break;

                case 7:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7);

        scanner.close();
    }
}
 
    
