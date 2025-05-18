/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author 63995
 */

import java.awt.Image;
import newPackage.Product;
import java.sql.Statement;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.ResultSet;

public class Buypage extends javax.swing.JFrame {

    private final Product product;
    private String username;

    public Buypage(Product product, String Username){
        this.username = Username;
        this.product = product;
        initComponents();
        buyername.setText(username);
        populateProductDetails();
        try {
            Connection();
            fetchUserDetails();
        } catch (SQLException ex) {
            Logger.getLogger(Buypage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //onnection method
     Connection con;
     //SQLstatement
     Statement st;
     
     //Required for Connections
     //DbName,DbDriver,Url,USername,Password
     private static final String DbName = "shipzy";
     private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
     private static final String DbUrl = "jdbc:mysql://localhost:3306/"+DbName; //instead of Concatenation i used the value directly
     private static final String DbUsername = "root";
     private static final String DbPassword = "";

     
     //Create a method for connections
     public void Connection() throws SQLException{
        try {
            con = DriverManager.getConnection(DbUrl, DbUsername, DbPassword);
            st = con.createStatement();
            if (con != null){
                System.out.println("Connection Successful");
            }
            Class.forName(DbDriver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     

private void populateProductDetails() {
    // Set default values first (cleaner approach)
    setDefaultProductDetails();
    
    if (product == null) return;
    
    // Product name
    jLabelProductname.setText(getSafeString(product.getProductName(), "No Product Name"));
    
    // Tagline
    jLabelTagline.setText(formatTagline(product.getTagline()));
    
    // Store name
    jLabelStorename.setText(getSafeString(product.getStoreName(), "No Store Name"));
    
    // Price
    jLabelPrice.setText(formatPrice(product.getPrice()));
    
    // Image
    setProductImage(product.getImage1());
    
    // Set spinner model and change listener
    spinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
    spinner.addChangeListener(new javax.swing.event.ChangeListener() {
        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            updateOrderSummary();
        }
    });
    
    // Initialize order summary
    updateOrderSummary();
}

private void updateOrderSummary() {
    if (product == null) return;
    
    try {
        int quantity = (Integer) spinner.getValue();
        double price = product.getPrice();
        double subtotalValue = quantity * price;
        double shippingFee = 50.00; // You can make this dynamic if needed
        double discount = 0.00; // You can add voucher logic here
        double totalValue = subtotalValue + shippingFee - discount;
        
        // Update labels
        Fee.setText(String.format("₱%.2f", shippingFee));
        subtotal.setText(String.format("₱%.2f", subtotalValue));
        shipping.setText(String.format("₱%.2f", shippingFee));
        discounts.setText(String.format("₱%.2f", discount));
        total.setText(String.format("₱%.2f", totalValue));
        
        // Update estimated time based on quantity (example logic)
        updateEstimatedTime(quantity);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error calculating order: " + e.getMessage());
    }
}

private void updateEstimatedTime(int quantity) {
    // Simple estimation - you can make this more sophisticated
    int baseDays = 3;
    int extraDays = quantity / 5; // Add 1 day for every 5 items
    Time.setText((baseDays + extraDays) + " days");
}



// Helper methods
private void setDefaultProductDetails() {
    jLabelProductname.setText("No Product Name");
    jLabelTagline.setText("#NoTagline");
    jLabelStorename.setText("No Store Name");
    jLabelPrice.setText("₱0.00");
    jLabelimage.setIcon(null);
    jLabelimage.setText("No Image");
}

private String getSafeString(String value, String defaultValue) {
    return (value != null && !value.isEmpty()) ? value : defaultValue;
}

private String formatTagline(String tagline) {
    return "#" + getSafeString(tagline, "NoTagline");
}

private String formatPrice(Double price) {
    return "₱" + (price != null ? String.format("%.2f", price) : "0.00");
}

private void setProductImage(byte[] imageData) {
    try {
        if (imageData != null && imageData.length > 0) {
            ImageIcon icon = new ImageIcon(imageData);
            Image img = icon.getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH);
            jLabelimage.setIcon(new ImageIcon(img));
            jLabelimage.setText("");
            return;
        }
    } catch (Exception e) {
        // Fall through to default handling
    }
    jLabelimage.setIcon(null);
    jLabelimage.setText("No Image");
}

private void fetchUserDetails() {
    try {
        String query = "SELECT Location, Contacts FROM users WHERE accUsername = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String userLocation = rs.getString("Location");
            String userContact = rs.getString("Contacts");
            
            // Update the contact label
            contactLabel.setText("Contact: " + (userContact != null ? userContact : "Not set"));
            
            // Update the location label
            locationLabel.setText("Location: " + (userLocation != null ? userLocation : "Not set"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(Buypage.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        spinner = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jLabelPrice = new javax.swing.JLabel();
        jLabelProductname = new javax.swing.JLabel();
        jLabelTagline = new javax.swing.JLabel();
        jLabelStorename = new javax.swing.JLabel();
        jLabelimage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        Changeaddress = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        buyername = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        Time = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Fee = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        subtotal = new javax.swing.JLabel();
        shipping = new javax.swing.JLabel();
        discounts = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        PlaceOrder = new javax.swing.JButton();
        locationLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(42, 58, 41));

        jPanel2.setBackground(new java.awt.Color(90, 133, 86));

        jPanel3.setBackground(new java.awt.Color(69, 125, 88));

        jLabelPrice.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPrice.setText("Price");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabelProductname.setFont(new java.awt.Font("Inknut Antiqua", 0, 24)); // NOI18N
        jLabelProductname.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProductname.setText("Product Name");

        jLabelTagline.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelTagline.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTagline.setText("Tagline");

        jLabelStorename.setFont(new java.awt.Font("Inknut Antiqua", 0, 10)); // NOI18N
        jLabelStorename.setForeground(new java.awt.Color(255, 255, 255));
        jLabelStorename.setText("Store Name");

        jLabelimage.setText("jLabel4");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelimage, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelStorename, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTagline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addComponent(jLabelProductname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelimage, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelProductname, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabelTagline, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelStorename, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(spinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        Changeaddress.setBackground(new java.awt.Color(69, 125, 88));
        Changeaddress.setForeground(new java.awt.Color(255, 255, 255));
        Changeaddress.setText("Change Address");
        Changeaddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeaddressActionPerformed(evt);
            }
        });

        Cancel.setBackground(new java.awt.Color(133, 28, 28));
        Cancel.setForeground(new java.awt.Color(255, 255, 255));
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        buyername.setForeground(new java.awt.Color(255, 255, 255));
        buyername.setText("buyers name");

        contactLabel.setForeground(new java.awt.Color(255, 255, 255));
        contactLabel.setText("Contacts");

        jPanel5.setBackground(new java.awt.Color(80, 113, 77));

        jLabel7.setFont(new java.awt.Font("Inknut Antiqua", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Estimated time: ");

        Time.setFont(new java.awt.Font("Inknut Antiqua", 0, 18)); // NOI18N
        Time.setForeground(new java.awt.Color(255, 255, 255));
        Time.setText("time");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Shipping Fee: ");

        Fee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Fee.setForeground(new java.awt.Color(255, 255, 255));
        Fee.setText("Fee");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Time, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Fee, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Time, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Fee))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(90, 133, 86));

        jLabel2.setFont(new java.awt.Font("Inknut Antiqua", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(129, 255, 221));
        jLabel2.setText("Order Summary");

        jLabel3.setFont(new java.awt.Font("Inknut Antiqua", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SubTotal:");

        jLabel5.setFont(new java.awt.Font("Inknut Antiqua", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Shipping: ");

        jLabel6.setFont(new java.awt.Font("Inknut Antiqua", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Discounts(voucher):");

        jLabel8.setFont(new java.awt.Font("Inknut Antiqua", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Total:");

        subtotal.setForeground(new java.awt.Color(255, 255, 255));
        subtotal.setText("jLabel9");

        shipping.setForeground(new java.awt.Color(255, 255, 255));
        shipping.setText("jLabel10");

        discounts.setForeground(new java.awt.Color(255, 255, 255));
        discounts.setText("jLabel11");

        total.setForeground(new java.awt.Color(255, 255, 255));
        total.setText("jLabel12");

        PlaceOrder.setBackground(new java.awt.Color(69, 125, 88));
        PlaceOrder.setForeground(new java.awt.Color(255, 255, 255));
        PlaceOrder.setText("Place Order");
        PlaceOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlaceOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(subtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(shipping, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(discounts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PlaceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(subtotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(shipping)
                        .addGap(12, 12, 12)
                        .addComponent(discounts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(PlaceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        locationLabel.setForeground(new java.awt.Color(255, 255, 255));
        locationLabel.setText("Location");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(locationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Cancel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(buyername, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(contactLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                                .addComponent(Changeaddress)))
                        .addGap(14, 14, 14)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buyername)
                    .addComponent(contactLabel)
                    .addComponent(Changeaddress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancel)
                    .addComponent(locationLabel))
                .addGap(7, 7, 7)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ChangeaddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeaddressActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ChangeaddressActionPerformed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_CancelActionPerformed

    private void PlaceOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlaceOrderActionPerformed
        // Show payment method dialog
        String[] options = {"Cash on Delivery", "Online Payment"};
        int choice = javax.swing.JOptionPane.showOptionDialog(
            this,
            "Choose your payment method:",
            "Payment Method",
            javax.swing.JOptionPane.DEFAULT_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        if (choice == javax.swing.JOptionPane.CLOSED_OPTION) {
            // User closed the dialog, do not proceed
            return;
        }
        String paymentMethod = options[choice];
        try {
            int quantity = (Integer) spinner.getValue();
            // Update the SQL query to include username and payment
            String query = "INSERT INTO orders (username, product_name, store_name, price, quantity, total_amount, payment) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);  // Add username as first parameter
            pst.setString(2, product.getProductName());
            pst.setString(3, product.getStoreName());
            pst.setDouble(4, product.getPrice());
            pst.setInt(5, quantity);
            pst.setDouble(6, Double.parseDouble(total.getText().replace("₱", "")));
            pst.setString(7, paymentMethod);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Order Placed Successfully!");
                this.dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to place order");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Buypage.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Error Placing Order: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error parsing total amount");
        }
    }//GEN-LAST:event_PlaceOrderActionPerformed

    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Buypage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Buypage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Buypage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Buypage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { 
                String Username = null;
                new Buypage(product, Username).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JButton Changeaddress;
    private javax.swing.JLabel Fee;
    private javax.swing.JButton PlaceOrder;
    private javax.swing.JLabel Time;
    private javax.swing.JLabel buyername;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JLabel discounts;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelPrice;
    private javax.swing.JLabel jLabelProductname;
    private javax.swing.JLabel jLabelStorename;
    private javax.swing.JLabel jLabelTagline;
    private javax.swing.JLabel jLabelimage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel shipping;
    private javax.swing.JSpinner spinner;
    private javax.swing.JLabel subtotal;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
