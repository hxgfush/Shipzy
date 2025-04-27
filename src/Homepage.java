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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
public class Homepage extends javax.swing.JFrame {

    private static Homepage instance;
    private WheelMenu wheelMenu;
    private JButton wheelButton;


    /**
     * Creates new form Homepage
     */
    public Homepage() {
        instance = this;
        initComponents();
        this.setLocationRelativeTo(null);
        
         // Initialize wheel menu
    String[] wheelItems = {"Profile", "Settings", "History", "Messages"};
        wheelMenu = new WheelMenu(this, wheelItems, e -> {
            String command = e.getActionCommand();
            JOptionPane.showMessageDialog(this, "Selected: " + command);
        });
        
        // Create wheel button
        wheelButton = new JButton("☰");
        wheelButton.setBackground(new Color(69, 125, 88));
        wheelButton.setForeground(Color.WHITE);
        wheelButton.setBorderPainted(false);
        wheelButton.setFocusPainted(false);
        wheelButton.addActionListener(e -> {
    if (wheelMenu.isVisible()) {
        wheelMenu.setVisible(false);
    } else {
        wheelMenu.showMenu(wheelButton); // Pass the button as reference component
    }
});
        
        // Add wheel button to panel
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(wheelButton, BorderLayout.EAST);
        
        // Add wheel button to panel
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(wheelButton, BorderLayout.EAST);
        
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
  
     
     @Override
public void dispose() {
    if (wheelMenu != null) {
        wheelMenu.dispose();
    }
    super.dispose();
}

 public class WheelMenu extends JDialog  {
    private String[] items;
    private ActionListener listener;
    private int radius = 120;
    private int buttonSize = 50;
    private Point[] buttonCenters;

    public WheelMenu(Window owner, String[] items, ActionListener listener) {
        super(owner);
        this.items = items;
        this.listener = listener;
        this.buttonCenters = new Point[items.length];
        setUndecorated(true);
        setModal(false);
        setFocusableWindowState(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        
        // Calculate size needed for the wheel
        int size = radius * 2 + buttonSize;
        setSize(size, size);
        
        // Add mouse listener to close when clicking outside
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                boolean clickedInside = false;
                
                // Check if click was inside any button
                for (Point center : buttonCenters) {
                    if (center != null && 
                        clickPoint.distance(center) <= buttonSize/2) {
                        clickedInside = true;
                        break;
                    }
                }
                
                // If clicked outside, close the menu
                if (!clickedInside) {
                    setVisible(false);
                }
            }
        });
    }

    // Modify the showMenu method to center properly
    public void showMenu(Component relativeTo) {
        // Calculate center position relative to the component
        Point loc = relativeTo.getLocationOnScreen();
        Dimension compSize = relativeTo.getSize();
        int x = loc.x + compSize.width/2 - getWidth()/2;
        int y = loc.y + compSize.height/2 - getHeight()/2;
        
        // Ensure it stays on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        x = Math.max(0, Math.min(x, screenSize.width - getWidth()));
        y = Math.max(0, Math.min(y, screenSize.height - getHeight()));
        
        setLocation(x, y);
        setVisible(true);
    }
    
    
  @Override
public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Clear the background
    g2.setColor(new Color(0, 0, 0, 0));
    g2.fillRect(0, 0, getWidth(), getHeight());
    
    Point center = new Point(getWidth()/2, getHeight()/2);
    
    // Draw green circle background
    g2.setColor(new Color(69, 125, 88));
    g2.fillOval(center.x - radius, center.y - radius, radius*2, radius*2);

    // Calculate positions for buttons
    double angleStep = 2 * Math.PI / items.length;
    double angle = -Math.PI / 2; // Start at top

    for (int i = 0; i < items.length; i++) {
        int x = center.x + (int)(radius * Math.cos(angle));
        int y = center.y + (int)(radius * Math.sin(angle));
        
        // Store button center for click detection
        buttonCenters[i] = new Point(x, y);
        
        // Draw button
        g2.setColor(Color.WHITE);
        g2.fillOval(x - buttonSize/2, y - buttonSize/2, buttonSize, buttonSize);
        
        // Draw text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        String text = items[i];
        int textWidth = fm.stringWidth(text);
        g2.drawString(text, x - textWidth/2, y + fm.getAscent()/2 - 2); // Slight vertical adjustment
        
        angle += angleStep;
    }
    
    g2.dispose();
}

    public void showMenu() {
        // Center on screen
        setLocationRelativeTo(null);
        setVisible(true);
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
               "FROM products";
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
                    
                    products.add(new Product(id, name, store, tagline, price, originalPrice, image1, image2, rating, location));
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
    // Create a main panel that will hold all product cards
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(new Color(42, 58, 41));
    
    // Add some padding at the top
    mainPanel.add(Box.createVerticalStrut(10));
    
    if (products.isEmpty()) {
        JLabel noProductsLabel = new JLabel("No products found");
        noProductsLabel.setForeground(Color.WHITE);
        noProductsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(noProductsLabel);
    } else {
        for (Product product : products) {
            JPanel card = createProductCard(product);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(card);
            // Add some spacing between cards
            mainPanel.add(Box.createVerticalStrut(15));
        }
    }
    
    // Create a scroll pane and add the main panel to it
    JScrollPane scrollPane = new JScrollPane(mainPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setBackground(new Color(42, 58, 41));
    
    // it Removes all existing tabs and add a new scrollable panel
    jTabbedPane2.removeAll();
    jTabbedPane2.addTab("Available Local Food", scrollPane);
    
    // Force a UI update
    jTabbedPane2.revalidate();
    jTabbedPane2.repaint();
}


private JPanel createProductCard(Product product) {
    JPanel card = new JPanel(new BorderLayout(10, 10));
    card.setPreferredSize(new Dimension(900, 150));
    card.setBackground(new Color(69, 125, 88));
    card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Left Section (Texts + Buttons)
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBackground(new Color(69, 125, 88));

    
    // Product name
    JLabel nameLabel = new JLabel(product.getProductName());
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    nameLabel.setForeground(Color.WHITE);
    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(nameLabel);

    // Store Name
    JLabel storeLabel = new JLabel(product.getStoreName());
    storeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    storeLabel.setForeground(Color.WHITE);
    storeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(storeLabel);

    // Tagline
    JLabel taglineLabel = new JLabel("#" + product.getTagline());
    taglineLabel.setFont(new Font("Arial", Font.ITALIC, 11));
    taglineLabel.setForeground(Color.LIGHT_GRAY);
    taglineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftPanel.add(taglineLabel);

   

 // Space between text and prices

    // Price + Buttons panel
    JPanel priceButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    priceButtonPanel.setBackground(new Color(69, 125, 88));

    JLabel currentPrice = new JLabel("₱" + String.format("%.2f", product.getPrice()));
    currentPrice.setFont(new Font("Arial", Font.BOLD, 14));
    currentPrice.setForeground(Color.WHITE);
    priceButtonPanel.add(currentPrice);

    if (product.getOriginalPrice() > product.getPrice()) {
        JLabel originalPrice = new JLabel("₱" + String.format("%.2f", product.getOriginalPrice()));
        originalPrice.setFont(new Font("Arial", Font.PLAIN, 12));
        originalPrice.setForeground(Color.RED);
        originalPrice.setText("<html><strike>" + originalPrice.getText() + "</strike></html>");
        priceButtonPanel.add(originalPrice);

        double discount = 100 * (product.getOriginalPrice() - product.getPrice()) / product.getOriginalPrice();
        JLabel discountLabel = new JLabel(String.format(" %.0f%% OFF", discount));
        discountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        discountLabel.setForeground(Color.WHITE);
        priceButtonPanel.add(discountLabel);
    }

    leftPanel.add(priceButtonPanel);

    // Buttons (Buy + Add to Cart)
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    buttonPanel.setBackground(new Color(69, 125, 88));

    JButton buyButton = new JButton("Buy");
    buyButton.setPreferredSize(new Dimension(80, 25));
    buyButton.setFont(new Font("Arial", Font.BOLD, 12));
    buyButton.addActionListener(e -> {
        try {
            Buypage buypage = new Buypage(product);
            buypage.setVisible(true);
            buypage.setLocationRelativeTo(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Error opening product details: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    buttonPanel.add(buyButton);

    JButton addToCartButton = new JButton("Add to Cart");
    addToCartButton.setPreferredSize(new Dimension(110, 25));
    addToCartButton.setFont(new Font("Arial", Font.BOLD, 12));
    addToCartButton.addActionListener(e -> {
        JOptionPane.showMessageDialog(null, product.getProductName() + " added to cart!",
                "Cart Update", JOptionPane.INFORMATION_MESSAGE);
    });
    buttonPanel.add(addToCartButton);

    leftPanel.add(buttonPanel);

    // Rating and location under buttons
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    infoPanel.setBackground(new Color(69, 125, 88));
    JLabel ratingLabel = new JLabel(String.format("★ %.1f", product.getRating()));
    ratingLabel.setForeground(Color.WHITE);
    ratingLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    infoPanel.add(ratingLabel);

    JLabel locationLabel = new JLabel(product.getLocation());
    locationLabel.setForeground(Color.WHITE);
    locationLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    infoPanel.add(locationLabel);

    leftPanel.add(infoPanel);

    // Right Panel for images
    JPanel rightPanel = new JPanel(new GridLayout(1, 2, 5, 0));
    rightPanel.setBackground(new Color(69, 125, 88));

    try {
        ImageIcon icon1 = new ImageIcon(product.getImage1());
        Image img1 = icon1.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel1 = new JLabel(new ImageIcon(img1));
        imageLabel1.setOpaque(true);
        imageLabel1.setBackground(Color.BLACK);
        rightPanel.add(imageLabel1);

        ImageIcon icon2 = new ImageIcon(product.getImage2());
        Image img2 = icon2.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel2 = new JLabel(new ImageIcon(img2));
        imageLabel2.setOpaque(true);
        imageLabel2.setBackground(Color.BLACK);
        rightPanel.add(imageLabel2);
    } catch (Exception e) {
        rightPanel.add(new JLabel("Image not available"));
        rightPanel.add(new JLabel("Image not available"));
    }

    // Add panels to the card
    card.add(leftPanel, BorderLayout.CENTER);
    card.add(rightPanel, BorderLayout.EAST);

    return card;
}




  public static Homepage getInstance() {
        if (instance == null) {
            instance = new Homepage();
        }
        return instance;
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
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        helpbtn = new javax.swing.JButton();
        logoutbtn = new javax.swing.JButton();

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel2.setText("Categories:");

        sellerbtn.setBackground(new java.awt.Color(69, 125, 88));
        sellerbtn.setForeground(new java.awt.Color(255, 255, 255));
        sellerbtn.setText("be a seller");
        sellerbtn.setAlignmentY(0.0F);
        sellerbtn.setPreferredSize(new java.awt.Dimension(102, 23));
        sellerbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellerbtnActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(42, 58, 41));
        // In the jPanel2 layout (initComponents):

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(670, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        // Remove the GroupLayout code for jPanel5
        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Shipzy");
        jPanel5.add(jLabel6, java.awt.BorderLayout.CENTER);
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

        jButton1.setBackground(new java.awt.Color(69, 125, 88));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("About");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        logoutbtn.setBackground(new java.awt.Color(69, 125, 88));
        logoutbtn.setForeground(new java.awt.Color(255, 255, 255));
        logoutbtn.setText("Log out");
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout navigationLayout = new javax.swing.GroupLayout(navigation);
        navigation.setLayout(navigationLayout);
        navigationLayout.setHorizontalGroup(
            navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationLayout.createSequentialGroup()
                .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(navigationLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(helpbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sellerbtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jToggleButton1)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(helpbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(logoutbtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sellerbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(navigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    Homepage homepage = Homepage.getInstance();
    
    // Create and show SellerHopIn, passing the homepage reference
    var hop = new SellerHopIn(homepage);
    hop.setVisible(true);
    hop.pack();
    hop.setLocationRelativeTo(null);
    
    // Optionally close the current frame if needed
    // this.dispose();
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JPanel navigation;
    private javax.swing.JButton sellerbtn;
    // End of variables declaration//GEN-END:variables
}
