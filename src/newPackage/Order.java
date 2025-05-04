/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newPackage;

/**
 *
 * @author Analyn Omac
 */
public class Order {
    private int orderId;
    private String productName;
    private String storeName;
    private double price;
    private int quantity;
    private double totalAmount;
    
     public Order(int orderId, String productName, String storeName, double price, int quantity, double totalAmount) {
        this.orderId = orderId;
        this.productName = productName;
        this.storeName = storeName;
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }
    
    // Add getters here
    public int getOrderId() {return orderId;}
    public String getProductName() { return productName; }
    public String getStoreName() { return storeName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
}
