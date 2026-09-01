package User;

/**
 * Represents a physical address associated with a user.
 * Validates all fields on construction to prevent invalid address objects being created.
 */
public class Address {

    private int houseNumber;
    private String postcode;
    private String city;

    public Address(int houseNumber, String postcode, String city) {
        if (houseNumber <= 0) {
            throw new IllegalArgumentException("House number must be a positive integer.");
        }
        if (postcode == null || postcode.isBlank()) {
            throw new IllegalArgumentException("Postcode cannot be null or empty.");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or empty.");
        }
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    // Format: "12, LE11 3TU, Loughborough"
    @Override
    public String toString() {
        return houseNumber + ", " + postcode + ", " + city;
    }
}