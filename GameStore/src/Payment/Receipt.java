package Payment;

import java.time.LocalDate;


 // Represents a receipt generated after a successful payment.

public class Receipt {

    private String message;
    private LocalDate date;


    // Constructs a Receipt with the specified message and date.	

    public Receipt(String message, LocalDate date) {
        this.message = message;
        this.date = date;
    }


     // Returns the receipt message.
    public String getMessage() {
        return message;
    }


     // Returns the date the payment was processed.

    public LocalDate getDate() {
        return date;
    }


    // Prints the receipt message to the screen.
    @Override
    public String toString() {
        return message;
    }
}
