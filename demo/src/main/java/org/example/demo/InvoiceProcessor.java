package org.example.demo;

public class InvoiceProcessor {

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

    public void printInvoiceDetails(String invoiceId, double amount) {
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Amount: " + amount);
    }
}
