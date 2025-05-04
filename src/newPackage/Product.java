package newPackage;

public class Product {
    private int productId;          // Unique ID for the product
    private String productName;     // Name of the product
    private String storeName;       // Name of the store selling the product
    private String type;            // Type of product (e.g., Electronics, Clothing, etc.)
    private String tagline;         // Short description or tagline for the product
    private double price;           // Current price of the product
    private double originalPrice;   // Original price of the product (for discounts)
    private byte[] image1;          // First image of the product
    private byte[] image2;          // Second image of the product
    private double rating;          // Average rating of the product
    private String location;        // Location associated with the product

    // Constructor
    public Product(int productId, String productName, String storeName, String type, String tagline,
                   double price, double originalPrice, byte[] image1, byte[] image2, double rating, String location) {
        this.productId = productId;
        this.productName = productName;
        this.storeName = storeName;
        this.type = type;
        this.tagline = tagline;
        this.price = price;
        this.originalPrice = originalPrice;
        this.image1 = image1;
        this.image2 = image2;
        this.rating = rating;
        this.location = location;
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getType() {
        return type;
    }

    public String getTagline() {
        return tagline;
    }

    public double getPrice() {
        return price;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public byte[] getImage1() {
        return image1;
    }

    public byte[] getImage2() {
        return image2;
    }

    public double getRating() {
        return rating;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Method to calculate the discount percentage
    public double getDiscountPercentage() {
        if (originalPrice > price) {
            return 100 * (originalPrice - price) / originalPrice;
        }
        return 0;
    }

    // Override toString for debugging purposes
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", type='" + type + '\'' +
                ", tagline='" + tagline + '\'' +
                ", price=" + price +
                ", originalPrice=" + originalPrice +
                ", rating=" + rating +
                ", location='" + location + '\'' +
                '}';
    }
}