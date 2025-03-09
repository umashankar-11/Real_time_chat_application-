public import java.util.*;

abstract class Message {
    protected String sender;
    protected String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public abstract void displayMessage();
    public abstract void editMessage(String newContent);
    public abstract void deleteMessage();
}

class TextMessage extends Message {
    public TextMessage(String sender, String content) {
        super(sender, content);
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + ": " + content);
    }

    @Override
    public void editMessage(String newContent) {
        this.content = newContent;
        System.out.println("Message edited.");
    }

    @Override
    public void deleteMessage() {
        this.content = "[Message deleted]";
        System.out.println("Message deleted.");
    }
}

class MediaMessage extends Message {
    private String mediaType;

    public MediaMessage(String sender, String content, String mediaType) {
        super(sender, content);
        this.mediaType = mediaType;
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " shared a " + mediaType + ": " + content);
    }

    @Override
    public void editMessage(String newContent) {
        this.content = newContent;
        System.out.println("Media message edited.");
    }

    @Override
    public void deleteMessage() {
        this.content = "[Media deleted]";
        System.out.println("Media message deleted.");
    }
}

class Chat {
    private List<Message> messageList;

    public Chat() {
        messageList = new ArrayList<>();
    }

    public void sendMessage(Message message) {
        messageList.add(message);
    }

    public void displayMessages() {
        for (Message message : messageList) {
            message.displayMessage();
        }
    }

    public void editMessage(int index, String newContent) {
        if (index >= 0 && index < messageList.size()) {
            messageList.get(index).editMessage(newContent);
        } else {
            System.out.println("Invalid message index.");
        }
    }

    public void deleteMessage(int index) {
        if (index >= 0 && index < messageList.size()) {
            messageList.get(index).deleteMessage();
        } else {
            System.out.println("Invalid message index.");
        }
    }
}

public class ChatApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Chat chat = new Chat();

        int choice;
        do {
            System.out.println("\n1. Send Text Message");
            System.out.println("2. Send Media Message");
            System.out.println("3. Display Messages");
            System.out.println("4. Edit Message");
            System.out.println("5. Delete Message");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter sender name: ");
                    String senderText = scanner.nextLine();
                    System.out.print("Enter message content: ");
                    String textContent = scanner.nextLine();
                    chat.sendMessage(new TextMessage(senderText, textContent));
                    break;

                case 2:
                    System.out.print("Enter sender name: ");
                    String senderMedia = scanner.nextLine();
                    System.out.print("Enter media content description: ");
                    String mediaContent = scanner.nextLine();
                    System.out.print("Enter media type (Image/Video/Audio): ");
                    String mediaType = scanner.nextLine();
                    chat.sendMessage(new MediaMessage(senderMedia, mediaContent, mediaType));
                    break;

                case 3:
                    System.out.println("\nMessages:");
                    chat.displayMessages();
                    break;

                case 4:
                    System.out.print("Enter message index to edit: ");
                    int editIndex = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter new message content: ");
                    String newContent = scanner.nextLine();
                    chat.editMessage(editIndex, newContent);
                    break;

                case 5:
                    System.out.print("Enter message index to delete: ");
                    int deleteIndex = scanner.nextInt();
                    chat.deleteMessage(deleteIndex);
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 6);

        scanner.close();
    }
}

    

