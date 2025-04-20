package org.example.demo;

public class InvoiceProcessor {

    /**
     * Processes an invoice by validating the amount and printing invoice details.
     *
     * <p>If the provided amount is greater than zero, the method calculates a 10% tax, computes the total
     * amount (original amount plus tax), and prints the resulting total. If the amount is not greater than zero,
     * it prints an error message indicating an invalid invoice amount.
     *
     * @param invoiceId the unique identifier for the invoice
     * @param amount the invoice amount to be processed
     */
    public void processInvoice(String invoiceId, double amount) {
        System.out.println("Processing invoice: " + invoiceId);
        if (amount > 0) {
            double tax = amount * 0.1;
            double totalAmount = amount + tax;
            System.out.println("Invoice Total (with tax): " + totalAmount);
        } else {
            System.out.println("Invalid invoice amount.");
        }
    }

    /**
     * Prints the details of an invoice to the console.
     *
     * <p>This method displays the invoice identifier and its corresponding amount on separate lines.</p>
     *
     * @param invoiceId the unique identifier for the invoice
     * @param amount the monetary value of the invoice
     */
    public void printInvoiceDetails(String invoiceId, double amount) {
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Amount: " + amount);
    }
}
