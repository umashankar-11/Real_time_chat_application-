public import java.util.*;

class Contact {
    protected String name;
    protected String phoneNumber;
    protected String email;

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void displayContact() {
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Email: " + email);
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}

class LocalContact extends Contact {
    private String address;

    public LocalContact(String name, String phoneNumber, String email, String address) {
        super(name, phoneNumber, email);
        this.address = address;
    }

    @Override
    public void displayContact() {
        super.displayContact();
        System.out.println("Address: " + address);
    }

    public String getAddress() {
        return address;
    }
}

class CloudContact extends Contact {
    private String profilePicture;

    public CloudContact(String name, String phoneNumber, String email, String profilePicture) {
        super(name, phoneNumber, email);
        this.profilePicture = profilePicture;
    }

    @Override
    public void displayContact() {
        super.displayContact();
        System.out.println("Profile Picture: " + profilePicture);
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}

class ContactSync {
    private List<Contact> localContacts;
    private List<Contact> cloudContacts;

    public ContactSync() {
        localContacts = new ArrayList<>();
        cloudContacts = new ArrayList<>();
    }

    public void addLocalContact(Contact contact) {
        localContacts.add(contact);
    }

    public void addCloudContact(Contact contact) {
        cloudContacts.add(contact);
    }

    public void syncContacts() {
        for (Contact local : localContacts) {
            boolean found = false;
            for (Contact cloud : cloudContacts) {
                if (local.getPhoneNumber().equals(cloud.getPhoneNumber())) {
                    System.out.println("Syncing Contact: " + local.getName() + " with Cloud.");
                    found = true;
                    break;
                }
            }
            if (!found) {
                cloudContacts.add(local);
                System.out.println("Added Contact to Cloud: " + local.getName());
            }
        }

        for (Contact cloud : cloudContacts) {
            boolean found = false;
            for (Contact local : localContacts) {
                if (cloud.getPhoneNumber().equals(local.getPhoneNumber())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                localContacts.add(cloud);
                System.out.println("Added Contact to Local: " + cloud.getName());
            }
        }
    }

    public void displayAllContacts() {
        System.out.println("\n--- Local Contacts ---");
        for (Contact contact : localContacts) {
            contact.displayContact();
            System.out.println("-----------------------");
        }

        System.out.println("\n--- Cloud Contacts ---");
        for (Contact contact : cloudContacts) {
            contact.displayContact();
            System.out.println("-----------------------");
        }
    }
}

public class ContactSynchronizationApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactSync contactSync = new ContactSync();

        int choice;
        do {
            System.out.println("\n1. Add Local Contact");
            System.out.println("2. Add Cloud Contact");
            System.out.println("3. Sync Contacts");
            System.out.println("4. Display All Contacts");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String localName = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String localPhone = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String localEmail = scanner.nextLine();
                    System.out.print("Enter Address: ");
                    String localAddress = scanner.nextLine();
                    contactSync.addLocalContact(new LocalContact(localName, localPhone, localEmail, localAddress));
                    break;

                case 2:
                    System.out.print("Enter Name: ");
                    String cloudName = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String cloudPhone = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String cloudEmail = scanner.nextLine();
                    System.out.print("Enter Profile Picture URL: ");
                    String cloudProfilePicture = scanner.nextLine();
                    contactSync.addCloudContact(new CloudContact(cloudName, cloudPhone, cloudEmail, cloudProfilePicture));
                    break;

                case 3:
                    contactSync.syncContacts();
                    break;

                case 4:
                    contactSync.displayAllContacts();
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
 
    

