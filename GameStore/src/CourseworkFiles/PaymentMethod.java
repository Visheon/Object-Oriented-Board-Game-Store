package CourseworkFiles;

import Payment.Receipt;
import User.Address;

public interface PaymentMethod {

    /**
     * Processes a payment for the specified amount.
     */
    Receipt processPayment(double total, Address address);
}
