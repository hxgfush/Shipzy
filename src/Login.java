
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.JOptionPane;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author 63995
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        this.setLocationRelativeTo(null);
        setTextFieldPlaceholder(txtLogUsername, "Username");
        setPasswordPlaceholder(txtLogPassword, "Password");
        try {
            Connection();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     //onnection method
     Connection con;
     //SQLstatement
     Statement st;
     //Prepared Statement 
     PreparedStatement pst;
     //Required for Connections
     //DbName,DbDriver,Url,USername,Password
     private static final String DbName = "shipzy";
     private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
     private static final String DbUrl = "jdbc:mysql://localhost:3306/"+DbName;//instead of Concatenation i used the value directly
     private static final String DbUsername = "root";
     private static final String DbPassword = "";
     
     //Create a method for connections 
     public void Connection()throws SQLException{
        try {
            con = DriverManager.getConnection(DbUrl,DbUsername,DbPassword);
            st = con.createStatement();
            if (con != null){
                System.out.println("Connection Successful");
            }
            Class.forName(DbDriver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

     
     private void setTextFieldPlaceholder(JTextField textField, String placeholder) {
    // Set initial placeholder text and style
    textField.setText(placeholder);
    textField.setForeground(new java.awt.Color(128, 128, 128));

    textField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(new java.awt.Color(0, 0, 0));
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(new java.awt.Color(128, 128, 128));
            }
        }
    });
}

     private void setPasswordPlaceholder(JPasswordField passwordField, String placeholder) {
    // Set initial placeholder text and style
    passwordField.setText(placeholder);
    passwordField.setForeground(new java.awt.Color(128, 128, 128));
    passwordField.setEchoChar((char) 0); // Show text, not bullets

    passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (new String(passwordField.getPassword()).equals(placeholder)) {
                passwordField.setText("");
                passwordField.setEchoChar('•');
                passwordField.setForeground(new java.awt.Color(0, 0, 0));
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (passwordField.getPassword().length == 0) {
                passwordField.setText(placeholder);
                passwordField.setEchoChar((char) 0);
                passwordField.setForeground(new java.awt.Color(128, 128, 128));
            }
        }
    });
}       
     
    private void showPasswordResetDialog(String username) {
    // Create the JDialog
    JDialog dialog = new JDialog(this, "Reset Password", true);
    dialog.setSize(300, 200);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new java.awt.GridLayout(4, 1, 10, 10));

    // Create input fields and labels
    JLabel newPasswordLabel = new JLabel("New Password:");
    JPasswordField newPasswordField = new JPasswordField();

    JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
    JPasswordField confirmPasswordField = new JPasswordField();

    // Create buttons
    JButton submitButton = new JButton("Submit");
    JButton cancelButton = new JButton("Cancel");

    // Add components to the dialog
    dialog.add(newPasswordLabel);
    dialog.add(newPasswordField);
    dialog.add(confirmPasswordLabel);
    dialog.add(confirmPasswordField);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(submitButton);
    buttonPanel.add(cancelButton);
    dialog.add(buttonPanel);

    // Add button actions
    submitButton.addActionListener(e -> {
        try {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Validate inputs
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update password in the database
            String updateQuery = "UPDATE users SET accPassword = ? WHERE accUsername = ?";
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, newPassword);
            pst.setString(2, username);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(dialog, "Password reset successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to reset password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(dialog, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
} 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtLogUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtLogPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        forgotBTN = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(42, 58, 41));

        jPanel3.setBackground(new java.awt.Color(51, 69, 48));

        txtLogUsername.setBackground(new java.awt.Color(255, 255, 255));
        txtLogUsername.setForeground(new java.awt.Color(0, 0, 0));
        txtLogPassword.setBackground(new java.awt.Color(255, 255, 255));

        txtLogUsername.setText("Username");  // Set placeholder text
        txtLogUsername.setForeground(new java.awt.Color(0, 0, 0));  // Gray color for placeholder
        txtLogUsername.setBackground(new java.awt.Color(255, 255, 255));  // Keep your white background

        // Focus listeners for placeholder behavior
        txtLogUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtLogUsername.getText().equals("Username")) {
                    txtLogUsername.setText("");  // Clear placeholder when focused
                    txtLogUsername.setForeground(new java.awt.Color(0, 0, 0));  // Black for user input
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtLogUsername.getText().isEmpty()) {
                    txtLogUsername.setText("Username");  // Restore placeholder if empty
                    txtLogUsername.setForeground(new java.awt.Color(128, 128, 128));
                }
            }
        });
        txtLogUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLogUsernameActionPerformed(evt);
            }
        });
        txtLogUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLogUsernameKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Irish Grover", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Sign In");

        txtLogPassword.setBackground(new java.awt.Color(255, 255, 255));
        txtLogPassword.setForeground(new java.awt.Color(0, 0, 0));
        txtLogPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLogPasswordActionPerformed(evt);
            }
        });
        txtLogPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLogPasswordKeyPressed(evt);
            }
        });

        btnLogin.setBackground(new java.awt.Color(53, 91, 66));
        btnLogin.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Log In");
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.setText("Don't have an Account?");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Inknut Antiqua", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));

        forgotBTN.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        forgotBTN.setForeground(new java.awt.Color(255, 255, 255));
        forgotBTN.setText("Forgot your password?");
        forgotBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forgotBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forgotBTNMouseClicked(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/logo.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(65, 65, 65)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(135, 135, 135))
                            .addComponent(btnLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(forgotBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jLabel5)
                        .addGap(29, 29, 29))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtLogPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(txtLogUsername))
                .addGap(42, 42, 42))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addComponent(txtLogUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(txtLogPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forgotBTN)
                    .addComponent(jLabel5))
                .addContainerGap(207, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(305, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        try {
        String username = txtLogUsername.getText();
        char[] passwordChars = txtLogPassword.getPassword();
        String password = new String(passwordChars);

        String queryLogin = "SELECT Firstname, Lastname, accUsername FROM users WHERE accUsername = ? AND accPassword = ?";
        PreparedStatement pst = con.prepareStatement(queryLogin);
        pst.setString(1, username);
        pst.setString(2, password);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String firstname = rs.getString("Firstname");
            String lastname = rs.getString("Lastname");
            String Username = rs.getString("accUsername");

            // Create new Homepage with all parameters
            Homepage home = new Homepage(null, firstname, lastname, Username);
            home.setVisible(true);
            home.setLocationRelativeTo(null);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Credentials");
        }
    } catch (SQLException ex) {
        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtLogPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLogPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLogPasswordActionPerformed
   
    private void txtLogUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLogUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLogUsernameActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
               Registration register = new Registration();
        register.setVisible(true);
        register.pack();
        register.setLocationRelativeTo(null);
        this.dispose();

    }//GEN-LAST:event_jLabel5MouseClicked

    private void txtLogPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLogPasswordKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        btnLogin.doClick(); // Simulate button click
        }
    }//GEN-LAST:event_txtLogPasswordKeyPressed

    private void txtLogUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLogUsernameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        btnLogin.doClick(); // Simulate button click
        }
    }//GEN-LAST:event_txtLogUsernameKeyPressed

    private void forgotBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotBTNMouseClicked
        // TODO add your handling code here:
    String username = txtLogUsername.getText().trim();

    if (username.isEmpty() || username.equals("Username")) {
        JOptionPane.showMessageDialog(null, "Please enter your username in the Username field to proceed.");
        return;
    }

    try {
        // Check if the username exists in the database
        String query = "SELECT accUsername FROM users WHERE accUsername = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Username exists, open the password reset dialog
            showPasswordResetDialog(username);
        } else {
            // Username does not exist
            JOptionPane.showMessageDialog(null, 
                "The username \"" + username + "\" does not exist. Please try again.");
        }

        rs.close();
        pst.close();
    } catch (SQLException ex) {
        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, 
            "An error occurred while checking the username: " + ex.getMessage(), 
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_forgotBTNMouseClicked

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
            for  (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
               
            }
        });
        
}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel forgotBTN;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField txtLogPassword;
    private javax.swing.JTextField txtLogUsername;
    // End of variables declaration//GEN-END:variables
}
