package Store;

import java.util.ArrayList;
import java.util.List;

import CourseworkFiles.Product;

/**
 * Represents a customer's shopping basket.
 * Products are held in a list and can be added, viewed, or cleared.
 */
public class ShoppingBasket {

    private List<Product> items;

    public ShoppingBasket() {
        this.items = new ArrayList<>();
    }

    // Rejects null products, a basket should never hold a reference to nothing
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Cannot add a null product to the basket.");
        }
        items.add(product);
    }

    public void clear() {
        items.clear();
    }

    public List<Product> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Sums the sale price of every item currently in the basket
    public double getTotal() {
        double total = 0;
        for (Product p : items) {
            total += p.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Your basket is empty.";
        }

        /*
         * Build a line-by-line summary of basket contents,
         * followed by the running total.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("--- Shopping Basket ---\n");
        for (Product p : items) {
            sb.append(p.getProductName())
              .append(" - £")
              .append(String.format("%.2f", p.getPrice()))
              .append("\n");
        }
        sb.append("Total: £").append(String.format("%.2f", getTotal()));
        return sb.toString();
    }
}