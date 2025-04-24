package newPackage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 63995
 */
public class Product {
    private final int productId;
    private String productName;
    private final String storeName;
    private String tagline;
    private double price;
    private double originalPrice;
    private final byte[] image1;
    private final byte[] image2;
    private double rating;
    private String location;

    public Product(int productId, String productName, String storeName, double price, byte[] image1, byte[] image2) {
        this.productId = productId;
        this.productName = productName;
        this.storeName = storeName;
        this.tagline = tagline;
        this.price = price;
        this.originalPrice = originalPrice; 
        this.image1 = image1;
        this.image2 = image2;
        this.rating = rating;
        this.location = location;
    }



       
     public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setOriginalprice(double originalPrice){
        this.originalPrice = originalPrice; 
    }

    
   
    // Add all getters
    
    public String getProductName() {
        return productName != null ? productName : "";
    }
    
    public String getTagline() {
        return tagline != null ? tagline : "";
    }
    
    public String getStoreName() {
        return storeName != null ? storeName : "";
    }
    
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    public byte[] getImage1() { return image1; }
    public byte[] getImage2() { return image2; }
    public double getRating() { return rating; }
    public String getLocation() { return location; }

}
