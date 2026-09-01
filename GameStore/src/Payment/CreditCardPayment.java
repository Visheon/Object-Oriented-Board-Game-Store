package Payment;

import java.time.LocalDate;

import CourseworkFiles.PaymentMethod;
import User.Address;

/**
 * Represents a credit card payment method.
 * Card number must be exactly 6 digits and security code exactly 3 digits —
 * both are validated in the constructor so an invalid object can never be created.
 */
public class CreditCardPayment implements PaymentMethod {

    private String cardNumber;
    private String securityCode;

    public CreditCardPayment(String cardNumber, String securityCode) {
        if (cardNumber == null || !cardNumber.matches("\\d{6}")) {
            throw new IllegalArgumentException("Card number must be exactly 6 digits.");
        }
        if (securityCode == null || !securityCode.matches("\\d{3}")) {
            throw new IllegalArgumentException("Security code must be exactly 3 digits.");
        }
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    /*
     * Generates a receipt in the format required by the brief:
     * "[amount] paid by Credit Card [cardNumber] on [date]. Billing address: [address]"
     */
    
    @Override
    public Receipt processPayment(double total, Address address) {
        LocalDate today = LocalDate.now();
        String message = String.format("%.2f", total)
                + " paid by Credit Card " + cardNumber
                + " on " + today
                + ". Billing address: " + address.toString();
        return new Receipt(message, today);
    }
}