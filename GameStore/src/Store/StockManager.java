package Store;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import CourseworkFiles.Product;

/**
 * Manages the product stock for the store.
 * Handles loading and saving products to Stock.txt, adding new products,
 * and searching/filtering the stock.
 */
public class StockManager implements Searchable {

    private static final String FILE_PATH = "Stock.txt";
    private List<Product> stock;

    // Constructs a StockManager and loads all products from Stock.txt.
    
    public StockManager() {
        stock = new ArrayList<>();
        loadStock();
    }

    /**
     * Reads all products from Stock.txt and populates the stock list.
     * Skips malformed or unrecognised lines.
     */
    private void loadStock() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 8) continue;

                int productId         = Integer.parseInt(parts[0].trim());
                String category       = parts[1].trim().toLowerCase();
                String type           = parts[2].trim().toLowerCase();
                String productName    = parts[3].trim();
                double price          = Double.parseDouble(parts[4].trim());
                int quantity          = Integer.parseInt(parts[5].trim());
                double purchaseCost   = Double.parseDouble(parts[6].trim());
                String additionalInfo = parts[7].trim();

                if (category.equals("board game")) {
                    GameType gameType = parseGameType(type);
                    int maxPlayers = Integer.parseInt(additionalInfo);
                    stock.add(new BoardGame(productId, gameType, productName,
                            purchaseCost, quantity, price, maxPlayers));

                } else if (category.equals("accessory")) {
                    AccessoryType accessoryType = parseAccessoryType(type);
                    stock.add(new Accessory(productId, accessoryType, productName,
                            purchaseCost, quantity, price, additionalInfo));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Stock.txt not found.");
        } catch (IOException e) {
            System.out.println("Error reading Stock.txt: " + e.getMessage());
        }
    }

    
    // Parses a string into a GameType value.
    private GameType parseGameType(String type) {
        switch (type) {
            case "party":    return GameType.PARTY;
            default:         return GameType.STRATEGY;
        }
    }

    
    // Parses a string into an AccessoryType value.

    
    private AccessoryType parseAccessoryType(String type) {
        switch (type) {
            case "dice":      return AccessoryType.DICE;
            case "miniature": return AccessoryType.MINIATURE;
            case "playmat":   return AccessoryType.PLAYMAT;
            default:          return AccessoryType.ACCESSORY_KIT;
        }
    }

    /**
     * Saves the current stock list back to Stock.txt.
     * Each product is written as a semicolon-separated line.
     */
    public void saveStock() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Product p : stock) {
                writer.write(productToFileLine(p));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving Stock.txt: " + e.getMessage());
        }
    }


    // Converts a Product object into a formatted file line string.

    private String productToFileLine(Product p) {
        if (p instanceof BoardGame) {
            BoardGame bg = (BoardGame) p;
            return bg.getProductId() + "; board game; "
                    + bg.getGameType().name().toLowerCase() + "; "
                    + bg.getProductName() + "; "
                    + String.format("%.2f", bg.getPrice()) + "; "
                    + bg.getQuantityInStock() + "; "
                    + String.format("%.2f", bg.getPurchaseCost()) + "; "
                    + bg.getMaxPlayers();
        } else {
            Accessory ac = (Accessory) p;
            return ac.getProductId() + "; accessory; "
                    + ac.getAccessoryType().name().toLowerCase().replace("_", " ") + "; "
                    + ac.getProductName() + "; "
                    + String.format("%.2f", ac.getPrice()) + "; "
                    + ac.getQuantityInStock() + "; "
                    + String.format("%.2f", ac.getPurchaseCost()) + "; "
                    + ac.getCompatibility();
        }
    }

    /**
     * Adds a new product to the stock and saves to file.
     * Rejects the product if a product with the same ID already exists.
     */
    
    public boolean addProduct(Product product) {
        if (findById(product.getProductId()) != null) {
            return false;
        }
        stock.add(product);
        saveStock();
        return true;
    }

    // Returns all products sorted descending by price.

    public List<Product> getAllProductsSorted() {
        List<Product> sorted = new ArrayList<>(stock);
        sorted.sort(Comparator.comparingDouble(Product::getPrice).reversed());
        return sorted;
    }

    // Looks up a product by its unique product ID.
    
    @Override
    public Product findById(int productId) {
        for (Product p : stock) {
            if (p.getProductId() == productId) return p;
        }
        return null;
    }

    /**
     * Filters accessories whose compatibility, name, or type contains the given
     * query string.
     */
    @Override
    public List<Accessory> filterByCompatibility(String query) {
        List<Accessory> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Product p : stock) {
            if (p instanceof Accessory) {
                Accessory a = (Accessory) p;
                boolean matchesCompatibility = a.getCompatibility().toLowerCase().contains(lowerQuery);
                boolean matchesName         = a.getProductName().toLowerCase().contains(lowerQuery);
                boolean matchesType         = a.getAccessoryType().name().toLowerCase()
                                               .replace("_", " ").contains(lowerQuery);
                if (matchesCompatibility || matchesName || matchesType) {
                    results.add(a);
                }
            }
        }
        return results;
    }

    /**
     * Searches all products whose name contains the given query string (case-insensitive).
     * Each word in the query is checked independently, so partial  queries will return relevant matches.
     */
    public List<Product> searchByName(String query) {
        List<Product> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Product p : stock) {
            if (p.getProductName().toLowerCase().contains(lowerQuery)) {
                results.add(p);
            }
        }
        return results;
    }
}