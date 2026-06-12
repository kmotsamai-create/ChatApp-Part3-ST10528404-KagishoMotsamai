

package Login;


import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
// Part1
public class Login {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ── PART 1 : REGISTRATION ─────────────────────────────────────────────

        System.out.println("  ===============================");
        System.out.println("           QUICK CHAT          ");
        System.out.println("  ===============================");

        System.out.print("Enter name: ");
        String name = input.nextLine();
        

        System.out.print("Enter firstname: ");
        String firstname = input.nextLine();
        
        System.out.print ("Enter lastname: ");
        String lastname = input.nextLine ();
        System.out.print ("Enter username: ");
         String username = input.nextLine ();
         
        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cell number: ");
        String cell = input.nextLine();

        boolean valid = true;
//  Must contain an underscore '_' and length must be <= 5.
        if (!username.contains("_") || username.length() > 5) {
            System.out.println("Incorrect username (missing _ and max 5 characters)");
            valid = false;
        }

        if (password.length() < 8
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[^a-zA-Z0-9].*")) {
            System.out.println("Incorrect password (min 8 chars, 1 capital, 1 special char)");
            valid = false;
        }
// Must be 9 numbers and starts with +27
        if (!cell.matches("\\+27\\d{9}")) {
            System.out.println("Incorrect cell number (must start with +27 and 9 digits)");
            valid = false;
        }

        if (!valid) {
            System.out.println("Registration unsuccessful.");
            input.close();
            return;
        }

        System.out.println("\nRegistration Successful!\n");

        

        System.out.println("+==== LOGIN ====+");

        System.out.print("Enter username: ");
        String loginUser = input.nextLine();

        System.out.print("Enter password: ");
        String loginPass = input.nextLine();

        if (!loginUser.equals(username) || !loginPass.equals(password)) {
            System.out.println("Unsuccessful Login.");
            input.close();
            return;
        }
        System.out.println("WELCOME: " + firstname + " " + lastname + ", it is great to see you.");


        

        System.out.println("\nWelcome To Quick Chat");

        boolean running = true;

        while (running) {

            System.out.println("\n===== Quick CHART  MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Choose option: ");

            String chatMenu = input.nextLine();

            switch (chatMenu) {

                // ── OPTION 1 : SEND MESSAGES 
                case "1":

                    System.out.print("How many messages do you want to send? ");
                    int numMessages = Integer.parseInt(input.nextLine());

                    for (int i = 1; i <= numMessages; i++) {

                        System.out.println("----- MESSAGE " + i + " ------");
                        Message msg = new Message(i);

                        String recipient;
                        while (true) {
                            System.out.print("Enter recipient number: ");
                            recipient = input.nextLine();
                            if (msg.checkRecipientCell(recipient)) {
                                System.out.println("Cell number accepted.");
                                break;
                            } else {
                                System.out.println("Invalid cell number.");
                            }
                        }
                        msg.setRecipient(recipient);

                        String text;
                        while (true) {
                            System.out.print("Enter your message: ");
                            text = input.nextLine();
                            if (text.length() <= 250) {
                                System.out.println("Message ready to send.");
                                break;
                            } else {
                                System.out.println("Message is over  250 characters.");
                            }
                        }
                        msg.setMessageText(text);
                        msg.createMessageHash();

                        System.out.println("\nMESSAGE DETAILS");
                        System.out.println("Message ID   : " + msg.getMessageID());
                        System.out.println("Message Hash : " + msg.getMessageHash());
                        System.out.println("Recipient    : " + msg.getRecipient());
                        System.out.println("Message      : " + msg.getMessageText());

                        msg.sentMessage(input);
                    }
                    break;

                // ── OPTION 2 : SHOW RECENTLY SENT MESSAGES 
                case "2":
                    System.out.println("This message is not available .");
                    break;

                // ── OPTION 3 : STORED MESSAGES SUB-MENU (Part 3) 
                case "3":
                    Message.loadStoredMessages();
                    boolean storedMenu = true;

                    while (storedMenu) {
                        System.out.println("\n===== STORED MESSAGES MENU =====");
                        System.out.println("a) Show sender and recipient of all stored messages");
                        System.out.println("b) Show the longest stored message");
                        System.out.println("c) Search for a message by ID");
                        System.out.println("d) Search messages for a  recipient");
                        System.out.println("e) Delete a message using message hash");
                        System.out.println("f) Display full report of all stored messages");
                        System.out.println("g) Back to main menu");
                        System.out.print("Select option: ");

                        String Choice = input.nextLine().toLowerCase();

                        switch (Choice) {
                            case "a":
                                Message.displayStoredSendersAndRecipients();
                                break;
                            case "b":
                                
                                Message.displayLongestStoredMessage();
                                break;
                            case "c":
                                System.out.print("Enter Message ID to search: ");
                                Message.searchByMessageID(input.nextLine());
                                break;
                            case "d":
                                System.out.print("Enter recipient number to search: ");
                                Message.searchByRecipient(input.nextLine());
                                break;
                            case "e":
                                System.out.print("Enter message hash to delete: ");
                                Message.deleteByHash(input.nextLine());
                                break;
                            case "f":
                                Message.displayFullReport();
                                break;
                            case "g":
                                storedMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option. Enter a - g");
                        }
                    }
                    break;

                // ── OPTION 4 : QUIT 
                case "4":
                    System.out.println("Thank You!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        input.close();
    }
}


class Message {

    private static Object messageId;

    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    
    public static int totalMessage =0;
    public static String senderName = "Unknown User";

    // ── Part 3 static arrays 
    private static ArrayList<String>  sentMessages         = new ArrayList<>();
    private static ArrayList<String>  disregardedMessages  = new ArrayList<>();
    private static ArrayList<Message> storedMessages       = new ArrayList<>();
    private static ArrayList<String>  messageHashes        = new ArrayList<>();
    private static ArrayList<String>  messageIDs           = new ArrayList<>();
    private static int totalMessages = 0;

    

    public Message(int number) {
        this.messageNumber = number;
        this.messageID     = generateID();
    }

    public Message(String recipient, String messageText, String flag, int number) {
        this.messageNumber = number;
        this.messageID     = generateID();
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = createMessageHash();
    }

    public Message(String id, String recipient, String messageText, String hash, int number) {
        this.messageID     = id;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = hash;
        this.messageNumber = number;
    }

    

    private String generateID() {
        long num = (long)(Math.random() * 9000000000L) + 1000000000L;
        return String.valueOf(num);
    }
// must be 12 num start with +
    public boolean checkRecipientCell(String cell) {
        return cell.startsWith("+") && cell.length() <= 12;
    }

    public String createMessageHash() {
        String[] words   = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord  = words[words.length - 1];
        messageHash = (messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    //  STORE TO JSON FILE  ( 2)

    public void storeMessage() {
        String json = "{\n"
                + "\"MessageID\":\"" + messageID  + "\",\n"
                + "\"Recipient\":\"" + recipient  + "\",\n"
                + "\"Message\":\""  + messageText + "\",\n"
                + "\"Hash\":\""     + messageHash + "\"\n"
                + "}\n";

        try {
            FileWriter file = new FileWriter("stored_messages.json", true);
            file.write(json);
            file.close();
            System.out.println("Message stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing message.");
        }

        storedMessages.add(this);
        messageHashes.add(messageHash);
        messageIDs.add(messageID);
    }

    
    //  SEND / DISCARD / STORE CHOICE  ( 2)
    

    public void sentMessage(Scanner input) {
        System.out.println("\n1) Send Message");
        System.out.println("2) Discard Message");
        System.out.println("3) Store Message");
        System.out.print("Choose option: ");
        String choice = input.nextLine();

        switch (choice) {
            case "1":
                totalMessages++;
                sentMessages.add(
                        "ID: "      + messageID
                        + " | Hash: "    + messageHash
                        + " | To: "      + recipient
                        + " | Message: " + messageText
                );
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                System.out.println("Message successfully sent.");
                break;

            case "2":
                disregardedMessages.add(
                        "ID: "  + messageID
                        + " | To: "      + recipient
                        + " | Message: " + messageText
                );
                System.out.println("Message deleted.");
                break;

            case "3":
                storeMessage();
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    
    //  MENU OPTION 2 — PRINT RECENTLY SENT MESSAGES  ( 2)

    public static void printSentMessages() {
        System.out.println("\n==== RECENTLY SENT MESSAGES ====");
        if (sentMessages.isEmpty()) {
            System.out.println("No messages sent yet.");
            return;
        }
        for (String msg : sentMessages) {
            System.out.println(msg);
        }
    }

    //  LOAD STORED MESSAGES FROM JSON  (Part 3)

    public static void loadStoredMessages() {
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("stored_messages.json"));
            String line;
            String id = "", rec = "", msg = "", hash = "";
            int count = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if      (line.startsWith("\"MessageID\"")) id   = extractValue(line);
                else if (line.startsWith("\"Recipient\"")) rec  = extractValue(line);
                else if (line.startsWith("\"Message\""))  msg  = extractValue(line);
                else if (line.startsWith("\"Hash\""))     hash = extractValue(line);
                else if (line.equals("}")) {
                    count++;
                    Message m = new Message(id, rec, msg, hash, count);
                    storedMessages.add(m);
                    messageHashes.add(hash);
                    messageIDs.add(id);
                    id = ""; rec = ""; msg = ""; hash = "";
                }
            }
            reader.close();

            if (storedMessages.isEmpty()) {
                System.out.println("No stored messages found.");
            } else {
                System.out.println(storedMessages.size() + " stored message(s) loaded.");
            }

        } catch (IOException e) {
            System.out.println("No stored messages file found.");
        }
    }

    private static String extractValue(String line) {
        int start = line.indexOf('"', line.indexOf(':') + 1) + 1;
        int end   = line.lastIndexOf('"');
        return (start > 0 && end > start) ? line.substring(start, end) : "";
    }

    //  STORED MESSAGES SUB-MENU FEATURES  ( 3)

    //a
    public static void displayStoredSendersAndRecipients() {
        System.out.println("\n==== STORED MESSAGES — SENDERS & RECIPIENTS ====");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        for (Message m : storedMessages) {
            System.out.println("ID: " + m.messageID + " | Recipient: " + m.recipient);
        }
    }

    //b
    public static void displayLongestStoredMessage() {
        System.out.println("\n==== LONGEST MESSAGE ====");
        ArrayList<String> allTexts = new ArrayList<>();
        for (Message m : storedMessages) allTexts.add(m.messageText);
        for (String s : sentMessages) {
            int idx = s.indexOf("| Message: ");
            if (idx != -1) allTexts.add(s.substring(idx + 11));
        }
        if (allTexts.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }
        String longest = "";
        for (String t : allTexts) {
            if (t.length() > longest.length()) longest = t;
        }
        System.out.println("Longest message: " + longest);
    }

    //c
    public static void searchByMessageID(String id) {
        System.out.println("\n==== SEARCH BY MESSAGE ID: " + id + " ====");
        boolean found = false;
        for (Message m : storedMessages) {
            if (m.messageID.equals(id)) {
                System.out.println("Recipient : " + m.recipient);
                System.out.println("Message   : " + m.messageText);
                found = true;
            }
        }
        for (String s : sentMessages) {
            if (s.contains("ID: " + id + " ")) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("No message found with ID: " + id);
    }

    //d
    public static void searchByRecipient(String recipient) {
        System.out.println("\n==== MESSAGES FOR: " + recipient + " ====");
        boolean found = false;
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipient)) {
                System.out.println("Message: " + m.messageText);
                found = true;
            }
        }
        for (String s : sentMessages) {
            if (s.contains("To: " + recipient)) {
                int idx = s.indexOf("| Message: ");
                if (idx != -1) System.out.println("Message: " + s.substring(idx + 11));
                found = true;
            }
        }
        if (!found) System.out.println("No messages found for: " + recipient);
    }

    //e
    public static void deleteByHash(String hash) {
        System.out.println("\n==== DELETE BY HASH ====");
        String upperHash = hash.toUpperCase();
        boolean found = false;
        for (int i = 0; i < storedMessages.size(); i++) {
            if (upperHash.equals(storedMessages.get(i).messageHash)) {
                String text = storedMessages.get(i).messageText;
                storedMessages.remove(i);
                messageHashes.remove(upperHash);
                System.out.println("Message: \"" + text + "\" successfully deleted.");
                found = true;
                break;
            }
        }
        if (!found) System.out.println("No message found with hash: " + hash);
    }
// f
     public static String displayFullReport() {
        ArrayList<String> masterList = new ArrayList<>();
        masterList.addAll(sentMessages);
        masterList.addAll(disregardedMessages);

        if (masterList.isEmpty()) {
            return "No messages found.";
        }

       String report = "=== FULL MESSAGE REPORT === ";
        for (int i = 0; i < masterList.size(); i++) {
            String[] splitParts = masterList.get(i).split("\t");
            if (splitParts.length >= 4) {
                String currentId = splitParts[0];
                int hashIdx = messageIDs.indexOf(currentId);
String messageHashValue = messageHashes.get(hashIdx);

                if (hashIdx != -1) {
    String messageHashValue = messageHashes.get(hashIdx);

    report += "\nHash: " + messageHashValue
            + " | Recipient: " + splitParts[2]
            + " | Text: " + splitParts[1];
}
            }
        }
        return report;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() 
    { return messageID; }
    public String getRecipient() 
    { return recipient; }
    public String getMessageText() 
    { return messageText; }
    public String getMessageHash() {
    return messageHash;
}

public void setMessageText(String messageText) {
    this.messageText = messageText;
}
    public String getDispatchStatus(String dispatchStatus) { return dispatchStatus; }

    public void setRecipient(String recipient) { 
        this.recipient = recipient; 
    }
}
    
   

                        