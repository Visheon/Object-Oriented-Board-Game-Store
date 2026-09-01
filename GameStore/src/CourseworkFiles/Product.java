package CourseworkFiles;

public abstract class Product {

    private int productId;
    private ProductCategory productCategory;
    private String productName;
    private double purchaseCost;
    private int quantityInStock;
    private double price;

    public Product(int productId, ProductCategory productCategory, String productName,
                   double purchaseCost, int quantityInStock, double price) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.purchaseCost = purchaseCost;
        this.quantityInStock = quantityInStock;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public String getProductName() {
        return productName;
    }

    // Returns the purchase cost of the product.

    public double getPurchaseCost() {
        return purchaseCost;
    }

    // Returns the current quantity of the product in stock.
 
    public int getQuantityInStock() {
        return quantityInStock;
    }


     // Sets the quantity of the product currently in stock.

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }


    // Returns the sale price of the product.
    public double getPrice() {
        return price;
    }

     // Returns a string representation of the product.
     
    @Override
    public abstract String toString();
}
