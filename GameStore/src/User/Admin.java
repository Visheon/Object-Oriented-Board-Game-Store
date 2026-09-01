package User;

/**
 * Represents an admin user of the system.
 * Admins can view all product details and add new products to the stock.
 */
public class Admin extends User {

 
    // Constructs an Admin with the specified details.

    public Admin(int userId, String name, Address address) {
        super(userId, name, address);
    }


    // Returns the role identifier for an admin user.

    @Override
    public String getRole() {
        return "admin";
    }
}