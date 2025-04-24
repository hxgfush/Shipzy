/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author 63995
 */


import java.awt.BorderLayout;
import newPackage.Product;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
public class Homepage extends javax.swing.JFrame {

    /**
     * Creates new form Homepage
     */
    public Homepage() {
        initComponents();
        
        try {
            Connection();
        } catch (SQLException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadProducts();
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
   
    

    
private void loadProducts() {
    SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
        @Override
        protected List<Product> doInBackground() throws Exception {
            List<Product> products = new ArrayList<>();
            try {
                // Solution 1: If products should all show the same store (first store found)
                String query = "SELECT p.product_id, p.product_name, " +
                             "COALESCE((SELECT s.Storename FROM sellermanager s LIMIT 1), 'Default Store') as Storename, " +
                             "COALESCE(p.tagline, '') as tagline, " +
                             "p.price, " +
                             "COALESCE(p.original_price, p.price) as original_price, " +
                             "p.image1, p.image2, " +
                             "COALESCE(p.rating, 0.0) as rating, " +
                             "COALESCE((SELECT s.Location FROM sellermanager s LIMIT 1), '') as location " +
                             "FROM products p";
                ResultSet rs = st.executeQuery(query);
                
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String store = rs.getString("Storename");
                    String tagline = rs.getString("tagline");
                    double price = rs.getDouble("price");
                    double originalPrice = rs.getDouble("original_price");
                    byte[] image1 = rs.getBytes("image1");
                    byte[] image2 = rs.getBytes("image2");
                    double rating = rs.getDouble("rating");
                    String location = rs.getString("location");
                    
                    products.add(new Product(id, name, store, price, image1, image2));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(Homepage.this, 
                    "Error loading products: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            return products;
        }
        
        @Override
        protected void done() {
            try {
                List<Product> products = get();
                displayProducts(products);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Homepage.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    worker.execute();
}
 
private void displayProducts(List<Product> products) {
    jPanel3.removeAll();
    jPanel3.setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15));
    
    if (products.isEmpty()) {
        JLabel noProductsLabel = new JLabel("No products available");
        noProductsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        jPanel3.add(noProductsLabel);
    } else {
        for (Product product : products) {
            JPanel card = createProductCard(product);
            jPanel3.add(card);
        }
    }
    
    jPanel3.revalidate();
    jPanel3.repaint();
}

private JPanel createProductCard(Product product) {
    JPanel card = new JPanel();
    card.setLayout(new BorderLayout());
    card.setPreferredSize(new Dimension(900, 180));
    card.setBackground(new Color(69, 125, 88));
    card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Left Panel for product info
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new GridBagLayout());
    leftPanel.setBackground(new Color(69, 125, 88));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.gridx = 0;
    gbc.gridy = 0;

    // Product Name
    JLabel nameLabel = new JLabel(product.getProductName());
    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    nameLabel.setForeground(Color.WHITE);
    leftPanel.add(nameLabel, gbc);

    // Tagline
    gbc.gridy++;
    JLabel taglineLabel = new JLabel("#" + product.getTagline());
    taglineLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    taglineLabel.setForeground(Color.WHITE);
    leftPanel.add(taglineLabel, gbc);

    // Price Section
    gbc.gridy++;
    JPanel pricePanel = new JPanel();
    pricePanel.setBackground(new Color(69, 125, 88));
    pricePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

    JLabel currentPrice = new JLabel("₱" + String.format("%.2f", product.getPrice()));
    currentPrice.setForeground(Color.WHITE);
    currentPrice.setFont(new Font("Arial", Font.BOLD, 14));
    pricePanel.add(currentPrice);

    if (product.getOriginalPrice() > product.getPrice()) {
        JLabel originalPrice = new JLabel("₱" + String.format("%.2f", product.getOriginalPrice()));
        originalPrice.setFont(new Font("Arial", Font.PLAIN, 12));
        originalPrice.setForeground(Color.RED);
        originalPrice.setText("<html><s>" + originalPrice.getText() + "</s></html>");
        pricePanel.add(originalPrice);

        double discount = 100 * (product.getOriginalPrice() - product.getPrice()) / product.getOriginalPrice();
        JLabel discountLabel = new JLabel(String.format(" %.0f%% OFF", discount));
        discountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        discountLabel.setForeground(Color.RED);
        pricePanel.add(discountLabel);
    }

    leftPanel.add(pricePanel, gbc);

    // Buttons
    gbc.gridy++;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    buttonPanel.setBackground(new Color(69, 125, 88));

    JButton buyButton = new JButton("Buy");
    buyButton.setPreferredSize(new Dimension(110, 30));
    buyButton.setFont(new Font("Arial", Font.BOLD, 12));
    buyButton.setBackground(Color.WHITE);
    buyButton.setForeground(Color.BLACK);
    buyButton.addActionListener(e -> {
    try {
        Buypage buypage = new Buypage(product);
        buypage.setVisible(true);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, 
            "Error opening product details: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
         }
    });
    buttonPanel.add(buyButton);

    JButton addToCartButton = new JButton("Add to Cart");
    addToCartButton.setPreferredSize(new Dimension(110, 30));
    addToCartButton.setFont(new Font("Arial", Font.BOLD, 12));
    addToCartButton.setBackground(Color.WHITE);
    addToCartButton.setForeground(Color.BLACK);
    addToCartButton.addActionListener(e -> {
        JOptionPane.showMessageDialog(null, product.getProductName() + " added to cart!",
                "Cart Update", JOptionPane.INFORMATION_MESSAGE);
    });
    buttonPanel.add(addToCartButton);

    leftPanel.add(buttonPanel, gbc);

    // Rating
    gbc.gridy++;
    JLabel ratingLabel = new JLabel(String.format("★ %.1f", product.getRating()));
    ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    ratingLabel.setForeground(Color.WHITE);
    leftPanel.add(ratingLabel, gbc);

    // Right Panel for image
    JPanel rightPanel = new JPanel();
    rightPanel.setBackground(new Color(69, 125, 88));
    rightPanel.setLayout(new BorderLayout());
    try {
        ImageIcon icon = new ImageIcon(product.getImage1());
        Image img = icon.getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        rightPanel.add(imageLabel, BorderLayout.CENTER);
    } catch (Exception e) {
        e.printStackTrace();
        rightPanel.add(new JLabel("Image not available"));
    }

    card.add(leftPanel, BorderLayout.WEST);
    card.add(rightPanel, BorderLayout.EAST);

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

        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        navigation = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        sellerbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        logoutbtn = new javax.swing.JButton();
        helpbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        navigation.setBackground(new java.awt.Color(52, 70, 49));

        jToggleButton1.setText("Items");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(69, 125, 88));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jLabel2.setText("Categories:");

        sellerbtn.setBackground(new java.awt.Color(69, 125, 88));
        sellerbtn.setForeground(new java.awt.Color(255, 255, 255));
        sellerbtn.setText("be a seller");
        sellerbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellerbtnActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(42, 58, 41));
        // In the jPanel2 layout (initComponents):

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Available Local food");

        jButton4.setBackground(new java.awt.Color(255, 255, 255));

        jButton5.setBackground(new java.awt.Color(255, 255, 51));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("Hot deals");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));
        jPanel3.setBackground(new java.awt.Color(42, 58, 41));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 948, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 448, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(37, 42, 26));

        jLabel6.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Shipzy");

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

        logoutbtn.setBackground(new java.awt.Color(69, 125, 88));
        logoutbtn.setForeground(new java.awt.Color(255, 255, 255));
        logoutbtn.setText("Log out");
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });

        helpbtn.setBackground(new java.awt.Color(69, 125, 88));
        helpbtn.setForeground(new java.awt.Color(255, 255, 255));
        helpbtn.setText("Help?");
        helpbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpbtnActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(69, 125, 88));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("About");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout navigationLayout = new javax.swing.GroupLayout(navigation);
        navigation.setLayout(navigationLayout);
        navigationLayout.setHorizontalGroup(
            navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationLayout.createSequentialGroup()
                .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(navigationLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(navigationLayout.createSequentialGroup()
                                .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(navigationLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(navigationLayout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(logoutbtn)
                                            .addComponent(sellerbtn)
                                            .addComponent(helpbtn)
                                            .addComponent(jButton1))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        navigationLayout.setVerticalGroup(
            navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navigationLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jToggleButton1)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(helpbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(logoutbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sellerbtn)
                        .addGap(39, 39, 39))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(navigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(navigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void logoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutbtnActionPerformed
        // TODO add your handling code here:
        Login login = new Login();
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_logoutbtnActionPerformed

    private void helpbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_helpbtnActionPerformed

    private void sellerbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellerbtnActionPerformed
        // TODO add your handling code here:
    var hop = new SellerHopIn();
        hop.setVisible(true);
        hop.pack();
        hop.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_sellerbtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Homepage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton helpbtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JPanel navigation;
    private javax.swing.JButton sellerbtn;
    // End of variables declaration//GEN-END:variables
}
