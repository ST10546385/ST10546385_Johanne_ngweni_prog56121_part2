/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog15121poebukho;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Game
 */
class Message {
    String id;
    String hash;
    String recipient;
    String content;
    String timestamp;
    String status;

    public Message(String id, String hash, String recipient,
                   String content, String timestamp, String status) {
        this.id = id;
        this.hash = hash;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }
}

public class PROG56121PART2 {

    static ArrayList<Message> messages = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    static int sentCount = 0;
    static int messageLimit = 0;

    // Validate username
    public static boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Validate password
    public static boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(regex, password);
    }

    // Validate South African phone number
    public static boolean checkCellPhoneNumber(String number) {
        return Pattern.matches("^\\+27\\d{9}$", number);
    }

    // Register user (validation message)
    public static String registerUser(String username, String password) {
        if (!checkUserName(username)) {
            return "Username is not correct. Must contain _ and have a maximum of 5 characters.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correct. Must have 8+ characters, a capital letter, a number, and a special character.";
        }

        return "Username and password successfully captured. User registered!";
    }

    // Login validation
    public static boolean loginUser(String username, String password,
                                    String storedUsername, String storedPassword) {
        return username.equals(storedUsername)
                && password.equals(storedPassword);
    }

    // Login status
    public static String returnLoginStatus(boolean status) {
        if (status) {
            return "Login successful! Welcome back!";
        }
        return "Username or password incorrect, please try again.";
    }

    // Validate recipient phone number
    public static String validateNumber(String number) {
        if (number == null || !number.startsWith("+27")) {
            return "Recipient number must start with +27.";
        }

        if (!number.matches("\\+27\\d{9}")) {
            return "Recipient number must contain +27 followed by 9 digits.";
        }

        return "Recipient number entered successfully.";
    }

    // Create message hash
    public static String createMessageHash(String id, int num, String msg) {
        String trimmed = msg.trim();

        if (trimmed.isEmpty()) {
            return (id.substring(0, 2) + ":" + num + ":EMPTY")
                    .toUpperCase();
        }

        String[] words = trimmed.split("\\s+");
        String first = words[0];
        String last = words[words.length - 1];

        return (id.substring(0, 2) + ":" + num + ":"
                + first + last).toUpperCase();
    }

    // Register and return username and password
    public static String[] registerUser() {
        System.out.println("===== REGISTRATION =====");

        String username;
        while (true) {
            System.out.print("Enter Username (must contain _ and max 5 characters): ");
            username = input.nextLine();

            if (checkUserName(username)) {
                break;
            }

            System.out.println("Invalid username. Example: user_");
        }

        String password;
        while (true) {
            System.out.print("Enter Password (8+ characters, capital, number, special character): ");
            password = input.nextLine();

            if (checkPasswordComplexity(password)) {
                break;
            }

            System.out.println("Invalid password. Example: Password5!");
        }

        String cellPhone;
        while (true) {
            System.out.print("Enter Cell Phone (+27 followed by 9 digits): ");
            cellPhone = input.nextLine();

            if (checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell phone number successfully added.");
                break;
            }

            System.out.println("Invalid number. Example: +27123456789");
        }

        System.out.println(registerUser(username, password));

        return new String[]{username, password};
    }

    // Login
    public static void login(String[] credentials) {
        System.out.println("\n===== LOGIN =====");

        while (true) {
            System.out.print("Enter Username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter Password: ");
            String loginPassword = input.nextLine();

            boolean status = loginUser(
                    loginUsername, loginPassword,
                    credentials[0], credentials[1]);

            System.out.println(returnLoginStatus(status));

            if (status) {
                System.out.println("WELCOME TO QUICKCHAT!");
                break;
            }
        }
    }

    // Get message limit
    public static int getMessageLimit() {
        while (true) {
            try {
                System.out.print("How many messages do you want to send? ");
                int limit = Integer.parseInt(input.nextLine());

                if (limit >= 0) {
                    return limit;
                }

                System.out.println("Enter zero or a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Enter a valid number.");
            }
        }
    }

    // Display menu
    public static void displayMenu() {
        System.out.println("\n========== QUICKCHAT MENU ==========");
        System.out.println("1. Send Message");
        System.out.println("2. Show Messages");
        System.out.println("3. Discard Last Message");
        System.out.println("4. Save Messages");
        System.out.println("5. Quit");
        System.out.println("====================================");
        System.out.print("Choose an option (1-5): ");
    }

    // Send message
    public static void sendMessage() {
        if (sentCount >= messageLimit) {
            System.out.println("[WARNING] Message limit reached!");
            return;
        }

        System.out.print("Enter Recipient Number (+27XXXXXXXXX): ");
        String recipient = input.nextLine();

        String validation = validateNumber(recipient);

        if (!validation.equals("Recipient number entered successfully.")) {
            System.out.println("[ERROR] " + validation);
            return;
        }

        String recipientUsername;
        while (true) {
            System.out.print("Enter Recipient Username (max 5 characters and contains _): ");
            recipientUsername = input.nextLine();

            if (checkUserName(recipientUsername)) {
                break;
            }

            System.out.println("Invalid recipient username. Example: user_");
        }

        System.out.print("Enter Message (maximum 250 characters): ");
        String content = input.nextLine();

        if (content.trim().isEmpty()) {
            System.out.println("[ERROR] Message cannot be empty.");
            return;
        }

        if (content.length() > 250) {
            System.out.println("[ERROR] Message exceeds 250 characters by "
                    + (content.length() - 250) + " characters.");
            return;
        }

        String id = String.format("%010d",
                (long) (Math.random() * 10000000000L));

        String hash = createMessageHash(id, sentCount + 1, content);

        String timestamp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date());

        Message message = new Message(
                id, hash, recipient, content, timestamp, "SENT");

        messages.add(message);
        sentCount++;

        System.out.println("\n===== MESSAGE DETAILS =====");
        System.out.println("Message ID: " + id);
        System.out.println("Recipient Number: " + recipient);
        System.out.println("Recipient Username: " + recipientUsername);
        System.out.println("Message: " + content);
        System.out.println("Hash: " + hash);
        System.out.println("Time: " + timestamp);
        System.out.println("Message successfully sent.");
    }

    // Show messages
    public static void showMessages() {
        if (messages.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }

        System.out.println("\n===== ALL MESSAGES =====");

        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);

            System.out.println("\nMessage " + (i + 1));
            System.out.println("ID: " + msg.id);
            System.out.println("Recipient: " + msg.recipient);
            System.out.println("Message: " + msg.content);
            System.out.println("Hash: " + msg.hash);
            System.out.println("Time: " + msg.timestamp);
            System.out.println("Status: " + msg.status);
        }
    }

    // Discard last message
    public static void discardLastMessage() {
        if (messages.isEmpty()) {
            System.out.println("No messages to discard.");
            return;
        }

        Message lastMessage = messages.remove(messages.size() - 1);
        sentCount--;

        System.out.println("Message discarded: " + lastMessage.content);
    }

    // Save messages to a JSON file
    public static void storeMessage() {
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write("[\n");

            for (int i = 0; i < messages.size(); i++) {
                Message msg = messages.get(i);

                file.write("  {\n");
                file.write("    \"id\": \"" + escapeJson(msg.id) + "\",\n");
                file.write("    \"hash\": \"" + escapeJson(msg.hash) + "\",\n");
                file.write("    \"recipient\": \"" + escapeJson(msg.recipient) + "\",\n");
                file.write("    \"message\": \"" + escapeJson(msg.content) + "\",\n");
                file.write("    \"status\": \"" + escapeJson(msg.status) + "\",\n");
                file.write("    \"time\": \"" + escapeJson(msg.timestamp) + "\"\n");
                file.write("  }" + (i < messages.size() - 1 ? "," : "") + "\n");
            }

            file.write("]");
            System.out.println("Messages successfully stored in messages.json.");

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save file: " + e.getMessage());
        }
    }

    // Escape special characters for JSON
    public static String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Save and exit
    public static void saveAndExit() {
        System.out.print("Save messages before exiting? (yes/no): ");
        String answer = input.nextLine();

        if (answer.equalsIgnoreCase("yes")) {
            storeMessage();
        } else if (!answer.equalsIgnoreCase("no")) {
            System.out.println("Invalid choice. Messages were not saved.");
        }

        System.out.println("Total messages sent: " + sentCount);
        System.out.println("Thank you for using QuickChat!");
    }

    // Main method
    public static void main(String[] args) {
        String[] credentials = registerUser();

        login(credentials);

        messageLimit = getMessageLimit();

        while (true) {
            displayMenu();
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    sendMessage();
                    break;

                case "2":
                    showMessages();
                    break;

                case "3":
                    discardLastMessage();
                    break;

                case "4":
                    storeMessage();
                    break;

                case "5":
                    saveAndExit();
                    input.close();
                    return;

                default:
                    System.out.println("[ERROR] Invalid option. Choose 1-5.");
            }
        }
    }
}