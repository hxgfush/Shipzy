package newPackage;

public class CartItem {
    private int cartId;
    private String productName;
    private String storeName;
    private double price;
    private int quantity;

    public CartItem(int cartId, String productName, String storeName, double price, int quantity) {
        this.cartId = cartId;
        this.productName = productName;
        this.storeName = storeName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public int getCartId() { return cartId; }
    public String getProductName() { return productName; }
    public String getStoreName() { return storeName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}