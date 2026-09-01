package CLIs;

import java.util.List;
import java.util.Scanner;

import Store.StockManager;
import User.Admin;
import User.Customer;
import User.User;
import User.UserManager;

/**
 * Entry point for the Board Game Management System.
 * Loads users and stock from file, then loops asking for a user ID to log in.
 * Routes to the appropriate CLI based on the user's role.
 */
public class Main {

    public static void main(String[] args) {

        Scanner consoleInput = new Scanner(System.in);
        UserManager userManager = new UserManager();
        StockManager stockManager = new StockManager();

        System.out.println("====================================");
        System.out.println("  Welcome to the Board Game Store  ");
        System.out.println("====================================");

        while (true) {
            printUserList(userManager.getUsers());

            System.out.println("Enter your user ID to log in (or 0 to exit):");
            System.out.print("> ");

            String input = consoleInput.nextLine().trim();

            try {
                if (!input.matches("\\d+")) {
                    throw new IllegalArgumentException("Invalid input. Please enter a numeric user ID.");
                }

                int userId = Integer.parseInt(input);

                if (userId == 0) {
                    System.out.println("Goodbye!");
                    System.out.println("Closing program...");
                    break;
                }

                User user = userManager.getUserById(userId);

                if (user == null) {
                    throw new IllegalArgumentException("No user found with ID " + userId + ". Please try again.");
                }

                System.out.println();
                System.out.println("Logged in as: " + user.getName() + " (" + user.getRole() + ")");
                System.out.println();

                // route to the correct CLI based on role
                if (user instanceof Admin) {
                    AdminCLI.run(consoleInput, (Admin) user, stockManager);
                } else if (user instanceof Customer) {
                    CustomerCLI.run(consoleInput, (Customer) user, stockManager);
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

            System.out.println();
        }

        consoleInput.close();
    }

    // Prints all loaded users so the user knows which IDs are available
    private static void printUserList(List<User> users) {
        System.out.println("--- Users ---");
        for (User u : users) {
            System.out.println(u.getUserId() + " | " + u.getName() + " | " + u.getRole());
        }
        System.out.println();
    }
}