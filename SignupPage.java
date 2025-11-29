package JFrame;

import java.sql.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

public class SignupPage extends javax.swing.JFrame {

    public SignupPage() {
        initComponents();
    }

    // --- LOGIC: CHECK DUPLICATE USER ---
    public boolean checkDuplicateUser() {
        String name = txtUsername.getText();
        boolean isExist = false;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement pst = con.prepareStatement("select * from users where name = ?");
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    // --- LOGIC: INSERT USER ---
    public void insertSignupDetails() {
        String name = txtUsername.getText();
        String pwd = txtPassword.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        try {
            Connection con = DBconnection.getConnection();
            String sql = "insert into users(name, password, email, contact) values(?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, name);
            pst.setString(2, pwd);
            pst.setString(3, email);
            pst.setString(4, contact);

            int updatedRowCount = pst.executeUpdate();

            if (updatedRowCount > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                LoginPage login = new LoginPage();
                login.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // --- LOGIC: VALIDATION ---
    public boolean validateSignup() {
        String name = txtUsername.getText();
        String pwd = txtPassword.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Username");
            return false;
        }
        if (pwd.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Password");
            return false;
        }
        if (email.equals("") || !email.matches("^.+@.+\\..+$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid Email");
            return false;
        }
        if (contact.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter Contact Number");
            return false;
        }
        return true;
    }

    // --- GUI SETUP ---
    private void initComponents() {
        jPanel1 = new JPanel();
        jPanel2 = new JPanel(); // Left Side Info Panel
        
        jLabelTitle = new JLabel("Signup Page");
        jLabelInfo = new JLabel("Create New Account");
        
        jLabelUsername = new JLabel("Username");
        jLabelPassword = new JLabel("Password");
        jLabelEmail = new JLabel("Email");
        jLabelContact = new JLabel("Contact");

        txtUsername = new JTextField();
        txtPassword = new JPasswordField(); // Secure password field
        txtEmail = new JTextField();
        txtContact = new JTextField();

        btnSignup = new JButton("SIGNUP");
        btnLogin = new JButton("Login");

        // Decoration Labels for Left Panel
        JLabel lblDev = new JLabel("Developer");
        JLabel lblWelcome = new JLabel("Welcome To");
        JLabel lblLib = new JLabel("Advance Library");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Signup");
        getContentPane().setLayout(null);

        // --- LEFT PANEL (White info side) ---
        jPanel2.setBackground(Color.WHITE);
        jPanel2.setLayout(null);
        jPanel2.setBounds(0, 0, 990, 830);

        lblDev.setFont(new Font("Segoe UI Black", 1, 18));
        lblDev.setForeground(new Color(51, 51, 255));
        lblDev.setBounds(10, 10, 150, 30);
        jPanel2.add(lblDev);

        lblWelcome.setFont(new Font("Segoe UI", 1, 30));
        lblWelcome.setForeground(new Color(255, 51, 51));
        lblWelcome.setBounds(300, 100, 400, 50);
        jPanel2.add(lblWelcome);

        lblLib.setFont(new Font("Segoe UI", 1, 30));
        lblLib.setForeground(new Color(102, 102, 255));
        lblLib.setBounds(300, 150, 400, 50);
        jPanel2.add(lblLib);
        
        // Add image only if exists to prevent crash, otherwise text
        JLabel lblImage = new JLabel("LIBRARY SYSTEM IMAGE");
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBounds(100, 250, 700, 400);
        jPanel2.add(lblImage);

        getContentPane().add(jPanel2);

        // --- RIGHT PANEL (Blue form side) ---
        jPanel1.setBackground(new Color(51, 102, 255));
        jPanel1.setLayout(null);
        jPanel1.setBounds(990, 0, 530, 830);

        jLabelTitle.setFont(new Font("Segoe UI", 1, 25));
        jLabelTitle.setForeground(Color.WHITE);
        jLabelTitle.setBounds(180, 50, 200, 40);
        jPanel1.add(jLabelTitle);

        jLabelInfo.setFont(new Font("Verdana", 0, 17));
        jLabelInfo.setForeground(Color.WHITE);
        jLabelInfo.setBounds(150, 100, 250, 30);
        jPanel1.add(jLabelInfo);

        // Inputs Layout
        int startX = 100;
        int labelY = 180;
        int inputY = 210;
        int gap = 80;
        int width = 300;

        // Username
        jLabelUsername.setFont(new Font("Verdana", 0, 17));
        jLabelUsername.setForeground(Color.WHITE);
        jLabelUsername.setBounds(startX, labelY, 200, 30);
        jPanel1.add(jLabelUsername);

        txtUsername.setBounds(startX, inputY, width, 35);
        txtUsername.setFont(new Font("Segoe UI", 0, 17));
        jPanel1.add(txtUsername);

        // Password
        jLabelPassword.setFont(new Font("Verdana", 0, 17));
        jLabelPassword.setForeground(Color.WHITE);
        jLabelPassword.setBounds(startX, labelY + gap, 200, 30);
        jPanel1.add(jLabelPassword);

        txtPassword.setBounds(startX, inputY + gap, width, 35);
        txtPassword.setFont(new Font("Segoe UI", 0, 17));
        jPanel1.add(txtPassword);

        // Email
        jLabelEmail.setFont(new Font("Verdana", 0, 17));
        jLabelEmail.setForeground(Color.WHITE);
        jLabelEmail.setBounds(startX, labelY + gap*2, 200, 30);
        jPanel1.add(jLabelEmail);

        txtEmail.setBounds(startX, inputY + gap*2, width, 35);
        txtEmail.setFont(new Font("Segoe UI", 0, 17));
        jPanel1.add(txtEmail);

        // Contact
        jLabelContact.setFont(new Font("Verdana", 0, 17));
        jLabelContact.setForeground(Color.WHITE);
        jLabelContact.setBounds(startX, labelY + gap*3, 200, 30);
        jPanel1.add(jLabelContact);

        txtContact.setBounds(startX, inputY + gap*3, width, 35);
        txtContact.setFont(new Font("Segoe UI", 0, 17));
        jPanel1.add(txtContact);

        // Buttons
        btnSignup.setBackground(new Color(255, 51, 51)); // Red
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setText("SIGNUP");
        btnSignup.setBounds(80, 600, 150, 50);
        btnSignup.addActionListener(e -> {
            if (validateSignup()) {
                if (!checkDuplicateUser()) {
                    insertSignupDetails();
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists");
                }
            }
        });
        jPanel1.add(btnSignup);

        btnLogin.setBackground(new Color(51, 51, 255)); // Blue
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setText("LOGIN");
        btnLogin.setBounds(280, 600, 150, 50);
        btnLogin.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
        jPanel1.add(btnLogin);

        getContentPane().add(jPanel1);
        
        setSize(1523, 830);
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new SignupPage().setVisible(true));
    }

    // Components
    private JPanel jPanel1, jPanel2;
    private JLabel jLabelTitle, jLabelInfo, jLabelUsername, jLabelPassword, jLabelEmail, jLabelContact;
    private JTextField txtUsername, txtEmail, txtContact;
    private JPasswordField txtPassword;
    private JButton btnSignup, btnLogin;
}