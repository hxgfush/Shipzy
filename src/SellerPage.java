/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author 63995
 */
import newPackage.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.Connection;         /*This the import section which get the fumctoionality from the libraries */
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;



public class SellerPage extends javax.swing.JFrame {    /*This the is the sellerPage class*/
    private final String storename;
    private final String Name;

    /**
     * Creates new form SellerPage
     */
    public SellerPage(String storename, String Name) {
        initComponents();
         this.storename = storename;
        jLabel3.setText(storename); 
        jLabel3.setText(this.storename != null ? this.storename : "Unnamed Store");  /*This is used to display the storename when enterimg the frame*/
        this.Name = Name;
        owberLabel.setText(Name); 
        owberLabel.setText(this.Name != null ? this.Name : "Unnamed Store");  /*This is used to display the store owner name when enterimg the frame*/
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
  

 
    
public class ProductDAO {
    private Connection con;
    
    public ProductDAO(Connection con) {
        this.con = con;
    }
    
    public List<Product> getProductsByStore(String storename) throws SQLException { 
        List<Product> products = new ArrayList<>();
        String query = "SELECT " +
               "product_id, " +
               "product_name, " +
               "COALESCE(storename, 'Default Store') as storename, " +
               "COALESCE(tagline, '') as tagline, " +
               "price, " +
               "COALESCE(original_price, price) as original_price, " +
               "image1, " +
               "image2, " +
               "COALESCE(rating, 0.0) as rating, " +
               "COALESCE(location, '') as location " +
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
                
                products.add(new Product(id, name, store, tagline, price, originalPrice, image1, image2, rating, location));
            }
        }
        return products;
    }
}

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

private JPanel createProductCard(Product product) {// This function used for creating the card 
    // Main card panel with horizontal BoxLayout
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    card.setBackground(new Color(69, 125, 88));
    card.setPreferredSize(new Dimension(500, 200)); // Wider horizontal card

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
    JLabel priceLabel = new JLabel(String.format("$ %.2f", product.getPrice()));
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
   
private void editProduct(Product product, JPanel card, String originalName) {  //this is used for editing the data inside the card
    // Create a dialog for editing
    JDialog editDialog = new JDialog(this, "Edit Product", true);
    editDialog.setLayout(new BorderLayout());
    editDialog.setSize(400, 300);
    
    // Form panel
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    // Form fields
    JTextField nameField = new JTextField(product.getProductName());
    JTextField taglineField = new JTextField(product.getTagline());
    JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
    
    formPanel.add(new JLabel("Product Name:"));
    formPanel.add(nameField);
    formPanel.add(new JLabel("Tagline:"));
    formPanel.add(taglineField);
    formPanel.add(new JLabel("Price:"));
    formPanel.add(priceField);
    
    // Save button
    JButton saveButton = new JButton("Save Changes");
    saveButton.addActionListener(e -> {
        try {
            // Update the product object
            product.setProductName(nameField.getText());
            product.setTagline(taglineField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            
            // Update database - now using product_id
            updateProductInDatabase(product);  //call the function to innitiate update
            
            // Refresh the card
            refreshCard(card, product);
            
            editDialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    
    editDialog.add(formPanel, BorderLayout.CENTER);
    editDialog.add(saveButton, BorderLayout.SOUTH);
    editDialog.setLocationRelativeTo(this);
    editDialog.setVisible(true);
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

private void updateProductInDatabase(Product product) {
    // Corrected SQL query with proper syntax
    String query = "UPDATE products SET product_name=?, tagline=?, price=?, original_price=? WHERE product_id=?";
    
    try 
         {
         PreparedStatement param = con.prepareStatement(query);
        // Debug output
        System.out.println("Updating product ID: " + product.getProductId());
        System.out.println("New values: " + product.getProductName() + ", " + 
                         product.getTagline() + ", " + product.getPrice());
        
        // Set parameters in correct order (1-based index)
        param.setString(1, product.getProductName());
        param.setString(2, product.getTagline());
        param.setDouble(3, product.getPrice());
        param.setDouble(4, product.getOriginalPrice());
        param.setInt(5, product.getProductId());

        int rowsAffected = param.executeUpdate();

        if (rowsAffected == 0) {
            System.out.println("No rows updated - product ID not found");
            JOptionPane.showMessageDialog(this, 
                "No product was updated. Product may not exist.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            System.out.println("Successfully updated product ID: " + product.getProductId());
            JOptionPane.showMessageDialog(this, 
                "Product updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        System.err.println("SQL Error: " + ex.getMessage());
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Database error: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
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
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jLabel3 = new javax.swing.JLabel();
        owberLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );

        jButton1.setBackground(new java.awt.Color(69, 125, 88));
        jButton1.setFont(new java.awt.Font("Inknut Antiqua", 0, 10)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Product");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(62, 93, 59));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Inknut Antiqua", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Your  Products:");

        jComboBox1.setBackground(new java.awt.Color(69, 125, 88));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Inknut Antiqua", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Categories");

        jTabbedPane2.setBackground(new java.awt.Color(60, 82, 80));
        jTabbedPane2.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Inknut Antiqua ExtraBold", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Store Name");

        owberLabel.setBackground(new java.awt.Color(255, 255, 255));
        owberLabel.setFont(new java.awt.Font("Inknut Antiqua", 0, 12)); // NOI18N
        owberLabel.setForeground(new java.awt.Color(255, 255, 255));
        owberLabel.setText("Owner Name");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Inknut Antiqua", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Seller:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(owberLabel))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(595, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(owberLabel)
                            .addComponent(jLabel5))
                        .addGap(35, 35, 35)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // This action  or functions enables the user to insert a product
        var product = new AddProduct(storename);
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

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
         var homepage = new Homepage();  
             homepage.setVisible(true);
              this.dispose();
        
    }//GEN-LAST:event_jLabel6MouseClicked

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
                String storename = null;
                String Name = null;
                new SellerPage(storename, Name).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel owberLabel;
    // End of variables declaration//GEN-END:variables
}
