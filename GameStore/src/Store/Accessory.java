package Store;

import CourseworkFiles.Product;
import CourseworkFiles.ProductCategory;

/**
 * Represents an accessory product sold by the store.
 * All fields are validated on construction, invalid values throw an exception.
 */
public class Accessory extends Product {

    private AccessoryType accessoryType;
    private String compatibility;

    public Accessory(int productId, AccessoryType accessoryType, String productName,
                     double purchaseCost, int quantityInStock, double price, String compatibility) {
        super(productId, ProductCategory.ACCESSORY, productName, purchaseCost, quantityInStock, price);

        /*
         * Validate all fields before assigning, this prevents an Accessory object
         * from ever existing in an invalid state.
         */
        if (accessoryType == null) {
            throw new IllegalArgumentException("Accessory type cannot be null.");
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
        if (compatibility == null || compatibility.isBlank()) {
            throw new IllegalArgumentException("Compatibility cannot be null or empty.");
        }

        this.accessoryType = accessoryType;
        this.compatibility = compatibility;
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public String getCompatibility() {
        return compatibility;
    }

    // Customer-facing view: purchase cost is excluded
    @Override
    public String toString() {
        return String.format("%-6d | %-10s | %-12s | %-30s | £%7.2f | Stock: %3d | %-25s",
                getProductId(),
                "Accessory",
                accessoryType.name().toLowerCase().replace("_", " "),
                getProductName(),
                getPrice(),
                getQuantityInStock(),
                "Compat: " + compatibility);
    }

    // Admin-facing view: appends purchase cost to the standard customer row
    public String toAdminString() {
        return toString() + String.format(" | Cost: £%.2f", getPurchaseCost());
    }
}