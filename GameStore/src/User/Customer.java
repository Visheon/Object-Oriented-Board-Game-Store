package User;

import Store.ShoppingBasket;

/**
 * Represents a customer user of the system.
 * Customers can browse products, manage a shopping basket, and complete purchases.
 */
public class Customer extends User {

    private ShoppingBasket basket;

    /**
     * Constructs a Customer with the specified details.
     * A new empty shopping basket is created automatically.
     */
    public Customer(int userId, String name, Address address) {
        super(userId, name, address);
        this.basket = new ShoppingBasket();
    }


    // Returns the role identifier for a customer user.
    
    @Override
    public String getRole() {
        return "customer";
    }


    // Returns the customer's shopping basket.

    public ShoppingBasket getBasket() {
        return basket;
    }
}
