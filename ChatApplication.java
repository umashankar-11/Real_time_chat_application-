import java.io.*;
import java.util.*;

abstract class Message {
    protected String sender;
    protected String content;
    protected String type;

    public Message(String sender, String content, String type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public abstract void displayMessage();
    public abstract void shareContent();
}

class TextMessage extends Message {
    public TextMessage(String sender, String content) {
        super(sender, content, "Text");
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Text): " + content);
    }

    @Override
    public void shareContent() {
        System.out.println("Text message shared: " + content);
    }
}

class ImageMessage extends Message {
    private String imagePath;

    public ImageMessage(String sender, String imagePath) {
        super(sender, "Image: " + imagePath, "Image");
        this.imagePath = imagePath;
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Image): " + imagePath);
    }

    @Override
    public void shareContent() {
        System.out.println("Image shared: " + imagePath);
    }
}

class VideoMessage extends Message {
    private String videoPath;

    public VideoMessage(String sender, String videoPath) {
        super(sender, "Video: " + videoPath, "Video");
        this.videoPath = videoPath;
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Video): " + videoPath);
    }

    @Override
    public void shareContent() {
        System.out.println("Video shared: " + videoPath);
    }
}

class FileMessage extends Message {
    private String filePath;

    public FileMessage(String sender, String filePath) {
        super(sender, "File: " + filePath, "File");
        this.filePath = filePath;
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (File): " + filePath);
    }

    @Override
    public void shareContent() {
        System.out.println("File shared: " + filePath);
    }
}

class Chat {
    private List<Message> messages;

    public Chat() {
        messages = new ArrayList<>();
    }

    public void sendMessage(Message message) {
        messages.add(message);
    }

    public void displayMessages() {
        for (Message message : messages) {
            message.displayMessage();
        }
    }

    public void shareMessages() {
        for (Message message : messages) {
            message.shareContent();
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
            System.out.println("2. Send Image Message");
            System.out.println("3. Send Video Message");
            System.out.println("4. Send File Message");
            System.out.println("5. Display Messages");
            System.out.println("6. Share Messages");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

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
                    String senderImage = scanner.nextLine();
                    System.out.print("Enter image path: ");
                    String imagePath = scanner.nextLine();
                    chat.sendMessage(new ImageMessage(senderImage, imagePath));
                    break;

                case 3:
                    System.out.print("Enter sender name: ");
                    String senderVideo = scanner.nextLine();
                    System.out.print("Enter video path: ");
                    String videoPath = scanner.nextLine();
                    chat.sendMessage(new VideoMessage(senderVideo, videoPath));
                    break;

                case 4:
                    System.out.print("Enter sender name: ");
                    String senderFile = scanner.nextLine();
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine();
                    chat.sendMessage(new FileMessage(senderFile, filePath));
                    break;

                case 5:
                    System.out.println("\nDisplaying all messages:");
                    chat.displayMessages();
                    break;

                case 6:
                    System.out.println("\nSharing all messages:");
                    chat.shareMessages();
                    break;

                case 7:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        } while (choice != 7);

        scanner.close();
    }
}
