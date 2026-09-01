package Payment;

import java.time.LocalDate;

import CourseworkFiles.PaymentMethod;
import User.Address;

/**
 * Represents a PayPal payment method.
 * The email address is validated in the constructor, a missing '@' or blank
 * value will throw an exception immediately.
 */
public class PaypalPayment implements PaymentMethod {

    private String email;

    public PaypalPayment(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("PayPal email address is invalid.");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    /*
     * Generates a receipt in the format required by the brief:
     * "[amount] paid via PayPal using [email] on [date]. Billing address: [address]"
     */
    
    @Override
    public Receipt processPayment(double total, Address address) {
        LocalDate today = LocalDate.now();
        String message = String.format("%.2f", total)
                + " paid via PayPal using " + email
                + " on " + today
                + ". Billing address: " + address.toString();
        return new Receipt(message, today);
    }
}