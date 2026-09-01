package Store;

import CourseworkFiles.Product;
import CourseworkFiles.ProductCategory;

/**
 * Represents a board game sold by the store.
 * All fields are validated on construction.
 */
public class BoardGame extends Product {

    private GameType gameType;
    private int maxPlayers;

    public BoardGame(int productId, GameType gameType, String productName,
                     double purchaseCost, int quantityInStock, double price, int maxPlayers) {
        super(productId, ProductCategory.BOARDGAME, productName, purchaseCost, quantityInStock, price);

        /*
         * Validate all fields before assigning, this prevents a BoardGame object
         * from ever existing in an invalid state.
         */
        if (gameType == null) {
            throw new IllegalArgumentException("Game type cannot be null.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
        if (purchaseCost <= 0) {
            throw new IllegalArgumentException("Purchase cost must be greater than 0.");
        }
        if (quantityInStock < 0) {
            throw new IllegalArgumentException("Quantity in stock cannot be negative.");
        }
        if (maxPlayers <= 0) {
            throw new IllegalArgumentException("Max players must be greater than 0.");
        }

        this.gameType = gameType;
        this.maxPlayers = maxPlayers;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    // Customer-facing view: purchase cost is intentionally excluded
    @Override
    public String toString() {
        return String.format("%-6d | %-10s | %-12s | %-30s | £%7.2f | Stock: %3d | %-25s",
                getProductId(),
                "Board Game",
                gameType.name().toLowerCase(),
                getProductName(),
                getPrice(),
                getQuantityInStock(),
                "Players: " + getMaxPlayers());
    }

    // Admin-facing view: appends purchase cost to the standard customer row
    public String toAdminString() {
        return toString() + String.format(" | Cost: £%.2f", getPurchaseCost());
    }
}