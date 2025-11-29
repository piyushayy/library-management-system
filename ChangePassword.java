package JFrame;

import java.sql.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

public class ChangePassword extends javax.swing.JFrame {

    public ChangePassword() {
        initComponents();
    }

    private void initComponents() {
        jPanel2 = new JPanel();
        jLabel9 = new JLabel();

        jLabelUsername = new JLabel("Username");
        jLabelOldPass = new JLabel("Old Password");
        jLabelNewPass = new JLabel("New Password");
        jLabelConfirmPass = new JLabel("Confirm Password");

        txtUsername = new JTextField();
        txtOldPass = new JPasswordField(); // Use JPasswordField for security
        txtNewPass = new JPasswordField();
        txtConfirmPass = new JPasswordField();

        btnChange = new JButton("Change Password");
        btnClose = new JButton("Close");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Change Password");

        // Main Layout
        getContentPane().setLayout(null);

        // Header Panel
        jPanel2.setBackground(new Color(51, 51, 255));
        jPanel2.setLayout(null);
        jPanel2.setBounds(0, 0, 800, 70);

        jLabel9.setFont(new Font("Segoe UI", 1, 36)); 
        jLabel9.setForeground(Color.WHITE);
        jLabel9.setText("Change Your Password");
        jLabel9.setBounds(200, 10, 500, 50);
        jPanel2.add(jLabel9);

        getContentPane().add(jPanel2);

        // --- INPUT FIELDS ---
        int startX = 150;
        int labelY = 120;
        int inputY = 120;
        int gap = 60;
        int labelW = 180;
        int inputW = 250;

        // Username
        jLabelUsername.setFont(new Font("Segoe UI", 1, 18));
        jLabelUsername.setForeground(new Color(0, 204, 204));
        jLabelUsername.setBounds(startX, labelY, labelW, 30);
        getContentPane().add(jLabelUsername);

        txtUsername.setBounds(startX + 180, inputY, inputW, 35);
        getContentPane().add(txtUsername);

        // Old Password
        jLabelOldPass.setFont(new Font("Segoe UI", 1, 18));
        jLabelOldPass.setForeground(new Color(0, 204, 204));
        jLabelOldPass.setBounds(startX, labelY + gap, labelW, 30);
        getContentPane().add(jLabelOldPass);

        txtOldPass.setBounds(startX + 180, inputY + gap, inputW, 35);
        getContentPane().add(txtOldPass);

        // New Password
        jLabelNewPass.setFont(new Font("Segoe UI", 1, 18));
        jLabelNewPass.setForeground(new Color(0, 204, 204));
        jLabelNewPass.setBounds(startX, labelY + gap*2, labelW, 30);
        getContentPane().add(jLabelNewPass);

        txtNewPass.setBounds(startX + 180, inputY + gap*2, inputW, 35);
        getContentPane().add(txtNewPass);

        // Confirm Password
        jLabelConfirmPass.setFont(new Font("Segoe UI", 1, 18));
        jLabelConfirmPass.setForeground(new Color(0, 204, 204));
        jLabelConfirmPass.setBounds(startX, labelY + gap*3, labelW, 30);
        getContentPane().add(jLabelConfirmPass);

        txtConfirmPass.setBounds(startX + 180, inputY + gap*3, inputW, 35);
        getContentPane().add(txtConfirmPass);

        // --- BUTTONS ---
        btnChange.setFont(new Font("Segoe UI", 1, 16));
        btnChange.setText("Change");
        btnChange.setBounds(330, 400, 120, 40);
        btnChange.addActionListener(e -> changePassword());
        getContentPane().add(btnChange);

        btnClose.setFont(new Font("Segoe UI", 1, 16));
        btnClose.setText("Close");
        btnClose.setBounds(480, 400, 100, 40);
        btnClose.setBackground(Color.RED);
        btnClose.setForeground(Color.WHITE);
        btnClose.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
        getContentPane().add(btnClose);

        // Frame Size
        setSize(800, 550);
        setLocationRelativeTo(null);
    }

    private void changePassword() {
        String username = txtUsername.getText();
        String oldPass = new String(txtOldPass.getPassword());
        String newPass = new String(txtNewPass.getPassword());
        String confPass = new String(txtConfirmPass.getPassword());

        if (!newPass.equals(confPass)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match!");
            return;
        }

        try {
            Connection con = DBconnection.getConnection();
            if (con == null) return;

            // Step 1: Verify Old Password
            PreparedStatement checkStmt = con.prepareStatement("select * from users where name=? and password=?");
            checkStmt.setString(1, username);
            checkStmt.setString(2, oldPass);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Step 2: Update to New Password
                PreparedStatement updateStmt = con.prepareStatement("update users set password=? where name=?");
                updateStmt.setString(1, newPass);
                updateStmt.setString(2, username);
                
                int k = updateStmt.executeUpdate();
                if (k == 1) {
                    JOptionPane.showMessageDialog(this, "Password Changed Successfully!");
                    new LoginPage().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating password.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Old Password or Username.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ChangePassword().setVisible(true));
    }

    // Variables
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelUsername, jLabelOldPass, jLabelNewPass, jLabelConfirmPass;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPasswordField txtOldPass, txtNewPass, txtConfirmPass;
}