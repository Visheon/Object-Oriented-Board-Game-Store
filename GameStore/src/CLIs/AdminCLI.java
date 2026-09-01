package CLIs;

import java.util.List;
import java.util.Scanner;

import CourseworkFiles.Product;
import Store.Accessory;
import Store.AccessoryType;
import Store.BoardGame;
import Store.GameType;
import Store.StockManager;
import User.Admin;

/**
 * Command-line interface for admin users.
 * Admins can view all products (including purchase cost) and add new products to stock.
 */
public class AdminCLI {

    public static void run(Scanner consoleInput, Admin admin, StockManager stockManager) {
        System.out.println("ADMIN VIEW - Welcome, " + admin.getName());
        System.out.println("====================================");

        while (true) {
            printAdminMenu();

            String input = consoleInput.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a number.");
                System.out.println();
                continue;
            }

            int selection = Integer.parseInt(input);

            switch (selection) {
                case 1:
                    viewAllProducts(stockManager);
                    break;
                case 2:
                    addNewProduct(consoleInput, stockManager);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    System.out.println();
                    return;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    System.out.println();
            }
        }
    }

    // Displays all products sorted high-to-low by price, including purchase cost (admin only)
    private static void viewAllProducts(StockManager stockManager) {
        List<Product> products = stockManager.getAllProductsSorted();
        System.out.println();
        System.out.println("--- All Products (sorted by price, high to low) ---");
        System.out.println(String.format("%-6s | %-10s | %-12s | %-30s | %-9s | %-9s | %-25s | %s",
                "ID", "Category", "Type", "Name", "Price", "Stock", "Extra Info", "Stock Cost"));
        System.out.println("-".repeat(115));
        if (products.isEmpty()) {
            System.out.println("No products in stock.");
        } else {
            for (Product p : products) {
                if (p instanceof BoardGame) {
                    System.out.println(((BoardGame) p).toAdminString());
                } else if (p instanceof Accessory) {
                    System.out.println(((Accessory) p).toAdminString());
                }
            }
        }
        System.out.println();
    }

    /*
     * Walks the admin through entering a new product's details step by step.
     * Every validation failure throws an exception which is caught
     * below and displayed as a friendly error message before returning to the menu.
     */
    private static void addNewProduct(Scanner consoleInput, StockManager stockManager) {
        System.out.println();
        System.out.println("--- Add New Product ---");

        try {
            // product ID
            int productId = readInt(consoleInput, "Enter 4-digit product ID: ");
            if (String.valueOf(productId).length() != 4) {
                throw new IllegalArgumentException("Product ID must be exactly 4 digits.");
            }
            if (stockManager.findById(productId) != null) {
                throw new IllegalArgumentException("A product with ID " + productId + " already exists.");
            }

            // category
            System.out.print("Enter category (board game / accessory): ");
            String category = consoleInput.nextLine().trim().toLowerCase();
            if (!category.equals("board game") && !category.equals("accessory")) {
                throw new IllegalArgumentException("Category must be 'board game' or 'accessory'.");
            }

            // product name
            System.out.print("Enter product name: ");
            String productName = consoleInput.nextLine().trim();
            if (productName.isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be empty.");
            }

            // price
            double price = readDouble(consoleInput, "Enter sale price: ");
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be greater than 0.");
            }

            // stock quantity
            int quantity = readInt(consoleInput, "Enter quantity in stock: ");
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }

            // purchase cost
            double purchaseCost = readDouble(consoleInput, "Enter purchase cost: ");
            if (purchaseCost <= 0) {
                throw new IllegalArgumentException("Purchase cost must be greater than 0.");
            }

            // category-specific fields
            Product newProduct = null;

            if (category.equals("board game")) {
                System.out.print("Enter type (strategy / party): ");
                String type = consoleInput.nextLine().trim().toLowerCase();
                GameType gameType;
                if (type.equals("party")) {
                    gameType = GameType.PARTY;
                } else if (type.equals("strategy")) {
                    gameType = GameType.STRATEGY;
                } else {
                    throw new IllegalArgumentException("Type must be 'strategy' or 'party'.");
                }

                int maxPlayers = readInt(consoleInput, "Enter max number of players: ");
                if (maxPlayers <= 0) {
                    throw new IllegalArgumentException("Max players must be greater than 0.");
                }

                // BoardGame constructor will also throw if any value is invalid
                newProduct = new BoardGame(productId, gameType, productName,
                        purchaseCost, quantity, price, maxPlayers);

            } else {
                System.out.print("Enter type (dice / miniature / playmat / accessory kit): ");
                String type = consoleInput.nextLine().trim().toLowerCase();
                AccessoryType accessoryType;
                switch (type) {
                    case "dice":          accessoryType = AccessoryType.DICE; break;
                    case "miniature":     accessoryType = AccessoryType.MINIATURE; break;
                    case "playmat":       accessoryType = AccessoryType.PLAYMAT; break;
                    case "accessory kit": accessoryType = AccessoryType.ACCESSORY_KIT; break;
                    default:
                        throw new IllegalArgumentException("Invalid accessory type.");
                }

                System.out.print("Enter compatibility (or 'Universal'): ");
                String compatibility = consoleInput.nextLine().trim();
                if (compatibility.isEmpty()) {
                    throw new IllegalArgumentException("Compatibility cannot be empty.");
                }

                // Accessory constructor will also throw if any value is invalid
                newProduct = new Accessory(productId, accessoryType, productName,
                        purchaseCost, quantity, price, compatibility);
            }

            boolean added = stockManager.addProduct(newProduct);
            if (added) {
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Error: Could not add product. ID may already exist.");
            }

        } catch (IllegalArgumentException e) {
            // catches every validation failure thrown anywhere in the product creation flow
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    private static void printAdminMenu() {
        System.out.println("Please select an action (or 0 to log out):");
        System.out.println("1) View all products");
        System.out.println("2) Add new product");
        System.out.println("0) Log out");
        System.out.print("> ");
    }

    // Returns -1 if the input is not a valid whole number
    private static int readInt(Scanner consoleInput, String prompt) {
        System.out.print(prompt);
        String input = consoleInput.nextLine().trim();
        if (!input.matches("-?\\d+")) {
            System.out.println("Invalid input. Expected a whole number.");
            return -1;
        }
        return Integer.parseInt(input);
    }

    // Returns -1 if the input cannot be parsed as a decimal number
    private static double readDouble(Scanner consoleInput, String prompt) {
        System.out.print(prompt);
        String input = consoleInput.nextLine().trim();
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Expected a number.");
            return -1;
        }
    }
}