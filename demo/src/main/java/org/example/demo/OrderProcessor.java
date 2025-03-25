package org.example.demo;

public class OrderProcessor {

    public void processOrder(String orderId, double amount) {
        System.out.println("Processing order: " + orderId);
        if (amount > 0) {
            double tax = amount * 0.1;
            double totalAmount = amount + tax;
            System.out.println("Order Total (with tax): " + totalAmount);
        } else {
            System.out.println("Invalid order amount.");
        }
    }

    public void printOrderDetails(String orderId, double amount) {
        System.out.println("Order ID: " + orderId);
        System.out.println("Amount: " + amount);
    }
}