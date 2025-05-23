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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
    public Homepage(Homepage homepage, String firstName, String lastName, String Username) {
        homepage = this;
        this.username = Username;
        this.firstName = firstName;
        this.lastName = lastName;
        initComponents();
        try {
            Connection();
            if (hasStoreRegistered(username)) {
                sellerbtn.setText("My Store");
            } else {
                sellerbtn.setText("Be a Seller");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
        jLabel4.setText(lastName + " " + firstName); // Set the name label
        txtName.setText(lastName + " " + firstName);
        labelUsername.setText(username); // Set the username label
        loadHistory();
        loadUserData();
        loadProfilePicture();
        pfpChangepass.addActionListener(e -> changePassword());
        pfpDelete.addActionListener(e -> deleteAccount());
        String[] wheelItems = {"P", "S", "H", "M"};
        wheelMenu = new WheelMenu(this, wheelItems, e -> {
            String command = e.getActionCommand();
            switch (command) {
                case "P":
                    parent.removeAll();
                    parent.add(Profile);
                    parent.repaint();
                    parent.revalidate();
                    break;
                case "S":
                    JOptionPane.showMessageDialog(this, "Settings clicked");
                    break;
                case "H":
                    parent.removeAll();
                    parent.add(History);
                    parent.repaint();
                    parent.revalidate();
                    loadHistory();
                    break;
                case "M":
                    JOptionPane.showMessageDialog(this, "Messages clicked");
                    break;
            }
        });

        loadProducts();

        this.setLocationRelativeTo(null);




        hotdealsBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (hotdealsBTN.isSelected()) {
                    hotdealsBTN.setText("Recommended");
                    hotdealsBTN.setBackground(new Color(255, 102, 102)); // Change color for toggled-on
                    jTabbedPane2.setTitleAt(0, "Hot Deals");
                    loadHotDeals(); // Show products with 50%+ discounts
                } else {
                    hotdealsBTN.setText("HOT DEALS");
                    hotdealsBTN.setBackground(new Color(255, 255, 0)); // Change color for toggled-off
                    hotdealsBTN.setForeground(Color.RED);
                    jTabbedPane2.setTitleAt(0, "Recommended");
                    loadProducts(); // Show all products
                }
            }
        });
        pfpUpload.addActionListener(e -> uploadProfilePicture());
        productList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // To prevent multiple triggers
                String selectedType = productList.getSelectedValue();

                if (selectedType.equalsIgnoreCase("ALL")) {
                    loadProducts(""); // Show all products
                } else {
                    loadProducts(selectedType); // Show products of the selected type
                }
            }
        });
    }

    //onnection method
    Connection con;
    //SQLstatement
    Statement st;

    //Required for Connections
    //DbName,DbDriver,Url,USername,Password
    private static final String DbName = "shipzy";
    private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DbUrl = "jdbc:mysql://localhost:3306/" + DbName; //instead of Concatenation i used the value directly
    private static final String DbUsername = "root";
    private static final String DbPassword = "";

    //Create a method for connections 
    public void Connection() throws SQLException {
        try {
            con = DriverManager.getConnection(DbUrl, DbUsername, DbPassword);
            st = con.createStatement();
            if (con != null) {
                System.out.println("Connection Successful");
            }
            Class.forName(DbDriver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


//Ratings
    private void rateProduct(Order order) {
        if (order.isRated()) {
            JOptionPane.showMessageDialog(this,
                    "You have already rated this product",
                    "Already Rated", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create rating dialog with message field
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel question = new JLabel("How would you rate " + order.getProductName() + "?");
        JLabel storeLabel = new JLabel("From: " + order.getStoreName());

        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(new FlowLayout());

        JLabel ratingLabel = new JLabel("Rating (1-5):");
        JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        ratingPanel.add(ratingLabel);
        ratingPanel.add(ratingCombo);

        // Add message area
        JLabel messageLabel = new JLabel("Your review (optional):");
        JTextArea messageArea = new JTextArea(3, 20);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        panel.add(question);
        panel.add(storeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(ratingPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(messageLabel);
        panel.add(messageScroll);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Rate Product",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int rating = (int) ratingCombo.getSelectedItem();
            String reviewMessage = messageArea.getText().trim();
            updateProductRating(order, rating, reviewMessage);
        }
    }
    private void updateProductRating(Order order, int newRating, String reviewMessage) {
        try {
            // Start transaction
            con.setAutoCommit(false);

            // 1. Record the individual rating and review
            String ratingQuery = "INSERT INTO product_ratings (username, product_name, rating, review_message, review_date) "
                    + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) "
                    + "ON DUPLICATE KEY UPDATE rating = VALUES(rating), review_message = VALUES(review_message), review_date = VALUES(review_date)";

            PreparedStatement ratingPst = con.prepareStatement(ratingQuery);
            ratingPst.setString(1, username);
            ratingPst.setString(2, order.getProductName());
            ratingPst.setInt(3, newRating);
            ratingPst.setString(4, reviewMessage.isEmpty() ? null : reviewMessage);
            ratingPst.executeUpdate();

            // 2. Update product's average rating
            String productQuery = "UPDATE products SET "
                    + "rating = (SELECT AVG(rating) FROM product_ratings WHERE product_name = ?), "
                    + "rating_count = (SELECT COUNT(*) FROM product_ratings WHERE product_name = ?) "
                    + "WHERE product_name = ?";

            PreparedStatement productPst = con.prepareStatement(productQuery);
            productPst.setString(1, order.getProductName());
            productPst.setString(2, order.getProductName());
            productPst.setString(3, order.getProductName());
            productPst.executeUpdate();

            // Commit transaction
            con.commit();

            // Update the order object
            order.setRated(true);

            JOptionPane.showMessageDialog(this, "Thank you for your rating and review!");
            loadHistory(); // Refresh the history view

        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, "Rollback failed", e);
            }
            JOptionPane.showMessageDialog(this,
                    "Error submitting rating: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, "Failed to reset auto-commit", e);
            }
        }
    }

    @Override
    public void dispose() {
        if (wheelMenu != null) {
            wheelMenu.dispose();
        }
        super.dispose();
    }

//Graphics
    public class WheelMenu extends JDialog {

        private String[] items;
        private ActionListener listener;
        private int radius = 120;
        private int buttonSize = 50;
        private Point[] buttonCenters;
        private JButton[] wheelButtons;
        private Color[] buttonColors = {
            new Color(255, 102, 102), // Red
            new Color(102, 178, 255), // Blue
            new Color(102, 255, 102), // Green
            new Color(255, 178, 102) // Orange
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
                    g2.setColor(new Color(15, 58, 41));
                    g2.fillOval(getWidth() / 2 - radius, getHeight() / 2 - radius, radius * 2, radius * 2);
                    g2.dispose();
                }
            };
            contentPane.setOpaque(false);
            setContentPane(contentPane);

            // Calculate positions and create buttons
            double angleStep = 2 * Math.PI / items.length;
            double angle = -Math.PI / 2; // Start at top

            for (int i = 0; i < items.length; i++) {
                final int buttonIndex = i;
                int x = getWidth() / 2 + (int) (radius * Math.cos(angle)) - buttonSize / 2;
                int y = getHeight() / 2 + (int) (radius * Math.sin(angle)) - buttonSize / 2;

                JButton button = new JButton();
                button.setBounds(x, y, buttonSize, buttonSize);
                button.setBackground(buttonColors[buttonIndex % buttonColors.length]);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Arial", Font.BOLD, 12));

                // Set icon based on item
                String item = items[buttonIndex].toUpperCase();
                String iconPath = null;
                switch (item) {
                    case "P":
                        iconPath = "/Assets/profile.png";
                        break;
                    case "S":
                        iconPath = "/Assets/settings.png";
                        break;
                    case "H":
                        iconPath = "/Assets/history.png";
                        break;
                    case "M":
                        iconPath = "/Assets/message.png";
                        break;
                }
                if (iconPath != null) {
                    try {
                        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                        Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(img));
                    } catch (Exception e) {
                        button.setText(item);
                    }
                } else {
                    button.setText(item);
                }

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
                buttonCenters[buttonIndex] = new Point(x + buttonSize / 2, y + buttonSize / 2);

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

                if (targetWidth == 0) {
                    targetWidth = Integer.MAX_VALUE;
                }

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
    private void editCartItem(CartItem item) {
        // Create input dialog for new quantity
        String newQtyStr = JOptionPane.showInputDialog(
                this,
                "Enter new quantity for " + item.getProductName() + ":",
                "Edit Quantity",
                JOptionPane.PLAIN_MESSAGE);

        if (newQtyStr != null && !newQtyStr.isEmpty()) {
            try {
                int newQty = Integer.parseInt(newQtyStr);
                if (newQty > 0) {
                    // Update quantity in database
                    String query = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setInt(1, newQty);
                    pst.setInt(2, item.getCartId());

                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        // Update the item object
                        item.setQuantity(newQty);
                        JOptionPane.showMessageDialog(this, "Quantity updated successfully");
                        loadCart(); // Refresh the cart view
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Quantity must be greater than 0",
                            "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error updating quantity: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void checkoutCartItem(CartItem item) {
        // Create payment options dialog
        Object[] options = {"Cash on Delivery", "Online Payment", "Cancel"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select payment method for: " + item.getProductName(),
                "Payment Method",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.CLOSED_OPTION || choice == 2) {
            return; // User cancelled
        }

        String paymentMethod = (choice == 0) ? "Cash on Delivery" : "Online Payment";

        try {
            // Insert into orders table with payment method
            String query = "INSERT INTO orders (username, product_name, store_name, price, quantity, total_amount, payment) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, item.getProductName());
            pst.setString(3, item.getStoreName());
            pst.setDouble(4, item.getPrice());
            pst.setInt(5, item.getQuantity());
            pst.setDouble(6, item.getPrice() * item.getQuantity());
            pst.setString(7, paymentMethod); // Add payment method

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                // Remove from cart after successful checkout
                deleteCartItem(item);
                JOptionPane.showMessageDialog(this,
                        "Checkout successful! Order has been placed with " + paymentMethod + ".");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to checkout item",
                        "Error", JOptionPane.ERROR_MESSAGE);
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
    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");

        // Filter for image files
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = getExtension(f);
                if (extension != null) {
                    return extension.equals("jpg")
                            || extension.equals("jpeg")
                            || extension.equals("png")
                            || extension.equals("gif");
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
                    ext = s.substring(i + 1).toLowerCase();
                }
                return ext;
            }
        });

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Read the image file into a byte array
                FileInputStream fis = new FileInputStream(selectedFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];

                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }

                byte[] imageBytes = bos.toByteArray();

                // Store in database - updated query for accountdetails table
                String query = "UPDATE users SET profile_image = ? WHERE accUsername = ?";

                PreparedStatement pst = con.prepareStatement(query);
                pst.setBytes(1, imageBytes);
                pst.setString(2, username);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    // Display the image
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image img = icon.getImage().getScaledInstance(
                            pfpImage.getWidth(), pfpImage.getHeight(), Image.SCALE_SMOOTH);
                    pfpImage.setIcon(new ImageIcon(img));
                    pfpImage.setText(""); // Clear the text if image is loaded
                    JOptionPane.showMessageDialog(this, "Profile picture updated successfully");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to update profile picture. User not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                fis.close();
                bos.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error uploading image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void changePassword() {
        // Current password field
        JPasswordField currentPass = new JPasswordField();
        // New password field
        JPasswordField newPass = new JPasswordField();
        // Confirm new password field
        JPasswordField confirmPass = new JPasswordField();

        Object[] message = {
            "Current Password:", currentPass,
            "New Password:", newPass,
            "Confirm New Password:", confirmPass
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Change Password",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Verify current password
                String verifyQuery = "SELECT accPassword FROM users WHERE accUsername = ?";
                PreparedStatement pst = con.prepareStatement(verifyQuery);
                pst.setString(1, username);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String currentPassword = rs.getString("accPassword");
                    if (!currentPassword.equals(new String(currentPass.getPassword()))) {
                        JOptionPane.showMessageDialog(this,
                                "Current password is incorrect",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Check if new passwords match
                if (!Arrays.equals(newPass.getPassword(), confirmPass.getPassword())) {
                    JOptionPane.showMessageDialog(this,
                            "New passwords do not match",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update password
                String updateQuery = "UPDATE users SET accPassword = ? WHERE accUsername = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, new String(newPass.getPassword()));
                pst.setString(2, username);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Password changed successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Update displayed password
                    String newPassword = new String(newPass.getPassword());
                    labelPassword.setText(newPassword.charAt(0) + "****" + newPassword.charAt(newPassword.length() - 1));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error changing password: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete your account? This action cannot be undone!",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Ask for password confirmation
            JPasswordField passwordField = new JPasswordField();
            int passConfirm = JOptionPane.showConfirmDialog(
                    this,
                    new Object[]{"Enter your password to confirm:", passwordField},
                    "Confirm Password",
                    JOptionPane.OK_CANCEL_OPTION);

            if (passConfirm == JOptionPane.OK_OPTION) {
                try {
                    // Verify password
                    String verifyQuery = "SELECT accPassword FROM users WHERE accUsername = ?";
                    PreparedStatement pst = con.prepareStatement(verifyQuery);
                    pst.setString(1, username);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        String currentPassword = rs.getString("accPassword");
                        if (!currentPassword.equals(new String(passwordField.getPassword()))) {
                            JOptionPane.showMessageDialog(this,
                                    "Incorrect password",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Delete account
                    String deleteQuery = "DELETE FROM users WHERE accUsername = ?";
                    pst = con.prepareStatement(deleteQuery);
                    pst.setString(1, username);

                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this,
                                "Account deleted successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Return to login screen
                        Login login = new Login();
                        login.setVisible(true);
                        login.pack();
                        login.setLocationRelativeTo(null);
                        this.dispose();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting account: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private boolean checkIfUserRatedProduct(String username, String productName) {
        try {
            String query = "SELECT 1 FROM product_ratings WHERE username = ? AND product_name = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, productName);

            ResultSet rs = pst.executeQuery();
            boolean hasRated = rs.next(); // Returns true if a rating exists

            rs.close();
            pst.close();
            return hasRated;
        } catch (SQLException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    private boolean hasStoreRegistered(String username) {
        try {
            String query = "SELECT Store_name FROM users WHERE accUsername = ? AND Store_name IS NOT NULL";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            boolean hasStore = rs.next();
            rs.close();
            pst.close();
            return hasStore;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public void updateSellerButton(boolean hasStore) {
        if (hasStore) {
            sellerbtn.setText("My Store");
        } else {
            sellerbtn.setText("Be a Seller");
        }
    }

//LOADERS
    private void loadProfilePicture() {
        try {
            String query = "SELECT profile_image FROM users WHERE accUsername = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("profile_image");
                if (imageBytes != null) {
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image img = icon.getImage().getScaledInstance(
                            pfpImage.getWidth(), pfpImage.getHeight(), Image.SCALE_SMOOTH);
                    pfpImage.setIcon(new ImageIcon(img));
                    pfpImage.setText(""); // Remove the text if image is loaded
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading profile picture: " + ex.getMessage());
        }
    }
    private void loadUserData() {
        // Check if connection exists first
        if (con == null) {
            try {
                Connection(); // Try to establish connection if not exists
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed: " + ex.getMessage(),
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // 1. Load password (masked)
            String passwordQuery = "SELECT accPassword, Contacts, User_id FROM users WHERE accUsername = ?";
            pst = con.prepareStatement(passwordQuery);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                String contacts = rs.getString("Contacts");
                String password = rs.getString("accPassword");
                int user_id = rs.getInt("User_id");
                // Close resources after first query
                rs.close();
                pst.close();
                labelUser_id.setText(String.valueOf(user_id));
                labelContacts.setText(contacts);
                // Mask password (show first and last character only)
                String maskedPassword = password.length() > 2
                        ? password.charAt(0) + "****" + password.charAt(password.length() - 1) : "****";
                labelPassword.setText(maskedPassword);
            }

            // 2. Load order and cart stats
            String statsQuery = "SELECT "
                    + "(SELECT COUNT(*) FROM orders WHERE username = ?) as order_count, "
                    + // Changed 'order' to 'orders'
                    "(SELECT COUNT(*) FROM cart WHERE username = ?) as cart_count, "
                    + "(SELECT IFNULL(SUM(total_amount), 0) FROM orders WHERE username = ?) as total_amount";

            pst = con.prepareStatement(statsQuery);
            pst.setString(1, username);
            pst.setString(2, username);
            pst.setString(3, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                labelORDERpfp.setText(String.valueOf(rs.getInt("order_count")));
                labelCARTpfp.setText(String.valueOf(rs.getInt("cart_count")));
                labelTOTALpfp.setText("₱" + String.format("%.2f", rs.getDouble("total_amount")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading user data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Ensure resources are closed
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
    private void loadHistory() {
        SwingWorker<List<Order>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                List<Order> orders = new ArrayList<>();

                try {
                    // Modified query to join with products table to check if rated
                    String query = "SELECT o.order_id, o.product_name, o.store_name, o.price, "
                            + "o.quantity, o.total_amount, o.payment "
                            + // Added payment to select
                            "FROM orders o "
                            + "WHERE o.username = ? AND o.status = 'received'";  // Removed payment condition

                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, username);

                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        String productName = rs.getString("product_name");
                        String storeName = rs.getString("store_name");
                        double price = rs.getDouble("price");
                        int quantity = rs.getInt("quantity");
                        double totalAmount = rs.getDouble("total_amount");
                        String paymentType = rs.getString("payment");

                        // Check if this user has rated by seeing if they appear in the rating log
                        boolean rated = checkIfUserRatedProduct(username, productName);

                        Order order = new Order(orderId, username, productName, storeName,
                                price, quantity, totalAmount, "Received", paymentType);
                        order.setRated(rated);
                        orders.add(order);
                    }

                    rs.close();
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(Homepage.this,
                            "Error loading history: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
                return orders;
            }

            @Override
            protected void done() {
                try {
                    List<Order> orders = get();
                    displayHistory(orders);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Homepage.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    private void loadHotDeals() {
        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                List<Product> products = new ArrayList<>();

                try {
                    String query = "SELECT "
                            + "product_id, "
                            + "product_name, "
                            + "COALESCE(storename, 'Default Store') as storename, "
                            + "COALESCE(type, 'Unknown') as type, "
                            + "COALESCE(tagline, '') as tagline, "
                            + "price, "
                            + "COALESCE(original_price, price) as original_price, "
                            + "image1, "
                            + "image2, "
                            + "COALESCE(rating, 0.0) as rating, "
                            + "COALESCE(location, '') as location "
                            + "FROM products "
                            + "WHERE original_price > price AND "
                            + "(original_price - price) / original_price >= 0.5 "
                            + // At least 50% discount
                            "ORDER BY product_id DESC";

                    PreparedStatement pst = con.prepareStatement(query);
                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("product_id");
                        String name = rs.getString("product_name");
                        String store = rs.getString("storename");
                        String type = rs.getString("type");
                        String tagline = rs.getString("tagline");
                        double price = rs.getDouble("price");
                        double originalPrice = rs.getDouble("original_price");
                        byte[] image1 = rs.getBytes("image1");
                        byte[] image2 = rs.getBytes("image2");
                        double rating = rs.getDouble("rating");
                        String location = rs.getString("location");

                        products.add(new Product(id, name, store, type, tagline, price, originalPrice, image1, image2, rating, location));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(Homepage.this,
                            "Error loading hot deals: " + ex.getMessage(),
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
                return products;
            }

            @Override
            protected void done() {
                try {
                    List<Product> products = get();
                    displayProducts(products); // Reuse the displayProducts method to show the results
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Homepage.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
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
        SwingWorker<List<Order>, Void> worker = new SwingWorker<List<Order>, Void>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                List<Order> orders = new ArrayList<>();

                try {
                    // Modified query to get all orders for the current user
                    String query = "SELECT order_id, username, product_name, store_name, price, quantity, "
                            + "total_amount, status, payment FROM orders WHERE username = ?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, username);  // Set the username parameter

                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        String productName = rs.getString("product_name");
                        String storeName = rs.getString("store_name");
                        double price = rs.getDouble("price");
                        int quantity = rs.getInt("quantity");
                        double totalAmount = rs.getDouble("total_amount");
                        String status = rs.getString("status");
                        String paymentType = rs.getString("payment");

                        orders.add(new Order(orderId, username, productName, storeName,
                                price, quantity, totalAmount, status, paymentType));
                    }

                    rs.close();
                    pst.close();
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
                    String query = "SELECT "
                            + "product_id, "
                            + "product_name, "
                            + "COALESCE(storename, 'Default Store') as storename, "
                            + "COALESCE(type, 'Unknown') as type, "
                            + "COALESCE(tagline, '') as tagline, "
                            + "price, "
                            + "COALESCE(original_price, price) as original_price, "
                            + "image1, "
                            + "image2, "
                            + "COALESCE(rating, 0.0) as rating, "
                            + "COALESCE(location, '') as location "
                            + "FROM products";

                    // Filter by type if a search term is provided
                    if (!searchTerm.isEmpty()) {
                        query += " WHERE type LIKE ?";
                    }

                    query += " ORDER BY product_id DESC";

                    PreparedStatement pst = con.prepareStatement(query);

                    if (!searchTerm.isEmpty()) {
                        pst.setString(1, searchTerm); // Set the type for filtering
                    }

                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("product_id");
                        String name = rs.getString("product_name");
                        String store = rs.getString("storename");
                        String type = rs.getString("type");
                        String tagline = rs.getString("tagline");
                        double price = rs.getDouble("price");
                        double originalPrice = rs.getDouble("original_price");
                        byte[] image1 = rs.getBytes("image1");
                        byte[] image2 = rs.getBytes("image2");
                        double rating = rs.getDouble("rating");
                        String location = rs.getString("location");

                        products.add(new Product(id, name, store, type, tagline, price, originalPrice, image1, image2, rating, location));
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
    private void loadProductsBySearch(String searchTerm) {
        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                List<Product> products = new ArrayList<>();

                try {
                    String query = "SELECT "
                            + "product_id, product_name, "
                            + "COALESCE(storename, 'Default Store') as storename, "
                            + "COALESCE(type, 'Unknown') as type, "
                            + "COALESCE(tagline, '') as tagline, "
                            + "price, COALESCE(original_price, price) as original_price, "
                            + "image1, image2, COALESCE(rating, 0.0) as rating, "
                            + "COALESCE(location, '') as location "
                            + "FROM products "
                            + "WHERE product_name LIKE ? OR storename LIKE ? OR tagline LIKE ? "
                            + "ORDER BY product_id DESC";

                    PreparedStatement pst = con.prepareStatement(query);
                    String searchPattern = "%" + searchTerm + "%";
                    pst.setString(1, searchPattern);
                    pst.setString(2, searchPattern);
                    pst.setString(3, searchPattern);

                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        products.add(new Product(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getString("storename"),
                                rs.getString("type"),
                                rs.getString("tagline"),
                                rs.getDouble("price"),
                                rs.getDouble("original_price"),
                                rs.getBytes("image1"),
                                rs.getBytes("image2"),
                                rs.getDouble("rating"),
                                rs.getString("location")
                        ));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(Homepage.this,
                            "Error searching products: " + ex.getMessage(),
                            "Search Error", JOptionPane.ERROR_MESSAGE);
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
    private void displayHistory(List<Order> orders) {
        // Clear existing tabs
        historyTAB.removeAll();

        if (orders.isEmpty()) {
            // Create a panel for "no history" message
            JPanel noHistoryPanel = new JPanel();
            noHistoryPanel.setBackground(new Color(42, 58, 41));
            JLabel noHistoryLabel = new JLabel("No history found");
            noHistoryLabel.setForeground(Color.WHITE);
            noHistoryPanel.add(noHistoryLabel);
            historyTAB.addTab("No History", noHistoryPanel);
        } else {
            // Create a panel with all history items
            JPanel historyPanel = new JPanel();
            historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
            historyPanel.setBackground(new Color(42, 58, 41));

            // Add some padding at the top
            historyPanel.add(Box.createVerticalStrut(10));

            for (Order order : orders) {
                JPanel card = createHistoryCard(order);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                historyPanel.add(card);
                // Add some spacing between cards
                historyPanel.add(Box.createVerticalStrut(15));
            }

            // Create a scroll pane for the history panel
            JScrollPane scrollPane = new JScrollPane(historyPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setBackground(new Color(42, 58, 41));

            // Add the tab
            historyTAB.addTab("History", scrollPane);
        }

        // Force a UI update
        historyTAB.revalidate();
        historyTAB.repaint();
    }
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
        System.out.println("Loaded");
    }

//creator
    private JPanel createHistoryCard(Order order) {
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
        leftPanel.add(orderIdLabel);

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

        // Right Panel - Rating button
        JPanel rightPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        rightPanel.setBackground(new Color(69, 125, 88));
        rightPanel.setPreferredSize(new Dimension(200, 130));

        // Rating button
        if (order.isRated()) {
            JLabel ratedLabel = new JLabel("Already Rated");
            ratedLabel.setFont(new Font("Arial", Font.BOLD, 12));
            ratedLabel.setForeground(Color.LIGHT_GRAY);
            ratedLabel.setHorizontalAlignment(JLabel.CENTER);
            rightPanel.add(ratedLabel);
        } else {
            JButton rateButton = new JButton("Rate Product");
            rateButton.setPreferredSize(new Dimension(110, 25));
            rateButton.setFont(new Font("Arial", Font.BOLD, 12));
            rateButton.setBackground(new Color(102, 178, 255));
            rateButton.setForeground(Color.WHITE);
            rateButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            rateButton.setFocusPainted(false);
            rateButton.addActionListener(e -> rateProduct(order));
            rightPanel.add(rateButton);
        }

        // Add panels to the card
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }
    private JPanel createCartItemPanel(CartItem item) {
        // Create the main panel (keep existing layout)
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setPreferredSize(new Dimension(900, 100));
        panel.setMaximumSize(new Dimension(900, 100));
        panel.setMinimumSize(new Dimension(900, 100));
        panel.setBackground(new Color(69, 125, 88));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(42, 58, 41)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Left side - Cart item info
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(69, 125, 88));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);

        // Cart ID
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

        // Edit button
        JButton editButton = new JButton("Edit");
        editButton.setPreferredSize(new Dimension(80, 30));
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.setBackground(new Color(102, 178, 255)); // Blue color
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editButton.setFocusPainted(false);
        editButton.addActionListener(e -> editCartItem(item));
        buttonPanel.add(editButton, gbcButtons);

        // Delete button
        gbcButtons.gridx++;
        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setBackground(new Color(255, 102, 102)); // Red color
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
        checkoutButton.setBackground(new Color(102, 178, 255)); // Blue color
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
        storeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        storeLabel.setForeground(Color.WHITE);
        leftPanel.add(storeLabel);

        // Payment Type
        JLabel paymentLabel = new JLabel("Payment: " + order.getPaymentType());
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        paymentLabel.setForeground(Color.WHITE);
        leftPanel.add(paymentLabel);

        // Status
        JLabel statusLabel = new JLabel("Status: " + order.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        leftPanel.add(statusLabel);

        // Right Panel - Buttons
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        rightPanel.setBackground(new Color(69, 125, 88));
        rightPanel.setPreferredSize(new Dimension(200, 130));

        // Only show Cancel button
        JButton cancelButton = new JButton("Cancel Order");
        cancelButton.setPreferredSize(new Dimension(100, 30)); // Same size as cart card buttons
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(255, 102, 102)); // Red color
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> cancelOrder(order));
        rightPanel.add(cancelButton);

        // Add panels to card
        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }
    private JPanel createProductCard(Product product) {
        // Create a fixed-size card panel
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setPreferredSize(new Dimension(900, 200)); // Adjusted size for including type
        card.setMaximumSize(new Dimension(900, 200));
        card.setMinimumSize(new Dimension(900, 200));
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
        JLabel storeLabel = new JLabel("Store: " + product.getStoreName());
        storeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        storeLabel.setForeground(Color.WHITE);
        storePanel.add(storeLabel);
        leftPanel.add(storePanel);

        // Type (Left aligned)
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(new Color(69, 125, 88));
        JLabel typeLabel = new JLabel("Type: " + product.getType()); // Display the product type
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        typeLabel.setForeground(Color.WHITE);
        typePanel.add(typeLabel);
        leftPanel.add(typePanel);

        // Tagline (Left aligned with hashtag)
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
        buyButton.setForeground(Color.WHITE);
        buyButton.setFont(new Font("Arial", Font.BOLD, 12));
        buyButton.setBackground(new Color(53, 91, 66));
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
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setPreferredSize(new Dimension(110, 25));
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 12));
        addToCartButton.setBackground(new Color(53, 91, 66));
        addToCartButton.addActionListener(e -> {
            try {
                Addtocart cart = new Addtocart(product, username);
                cart.setVisible(true);
                cart.setLocationRelativeTo(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error opening product details: " + ex.getMessage(),
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
            homepage = new Homepage(homepage, firstName, lastName, username);
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
        productList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        sellerbtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        menuBTN = new javax.swing.JToggleButton();
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
        jTabbedPane2 = new javax.swing.JTabbedPane();
        hotdealsBTN = new javax.swing.JToggleButton();
        Order = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        orderTab = new javax.swing.JTabbedPane();
        History = new javax.swing.JPanel();
        historyTAB = new javax.swing.JTabbedPane();
        jLabel13 = new javax.swing.JLabel();
        Profile = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        pfpImage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pfpChangepass = new javax.swing.JButton();
        pfpDelete = new javax.swing.JButton();
        labelUsername = new javax.swing.JLabel();
        labelPassword = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        labelContacts = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        labelUser_id = new javax.swing.JLabel();
        pfpCart = new javax.swing.JButton();
        pfpOrders = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        labelORDERpfp = new javax.swing.JLabel();
        labelCARTpfp = new javax.swing.JLabel();
        labelTOTALpfp = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        pfpUpload = new javax.swing.JButton();
        About = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        Cart = new javax.swing.JPanel();
        cartTab = new javax.swing.JTabbedPane();
        jLabel5 = new javax.swing.JLabel();
        Help = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        navigation.setBackground(new java.awt.Color(52, 70, 49));

        jPanel1.setBackground(new java.awt.Color(69, 125, 88));

        productList.setBackground(new java.awt.Color(15, 58, 41));
        productList.setForeground(new java.awt.Color(255, 255, 255));
        productList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "MISCS", "DRUGS", "BEVERAGE", "CLOTHES", "TOOLS", "GADGETS", "FOOD", "ALL" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(productList);

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

        sellerbtn.setBackground(new java.awt.Color(53, 91, 66));
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

        menuBTN.setBackground(new java.awt.Color(53, 91, 66));
        menuBTN.setForeground(new java.awt.Color(255, 255, 255));
        menuBTN.setText("MENU");
        menuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBTNActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(menuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(menuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(53, 91, 66));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("About");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        helpbtn.setBackground(new java.awt.Color(53, 91, 66));
        helpbtn.setForeground(new java.awt.Color(255, 255, 255));
        helpbtn.setText("Help?");
        helpbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpbtnActionPerformed(evt);
            }
        });

        logoutbtn.setBackground(new java.awt.Color(53, 91, 66));
        logoutbtn.setForeground(new java.awt.Color(255, 255, 255));
        logoutbtn.setText("Log out");
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });

        btnCart.setBackground(new java.awt.Color(53, 91, 66));
        btnCart.setForeground(new java.awt.Color(255, 255, 255));
        btnCart.setText("MY CART");
        btnCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCartActionPerformed(evt);
            }
        });

        btnOrder.setBackground(new java.awt.Color(53, 91, 66));
        btnOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnOrder.setText("ORDERS");
        btnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrderActionPerformed(evt);
            }
        });

        btnHome.setBackground(new java.awt.Color(53, 91, 66));
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
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        searchButton.setBackground(new java.awt.Color(255, 255, 255));
        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/search.png"))); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        hotdealsBTN.setBackground(new java.awt.Color(255, 255, 0));
        hotdealsBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        hotdealsBTN.setForeground(new java.awt.Color(255, 0, 0));
        hotdealsBTN.setText("HOT DEALS");
        hotdealsBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hotdealsBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HomeLayout = new javax.swing.GroupLayout(Home);
        Home.setLayout(HomeLayout);
        HomeLayout.setHorizontalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomeLayout.createSequentialGroup()
                .addContainerGap(851, Short.MAX_VALUE)
                .addComponent(hotdealsBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
            .addGroup(HomeLayout.createSequentialGroup()
                .addGroup(HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HomeLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HomeLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HomeLayout.setVerticalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(hotdealsBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        parent.add(Home, "card5");

        Order.setBackground(new java.awt.Color(42, 58, 41));

        jLabel3.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
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
                        .addGap(0, 799, Short.MAX_VALUE)))
                .addContainerGap())
        );
        OrderLayout.setVerticalGroup(
            OrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OrderLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(orderTab, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        parent.add(Order, "card3");

        History.setBackground(new java.awt.Color(42, 58, 41));

        jLabel13.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("History:");

        javax.swing.GroupLayout HistoryLayout = new javax.swing.GroupLayout(History);
        History.setLayout(HistoryLayout);
        HistoryLayout.setHorizontalGroup(
            HistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoryLayout.createSequentialGroup()
                .addGroup(HistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistoryLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(historyTAB, javax.swing.GroupLayout.PREFERRED_SIZE, 936, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HistoryLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        HistoryLayout.setVerticalGroup(
            HistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoryLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(historyTAB, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        parent.add(History, "card6");

        Profile.setBackground(new java.awt.Color(42, 58, 41));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("NAME");

        pfpImage.setText("           image");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pfpImage, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pfpImage, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(63, 94, 61));

        jPanel6.setBackground(new java.awt.Color(97, 121, 109));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Username:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Password:");

        pfpChangepass.setBackground(new java.awt.Color(53, 91, 66));
        pfpChangepass.setForeground(new java.awt.Color(255, 255, 255));
        pfpChangepass.setText("CHANGE PASSWORD");
        pfpChangepass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfpChangepassActionPerformed(evt);
            }
        });

        pfpDelete.setBackground(new java.awt.Color(133, 28, 28));
        pfpDelete.setForeground(new java.awt.Color(255, 255, 255));
        pfpDelete.setText("DELETE ACC");

        labelUsername.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername.setText("jLabel9");

        labelPassword.setForeground(new java.awt.Color(255, 255, 255));
        labelPassword.setText("jLabel10");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Contact No/Email:");

        labelContacts.setForeground(new java.awt.Color(255, 255, 255));
        labelContacts.setText("jLabel10");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("User ID:");

        labelUser_id.setForeground(new java.awt.Color(255, 255, 255));
        labelUser_id.setText("jLabel11");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pfpChangepass, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(pfpDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel14)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addComponent(labelContacts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelUser_id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labelUsername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labelPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(labelContacts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(labelUser_id))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(pfpChangepass, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pfpDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        pfpCart.setBackground(new java.awt.Color(53, 91, 66));
        pfpCart.setForeground(new java.awt.Color(255, 255, 255));
        pfpCart.setText("MY CART");
        pfpCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfpCartActionPerformed(evt);
            }
        });

        pfpOrders.setBackground(new java.awt.Color(53, 91, 66));
        pfpOrders.setForeground(new java.awt.Color(255, 255, 255));
        pfpOrders.setText("MY ORDERS");
        pfpOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfpOrdersActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ORDER SUMMARY");

        labelORDERpfp.setForeground(new java.awt.Color(255, 255, 255));
        labelORDERpfp.setText("null");

        labelCARTpfp.setForeground(new java.awt.Color(255, 255, 255));
        labelCARTpfp.setText("null");

        labelTOTALpfp.setForeground(new java.awt.Color(255, 255, 255));
        labelTOTALpfp.setText("null");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TOTAL ORDERS:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("TOTAL CARTS:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("ALL TOTAL:");

        pfpUpload.setBackground(new java.awt.Color(53, 91, 66));
        pfpUpload.setForeground(new java.awt.Color(255, 255, 255));
        pfpUpload.setText("UPLOAD PHOTO");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(pfpUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                        .addComponent(pfpCart, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pfpOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(164, 164, 164)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(labelTOTALpfp)
                                    .addComponent(labelCARTpfp, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelORDERpfp, javax.swing.GroupLayout.Alignment.LEADING))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pfpCart, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pfpOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pfpUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelORDERpfp)
                            .addComponent(jLabel10))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCARTpfp)
                            .addComponent(jLabel11))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelTOTALpfp)
                            .addComponent(jLabel12)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ProfileLayout = new javax.swing.GroupLayout(Profile);
        Profile.setLayout(ProfileLayout);
        ProfileLayout.setHorizontalGroup(
            ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProfileLayout.createSequentialGroup()
                .addGroup(ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProfileLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ProfileLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ProfileLayout.setVerticalGroup(
            ProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProfileLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        parent.add(Profile, "card7");

        About.setBackground(new java.awt.Color(42, 58, 41));

        jLabel17.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("About");

        javax.swing.GroupLayout AboutLayout = new javax.swing.GroupLayout(About);
        About.setLayout(AboutLayout);
        AboutLayout.setHorizontalGroup(
            AboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AboutLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(811, Short.MAX_VALUE))
        );
        AboutLayout.setVerticalGroup(
            AboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AboutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(607, Short.MAX_VALUE))
        );

        parent.add(About, "card5");

        Cart.setBackground(new java.awt.Color(42, 58, 41));

        jLabel5.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Your Cart:");

        javax.swing.GroupLayout CartLayout = new javax.swing.GroupLayout(Cart);
        Cart.setLayout(CartLayout);
        CartLayout.setHorizontalGroup(
            CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cartTab, javax.swing.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(CartLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CartLayout.setVerticalGroup(
            CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(cartTab, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        parent.add(Cart, "card4");

        Help.setBackground(new java.awt.Color(42, 58, 41));

        jLabel16.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Help?");

        javax.swing.GroupLayout HelpLayout = new javax.swing.GroupLayout(Help);
        Help.setLayout(HelpLayout);
        HelpLayout.setHorizontalGroup(
            HelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HelpLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(811, Short.MAX_VALUE))
        );
        HelpLayout.setVerticalGroup(
            HelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HelpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(607, Short.MAX_VALUE))
        );

        parent.add(Help, "card8");

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
                        .addGap(24, 24, 24)
                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCart, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(navigationLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(helpbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sellerbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(parent, javax.swing.GroupLayout.PREFERRED_SIZE, 1031, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        navigationLayout.setVerticalGroup(
            navigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navigationLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
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
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(helpbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sellerbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(logoutbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(parent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navigation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ACTION BUTTONS
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
        parent.removeAll();
        parent.add(Help);
        parent.repaint();
        parent.revalidate();
    }//GEN-LAST:event_helpbtnActionPerformed
    private void sellerbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellerbtnActionPerformed
        try {
            String query = "SELECT Store_name, User_id, Location, Contacts FROM users WHERE accUsername = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getString("Store_name") != null) {
                // If a store is found, open SellerPage with all required parameters
                String storename = rs.getString("Store_name");
                int sellerId = rs.getInt("User_id");
                String location = rs.getString("Location");
                String contact = rs.getString("Contacts");

                SellerPage sellerPage = new SellerPage(
                        storename,
                        username,
                        lastName + " " + firstName,
                        location,
                        sellerId,
                        contact
                );
                sellerPage.setVisible(true);
                sellerPage.pack();
                sellerPage.setLocationRelativeTo(null);
            } else {
                // If no store is found, ask if they want to register
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "You are not registered as a seller. Would you like to register now?",
                        "Not Registered",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Open the SellerRegistration form
                    SellerRegistration register = new SellerRegistration(this, username);
                    register.setVisible(true);
                    register.pack();
                    register.setLocationRelativeTo(null);
                }
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_sellerbtnActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      // TODO add your handling code here
        parent.removeAll();
        parent.add(About);
        parent.repaint();
        parent.revalidate();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        String searchTerm = txtSearch.getText().trim();
        if (!searchTerm.isEmpty()) {
            loadProductsBySearch(searchTerm); // Use the new search method
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
    private void pfpOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfpOrdersActionPerformed
        // TODO add your handling code here:
        parent.removeAll();
        parent.add(Order);
        parent.repaint();
        parent.revalidate();
        loadOrders();
    }//GEN-LAST:event_pfpOrdersActionPerformed
    private void pfpCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfpCartActionPerformed
        // TODO add your handling code here:
        parent.removeAll();
        parent.add(Cart);
        parent.repaint();
        parent.revalidate();
        loadCart();
    }//GEN-LAST:event_pfpCartActionPerformed
    private void hotdealsBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hotdealsBTNActionPerformed
        // TODO add your handling code here:
        loadHotDeals();
    }//GEN-LAST:event_hotdealsBTNActionPerformed
    private void pfpChangepassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfpChangepassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pfpChangepassActionPerformed
    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            searchButton.doClick(); // Simulate button click
        }
    }//GEN-LAST:event_txtSearchKeyPressed
    private void menuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBTNActionPerformed
        // TODO add your handling code here:
         if (wheelMenu.isVisible()) {
                wheelMenu.setVisible(false);
            } else {
                wheelMenu.showMenu(wheelButton);
            }
    }//GEN-LAST:event_menuBTNActionPerformed

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
                Homepage homepage = null;
                String firstname = null;
                String lastname = null;
                String Username = null;
                new Homepage(homepage, firstname, lastname, Username).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel About;
    private javax.swing.JPanel Cart;
    private javax.swing.JPanel Help;
    private javax.swing.JPanel History;
    private javax.swing.JPanel Home;
    private javax.swing.JPanel Order;
    private javax.swing.JPanel Profile;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnOrder;
    private javax.swing.JTabbedPane cartTab;
    private javax.swing.JButton helpbtn;
    private javax.swing.JTabbedPane historyTAB;
    private javax.swing.JToggleButton hotdealsBTN;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel labelCARTpfp;
    private javax.swing.JLabel labelContacts;
    private javax.swing.JLabel labelORDERpfp;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelTOTALpfp;
    private javax.swing.JLabel labelUser_id;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JToggleButton menuBTN;
    private javax.swing.JPanel navigation;
    private javax.swing.JTabbedPane orderTab;
    private javax.swing.JPanel parent;
    private javax.swing.JButton pfpCart;
    private javax.swing.JButton pfpChangepass;
    private javax.swing.JButton pfpDelete;
    private javax.swing.JLabel pfpImage;
    private javax.swing.JButton pfpOrders;
    private javax.swing.JButton pfpUpload;
    private javax.swing.JList<String> productList;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton sellerbtn;
    private javax.swing.JLabel txtName;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
