package newPackage;

public class Order {
    private int orderId;
    private boolean rated;
    private String username; // Buyer's username
    private String productName;
    private String storeName;
    private double price;
    private int quantity;
    private double totalAmount;
    private String status; // Order status (e.g., pending, shipped, delivered)
    private String paymentType; // Payment type (e.g., Cash on Delivery, Online Payment)

    // Constructor
    public Order(int orderId, String username, String productName, String storeName, double price, int quantity, double totalAmount, String status, String paymentType) {
        this.orderId = orderId;
        this.username = username;
        this.productName = productName;
        this.storeName = storeName;
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentType = paymentType;
    }

     public void setRated(boolean rated) {
        this.rated = rated;
    }
    
    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    // Setters
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalAmount(); // Recalculate total amount when quantity changes
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    // Method to calculate total amount
    public void calculateTotalAmount() {
        this.totalAmount = this.price * this.quantity;
    }

    
    public boolean isRated() {
        return rated;
    }
    // Override toString for better debugging
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", username='" + username + '\'' +
                ", productName='" + productName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}