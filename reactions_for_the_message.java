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
    public abstract void addReaction(String reaction);
    public abstract void showReactions();
}

class TextMessage extends Message {
    private List<String> reactions;

    public TextMessage(String sender, String content) {
        super(sender, content);
        reactions = new ArrayList<>();
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Text): " + content);
    }

    @Override
    public void addReaction(String reaction) {
        reactions.add(reaction);
    }

    @Override
    public void showReactions() {
        if (reactions.isEmpty()) {
            System.out.println("No reactions yet.");
        } else {
            System.out.println("Reactions: " + String.join(", ", reactions));
        }
    }
}

class ImageMessage extends Message {
    private List<String> reactions;

    public ImageMessage(String sender, String content) {
        super(sender, content);
        reactions = new ArrayList<>();
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Image): " + content);
    }

    @Override
    public void addReaction(String reaction) {
        reactions.add(reaction);
    }

    @Override
    public void showReactions() {
        if (reactions.isEmpty()) {
            System.out.println("No reactions yet.");
        } else {
            System.out.println("Reactions: " + String.join(", ", reactions));
        }
    }
}

class VideoMessage extends Message {
    private List<String> reactions;

    public VideoMessage(String sender, String content) {
        super(sender, content);
        reactions = new ArrayList<>();
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Video): " + content);
    }

    @Override
    public void addReaction(String reaction) {
        reactions.add(reaction);
    }

    @Override
    public void showReactions() {
        if (reactions.isEmpty()) {
            System.out.println("No reactions yet.");
        } else {
            System.out.println("Reactions: " + String.join(", ", reactions));
        }
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
        if (messages.isEmpty()) {
            System.out.println("No messages to display.");
        } else {
            for (Message message : messages) {
                message.displayMessage();
                message.showReactions();
                System.out.println("reaction");
            }
        }
    }

    public void reactToMessage(int messageIndex, String reaction) {
        if (messageIndex >= 0 && messageIndex < messages.size()) {
            messages.get(messageIndex).addReaction(reaction);
            System.out.println("Reaction added.");
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
            System.out.println("2. Send Image Message");
            System.out.println("3. Send Video Message");
            System.out.println("4. Display All Messages");
            System.out.println("5. Add Reaction to a Message");
            System.out.println("6. Exit");
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
                    System.out.print("Enter image content: ");
                    String imageContent = scanner.nextLine();
                    chat.sendMessage(new ImageMessage(senderImage, imageContent));
                    break;

                case 3:
                    System.out.print("Enter sender name: ");
                    String senderVideo = scanner.nextLine();
                    System.out.print("Enter video content: ");
                    String videoContent = scanner.nextLine();
                    chat.sendMessage(new VideoMessage(senderVideo, videoContent));
                    break;

                case 4:
                    chat.displayMessages();
                    break;

                case 5:
                    System.out.print("Enter the message index to react to: ");
                    int messageIndex = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("Enter your reaction: ");
                    String reaction = scanner.nextLine();
                    chat.reactToMessage(messageIndex, reaction);
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
}
 
    

