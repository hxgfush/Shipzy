/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newPackage;

import java.sql.Timestamp;

public class Feedback {
    private String username;
    private String productName;
    private int rating;
    private String reviewMessage;
    private Timestamp reviewDate;
    private String storeName;
    
    public Feedback(String username, String productName, int rating, String reviewMessage, 
                   Timestamp reviewDate, String storeName) {
        this.username = username;
        this.productName = productName;
        this.rating = rating;
        this.reviewMessage = reviewMessage;
        this.reviewDate = reviewDate;
        this.storeName = storeName;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getProductName() { return productName; }
    public int getRating() { return rating; }
    public String getReviewMessage() { return reviewMessage; }
    public Timestamp getReviewDate() { return reviewDate; }
    public String getStoreName() { return storeName; }
}
