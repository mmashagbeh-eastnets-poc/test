package org.example.demo;

public class OrderProcessor {

    /**
     * Processes the order by calculating and printing the total amount including a 10% tax for valid amounts.
     * <p>
     * If the provided amount is greater than zero, a 10% tax is applied and the total amount (base amount plus tax)
     * is printed. Otherwise, an error message is printed indicating an invalid order amount.
     *
     * @param orderId the unique identifier of the order
     * @param amount  the base amount of the order before tax computation
     */
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

    /**
     * Prints the order details to the console.
     *
     * <p>This method outputs the order ID and the associated amount to the standard output.</p>
     *
     * @param orderId the unique identifier for the order
     * @param amount the amount associated with the order
     */
    public void printOrderDetails(String orderId, double amount) {
        System.out.println("Order ID: " + orderId);
        System.out.println("Amount: " + amount);
    }
}