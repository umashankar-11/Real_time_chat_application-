public import java.util.*;

abstract class Message {
    protected String sender;
    protected String content;
    protected String language;

    public Message(String sender, String content, String language) {
        this.sender = sender;
        this.content = content;
        this.language = language;
    }

    public abstract void displayMessage();

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getLanguage() {
        return language;
    }
}

class TextMessage extends Message {
    public TextMessage(String sender, String content, String language) {
        super(sender, content, language);
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Text) [" + language + "]: " + content);
    }
}

class ImageMessage extends Message {
    public ImageMessage(String sender, String content, String language) {
        super(sender, content, language);
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Image) [" + language + "]: " + content);
    }
}

class VideoMessage extends Message {
    public VideoMessage(String sender, String content, String language) {
        super(sender, content, language);
    }

    @Override
    public void displayMessage() {
        System.out.println(sender + " (Video) [" + language + "]: " + content);
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
                System.out.println("multi language");
            }
        }
    }
}

class LanguageSupport {
    private Map<String, String> translations;

    public LanguageSupport() {
        translations = new HashMap<>();
        translations.put("Hello", "Hola"); 
        translations.put("Goodbye", "Adiós"); 
        translations.put("Hello", "Bonjour"); 
        translations.put("Goodbye", "Au revoir"); 
    }

    public String translate(String content, String language) {
        if (language.equalsIgnoreCase("es") && translations.containsKey(content)) {
            return translations.get(content);
        } else if (language.equalsIgnoreCase("fr") && translations.containsKey(content)) {
            return translations.get(content);
        }
        return content;
    }
}

class ChatApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Chat chat = new Chat();
        LanguageSupport languageSupport = new LanguageSupport();

        int choice;
        do {
            System.out.println("\n1. Send Text Message");
            System.out.println("2. Send Image Message");
            System.out.println("3. Send Video Message");
            System.out.println("4. Display All Messages");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter sender name: ");
                    String senderText = scanner.nextLine();
                    System.out.print("Enter message content: ");
                    String textContent = scanner.nextLine();
                    System.out.print("Enter message language (en, es, fr): ");
                    String textLanguage = scanner.nextLine();
                    textContent = languageSupport.translate(textContent, textLanguage);
                    chat.sendMessage(new TextMessage(senderText, textContent, textLanguage));
                    break;

                case 2:
                    System.out.print("Enter sender name: ");
                    String senderImage = scanner.nextLine();
                    System.out.print("Enter image content: ");
                    String imageContent = scanner.nextLine();
                    System.out.print("Enter message language (en, es, fr): ");
                    String imageLanguage = scanner.nextLine();
                    imageContent = languageSupport.translate(imageContent, imageLanguage);
                    chat.sendMessage(new ImageMessage(senderImage, imageContent, imageLanguage));
                    break;

                case 3:
                    System.out.print("Enter sender name: ");
                    String senderVideo = scanner.nextLine();
                    System.out.print("Enter video content: ");
                    String videoContent = scanner.nextLine();
                    System.out.print("Enter message language (en, es, fr): ");
                    String videoLanguage = scanner.nextLine();
                    videoContent = languageSupport.translate(videoContent, videoLanguage);
                    chat.sendMessage(new VideoMessage(senderVideo, videoContent, videoLanguage));
                    break;

                case 4:
                    chat.displayMessages();
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

    

