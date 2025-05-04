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
import newPackage.Order;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
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
import newPackage.CartItem;
public class Homepage extends javax.swing.JFrame {

    private Homepage homepage;
    private WheelMenu wheelMenu;
    private JButton wheelButton;
    private String firstName;
    private String lastName;
    private String username;

    /**
     * Creates new form Homepage
     */
    public Homepage(String firstName, String lastName, String Username) {
        homepage = this;
        this.username = Username;
        this.firstName = firstName;
        this.lastName = lastName;
        initComponents();
         txtName.setText(lastName + " " + firstName);
        this.setLocationRelativeTo(null);
        
        // Initialize wheel menu with functional buttons
    String[] wheelItems = {"Profile", "Settings", "History", "Messages"};
    wheelMenu = new WheelMenu(this, wheelItems, e -> {
        String command = e.getActionCommand();
        // Handle each menu item click
        switch(command) {
            case "Profile":
                JOptionPane.showMessageDialog(this, "Profile clicked");
                // Add your profile opening code here
                break;
            case "Settings":
                JOptionPane.showMessageDialog(this, "Settings clicked");
                // Add your settings opening code here
                break;
            case "History":
                JOptionPane.showMessageDialog(this, "History clicked");
                // Add your history opening code here
                break;
            case "Messages":
                JOptionPane.showMessageDialog(this, "Messages clicked");
                // Add your messages opening code here
                break;
        }
    });
    
    // Create wheel button
    wheelButton = new JButton("☰");
    wheelButton.setBackground(new Color(69, 125, 88));
    wheelButton.setForeground(Color.WHITE);
    wheelButton.setBorderPainted(false);
    wheelButton.setFocusPainted(false);
    wheelButton.setPreferredSize(new Dimension(40, 52));
    wheelButton.setFont(new Font("Arial", Font.PLAIN, 20));

    wheelButton.addActionListener(e -> {
        if (wheelMenu.isVisible()) {
            wheelMenu.setVisible(false);
        } else {
            wheelMenu.showMenu(wheelButton);
        }
    });
    
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


public class WheelMenu extends JDialog {
    private String[] items;
    private ActionListener listener;
    private int radius = 120;
    private int buttonSize = 50;
    private Point[] buttonCenters;
    private JButton[] wheelButtons;
    private Color[] buttonColors = {
        new Color(255, 102, 102),  // Red
        new Color(102, 178, 255),  // Blue
        new Color(102, 255, 102),  // Green
        new Color(255, 178, 102)   // Orange
    };

    public WheelMenu(Window owner, String[] items, ActionListener listener) {
        super(owner);
        this.items = items;
        this.listener = listener;
        this.buttonCenters = new Point[items.length];
        this.wheelButtons = new JButton[items.length];
        setUndecorated(true);
        setModal(false);
        setFocusableWindowState(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        
        // Calculate size needed for the wheel
        int size = radius * 2 + buttonSize;
        setSize(size, size);
        
        // Create transparent content pane
        JPanel contentPane = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw green circle background
                g2.setColor(new Color(15,58,41));
                g2.fillOval(getWidth()/2 - radius, getHeight()/2 - radius, radius*2, radius*2);
                g2.dispose();
            }
        };
        contentPane.setOpaque(false);
        setContentPane(contentPane);
        
        // Calculate positions and create buttons
        double angleStep = 2 * Math.PI / items.length;
        double angle = -Math.PI / 2; // Start at top
        
    for (int i = 0; i < items.length; i++) {
    final int buttonIndex = i; // Create final copy of i for use in inner class
    int x = getWidth()/2 + (int)(radius * Math.cos(angle)) - buttonSize/2;
    int y = getHeight()/2 + (int)(radius * Math.sin(angle)) - buttonSize/2;

    JButton button = new JButton(items[i]);
    button.setBounds(x, y, buttonSize, buttonSize);
    button.setBackground(buttonColors[buttonIndex % buttonColors.length]);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setForeground(Color.WHITE);
    button.setFont(new Font("Arial", Font.BOLD, 12));

    // Add hover effect
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(button.getBackground().darker());
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(buttonColors[buttonIndex % buttonColors.length]);
        }
    });

    // Store button center for click detection
    buttonCenters[buttonIndex] = new Point(x + buttonSize/2, y + buttonSize/2);

    final String command = items[buttonIndex];
    button.addActionListener(e -> {
        listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command));
        setVisible(false);
    });
    
    contentPane.add(button);
    wheelButtons[buttonIndex] = button;
    
    angle += angleStep;
}
    }

    public void showMenu(Component relativeTo) {
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
 
//methods
private void deleteCartItem(CartItem item) {
    try {
        String query = "DELETE FROM cart WHERE cart_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, item.getCartId());
        
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Item removed from cart");
            loadCart(); // Refresh the cart
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error removing item: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void editOrder(Order order) {
    try {
        // Open a dialog to edit the order quantity
        String newQtyStr = JOptionPane.showInputDialog(this, 
            "Edit quantity for " + order.getProductName() + ":", 
            order.getQuantity());
        
        if (newQtyStr != null && !newQtyStr.isEmpty()) {
            int newQty = Integer.parseInt(newQtyStr);
            if (newQty > 0) {
                // Update the order in database
                String query = "UPDATE orders SET quantity = ?, total_amount = ? WHERE order_id = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, newQty);
                pst.setDouble(2, order.getPrice() * newQty);
                pst.setInt(3, order.getOrderId());
                
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Order updated successfully");
                    loadOrders(); // Refresh the order list
                }
            } else {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0");
            }
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error updating order: " + ex.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void checkoutCartItem(CartItem item) {
    try {
        // Insert into orders table
        String query = "INSERT INTO orders (username, product_name, store_name, price, quantity, total_amount) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);
        pst.setString(2, item.getProductName());
        pst.setString(3, item.getStoreName());
        pst.setDouble(4, item.getPrice());
        pst.setInt(5, item.getQuantity());
        pst.setDouble(6, item.getPrice() * item.getQuantity()); // Calculate total
        
        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected > 0) {
            // Remove from cart after successful checkout
            deleteCartItem(item);
            JOptionPane.showMessageDialog(this, "Checkout successful! Order has been placed.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to checkout item", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error during checkout: " + ex.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void cancelOrder(Order order) {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to cancel order #" + order.getOrderId() + "?",
        "Confirm Cancellation",
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String query = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, order.getOrderId()); // Use order ID instead of product name
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Order #" + order.getOrderId() + " cancelled successfully");
                loadOrders(); // Refresh the order list
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to cancel order #" + order.getOrderId());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

//Loaders  
private void loadCart() {
    SwingWorker<List<CartItem>, Void> worker = new SwingWorker<List<CartItem>, Void>() {
        @Override
        protected List<CartItem> doInBackground() throws Exception {
            List<CartItem> cartItems = new ArrayList<>();
            
            try {
                String query = "SELECT cart_id, product_name, store_name, price, quantity FROM cart WHERE username = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, username);
                
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()) {
                    int cartId = rs.getInt("cart_id");
                    String productName = rs.getString("product_name");
                    String storeName = rs.getString("store_name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    
                    cartItems.add(new CartItem(cartId, productName, storeName, price, quantity));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(Homepage.this, 
                    "Error loading cart: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            return cartItems;
        }
        
        @Override
        protected void done() {
            try {
                List<CartItem> cartItems = get();
                displayCart(cartItems); // Make sure this method exists and is properly defined
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Homepage.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    worker.execute();
}
private void loadOrders() {
    SwingWorker<List<Order>, Void> worker = new SwingWorker<>() {
        @Override
        protected List<Order> doInBackground() throws Exception {
            List<Order> orders = new ArrayList<>();
            
            try {
                String query = "SELECT order_id, username, product_name, store_name, price, quantity, total_amount FROM orders WHERE username = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, username);
                
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()) {
                    int orderId = rs.getInt("order_id"); // Get order ID
                    String productName = rs.getString("product_name");
                    String storeName = rs.getString("store_name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    double totalAmount = rs.getDouble("total_amount");
                    
                    // Create Order with orderId
                    orders.add(new Order(orderId, productName, storeName, price, quantity, totalAmount));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(Homepage.this, 
                    "Error loading orders: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            return orders;
        }
        
        @Override
        protected void done() {
            try {
                List<Order> orders = get();
                displayOrders(orders);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Homepage.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    worker.execute();
}  
private void loadProducts() {
    loadProducts(""); // Load all products by default
}
private void loadProducts(String searchTerm) {
    SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
        @Override
        protected List<Product> doInBackground() throws Exception {
            List<Product> products = new ArrayList<>();
            
            try {
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
                
                // Add search condition if search term is not empty
                if (!searchTerm.isEmpty()) {
                    query += " WHERE product_name LIKE ? OR " +
                                "tagline LIKE ? OR " +
                                "tagline LIKE ? OR " +
                                "storename LIKE ?";
                }
                
                // Add order by clause
                query += " ORDER BY product_id DESC";

                PreparedStatement pst = con.prepareStatement(query);
                
                if (!searchTerm.isEmpty()) {
                    String likeTerm = "%" + searchTerm + "%";
                    String hashTerm = "%#" + searchTerm + "%";  // For hashtag matching
                    pst.setString(1, likeTerm);   // product_name
                    pst.setString(2, likeTerm);   // tagline (regular match)
                    pst.setString(3, hashTerm);   // tagline (hashtag match)
                    pst.setString(4, likeTerm);   // storename
                }
                
                ResultSet rs = pst.executeQuery();
                
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

//displayer
private void displayOrders(List<Order> orders) {
    // Clear existing tabs
    orderTab.removeAll();
    
    if (orders.isEmpty()) {
        // Create a panel for "no orders" message
        JPanel noOrdersPanel = new JPanel();
        noOrdersPanel.setBackground(new Color(42, 58, 41));
        JLabel noOrdersLabel = new JLabel("No orders found");
        noOrdersLabel.setForeground(Color.WHITE);
        noOrdersPanel.add(noOrdersLabel);
        orderTab.addTab("No Orders", noOrdersPanel);
    } else {
        // Group orders by status or create tabs as needed
        // For this example, we'll just create one tab with all orders
        JPanel allOrdersPanel = new JPanel();
        allOrdersPanel.setLayout(new BoxLayout(allOrdersPanel, BoxLayout.Y_AXIS));
        allOrdersPanel.setBackground(new Color(42, 58, 41));
        
        // Add some padding at the top
        allOrdersPanel.add(Box.createVerticalStrut(10));
        
        for (Order order : orders) {
            JPanel card = createOrderCard(order);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            allOrdersPanel.add(card);
            // Add some spacing between cards
            allOrdersPanel.add(Box.createVerticalStrut(15));
        }
        
        // Create a scroll pane for the orders panel
        JScrollPane scrollPane = new JScrollPane(allOrdersPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(42, 58, 41));
        
        // Add the tab
        orderTab.addTab("All Orders", scrollPane);
    }
    
    // Force a UI update
    orderTab.revalidate();
    orderTab.repaint();
}
private void displayCart(List<CartItem> cartItems) {
    // Clear existing components from cartTab
    cartTab.removeAll();
    
    if (cartItems.isEmpty()) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(42, 58, 41));
        JLabel emptyLabel = new JLabel("Your cart is empty");
        emptyLabel.setForeground(Color.WHITE);
        emptyPanel.add(emptyLabel);
        cartTab.add("Empty Cart", emptyPanel);
    } else {
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(new Color(42, 58, 41));
        
        for (CartItem item : cartItems) {
            JPanel itemPanel = createCartItemPanel(item);
            cartPanel.add(itemPanel);
            cartPanel.add(Box.createVerticalStrut(10));
        }
        
        JScrollPane scrollPane = new JScrollPane(cartPanel);
        scrollPane.setBackground(new Color(42, 58, 41));
        cartTab.add("Your Cart", scrollPane);
    }
    
    cartTab.revalidate();
    cartTab.repaint();
}

//creator
private JPanel createCartItemPanel(CartItem item) {
    // Create a fixed-size card panel with BorderLayout
    JPanel panel = new JPanel(new BorderLayout(10, 0));
    panel.setPreferredSize(new Dimension(900, 100));
    panel.setMaximumSize(new Dimension(900, 100));
    panel.setMinimumSize(new Dimension(900, 100));
    panel.setBackground(new Color(69, 125, 88));
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(42, 58, 41)),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));

    // Left side - Cart item info (using GridBagLayout for precise control)
    JPanel infoPanel = new JPanel(new GridBagLayout());
    infoPanel.setBackground(new Color(69, 125, 88));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 5, 0);

    // Cart ID with bold font
    JLabel idLabel = new JLabel("Cart ID: " + item.getCartId());
    idLabel.setFont(new Font("Arial", Font.BOLD, 14));
    idLabel.setForeground(Color.WHITE);
    infoPanel.add(idLabel, gbc);

    // Product name
    gbc.gridy++;
    JLabel nameLabel = new JLabel(item.getProductName());
    nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    nameLabel.setForeground(Color.WHITE);
    infoPanel.add(nameLabel, gbc);

    // Store name and price
    gbc.gridy++;
    JLabel detailsLabel = new JLabel(String.format("From: %s | ₱%.2f × %d", 
        item.getStoreName(), item.getPrice(), item.getQuantity()));
    detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    detailsLabel.setForeground(Color.WHITE);
    infoPanel.add(detailsLabel, gbc);

    // Right side - Buttons panel
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    buttonPanel.setBackground(new Color(69, 125, 88));
    GridBagConstraints gbcButtons = new GridBagConstraints();
    gbcButtons.insets = new Insets(0, 10, 0, 0);
    gbcButtons.anchor = GridBagConstraints.EAST;

    // Delete button
    JButton deleteButton = new JButton("Delete");
    deleteButton.setPreferredSize(new Dimension(100, 30));
    deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
    deleteButton.setBackground(new Color(255, 102, 102));
    deleteButton.setForeground(Color.WHITE);
    deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    deleteButton.setFocusPainted(false);
    deleteButton.addActionListener(e -> deleteCartItem(item));
    buttonPanel.add(deleteButton, gbcButtons);

    // Checkout button
    gbcButtons.gridx++;
    JButton checkoutButton = new JButton("Check Out");
    checkoutButton.setPreferredSize(new Dimension(100, 30));
    checkoutButton.setFont(new Font("Arial", Font.BOLD, 12));
    checkoutButton.setBackground(new Color(102, 178, 255));
    checkoutButton.setForeground(Color.WHITE);
    checkoutButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    checkoutButton.setFocusPainted(false);
    checkoutButton.addActionListener(e -> checkoutCartItem(item));
    buttonPanel.add(checkoutButton, gbcButtons);

    // Add panels to main panel
    panel.add(infoPanel, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.EAST);
    
    return panel;
}
private JPanel createOrderCard(Order order) {
    // Create a fixed-size card panel
    JPanel card = new JPanel(new BorderLayout(15, 0));
    card.setPreferredSize(new Dimension(900, 150));
    card.setMaximumSize(new Dimension(900, 150));
    card.setMinimumSize(new Dimension(900, 150));
    card.setBackground(new Color(69, 125, 88));
    card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    
    // Left Panel - Order details
    JPanel leftPanel = new JPanel(new GridLayout(0, 1, 0, 5));
    leftPanel.setBackground(new Color(69, 125, 88));
    leftPanel.setPreferredSize(new Dimension(600, 130));

    JLabel orderIdLabel = new JLabel("Order #" + order.getOrderId());
    orderIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
    orderIdLabel.setForeground(Color.WHITE);
    leftPanel.add(orderIdLabel, 0);
    
    // Product Name
    JLabel nameLabel = new JLabel("Product: " + order.getProductName());
    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
    nameLabel.setForeground(Color.WHITE);
    leftPanel.add(nameLabel);

    // Store Name
    JLabel storeLabel = new JLabel("Store: " + order.getStoreName());
    storeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    storeLabel.setForeground(Color.WHITE);
    leftPanel.add(storeLabel);

    // Price and Quantity
    JPanel priceQtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    priceQtyPanel.setBackground(new Color(69, 125, 88));
    
    JLabel priceLabel = new JLabel("Price: ₱" + String.format("%.2f", order.getPrice()));
    priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    priceLabel.setForeground(Color.WHITE);
    priceQtyPanel.add(priceLabel);
    
    JLabel qtyLabel = new JLabel("  Qty: " + order.getQuantity());
    qtyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    qtyLabel.setForeground(Color.WHITE);
    priceQtyPanel.add(qtyLabel);
    
    leftPanel.add(priceQtyPanel);

    // Total Amount
    JLabel totalLabel = new JLabel("Total: ₱" + String.format("%.2f", order.getTotalAmount()));
    totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
    totalLabel.setForeground(Color.WHITE);
    leftPanel.add(totalLabel);

    // Right Panel - Buttons
    JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 10));
    rightPanel.setBackground(new Color(69, 125, 88));
    rightPanel.setPreferredSize(new Dimension(200, 130));

    // Edit button - Standardized size
    JButton editButton = new JButton("Edit Order");
    editButton.setPreferredSize(new Dimension(50, 25)); // Same size as cart card buttons
    editButton.setFont(new Font("Arial", Font.BOLD, 12));
    editButton.setBackground(new Color(102, 178, 255)); // Blue color
    editButton.setForeground(Color.WHITE);
    editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    editButton.setFocusPainted(false);
    editButton.addActionListener(e -> editOrder(order));
    rightPanel.add(editButton);

    // Cancel button - Standardized size
    JButton cancelButton = new JButton("Cancel Order");
    cancelButton.setPreferredSize(new Dimension(100, 30)); // Same size as cart card buttons
    cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
    cancelButton.setBackground(new Color(255, 102, 102)); // Red color
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    cancelButton.setFocusPainted(false);
    cancelButton.addActionListener(e -> cancelOrder(order));
    rightPanel.add(cancelButton);

    // Add panels to the card
    card.add(leftPanel, BorderLayout.CENTER);
    card.add(rightPanel, BorderLayout.EAST);

    return card;
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
    jTabbedPane2.addTab("Recommended", scrollPane);
    
    // Force a UI update
    jTabbedPane2.revalidate();
    jTabbedPane2.repaint();
    System.out.println("gay");  
}
private JPanel createProductCard(Product product) {
    // Create a fixed-size card panel
    JPanel card = new JPanel(new BorderLayout(15, 0));
    card.setPreferredSize(new Dimension(900, 190)); // Fixed size
    card.setMaximumSize(new Dimension(900, 190));   // Ensures it won't grow
    card.setMinimumSize(new Dimension(900, 190));   // Ensures it won't shrink
    card.setBackground(new Color(69, 125, 88));
    card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    // Left Panel - Text Content using GridLayout
    JPanel leftPanel = new JPanel(new GridLayout(0, 1, 0, 5));
    leftPanel.setBackground(new Color(69, 125, 88));
    leftPanel.setPreferredSize(new Dimension(600, 170)); // Fixed width for left panel

    // Product Name (Left aligned)
    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    namePanel.setBackground(new Color(69, 125, 88));
    JLabel nameLabel = new JLabel(product.getProductName());
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    nameLabel.setForeground(Color.WHITE);
    namePanel.add(nameLabel);
    leftPanel.add(namePanel);

    // Store Name (Left aligned)
    JPanel storePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    storePanel.setBackground(new Color(69, 125, 88));
    JLabel storeLabel = new JLabel(product.getStoreName());
    storeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    storeLabel.setForeground(Color.WHITE);
    storePanel.add(storeLabel);
    leftPanel.add(storePanel);

    // Tagline (Left aligned)
    JPanel taglinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    taglinePanel.setBackground(new Color(69, 125, 88));
    JLabel taglineLabel = new JLabel("#" + product.getTagline());
    taglineLabel.setFont(new Font("Arial", Font.ITALIC, 11));
    taglineLabel.setForeground(Color.LIGHT_GRAY);
    taglinePanel.add(taglineLabel);
    leftPanel.add(taglinePanel);

    // Price Panel (Left aligned)
    JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    pricePanel.setBackground(new Color(69, 125, 88));
    
    JLabel currentPrice = new JLabel("₱" + String.format("%.2f", product.getPrice()));
    currentPrice.setFont(new Font("Arial", Font.BOLD, 14));
    currentPrice.setForeground(Color.WHITE);
    pricePanel.add(currentPrice);

    if (product.getOriginalPrice() > product.getPrice()) {
        JLabel originalPrice = new JLabel("₱" + String.format("%.2f", product.getOriginalPrice()));
        originalPrice.setFont(new Font("Arial", Font.PLAIN, 12));
        originalPrice.setForeground(Color.RED);
        originalPrice.setText("<html><s>" + originalPrice.getText() + "</s></html>");
        pricePanel.add(originalPrice);

        JLabel discountLabel = new JLabel(String.format(" %.0f%% OFF", 
            100 * (product.getOriginalPrice() - product.getPrice()) / product.getOriginalPrice()));
        discountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        discountLabel.setForeground(Color.WHITE);
        pricePanel.add(discountLabel);
    }
    leftPanel.add(pricePanel);

    // Buttons Panel (Left aligned)
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    buttonPanel.setBackground(new Color(69, 125, 88));
    
    JButton buyButton = new JButton("Buy");
    buyButton.setPreferredSize(new Dimension(80, 25));
    buyButton.setFont(new Font("Arial", Font.BOLD, 12));
    buyButton.setBackground(Color.WHITE);
    buyButton.addActionListener(e -> {
         try {
        Buypage buypage = new Buypage(product, username); // Pass username here
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
    addToCartButton.setBackground(Color.WHITE);
    addToCartButton.addActionListener(e -> {
     try {
         Addtocart cart = new Addtocart(product, username);
            cart.setVisible(true);
            cart.setLocationRelativeTo(null);
     }catch(Exception ex){
         JOptionPane.showMessageDialog(null,
                 "Error openning product details: " + ex.getMessage(),
                 "Error", JOptionPane.ERROR_MESSAGE);
         
     }
    });
    buttonPanel.add(addToCartButton);
    leftPanel.add(buttonPanel);

    // Rating and Location (Left aligned)
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    infoPanel.setBackground(new Color(69, 125, 88));
    
    JLabel ratingLabel = new JLabel(String.format("★ %.1f", product.getRating()));
    ratingLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    ratingLabel.setForeground(Color.WHITE);
    infoPanel.add(ratingLabel);

    JLabel locationLabel = new JLabel(product.getLocation());
    locationLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    locationLabel.setForeground(Color.WHITE);
    infoPanel.add(locationLabel);
    leftPanel.add(infoPanel);

    // Right Panel - Image (fixed size)
    JPanel rightPanel = new JPanel(new GridLayout(1, 2, 5, 0));
    rightPanel.setBackground(new Color(69, 125, 88));
    rightPanel.setPreferredSize(new Dimension(300, 170)); // Fixed width for right panel

    try {
        // First image
        ImageIcon icon1 = new ImageIcon(product.getImage1());
        Image img1 = icon1.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel1 = new JLabel(new ImageIcon(img1));
        imageLabel1.setPreferredSize(new Dimension(140, 100));
        imageLabel1.setOpaque(true);
        imageLabel1.setBackground(Color.BLACK);
        rightPanel.add(imageLabel1);

        // Second image
        ImageIcon icon2 = new ImageIcon(product.getImage2());
        Image img2 = icon2.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel2 = new JLabel(new ImageIcon(img2));
        imageLabel2.setPreferredSize(new Dimension(140, 100));
        imageLabel2.setOpaque(true);
        imageLabel2.setBackground(Color.BLACK);
        rightPanel.add(imageLabel2);
    } catch (Exception e) {
        // Placeholder if images fail to load
        JLabel placeholder1 = new JLabel("Image 1 not available");
        placeholder1.setPreferredSize(new Dimension(140, 100));
        rightPanel.add(placeholder1);
        
        JLabel placeholder2 = new JLabel("Image 2 not available");
        placeholder2.setPreferredSize(new Dimension(140, 100));
        rightPanel.add(placeholder2);
    }

    // Add panels to the card
    card.add(leftPanel, BorderLayout.CENTER);
    card.add(rightPanel, BorderLayout.EAST);

    return card;
}

    
//Get instnance
public Homepage getInstance(String firstname, String lastname) {
        if (homepage == null) {
            homepage = new Homepage(firstName, lastName, username);
        }
        return homepage;
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        sellerbtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        helpbtn = new javax.swing.JButton();
        logoutbtn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        btnOrder = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        parent = new javax.swing.JPanel();
        Home = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        Order = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        orderTab = new javax.swing.JTabbedPane();
        Cart = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cartTab = new javax.swing.JTabbedPane();
        History = new javax.swing.JPanel();
        Profile = new javax.swing.JPanel();
        About = new javax.swing.JPanel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        navigation.setBackground(new java.awt.Color(52, 70, 49));

        jPanel1.setBackground(new java.awt.Color(69, 125, 88));

        jList1.setBackground(new java.awt.Color(15, 58, 41));
        jList1.setForeground(new java.awt.Color(255, 255, 255));
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "MISCS", "DRUGS", "BEVERAGE", "CLOTHES", "TOOLS", "GADGETS", "MORE..." };
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

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Categories:");

        sellerbtn.setBackground(new java.awt.Color(69, 125, 88));
        sellerbtn.setForeground(new java.awt.Color(255, 255, 255));
        sellerbtn.setText("Be a Seller");
        sellerbtn.setAlignmentY(0.0F);
        sellerbtn.setPreferredSize(new java.awt.Dimension(102, 23));
        sellerbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellerbtnActionPerformed(evt);
            }
        });

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

        txtName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtName.setForeground(new java.awt.Color(255, 255, 255));
        txtName.setText("NAME");

        jPanel2.setBackground(new java.awt.Color(51, 102, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("User:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnCart.setBackground(new java.awt.Color(69, 125, 88));
        btnCart.setForeground(new java.awt.Color(255, 255, 255));
        btnCart.setText("MY CART");
        btnCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCartActionPerformed(evt);
            }
        });

        btnOrder.setBackground(new java.awt.Color(69, 125, 88));
        btnOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnOrder.setText("ORDERS");
        btnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrderActionPerformed(evt);
            }
        });

        btnHome.setBackground(new java.awt.Color(69, 125, 88));
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setText("HOME");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        parent.setBackground(new java.awt.Color(42, 58, 41));
        parent.setLayout(new java.awt.CardLayout());

        Home.setBackground(new java.awt.Color(42, 58, 41));
        // In the Home layout (initComponents):

        txtSearch.setBackground(new java.awt.Color(255, 255, 255));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        searchButton.setBackground(new java.awt.Color(255, 255, 255));
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/search.png"))); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 51));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("Hot deals");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HomeLayout = new javax.swing.GroupLayout(Home);
        Home.setLayout(HomeLayout);
        HomeLayout.setHorizontalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(729, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 952, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );
        HomeLayout.setVerticalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        parent.add(Home, "card5");

        Order.setBackground(new java.awt.Color(42, 58, 41));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Your Orders:");

        javax.swing.GroupLayout OrderLayout = new javax.swing.GroupLayout(Order);
        Order.setLayout(OrderLayout);
        OrderLayout.setHorizontalGroup(
            OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OrderLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(orderTab))
                    .addGroup(OrderLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 862, Short.MAX_VALUE)))
                .addContainerGap())
        );
        OrderLayout.setVerticalGroup(
            OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OrderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(orderTab, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        parent.add(Order, "card3");

        Cart.setBackground(new java.awt.Color(42, 58, 41));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Your Cart:");

        javax.swing.GroupLayout CartLayout = new javax.swing.GroupLayout(Cart);
        Cart.setLayout(CartLayout);
        CartLayout.setHorizontalGroup(
            CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CartLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel5)
                .addContainerGap(877, Short.MAX_VALUE))
            .addGroup(CartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cartTab)
                .addContainerGap())
        );
        CartLayout.setVerticalGroup(
            CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CartLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel5)
                .addGap(30, 30, 30)
                .addComponent(cartTab, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        parent.add(Cart, "card4");

        History.setBackground(new java.awt.Color(42, 58, 41));

        javax.swing.GroupLayout HistoryLayout = new javax.swing.GroupLayout(History);
        History.setLayout(HistoryLayout);
        HistoryLayout.setHorizontalGroup(
            HistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1014, Short.MAX_VALUE)
        );
        HistoryLayout.setVerticalGroup(
            HistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );

        parent.add(History, "card6");

        Profile.setBackground(new java.awt.Color(42, 58, 41));

        javax.swing.GroupLayout ProfileLayout = new javax.swing.GroupLayout(Profile);
        Profile.setLayout(ProfileLayout);
        ProfileLayout.setHorizontalGroup(
            ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1014, Short.MAX_VALUE)
        );
        ProfileLayout.setVerticalGroup(
            ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );

        parent.add(Profile, "card7");

        About.setBackground(new java.awt.Color(42, 58, 41));

        javax.swing.GroupLayout AboutLayout = new javax.swing.GroupLayout(About);
        About.setLayout(AboutLayout);
        AboutLayout.setHorizontalGroup(
            AboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1014, Short.MAX_VALUE)
        );
        AboutLayout.setVerticalGroup(
            AboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );

        parent.add(About, "card5");

        javax.swing.GroupLayout navigationLayout = new javax.swing.GroupLayout(navigation);
        navigation.setLayout(navigationLayout);
        navigationLayout.setHorizontalGroup(
            navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationLayout.createSequentialGroup()
                .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(sellerbtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCart, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(parent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        navigationLayout.setVerticalGroup(
            navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addComponent(btnOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCart, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
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
                        .addComponent(sellerbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(parent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navigation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navigation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutbtnActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(
            Homepage.this,
            "Are you sure you want to LOG OUT?",
            "Confirm LOG OUT",
            JOptionPane.YES_NO_OPTION);
         if (confirm == JOptionPane.YES_OPTION) {
            Login login = new Login();
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
        this.dispose();
        }
        
    }//GEN-LAST:event_logoutbtnActionPerformed

    private void helpbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_helpbtnActionPerformed

    private void sellerbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellerbtnActionPerformed
        // TODO add your handling code here:
    // Create and show SellerHopIn, passing the homepage reference
    var hop = new SellerHopIn(homepage, username);
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

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
         String searchTerm = txtSearch.getText().trim();
    if (!searchTerm.isEmpty()) {
        try {
            loadProducts(searchTerm);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error searching products: " + ex.getMessage(),
                "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        loadProducts(); // Load all products if search term is empty
    }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void btnOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrderActionPerformed
        // TODO add your handling code here:
        parent.removeAll();
        parent.add(Order);
        parent.repaint();
        parent.revalidate();
        loadOrders();
    }//GEN-LAST:event_btnOrderActionPerformed

    private void btnCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCartActionPerformed
        // TODO add your handling code here:
        parent.removeAll();
        parent.add(Cart);
        parent.repaint();
        parent.revalidate();
        loadCart();
    }//GEN-LAST:event_btnCartActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        parent.removeAll();
        parent.add(Home);
        parent.repaint();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

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
                String firstname = null;
                String lastname = null;
                String Username = null;
                new Homepage(firstname, lastname, Username).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel About;
    private javax.swing.JPanel Cart;
    private javax.swing.JPanel History;
    private javax.swing.JPanel Home;
    private javax.swing.JPanel Order;
    private javax.swing.JPanel Profile;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnOrder;
    private javax.swing.JTabbedPane cartTab;
    private javax.swing.JButton helpbtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JButton logoutbtn;
    private javax.swing.JPanel navigation;
    private javax.swing.JTabbedPane orderTab;
    private javax.swing.JPanel parent;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton sellerbtn;
    private javax.swing.JLabel txtName;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
