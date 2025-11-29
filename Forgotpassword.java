package JFrame;

import java.sql.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

public class Forgotpassword extends javax.swing.JFrame {

    public Forgotpassword() {
        initComponents();
    }

    // --- GUI COMPONENTS ---
    private void initComponents() {
        jPanel1 = new JPanel();
        jLabelTitle = new JLabel();

        jLabelUsername = new JLabel("Username");
        jLabelContact = new JLabel("Contact No");
        jLabelNewPass = new JLabel("New Password");

        txtUsername = new JTextField();
        txtContact = new JTextField();
        txtNewPass = new JPasswordField(); // Secure password field

        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update Password");
        btnBack = new JButton("Back");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Forgot Password");

        // Main Layout
        getContentPane().setLayout(null);

        // --- HEADER ---
        jPanel1.setBackground(new Color(51, 51, 255));
        jPanel1.setLayout(null);
        jPanel1.setBounds(0, 0, 600, 70);

        jLabelTitle.setFont(new Font("Segoe UI", 1, 30)); 
        jLabelTitle.setForeground(Color.WHITE);
        jLabelTitle.setText("Recover Password");
        jLabelTitle.setBounds(150, 10, 300, 50);
        jPanel1.add(jLabelTitle);

        getContentPane().add(jPanel1);

        // --- INPUTS ---
        int startX = 50;
        int labelY = 100;
        int inputY = 100;
        int gap = 70;
        int labelW = 150;
        int inputW = 250;

        // Username
        jLabelUsername.setFont(new Font("Segoe UI", 1, 18));
        jLabelUsername.setForeground(new Color(0, 204, 204));
        jLabelUsername.setBounds(startX, labelY, labelW, 30);
        getContentPane().add(jLabelUsername);

        txtUsername.setBounds(startX + 140, inputY, inputW, 35);
        getContentPane().add(txtUsername);

        // Search Button (to verify user exists)
        btnSearch.setText("Verify User");
        btnSearch.setBounds(startX + 140 + inputW + 10, inputY, 100, 35);
        btnSearch.addActionListener(e -> verifyUser());
        getContentPane().add(btnSearch);

        // Contact
        jLabelContact.setFont(new Font("Segoe UI", 1, 18));
        jLabelContact.setForeground(new Color(0, 204, 204));
        jLabelContact.setBounds(startX, labelY + gap, labelW, 30);
        getContentPane().add(jLabelContact);

        txtContact.setBounds(startX + 140, inputY + gap, inputW, 35);
        getContentPane().add(txtContact);

        // New Password
        jLabelNewPass.setFont(new Font("Segoe UI", 1, 18));
        jLabelNewPass.setForeground(new Color(0, 204, 204));
        jLabelNewPass.setBounds(startX, labelY + gap*2, labelW, 30);
        getContentPane().add(jLabelNewPass);

        txtNewPass.setBounds(startX + 140, inputY + gap*2, inputW, 35);
        getContentPane().add(txtNewPass);

        // --- ACTION BUTTONS ---
        btnUpdate.setFont(new Font("Segoe UI", 1, 18));
        btnUpdate.setText("Reset Password");
        btnUpdate.setBackground(new Color(0, 153, 51)); // Green
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBounds(190, 350, 200, 50);
        btnUpdate.addActionListener(e -> updatePassword());
        getContentPane().add(btnUpdate);

        btnBack.setFont(new Font("Segoe UI", 1, 18));
        btnBack.setText("Back");
        btnBack.setBackground(Color.RED);
        btnBack.setForeground(Color.WHITE);
        btnBack.setBounds(410, 350, 100, 50);
        btnBack.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
        getContentPane().add(btnBack);

        // Frame Settings
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    // --- LOGIC ---

    private void verifyUser() {
        String username = txtUsername.getText();
        
        try {
            Connection con = DBconnection.getConnection();
            // Checking if user exists based on name
            PreparedStatement pst = con.prepareStatement("select * from users where name=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "User Found! Please enter contact to verify.");
                txtContact.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePassword() {
        String username = txtUsername.getText();
        String contact = txtContact.getText();
        String newPass = new String(txtNewPass.getPassword());

        if (username.isEmpty() || contact.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Connection con = DBconnection.getConnection();
            // Verify Contact and Username match before updating
            String checkSql = "select * from users where name=? and contact=?";
            PreparedStatement checkPst = con.prepareStatement(checkSql);
            checkPst.setString(1, username);
            checkPst.setString(2, contact);
            
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                // User verified, update password
                String updateSql = "update users set password=? where name=?";
                PreparedStatement updatePst = con.prepareStatement(updateSql);
                updatePst.setString(1, newPass);
                updatePst.setString(2, username);
                
                int k = updatePst.executeUpdate();
                if (k == 1) {
                    JOptionPane.showMessageDialog(this, "Password Reset Successfully!");
                    new LoginPage().setVisible(true);
                    this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Contact Number for this Username.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Forgotpassword().setVisible(true));
    }

    // Variables
    private javax.swing.JButton btnSearch, btnUpdate, btnBack;
    private javax.swing.JLabel jLabelTitle, jLabelUsername, jLabelContact, jLabelNewPass;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtUsername, txtContact;
    private javax.swing.JPasswordField txtNewPass;
}