/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author 63995
 */
import newPackage.Feedback;
import newPackage.Order;
import newPackage.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.sql.Connection;         /*This the import section which get the fumctoionality from the libraries */
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class SellerPage extends javax.swing.JFrame {    /*This the is the sellerPage class*/
    private String storename;
    private String Name;
    private String location;
    private int id;
    private String contacts;
    private String username;

    /**
     * Creates new form SellerPage
     */
    public SellerPage(String storename, String Username, String Name, String Location, int ID, String Contacts) {
        initComponents();
        this.username = Username;
         this.location = Location;
         this.storename = storename;
         this.id = ID;
         this.contacts = Contacts;
         infoStorename.setText(storename);
         infoContacts.setText(String.valueOf(contacts));
         infoLocation.setText(location);
         infoID.setText(String.valueOf(id));
         editPanel.setVisible(false);
        jLabel3.setText(storename); 
        jLabel3.setText(this.storename != null ? this.storename : "Unnamed Store");  /*This is used to display the storename when enterimg the frame*/
        this.Name = Name;
        owberLabel.setText(Name); 
        owberLabel.setText(this.Name != null ? this.Name : "Unnamed Seller");  /*This is used to display the store owner name when enterimg the frame*/
        searchBTN.addActionListener(e -> performSearch());
        searchTXT.addActionListener(e -> performSearch());
        try {
            getConnection();
            displayProductCards();
        } catch (SQLException ex) {
            Logger.getLogger(SellerPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Connection con;   /*This block of code is used to initiate connection to the databse*/
    
    private static final String DbName = "shipzy";
    private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DbUrl = "jdbc:mysql://localhost:3306/"+DbName;     
    private static final String DbUsername = "root";
    private static final String DbPassword = "";

    
  private Connection getConnection() throws SQLException {  /*This block of code is a method to connect to the databse*/
    if (con == null || con.isClosed()) {
        try {
            Class.forName(DbDriver);
            con = DriverManager.getConnection(DbUrl, DbUsername, DbPassword);    
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }
    }
    return con;
}
  
//GRAPHICS
public class WrapLayout extends FlowLayout { //This block of code is used to arange the card
    
    public WrapLayout() {
        super();
    }
    
    public WrapLayout(int align) {
        super(align);
    }
    
    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }
    
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }
    
    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }
    
    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            
            if (targetWidth == 0)
                targetWidth = Integer.MAX_VALUE;
            
            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
            
            Dimension dim = new Dimension(0, 0);
            int rowWidth = 0;
            int rowHeight = 0;
            
            for (Component comp : target.getComponents()) {
                if (comp.isVisible()) {
                    Dimension d = preferred ? comp.getPreferredSize() : comp.getMinimumSize();
                    
                    if (rowWidth + d.width > maxWidth) {
                        dim.width = Math.max(dim.width, rowWidth);
                        dim.height += rowHeight + vgap;
                        rowWidth = 0;
                        rowHeight = 0;
                    }
                    
                    if (rowWidth != 0) {
                        rowWidth += hgap;
                    }
                    
                    rowWidth += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                }
            }
            
            dim.width = Math.max(dim.width, rowWidth);
            dim.height += rowHeight;
            
            dim.width += insets.left + insets.right + hgap * 2;
            dim.height += insets.top + insets.bottom + vgap * 2;
            
            return dim;
        }
    }
}   
 

//METHODS
private void editProduct(Product product, JPanel card, String originalName) {
    // Create a dialog for editing
    JDialog editDialog = new JDialog(this, "Edit Product", true);
    editDialog.setLayout(new BorderLayout());

    // Form panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Calculate discount percentage if applicable
    double discountPercent = 0;
    if (product.getOriginalPrice() > 0 && product.getPrice() < product.getOriginalPrice()) {
        discountPercent = ((product.getOriginalPrice() - product.getPrice()) / product.getOriginalPrice()) * 100;
    }

    // Form fields
    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    namePanel.add(new JLabel("Product Name:"));
    JTextField nameField = new JTextField(product.getProductName(), 20);
    namePanel.add(nameField);
    
    JPanel taglinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    taglinePanel.add(new JLabel("Tagline:"));
    JTextField taglineField = new JTextField(product.getTagline(), 20);
    taglinePanel.add(taglineField);
    
    // Original Price Panel
    JPanel originalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    originalPricePanel.add(new JLabel("Original Price:"));
    JTextField originalPriceField = new JTextField(String.valueOf(product.getOriginalPrice() > 0 ? 
        product.getOriginalPrice() : product.getPrice()), 10);
    originalPricePanel.add(originalPriceField);
    
    // Discount Percentage Panel
    JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    discountPanel.add(new JLabel("Discount %:"));
    JTextField discountField = new JTextField(String.format("%.0f", discountPercent), 5);
    discountPanel.add(discountField);
    
    // Final Price Panel (read-only)
    JPanel finalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    finalPricePanel.add(new JLabel("Final Price:"));
    JLabel finalPriceLabel = new JLabel(String.format("₱%.2f", product.getPrice()));
    finalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    finalPricePanel.add(finalPriceLabel);
    
    // Add listener to recalculate price when discount changes
    discountField.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) { updatePrice(); }
        @Override
        public void removeUpdate(DocumentEvent e) { updatePrice(); }
        @Override
        public void changedUpdate(DocumentEvent e) { updatePrice(); }
        
        private void updatePrice() {
            try {
                double originalPrice = Double.parseDouble(originalPriceField.getText());
                double discount = Double.parseDouble(discountField.getText());
                
                if (discount >= 0 && discount <= 100) {
                    double finalPrice = originalPrice * (1 - (discount / 100));
                    finalPriceLabel.setText(String.format("₱%.2f", finalPrice));
                }
            } catch (NumberFormatException ex) {
                // Ignore invalid inputs
            }
        }
    });
    
    // Image panels (same as before)
    JPanel image1Panel = new JPanel(new BorderLayout());
    image1Panel.add(new JLabel("Image 1:"), BorderLayout.NORTH);
    JLabel image1Label = new JLabel();
    if (product.getImage1() != null && product.getImage1().length > 0) {
        ImageIcon icon = new ImageIcon(product.getImage1());
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        image1Label.setIcon(new ImageIcon(img));
    } else {
        image1Label.setText("No Image");
    }
    JButton changeImage1Btn = new JButton("Change Image 1");
    changeImage1Btn.addActionListener(e -> {
        byte[] newImage = selectImage();
        if (newImage != null) {
            product.setImage1(newImage);
            ImageIcon icon = new ImageIcon(newImage);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            image1Label.setIcon(new ImageIcon(img));
            image1Label.setText("");
        }
    });
    image1Panel.add(image1Label, BorderLayout.CENTER);
    image1Panel.add(changeImage1Btn, BorderLayout.SOUTH);
    
    JPanel image2Panel = new JPanel(new BorderLayout());
    image2Panel.add(new JLabel("Image 2:"), BorderLayout.NORTH);
    JLabel image2Label = new JLabel();
    if (product.getImage2() != null && product.getImage2().length > 0) {
        ImageIcon icon = new ImageIcon(product.getImage2());
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        image2Label.setIcon(new ImageIcon(img));
    } else {
        image2Label.setText("No Image");
    }
    JButton changeImage2Btn = new JButton("Change Image 2");
    changeImage2Btn.addActionListener(e -> {
        byte[] newImage = selectImage();
        if (newImage != null) {
            product.setImage2(newImage);
            ImageIcon icon = new ImageIcon(newImage);
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            image2Label.setIcon(new ImageIcon(img));
            image2Label.setText("");
        }
    });
    image2Panel.add(image2Label, BorderLayout.CENTER);
    image2Panel.add(changeImage2Btn, BorderLayout.SOUTH);
    
    // Add components to form panel
    formPanel.add(namePanel);
    formPanel.add(taglinePanel);
    formPanel.add(originalPricePanel);
    formPanel.add(discountPanel);
    formPanel.add(finalPricePanel);
    formPanel.add(image1Panel);
    formPanel.add(image2Panel);
    
    // Button panel
    JPanel buttonPanel = new JPanel();
    JButton saveButton = new JButton("Save Changes");
    saveButton.addActionListener(e -> {
        try {
            // Update the product object
            product.setProductName(nameField.getText());
            product.setTagline(taglineField.getText());
            
            double originalPrice = Double.parseDouble(originalPriceField.getText());
            double newDiscountPercent = Double.parseDouble(discountField.getText());
            
            if (newDiscountPercent > 0) {
                double finalPrice = originalPrice * (1 - (newDiscountPercent / 100));
                product.setOriginalPrice(originalPrice);
                product.setPrice(finalPrice);
            } else {
                // No discount
                product.setOriginalPrice(0); // Or originalPrice if you want to keep it
                product.setPrice(originalPrice);
            }
            
            // Update database with images and prices
            updateProductInDatabaseWithImagesAndPrices(product);
            
            // Refresh the card
            refreshCard(card, product);
            
            editDialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for prices/discount", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    buttonPanel.add(saveButton);

    // Wrap the formPanel in a scroll pane
    JScrollPane scrollPane = new JScrollPane(formPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    // Set a smaller preferred size for the dialog
    editDialog.setPreferredSize(new Dimension(500, 500)); // Adjusted for comfort

    editDialog.add(scrollPane, BorderLayout.CENTER);
    editDialog.add(buttonPanel, BorderLayout.SOUTH);

    // Pack to fit content, then center
    editDialog.pack();
    editDialog.setLocationRelativeTo(this);
    editDialog.setVisible(true);
}
private byte[] selectImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Product Image");
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            
            String extension = getExtension(f);
            if (extension != null) {
                return extension.equalsIgnoreCase("jpg") || 
                       extension.equalsIgnoreCase("jpeg") ||
                       extension.equalsIgnoreCase("png") ||
                       extension.equalsIgnoreCase("gif");
            }
            return false;
        }
        
        public String getDescription() {
            return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
        }
        
        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');
            
            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }
            return ext;
        }
    });
    
    int returnValue = fileChooser.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            
            return bos.toByteArray();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading image: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    return null;
}
private void updateProductInDatabaseWithImagesAndPrices(Product product) {
    String query = "UPDATE products SET product_name=?, tagline=?, price=?, original_price=?, image1=?, image2=? WHERE product_id=?";
    
    try {
        PreparedStatement param = con.prepareStatement(query);
        param.setString(1, product.getProductName());
        param.setString(2, product.getTagline());
        param.setDouble(3, product.getPrice());
        param.setDouble(4, product.getOriginalPrice());
        
        // Set images (can be null)
        if (product.getImage1() != null) {
            param.setBytes(5, product.getImage1());
        } else {
            param.setNull(5, Types.BLOB);
        }
        
        if (product.getImage2() != null) {
            param.setBytes(6, product.getImage2());
        } else {
            param.setNull(6, Types.BLOB);
        }
        
        param.setInt(7, product.getProductId());

        int rowsAffected = param.executeUpdate();

        if (rowsAffected == 0) {
            JOptionPane.showMessageDialog(this, 
                "No product was updated. Product may not exist.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Product updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Database error: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
private void deleteProduct(Product product, JPanel card) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    
    try {
        conn = getConnection();
        String query = "DELETE FROM products WHERE product_id = ? AND storename = ?";
        
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, product.getProductId());
        pstmt.setString(2, storename);
        
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
            // Remove from UI only if database delete succeeded
            Container parent = card.getParent();
            parent.remove(card);
            parent.revalidate();
            parent.repaint();
            
            JOptionPane.showMessageDialog(this, "Product deleted successfully");
        } else {
            JOptionPane.showMessageDialog(this, 
                "No product was deleted. Either it doesn't exist or you don't have permission.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Database error: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } finally {
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
        }
    }
}
private void deleteOrder(Order order, JPanel card) {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete this order record?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String query = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, order.getOrderId());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                // Remove the order card from the UI
                Container parent = card.getParent();
                parent.remove(card);
                parent.revalidate();
                parent.repaint();

                JOptionPane.showMessageDialog(this, "Order deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete the order. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
private void markOrderAsReceived(Order order, JPanel card) {
    try {
        String query = "UPDATE orders SET status = ? WHERE order_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, "received"); // Update status to "received"
        pst.setInt(2, order.getOrderId());

        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Order marked as received.");
            
            // Optionally, update the UI to reflect the status change
            JLabel statusLabel = new JLabel("Status: Received");
            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            statusLabel.setForeground(Color.GREEN);
            card.add(statusLabel);
            card.revalidate();
            card.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update order status. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
private void cancelOrder(Order order, JPanel card) {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to cancel this order?",
        "Cancel Order",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String query = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, order.getOrderId());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                // Remove the order card from the UI
                Container parent = card.getParent();
                parent.remove(card);
                parent.revalidate();
                parent.repaint();

                JOptionPane.showMessageDialog(this, "Order canceled successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel the order. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
private void refreshCard(JPanel card, Product product) {  //This block of code is used to refresh cards that display the product
    // Remove all components from the card
    card.removeAll();
    
    // Recreate the card with updated product
    JPanel newCard = createProductCard(product);
    
    // Copy the new card's components to the old card
    for (Component comp : newCard.getComponents()) {
        card.add(comp);
    }
    
    // Refresh the UI
    card.revalidate();
    card.repaint();
}
private List<Feedback> getStoreFeedbacks(String storename) throws SQLException {
    List<Feedback> feedbacks = new ArrayList<>();
    String query = "SELECT pr.username, pr.product_name, pr.rating, pr.review_message, pr.review_date, " +
                  "p.storename " +
                  "FROM product_ratings pr " +
                  "JOIN products p ON pr.product_name = p.product_name " +
                  "WHERE p.storename = ? " +
                  "ORDER BY pr.review_date DESC";
    
    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, storename);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            String username = rs.getString("username");
            String productName = rs.getString("product_name");
            int rating = rs.getInt("rating");
            String reviewMessage = rs.getString("review_message");
            java.sql.Timestamp reviewDate = rs.getTimestamp("review_date"); // Explicitly use java.sql.Timestamp
            String storeName = rs.getString("storename");
            
            feedbacks.add(new Feedback(username, productName, rating, reviewMessage, reviewDate, storeName));
        }
    }
    return feedbacks;
}
private void performSearch() {
    String searchTerm = searchTXT.getText().trim().toLowerCase();
    
    if (searchTerm.isEmpty()) {
        displayProductCards(); // Show all products if search is empty
        return;
    }
    
    try {
        jTabbedPane2.removeAll();
        jTabbedPane2.addTab("Products", new JLabel("Searching products..."));
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            JPanel cardsPanel;
            
            @Override
            protected Void doInBackground() throws Exception {
                Connection conn = getConnection();
                ProductDAO productDAO = new ProductDAO(conn);
                List<Product> allProducts = productDAO.getProductsByStore(storename);
                
                // Filter products based on search term
                List<Product> filteredProducts = allProducts.stream()
                    .filter(p -> p.getProductName().toLowerCase().contains(searchTerm) || 
                                p.getTagline().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
                
                cardsPanel = new JPanel();
                cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
                cardsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                cardsPanel.setBackground(new Color(62,93,59));
                
                if (filteredProducts.isEmpty()) {
                    cardsPanel.add(new JLabel("No products match your search: " + searchTerm));
                } else {
                    for (Product product : filteredProducts) {
                        JPanel card = createProductCard(product);
                        card.setAlignmentX(Component.LEFT_ALIGNMENT);
                        cardsPanel.add(card);
                        cardsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JScrollPane scrollPane = new JScrollPane(cardsPanel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.setBackground(new Color(62,93,59));
                    scrollPane.setBorder(null);
                    
                    jTabbedPane2.removeAll();
                    jTabbedPane2.addTab("Search Results", scrollPane);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SellerPage.this, 
                        "Error searching products: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error initializing search: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

//LOADER
private List<Order> getOrdersForStore(String storename) throws SQLException {
    List<Order> orders = new ArrayList<>();
    String query = "SELECT order_id, username, product_name, store_name, price, quantity, total_amount, status, payment " +
                   "FROM orders WHERE store_name = ?";

    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, storename); // Use the store name to filter orders
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            int orderId = rs.getInt("order_id");
            String username = rs.getString("username"); // Buyer's username
            String productName = rs.getString("product_name");
            String storeName = rs.getString("store_name");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("quantity");
            double totalAmount = rs.getDouble("total_amount");
            String status = rs.getString("status"); // Fetch status
            String paymentType = rs.getString("payment");
            // Create an Order object and add it to the list
            orders.add(new Order(orderId, username, productName, storeName, price, quantity, totalAmount, status, paymentType));
        }
    }
    return orders;
} 

//DISPLAYER
private void displayProductCards() {
    try {
        jTabbedPane2.removeAll();
        jTabbedPane2.addTab("Products", new JLabel("Loading products..."));

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            JPanel cardsPanel;
            
            @Override
            protected Void doInBackground() throws Exception {
                Connection conn = getConnection();
                ProductDAO productDAO = new ProductDAO(conn);
                List<Product> storeProducts = productDAO.getProductsByStore(storename); // Changed to use storename filter
                
                cardsPanel = new JPanel();
                cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
                cardsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                cardsPanel.setBackground(new Color(62,93,59));
                
                if (storeProducts.isEmpty()) {
                    cardsPanel.add(new JLabel("No products found for your store"));
                } else {
                    for (Product product : storeProducts) {
                        JPanel card = createProductCard(product);
                        card.setAlignmentX(Component.LEFT_ALIGNMENT);
                        cardsPanel.add(card);
                        cardsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JScrollPane scrollPane = new JScrollPane(cardsPanel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.setBackground(new Color(62,93,59));
                    scrollPane.setBorder(null);
                    
                    jTabbedPane2.removeAll();
                    jTabbedPane2.addTab("My Products", scrollPane); // Changed tab name
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SellerPage.this, 
                        "Error loading products: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error initializing product display: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void displayOrdersForStore() {
    try {
        orderTAB.removeAll();
        orderTAB.addTab("Orders", new JLabel("Loading orders..."));

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            JPanel ordersPanel;

            @Override
            protected Void doInBackground() throws Exception {
                Connection conn = getConnection();
                List<Order> orders = getOrdersForStore(storename); // Fetch orders for this store

                ordersPanel = new JPanel();
                ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
                ordersPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                ordersPanel.setBackground(new Color(62, 93, 59));

                if (orders.isEmpty()) {
                    ordersPanel.add(new JLabel("No orders found for your store"));
                } else {
                    for (Order order : orders) {
                        JPanel orderCard = createOrderCard(order);
                        orderCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                        ordersPanel.add(orderCard);
                        ordersPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Ensure background task completed successfully
                    JScrollPane scrollPane = new JScrollPane(ordersPanel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.setBackground(new Color(62, 93, 59));
                    scrollPane.setBorder(null);

                    orderTAB.removeAll();
                    orderTAB.addTab("Store Orders", scrollPane);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SellerPage.this,
                        "Error loading orders: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error initializing order display: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void displayFeedbacks() {
    try {
        feedbackTAB.removeAll();
        feedbackTAB.addTab("Feedbacks", new JLabel("Loading feedbacks..."));
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            JPanel feedbackPanel;
            
            @Override
            protected Void doInBackground() throws Exception {
                Connection conn = getConnection();
                List<Feedback> feedbacks = getStoreFeedbacks(storename);
                
                feedbackPanel = new JPanel();
                feedbackPanel.setLayout(new BoxLayout(feedbackPanel, BoxLayout.Y_AXIS));
                feedbackPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                feedbackPanel.setBackground(new Color(62, 93, 59));
                
                if (feedbacks.isEmpty()) {
                    JLabel noFeedbackLabel = new JLabel("No feedbacks available for your store yet");
                    noFeedbackLabel.setForeground(Color.WHITE);
                    feedbackPanel.add(noFeedbackLabel);
                } else {
                    for (Feedback feedback : feedbacks) {
                        JPanel feedbackCard = createFeedbackCard(feedback);
                        feedbackCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                        feedbackPanel.add(feedbackCard);
                        feedbackPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JScrollPane scrollPane = new JScrollPane(feedbackPanel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.setBackground(new Color(62, 93, 59));
                    scrollPane.setBorder(null);
                    
                    feedbackTAB.removeAll();
                    feedbackTAB.addTab("Customer Feedbacks", scrollPane);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SellerPage.this,
                        "Error loading feedbacks: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error initializing feedback display: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

//CREATOR
private JPanel createProductCard(Product product) {// This function used for creating the card 
    // Main card panel with horizontal BoxLayout
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    card.setBackground(new Color(69, 125, 88));
    // Set fixed size for the product card
    Dimension fixedSize = new Dimension(700, 200);
    card.setPreferredSize(fixedSize);
    card.setMaximumSize(fixedSize);
    card.setMinimumSize(fixedSize);

    // Left panel for text content
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setOpaque(false);

    // Store name (top-left)
    JLabel storeLabel = new JLabel(product.getStoreName());
    storeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    storeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    storeLabel.setForeground(Color.WHITE);
    leftPanel.add(storeLabel);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Product name (left-aligned and bold)
    JLabel nameLabel = new JLabel(product.getProductName());
    nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    nameLabel.setForeground(Color.WHITE);
    leftPanel.add(nameLabel);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Tagline (left-aligned with hashtag)
    JLabel taglineLabel = new JLabel("#" + product.getTagline());
    taglineLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    taglineLabel.setForeground(Color.WHITE);
    taglineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(taglineLabel);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Edit/Delete buttons (left-aligned)
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    buttonPanel.setOpaque(false);

    JButton editButton = new JButton("Edit");
    editButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    editButton.setBackground(new Color(69, 125, 88));
    editButton.setForeground(Color.WHITE);
    editButton.setBorderPainted(false);
    editButton.addActionListener(e -> {
    String originalName = product.getProductName(); // Store original name
    editProduct(product, card, originalName);
});


    JButton deleteButton = new JButton("Delete");
    deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    deleteButton.setBackground(new Color(200, 50, 50));
    deleteButton.setForeground(Color.WHITE);
    deleteButton.setBorderPainted(false);
    deleteButton.addActionListener(e -> {
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
            SellerPage.this,
            "Are you sure you want to delete this product?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            deleteProduct(product, card); // We'll implement this method
        }
    });
    
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    leftPanel.add(buttonPanel);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Price (left-aligned)
    JLabel priceLabel = new JLabel(String.format("₱ %.2f", product.getPrice()));
    priceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    priceLabel.setForeground(Color.WHITE);
    leftPanel.add(priceLabel);
    leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Location (left-aligned)
    JLabel locationLabel = new JLabel(product.getLocation());
    locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    locationLabel.setForeground(Color.WHITE);
    leftPanel.add(locationLabel);

    card.add(leftPanel);
    card.add(Box.createRigidArea(new Dimension(15, 0)));

    // Right panel for images (now side by side)
    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
    imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imagePanel.setOpaque(false);

    // this for the First image
    if (product.getImage1() != null && product.getImage1().length > 0) {
        try {
            ImageIcon icon1 = new ImageIcon(product.getImage1());
            Image img1 = icon1.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel1 = new JLabel(new ImageIcon(img1));
            imageLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(imageLabel1);
            imagePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        } catch (Exception e) {
            JLabel errorLabel1 = new JLabel("Image 1");
            errorLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(errorLabel1);
        }
    }

    // this is for the Second image
    if (product.getImage2() != null && product.getImage2().length > 0) {
        try {
            ImageIcon icon2 = new ImageIcon(product.getImage2());
            Image img2 = icon2.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel2 = new JLabel(new ImageIcon(img2));
            imageLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(imageLabel2);
        } catch (Exception e) {
            JLabel errorLabel2 = new JLabel("Image 2");
            errorLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(errorLabel2);
        }
    }

    card.add(imagePanel);

    return card;
}
private JPanel createOrderCard(Order order) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    card.setBackground(new Color(69, 125, 88));
    card.setPreferredSize(new Dimension(500, 200));

    // Order ID
    JLabel orderIdLabel = new JLabel("Order ID: " + order.getOrderId());
    orderIdLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    orderIdLabel.setForeground(Color.WHITE);

    // Buyer's Username
    JLabel buyerLabel = new JLabel("Buyer: " + order.getUsername());
    buyerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    buyerLabel.setForeground(Color.WHITE);

    // Product Name
    JLabel productNameLabel = new JLabel("Product: " + order.getProductName());
    productNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    productNameLabel.setForeground(Color.WHITE);

    // Quantity and Total Amount
    JLabel qtyLabel = new JLabel("Quantity: " + order.getQuantity());
    qtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    qtyLabel.setForeground(Color.WHITE);

    JLabel totalLabel = new JLabel("Total Amount: ₱" + String.format("%.2f", order.getTotalAmount()));
    totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    totalLabel.setForeground(Color.WHITE);

    // Status label
    JLabel statusLabel = new JLabel("Status: " + order.getStatus());
    statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    statusLabel.setForeground(order.getStatus().equalsIgnoreCase("received") ? Color.GREEN : Color.YELLOW);

    // Buttons Panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    buttonPanel.setOpaque(false);

    if (order.getStatus().equalsIgnoreCase("received")) {
        // For received orders - only show Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteButton.setBackground(new Color(200, 50, 50)); // Red color
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorderPainted(false);
        deleteButton.addActionListener(e -> deleteOrder(order, card));
        buttonPanel.add(deleteButton);
    } else {
        // For pending orders - show Cancel and Received buttons
        JButton cancelButton = new JButton("Cancel Order");
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cancelButton.setBackground(new Color(255, 102, 102)); // Red color
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> cancelOrder(order, card));
        buttonPanel.add(cancelButton);

        JButton receivedButton = new JButton("Received");
        receivedButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        receivedButton.setBackground(new Color(50, 200, 50)); // Green color
        receivedButton.setForeground(Color.WHITE);
        receivedButton.setBorderPainted(false);
        receivedButton.addActionListener(e -> markOrderAsReceived(order, card));
        buttonPanel.add(receivedButton);
    }

    // Add components to the card
    card.add(orderIdLabel);
    card.add(buyerLabel);
    card.add(productNameLabel);
    card.add(qtyLabel);
    card.add(totalLabel);
    card.add(statusLabel);
    card.add(buttonPanel);

    return card;
}
public class ProductDAO {
    private Connection con;
    
    public ProductDAO(Connection con) {
        this.con = con;
    }
    
 public List<Product> getProductsByStore(String storename) throws SQLException {
    List<Product> products = new ArrayList<>();
    String query = "SELECT product_id, product_name, storename, type, tagline, price, " +
                  "original_price, image1, image2, rating, location " +
                  "FROM products WHERE storename = ?";
    
    try (PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, storename);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("product_id");
            String name = rs.getString("product_name");
            String store = rs.getString("storename");
            String tagline = rs.getString("tagline");
            double price = rs.getDouble("price");
            double originalPrice = rs.getDouble("original_price");
            byte[] image1 = rs.getBytes("image1");
            byte[] image2 = rs.getBytes("image2");
            double rating = rs.getDouble("rating");
            String location = rs.getString("location");
            String type = rs.getString("type");
            
            products.add(new Product(id, name, store, type, tagline, price, 
                                   originalPrice, image1, image2, rating, location));
        }
    }
    return products;
}
}
private JPanel createFeedbackCard(Feedback feedback) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    card.setBackground(new Color(69, 125, 88));
    // Set fixed size for the feedback card
    Dimension fixedSize = new Dimension(700, 150);
    card.setPreferredSize(fixedSize);
    card.setMaximumSize(fixedSize);
    card.setMinimumSize(fixedSize);
    
    // Buyer info (left aligned)
    JLabel buyerLabel = new JLabel("Buyer: " + feedback.getUsername());
    buyerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    buyerLabel.setForeground(Color.WHITE);
    buyerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Product info (left aligned)
    JLabel productLabel = new JLabel("Product: " + feedback.getProductName());
    productLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    productLabel.setForeground(Color.WHITE);
    productLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Rating stars - using text stars if images not found
    JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    ratingPanel.setOpaque(false);
    JLabel ratingLabel = new JLabel("Rating: ");
    ratingLabel.setForeground(Color.WHITE);
    ratingPanel.add(ratingLabel);
    
    // Add stars based on rating
    for (int i = 0; i < 5; i++) {
        JLabel star = new JLabel();
        if (i < feedback.getRating()) {
            star.setText("★"); // Filled star character
        } else {
            star.setText("☆"); // Empty star character
        }
        star.setForeground(Color.YELLOW);
        star.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ratingPanel.add(star);
    }
    
    // Review message
    JTextArea reviewArea = new JTextArea(feedback.getReviewMessage());
    reviewArea.setLineWrap(true);
    reviewArea.setWrapStyleWord(true);
    reviewArea.setEditable(false);
    reviewArea.setBackground(new Color(69, 125, 88));
    reviewArea.setForeground(Color.WHITE);
    reviewArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    // Date formatting
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
    String formattedDate = dateFormat.format(new Date(feedback.getReviewDate().getTime()));
    JLabel dateLabel = new JLabel("Posted on: " + formattedDate);
    dateLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
    dateLabel.setForeground(Color.LIGHT_GRAY);
    dateLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
    
    // Add components to card
    card.add(buyerLabel);
    card.add(productLabel);
    card.add(ratingPanel);
    card.add(Box.createRigidArea(new Dimension(0, 5)));
    card.add(new JScrollPane(reviewArea));
    card.add(Box.createRigidArea(new Dimension(0, 5)));
    card.add(dateLabel);
    
    return card;
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
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        exitBTN = new javax.swing.JButton();
        btnInfo = new javax.swing.JButton();
        btnFeedback = new javax.swing.JButton();
        btnOrder = new javax.swing.JButton();
        btnProducts = new javax.swing.JButton();
        messageBTN = new javax.swing.JButton();
        parent = new javax.swing.JPanel();
        Myproducts = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        owberLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        searchTXT = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        searchBTN = new javax.swing.JButton();
        Order = new javax.swing.JPanel();
        orderTAB = new javax.swing.JTabbedPane();
        jLabel11 = new javax.swing.JLabel();
        Info = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        infoBTNdelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        infoContacts = new javax.swing.JLabel();
        infoStorename = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        infoLocation = new javax.swing.JLabel();
        infoID = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        editPanel = new javax.swing.JPanel();
        infoTXTstorename = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        infoTXTcontact = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        infoBTNconfirm = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        Feedbacks = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        feedbackTAB = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(42, 58, 41));

        jPanel5.setBackground(new java.awt.Color(37, 42, 26));

        jLabel6.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Shipzy");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(52, 70, 49));

        exitBTN.setBackground(new java.awt.Color(53, 91, 66));
        exitBTN.setForeground(new java.awt.Color(255, 255, 255));
        exitBTN.setText("EXIT");
        exitBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBTNActionPerformed(evt);
            }
        });

        btnInfo.setBackground(new java.awt.Color(53, 91, 66));
        btnInfo.setForeground(new java.awt.Color(255, 255, 255));
        btnInfo.setText("Store Info");
        btnInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoActionPerformed(evt);
            }
        });

        btnFeedback.setBackground(new java.awt.Color(53, 91, 66));
        btnFeedback.setForeground(new java.awt.Color(255, 255, 255));
        btnFeedback.setText("Feed backs");
        btnFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFeedbackActionPerformed(evt);
            }
        });

        btnOrder.setBackground(new java.awt.Color(53, 91, 66));
        btnOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnOrder.setText("Store Orders");
        btnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrderActionPerformed(evt);
            }
        });

        btnProducts.setBackground(new java.awt.Color(53, 91, 66));
        btnProducts.setForeground(new java.awt.Color(255, 255, 255));
        btnProducts.setText("My Product");
        btnProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductsActionPerformed(evt);
            }
        });

        messageBTN.setBackground(new java.awt.Color(53, 91, 66));
        messageBTN.setForeground(new java.awt.Color(255, 255, 255));
        messageBTN.setText("Messages");
        messageBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(exitBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFeedback, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                        .addComponent(btnOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(messageBTN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(btnProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnFeedback, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(messageBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(exitBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(264, Short.MAX_VALUE))
        );

        parent.setBackground(new java.awt.Color(42, 58, 41));
        parent.setLayout(new java.awt.CardLayout());

        Myproducts.setBackground(new java.awt.Color(42, 58, 41));

        jLabel3.setFont(new java.awt.Font("Inknut Antiqua ExtraBold", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Store Name");

        owberLabel.setBackground(new java.awt.Color(255, 255, 255));
        owberLabel.setFont(new java.awt.Font("Inknut Antiqua", 0, 12)); // NOI18N
        owberLabel.setForeground(new java.awt.Color(255, 255, 255));
        owberLabel.setText("Owner Name");

        jButton1.setBackground(new java.awt.Color(53, 91, 66));
        jButton1.setFont(new java.awt.Font("Inknut Antiqua", 0, 10)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Product");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Inknut Antiqua", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Seller:");

        jPanel4.setBackground(new java.awt.Color(62, 93, 59));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Inknut Antiqua", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Your  Products:");

        jTabbedPane2.setBackground(new java.awt.Color(60, 82, 80));
        jTabbedPane2.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        searchTXT.setBackground(new java.awt.Color(255, 255, 255));
        searchTXT.setForeground(new java.awt.Color(0, 0, 0));

        searchButton.setBackground(new java.awt.Color(255, 255, 255));
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/search.png"))); // NOI18N

        searchBTN.setBackground(new java.awt.Color(255, 255, 255));
        searchBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/search.png"))); // NOI18N

        javax.swing.GroupLayout MyproductsLayout = new javax.swing.GroupLayout(Myproducts);
        Myproducts.setLayout(MyproductsLayout);
        MyproductsLayout.setHorizontalGroup(
            MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MyproductsLayout.createSequentialGroup()
                .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(MyproductsLayout.createSequentialGroup()
                        .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(MyproductsLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(owberLabel))
                            .addGroup(MyproductsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(137, 137, 137)
                                .addComponent(searchTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 261, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MyproductsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(searchButton)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        MyproductsLayout.setVerticalGroup(
            MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MyproductsLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(owberLabel)
                    .addComponent(jLabel5))
                .addGap(35, 35, 35)
                .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(649, Short.MAX_VALUE))
            .addGroup(MyproductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MyproductsLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(searchButton)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        parent.add(Myproducts, "card2");

        Order.setBackground(new java.awt.Color(62, 93, 59));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Orders:");

        javax.swing.GroupLayout OrderLayout = new javax.swing.GroupLayout(Order);
        Order.setLayout(OrderLayout);
        OrderLayout.setHorizontalGroup(
            OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderTAB)
            .addGroup(OrderLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(701, Short.MAX_VALUE))
        );
        OrderLayout.setVerticalGroup(
            OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrderLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(orderTAB, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(641, Short.MAX_VALUE))
        );

        parent.add(Order, "card3");

        Info.setBackground(new java.awt.Color(62, 93, 59));

        jLabel2.setFont(new java.awt.Font("Irish Grover", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("STORE INFO");

        infoBTNdelete.setBackground(new java.awt.Color(133, 28, 28));
        infoBTNdelete.setForeground(new java.awt.Color(255, 255, 255));
        infoBTNdelete.setText("DELETE STORE");
        infoBTNdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoBTNdeleteActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(90, 133, 86));

        infoContacts.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoContacts.setForeground(new java.awt.Color(255, 255, 255));
        infoContacts.setText("contact");

        infoStorename.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoStorename.setForeground(new java.awt.Color(255, 255, 255));
        infoStorename.setText("storename");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Store Name: ");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Contact:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Location:");

        infoLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoLocation.setForeground(new java.awt.Color(255, 255, 255));
        infoLocation.setText("location");

        infoID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        infoID.setForeground(new java.awt.Color(255, 255, 255));
        infoID.setText("id");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("ID:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(infoStorename, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoContacts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoID, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(infoContacts))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(infoLocation))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(infoID)))
                    .addComponent(infoStorename))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        editPanel.setBackground(new java.awt.Color(90, 133, 86));
        editPanel.setForeground(new java.awt.Color(255, 255, 255));

        infoTXTstorename.setBackground(new java.awt.Color(255, 255, 255));
        infoTXTstorename.setForeground(new java.awt.Color(0, 0, 0));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Storename:");

        infoTXTcontact.setBackground(new java.awt.Color(255, 255, 255));
        infoTXTcontact.setForeground(new java.awt.Color(0, 0, 0));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Contact: ");

        infoBTNconfirm.setBackground(new java.awt.Color(53, 91, 66));
        infoBTNconfirm.setForeground(new java.awt.Color(255, 255, 255));
        infoBTNconfirm.setText("CONFIRM");
        infoBTNconfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoBTNconfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editPanelLayout = new javax.swing.GroupLayout(editPanel);
        editPanel.setLayout(editPanelLayout);
        editPanelLayout.setHorizontalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editPanelLayout.createSequentialGroup()
                        .addComponent(infoTXTstorename, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(infoTXTcontact, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(123, 123, 123)
                        .addComponent(jLabel10))
                    .addComponent(infoBTNconfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        editPanelLayout.setVerticalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(infoTXTstorename, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoTXTcontact, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(infoBTNconfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jToggleButton1.setBackground(new java.awt.Color(53, 91, 66));
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("EDIT INFO");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InfoLayout = new javax.swing.GroupLayout(Info);
        Info.setLayout(InfoLayout);
        InfoLayout.setHorizontalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(InfoLayout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoBTNdelete, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(editPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(218, Short.MAX_VALUE))
        );
        InfoLayout.setVerticalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(infoBTNdelete, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(762, Short.MAX_VALUE))
        );

        parent.add(Info, "card4");

        Feedbacks.setBackground(new java.awt.Color(42, 58, 41));
        Feedbacks.setForeground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Irish Grover", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Feedbacks:");

        javax.swing.GroupLayout FeedbacksLayout = new javax.swing.GroupLayout(Feedbacks);
        Feedbacks.setLayout(FeedbacksLayout);
        FeedbacksLayout.setHorizontalGroup(
            FeedbacksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FeedbacksLayout.createSequentialGroup()
                .addComponent(jLabel12)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(FeedbacksLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(feedbackTAB, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        FeedbacksLayout.setVerticalGroup(
            FeedbacksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FeedbacksLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(feedbackTAB, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(670, Short.MAX_VALUE))
        );

        parent.add(Feedbacks, "card5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(parent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    //ACTION BUTTONS
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // This action  or functions enables the user to insert a product
        var product = new AddProduct(storename, location);
        product.setVisible(true);
        product.setLocationRelativeTo(this);

        // Add a window listener to refresh when the AddProduct window closes
        product.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                displayProductCards();
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed
    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        String firstname = null;
        String lastname = null;
        String Username = null;
        Homepage Homepage = null;
        var homepage = new Homepage(Homepage, firstname, lastname, Username);
        homepage.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jLabel6MouseClicked
    private void infoBTNdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoBTNdeleteActionPerformed
        // Confirm deletion
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete your store? This will remove your store information but keep your account.",
        "Confirm Store Deletion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Only clear the store name in the user table (not delete the user)
            String updateQuery = "UPDATE users SET Store_name = NULL WHERE User_id = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            updateStmt.setInt(1, id);
            int rowsAffected = updateStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Also delete all products associated with this store
                String deleteProductsQuery = "DELETE FROM products WHERE storename = ?";
                PreparedStatement deleteProductsStmt = con.prepareStatement(deleteProductsQuery);
                deleteProductsStmt.setString(1, storename);
                deleteProductsStmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Store deleted successfully. Your account remains active.");
                
                // Close the seller page and go back to homepage
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete store", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    }//GEN-LAST:event_infoBTNdeleteActionPerformed
    private void infoBTNconfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoBTNconfirmActionPerformed

         // Get the new values from text fields
    // Get the new values from text fields
    String newStoreName = infoTXTstorename.getText().trim();
    String newContact = infoTXTcontact.getText().trim();
    
    // Validate inputs
    if (newStoreName.isEmpty() || newContact.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    try {
        // Update only storename and contact in database
        String query = "UPDATE users SET Store_name = ?, Contacts = ? WHERE User_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, newStoreName);
        stmt.setString(2, newContact);
        stmt.setInt(3, id);  // Use the existing ID as the WHERE condition
        
        int rowsAffected = stmt.executeUpdate();
        
        if (rowsAffected > 0) {
            // Update the UI and class variables
            String oldStoreName = storename; // Store the old name for product updates
            storename = newStoreName;
            contacts = newContact;
            
            // Update all labels that display store information
            infoStorename.setText(newStoreName);
            infoContacts.setText(newContact);
            jLabel3.setText(newStoreName);  // Main store name label
            owberLabel.setText(Name);       // Owner name label
            
            // Update the store name in product cards
            updateProductsStoreName(oldStoreName, newStoreName);
            
            // Hide edit panel
            editPanel.setVisible(false);
            jToggleButton1.setSelected(false);
            
            JOptionPane.showMessageDialog(this, "Store information updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update store information", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}                                                                                       private void updateProductsStoreName(String oldStoreName, String newStoreName) {
    try {
        // First update the products in the database
        String query = "UPDATE products SET storename = ? WHERE storename = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, newStoreName);
        stmt.setString(2, oldStoreName);
        int updatedRows = stmt.executeUpdate();
        
        System.out.println("Updated " + updatedRows + " products with new store name");
        
        // Then refresh the product display
        displayProductCards();
        
        // Also update the location in products if needed
        if (!location.isEmpty()) {
            String locationQuery = "UPDATE products SET location = ? WHERE storename = ?";
            PreparedStatement locationStmt = con.prepareStatement(locationQuery);
            locationStmt.setString(1, location);
            locationStmt.setString(2, newStoreName);
            locationStmt.executeUpdate();
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error updating products: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }//GEN-LAST:event_infoBTNconfirmActionPerformed
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton1.isSelected()) {
        editPanel.setVisible(true);
    } else {
        editPanel.setVisible(false);
    }
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    private void btnFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFeedbackActionPerformed
        // TODO add your handling code here:
         parent.removeAll();
    parent.add(Feedbacks);
    parent.repaint();
    parent.revalidate();
    displayFeedbacks();
    }//GEN-LAST:event_btnFeedbackActionPerformed
    private void btnInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoActionPerformed
        // TODO add your handling code here:
        
          parent.removeAll();
        parent.add(Info);
        parent.repaint();
        parent.revalidate();
    }//GEN-LAST:event_btnInfoActionPerformed
    private void btnOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrderActionPerformed
        // TODO add your handling code here:
              parent.removeAll();
        parent.add(Order);
        parent.repaint();
        parent.revalidate();
         displayOrdersForStore();
    }//GEN-LAST:event_btnOrderActionPerformed
    private void btnProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductsActionPerformed
        // TODO add your handling code here:
         parent.removeAll();
        parent.add(Myproducts);
        parent.repaint();
        parent.revalidate();
    }//GEN-LAST:event_btnProductsActionPerformed
    private void exitBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBTNActionPerformed
        // TODO add your handling code here:
           int confirm = JOptionPane.showConfirmDialog(
        SellerPage.this,
        "Are you sure you want to EXIT?",
        "Confirm EXIT",
        JOptionPane.YES_NO_OPTION);
     
    if (confirm == JOptionPane.YES_OPTION) {
        // Create new Homepage with stored user info
        this.dispose();
    } 
    }//GEN-LAST:event_exitBTNActionPerformed
    private void messageBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageBTNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messageBTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(SellerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SellerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SellerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SellerPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String Username = null;
                String storename = null;
                String Name = null;
                String Location = null;
                int ID = 0;
                String Contacts = null;
                new SellerPage(storename, Username, Name, Location, ID, Contacts).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Feedbacks;
    private javax.swing.JPanel Info;
    private javax.swing.JPanel Myproducts;
    private javax.swing.JPanel Order;
    private javax.swing.JButton btnFeedback;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnOrder;
    private javax.swing.JButton btnProducts;
    private javax.swing.JPanel editPanel;
    private javax.swing.JButton exitBTN;
    private javax.swing.JTabbedPane feedbackTAB;
    private javax.swing.JButton infoBTNconfirm;
    private javax.swing.JButton infoBTNdelete;
    private javax.swing.JLabel infoContacts;
    private javax.swing.JLabel infoID;
    private javax.swing.JLabel infoLocation;
    private javax.swing.JLabel infoStorename;
    private javax.swing.JTextField infoTXTcontact;
    private javax.swing.JTextField infoTXTstorename;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton messageBTN;
    private javax.swing.JTabbedPane orderTAB;
    private javax.swing.JLabel owberLabel;
    private javax.swing.JPanel parent;
    private javax.swing.JButton searchBTN;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTXT;
    // End of variables declaration//GEN-END:variables
}
