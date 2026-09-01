package CLIs;

import java.util.List;
import java.util.Scanner;

import CourseworkFiles.PaymentMethod;
import CourseworkFiles.Product;
import Payment.CreditCardPayment;
import Payment.PaypalPayment;
import Payment.Receipt;
import Store.Accessory;
import Store.StockManager;
import User.Customer;

/**
 * Command-line interface for customer users.
 * Customers can browse products, manage a basket, and complete purchases.
 */
public class CustomerCLI {

    public static void run(Scanner consoleInput, Customer customer, StockManager stockManager) {
        System.out.println("CUSTOMER VIEW - Welcome, " + customer.getName());
        System.out.println("====================================");

        // ensure basket is empty at start of each session
        customer.getBasket().clear();

        while (true) {
            printCustomerMenu();

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
                    addToBasket(consoleInput, customer, stockManager);
                    break;
                case 3:
                    viewBasket(customer);
                    break;
                case 4:
                    purchase(consoleInput, customer, stockManager);
                    break;
                case 5:
                    cancelBasket(customer);
                    break;
                case 6:
                    lookupById(consoleInput, stockManager);
                    break;
                case 7:
                    filterByCompatibility(consoleInput, stockManager);
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

    // Displays all products sorted high-to-low by price, purchase cost excluded
    private static void viewAllProducts(StockManager stockManager) {
        List<Product> products = stockManager.getAllProductsSorted();
        System.out.println();
        System.out.println("--- All Products (sorted by price, high to low) ---");
        System.out.println(String.format("%-6s | %-10s | %-12s | %-30s | %-9s | %-9s | %s",
                "ID", "Category", "Type", "Name", "Price", "Stock", "Extra Info"));
        System.out.println("-".repeat(100));
        if (products.isEmpty()) {
            System.out.println("No products currently in stock.");
        } else {
            for (Product p : products) {
                System.out.println(p.toString());
            }
        }
        System.out.println();
    }

    /*
     * Looks up the product by ID, checks how many units are already in the basket
     * against available stock, then adds it. Throws an error for
     * any invalid input, caught below and shown as an error message.
     */
    private static void addToBasket(Scanner consoleInput, Customer customer, StockManager stockManager) {
        System.out.println();
        System.out.print("Enter product ID to add to basket: ");
        String input = consoleInput.nextLine().trim();

        try {
            if (!input.matches("\\d+")) {
                throw new IllegalArgumentException("Invalid input. Please enter a numeric product ID.");
            }

            int productId = Integer.parseInt(input);
            Product product = stockManager.findById(productId);

            if (product == null) {
                throw new IllegalArgumentException("No product found with ID " + productId + ".");
            }

            // count how many of this product are already in the basket
            long alreadyInBasket = customer.getBasket().getItems().stream()
                    .filter(p -> p.getProductId() == productId)
                    .count();

            if (alreadyInBasket >= product.getQuantityInStock()) {
                if (product.getQuantityInStock() == 0) {
                    throw new IllegalArgumentException("Sorry, " + product.getProductName() + " is out of stock.");
                } else {
                    throw new IllegalArgumentException("Sorry, only " + product.getQuantityInStock()
                            + " unit(s) of " + product.getProductName() + " available.");
                }
            }

            customer.getBasket().addProduct(product);
            System.out.println(product.getProductName() + " added to your basket.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
    }

    private static void viewBasket(Customer customer) {
        System.out.println();
        System.out.println(customer.getBasket().toString());
        System.out.println();
    }

    /*
     * Handles the full purchase flow: show the basket, collect payment details,
     * process the payment, decrement stock quantities, clear the basket, print receipt.
     * Invalid payment input causes the payment constructor to throw an error,
     * which is caught here and shown as a friendly error message.
     */
    private static void purchase(Scanner consoleInput, Customer customer, StockManager stockManager) {
        System.out.println();

        try {
            if (customer.getBasket().isEmpty()) {
                throw new IllegalArgumentException("Your basket is empty. Add some products first.");
            }

            // show basket summary before payment
            System.out.println(customer.getBasket().toString());
            System.out.println();

            // choose payment method
            System.out.println("Select payment method:");
            System.out.println("1) PayPal");
            System.out.println("2) Credit Card");
            System.out.print("> ");

            String methodInput = consoleInput.nextLine().trim();
            PaymentMethod paymentMethod = null;

            if (methodInput.equals("1")) {
                System.out.print("Enter your PayPal email address: ");
                String email = consoleInput.nextLine().trim();
                // throws IllegalArgumentException if email is invalid
                paymentMethod = new PaypalPayment(email);

            } else if (methodInput.equals("2")) {
                System.out.print("Enter your 6-digit card number: ");
                String cardNumber = consoleInput.nextLine().trim();
                System.out.print("Enter your 3-digit security code: ");
                String securityCode = consoleInput.nextLine().trim();
                // throws IllegalArgumentException if card number or security code are invalid
                paymentMethod = new CreditCardPayment(cardNumber, securityCode);

            } else {
                throw new IllegalArgumentException("Invalid selection. Returning to menu.");
            }

            // process payment
            double total = customer.getBasket().getTotal();
            Receipt receipt = paymentMethod.processPayment(total, customer.getAddress());

            // update stock quantities
            for (Product p : customer.getBasket().getItems()) {
                p.setQuantityInStock(p.getQuantityInStock() - 1);
            }
            stockManager.saveStock();

            // clear basket
            customer.getBasket().clear();

            // print receipt
            System.out.println();
            System.out.println("--- Receipt ---");
            System.out.println(receipt.toString());

        } catch (IllegalArgumentException e) {
            // catches empty basket, invalid payment method selection, and invalid payment details
            System.out.println(e.getMessage());
        }

        System.out.println();
    }

    private static void cancelBasket(Customer customer) {
        if (customer.getBasket().isEmpty()) {
            System.out.println("Your basket is already empty.");
        } else {
            customer.getBasket().clear();
            System.out.println("Your basket has been cleared.");
        }
        System.out.println();
    }

    // Looks up a single product by its 4-digit ID and prints its details
    private static void lookupById(Scanner consoleInput, StockManager stockManager) {
        System.out.println();
        System.out.print("Enter product ID to look up: ");
        String input = consoleInput.nextLine().trim();

        try {
            if (!input.matches("\\d+")) {
                throw new IllegalArgumentException("Invalid input. Please enter a numeric product ID.");
            }

            int productId = Integer.parseInt(input);
            Product product = stockManager.findById(productId);

            if (product == null) {
                throw new IllegalArgumentException("No product found with ID " + productId + ".");
            }

            System.out.println(product.toString());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
    }

    /*
     * Searches accessories using a broad query, matches against compatibility,
     * product name, and accessory type so customers don't need to know the exact field.
     * For example, "dice" finds all dice accessories; "Universal" finds all universal ones.
     */
    private static void filterByCompatibility(Scanner consoleInput, StockManager stockManager) {
        System.out.println();
        System.out.print("Enter compatibility to search for (e.g. Universal, Warhammer 40K): ");
        String compatibility = consoleInput.nextLine().trim();

        try {
            if (compatibility.isEmpty()) {
                throw new IllegalArgumentException("Please enter a compatibility to search for.");
            }

            List<Accessory> results = stockManager.filterByCompatibility(compatibility);
            System.out.println();
            if (results.isEmpty()) {
                System.out.println("No accessories found matching: " + compatibility);
            } else {
                System.out.println("--- Accessories matching: " + compatibility + " ---");
                System.out.println(String.format("%-6s | %-10s | %-12s | %-30s | %-9s | %-9s | %s",
                        "ID", "Category", "Type", "Name", "Price", "Stock", "Extra Info"));
                System.out.println("-".repeat(100));
                for (Accessory a : results) {
                    System.out.println(a.toString());
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    private static void printCustomerMenu() {
        System.out.println("Please select an action (or 0 to log out):");
        System.out.println("1) View all products");
        System.out.println("2) Add product to basket");
        System.out.println("3) View basket");
        System.out.println("4) Purchase items in basket");
        System.out.println("5) Cancel basket");
        System.out.println("6) Look up product by ID");
        System.out.println("7) Search accessories (by type, name, or compatibility)");
        System.out.println("0) Log out");
        System.out.print("> ");
    }
}