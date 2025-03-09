public import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;
import java.util.Scanner;

public class MessageEncryption {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            
           
            SecretKey secretKey = generateKey();
            
           
            System.out.print("Enter the message to encrypt: ");
            String message = scanner.nextLine();
            
            
            String encryptedMessage = encrypt(message, secretKey);
            System.out.println("Encrypted Message: " + encryptedMessage);
            
          
            String decryptedMessage = decrypt(encryptedMessage, secretKey);
            System.out.println("Decrypted Message: " + decryptedMessage);
            
            
            System.out.print("Enter a custom 16-character key for encryption: ");
            String keyString = scanner.nextLine();
            SecretKeySpec customKey = getKeyFromString(keyString);
            
            String customEncryptedMessage = encrypt(message, customKey);
            System.out.println("Encrypted with Custom Key: " + customEncryptedMessage);
            
            String customDecryptedMessage = decrypt(customEncryptedMessage, customKey);
            System.out.println("Decrypted with Custom Key: " + customDecryptedMessage);

           
            System.out.print("Do you want to derive a key from a passphrase? (yes/no): ");
            String choice = scanner.nextLine();
            
            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("Enter passphrase: ");
                String passphrase = scanner.nextLine();
                SecretKey derivedKey = deriveKeyFromPassphrase(passphrase);
                String derivedEncryptedMessage = encrypt(message, derivedKey);
                System.out.println("Encrypted with Derived Key: " + derivedEncryptedMessage);
                String derivedDecryptedMessage = decrypt(derivedEncryptedMessage, derivedKey);
                System.out.println("Decrypted with Derived Key: " + derivedDecryptedMessage);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); 
        return keyGenerator.generateKey();
    }

    
    public static String encrypt(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    
    public static String decrypt(String encryptedMessage, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

   
    public static SecretKeySpec getKeyFromString(String keyString) {
        return new SecretKeySpec(keyString.getBytes(), "AES");
    }

    
    public static SecretKey deriveKeyFromPassphrase(String passphrase) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt); 
        String combinedPassphrase = passphrase + new String(salt); 
        byte[] derivedKey = java.security.MessageDigest.getInstance("SHA-256").digest(combinedPassphrase.getBytes());
        return new SecretKeySpec(derivedKey, "AES");
    }

    
    public static String encryptWithSalt(String message, String keyString) throws Exception {
        SecretKeySpec secretKeySpec = getKeyFromString(keyString);
        return encrypt(message, secretKeySpec);
    }

    
    public static String decryptWithSalt(String encryptedMessage, String keyString) throws Exception {
        SecretKeySpec secretKeySpec = getKeyFromString(keyString);
        return decrypt(encryptedMessage, secretKeySpec);
    }

   
    public static byte[] generateNonce() {
        byte[] nonce = new byte[16];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

   
    public static boolean validateMessage(String originalMessage, String decryptedMessage) {
        return originalMessage.equals(decryptedMessage);
    }
}

