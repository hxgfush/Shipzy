package newPackage;

/**
 * Represents a product with all its details including images, pricing, and location.
 */
public class Product {
    private  int productId;
    private  String productName;
    private  String storeName;
    private  String tagline;
    private  double price;
    private final double originalPrice;
    private final byte[] image1;
    private final byte[] image2;
    private final double rating;
    private final String location;

    /**
     * Constructs a Product with all details.
     * 
     * @param productId Unique product identifier
     * @param productName Name of the product
     * @param storeName Name of the store selling the product
     * @param tagline Short description or tagline
     * @param price Current selling price
     * @param originalPrice Original price (for showing discounts)
     * @param image1 Primary product image
     * @param image2 Secondary product image
     * @param rating Product rating (0-5)
     * @param location Product/store location
     */
    public Product(int productId, String productName, String storeName, String tagline, 
                  double price, double originalPrice, byte[] image1, byte[] image2, 
                  double rating, String location) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0 || originalPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        this.productId = productId;
        this.productName = productName.trim();
        this.storeName = storeName != null ? storeName.trim() : "";
        this.tagline = tagline != null ? tagline.trim() : "";
        this.price = price;
        this.originalPrice = originalPrice;
        this.image1 = image1 != null ? image1 : new byte[0];
        this.image2 = image2 != null ? image2 : new byte[0];
        this.rating = rating;
        this.location = location != null ? location.trim() : "";
    }

    // Getters only (immutable)
    public int getProductId() { return productId; }
    
    public String getProductName() {
        return productName;
    }
    
    public String getTagline() {
        return tagline;
    }
    
    public String getStoreName() {
        return storeName;
    }
    
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    
    public byte[] getImage1() { 
        return image1 != null ? image1.clone() : new byte[0]; 
    }
    
    public byte[] getImage2() { 
        return image2 != null ? image2.clone() : new byte[0]; 
    }
    
    public double getRating() { return rating; }
    public String getLocation() { return location; }

    /**
     * Returns whether the product is on discount.
     */
    public boolean isOnDiscount() {
        return originalPrice > price;
    }

    /**
     * Calculates discount percentage.
     */
    public double getDiscountPercentage() {
        return isOnDiscount() ? 
               ((originalPrice - price) / originalPrice) * 100 : 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Product[id=%d, name='%s', store='%s', price=%.2f, rating=%.1f]",
            productId, productName, storeName, price, rating
        );
    }
   public void setProductName(String productName) {
    if (productName == null || productName.trim().isEmpty()) {
        throw new IllegalArgumentException("Product name cannot be empty");
    }
    this.productName = productName.trim();
}

public void setTagline(String tagline) {
    this.tagline = tagline != null ? tagline.trim() : "";
}

public void setPrice(double price) {
    if (price < 0) {
        throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
}
 
}