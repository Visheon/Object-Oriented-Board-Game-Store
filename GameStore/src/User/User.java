package User;
/**
 * Abstract representation of a user of the system.
 * All users have a unique ID, a name, and an address.
 */
public abstract class User {

    private int userId;
    private String name;
    private Address address;

    // Constructs a User with the specified details.
    
    public User(int userId, String name, Address address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
    }
    
    // Returns the user ID.
    public int getUserId() {
        return userId;
    }

    // Returns the name of the user.
    public String getName() {
        return name;
    }

    // Returns the address of the user. 
    public Address getAddress() {
        return address;
    }


    // Returns the role of the user.
    
    public abstract String getRole();

    // Returns a string representation of the user.
    
    @Override
    public String toString() {
        return "ID: " + userId + " | Name: " + name + " | Role: " + getRole();
    }
}