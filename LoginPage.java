

import java.sql.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPage extends javax.swing.JFrame {

    // Colors
    Color primaryColor = new Color(44, 62, 80); // Midnight Blue
    Color secondaryColor = new Color(52, 152, 219); // Bright Blue
    Color white = Color.WHITE;
    Color gray = new Color(236, 240, 241);

    public LoginPage() {
        initComponents();
        this.setLocationRelativeTo(null); 
    }

    // --- LOGIC ---
    public boolean ValidateLogin() {
        String username = txtUsername.getText();
        String pwd = new String(txtPassword.getPassword());

        if (username.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter your Username");
            return false;
        }
        if (pwd.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter your Password");
            return false;
        }
        return true;
    }

    public void login() {
        String username = txtUsername.getText();
        String pwd = new String(txtPassword.getPassword());
        
        try {
            Connection con = DBconnection.getConnection(); 
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database Connection Failed.");
                return;
            }
            String sql = "select * from users where name = ? and password = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, pwd);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                HomePage home = new HomePage();
                home.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Username or Password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // --- GUI ---
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true); // Removes standard window border for a custom look
        setSize(1000, 600);
        setLayout(null);

        // --- LEFT PANEL (Branding) ---
        JPanel panelLeft = new JPanel();
        panelLeft.setBackground(primaryColor);
        panelLeft.setBounds(0, 0, 500, 600);
        panelLeft.setLayout(null);

        JLabel lblTitle = new JLabel("Library System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitle.setForeground(white);
        lblTitle.setBounds(100, 100, 300, 50);
        panelLeft.add(lblTitle);

        JLabel lblDesc = new JLabel("Manage Books & Students");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblDesc.setForeground(new Color(189, 195, 199)); // Light Gray
        lblDesc.setBounds(130, 160, 300, 30);
        panelLeft.add(lblDesc);
        
        // Decorative Circle
        JLabel lblIcon = new JLabel("📚");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 150));
        lblIcon.setBounds(160, 250, 200, 200);
        panelLeft.add(lblIcon);

        add(panelLeft);

        // --- RIGHT PANEL (Login Form) ---
        JPanel panelRight = new JPanel();
        panelRight.setBackground(white);
        panelRight.setBounds(500, 0, 500, 600);
        panelRight.setLayout(null);

        // Close Button (X)
        JLabel lblClose = new JLabel("X");
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblClose.setForeground(primaryColor);
        lblClose.setBounds(460, 10, 30, 30);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { System.exit(0); }
        });
        panelRight.add(lblClose);

        // Login Header
        JLabel lblLogin = new JLabel("Welcome Back");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblLogin.setForeground(primaryColor);
        lblLogin.setBounds(130, 80, 250, 40);
        panelRight.add(lblLogin);

        // Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUser.setForeground(primaryColor);
        lblUser.setBounds(100, 180, 100, 20);
        panelRight.add(lblUser);

        txtUsername = new JTextField();
        styleField(txtUsername);
        txtUsername.setBounds(100, 210, 300, 40);
        panelRight.add(txtUsername);

        // Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPass.setForeground(primaryColor);
        lblPass.setBounds(100, 280, 100, 20);
        panelRight.add(lblPass);

        txtPassword = new JPasswordField();
        styleField(txtPassword);
        txtPassword.setBounds(100, 310, 300, 40);
        panelRight.add(txtPassword);

        // Login Button
        btnLogin = new JButton("LOGIN");
        styleButton(btnLogin, secondaryColor, white);
        btnLogin.setBounds(100, 400, 300, 50);
        btnLogin.addActionListener(e -> { if(ValidateLogin()) login(); });
        panelRight.add(btnLogin);

        // Signup Button
        btnSignup = new JButton("Create New Account");
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignup.setForeground(primaryColor);
        btnSignup.setBackground(white);
        btnSignup.setBorder(null);
        btnSignup.setFocusPainted(false);
        btnSignup.setBounds(150, 470, 200, 30);
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignup.addActionListener(e -> {
            SignupPage r = new SignupPage();
            r.setVisible(true);
            this.dispose();
        });
        panelRight.add(btnSignup);
        
        // Forgot Password
        JButton btnForgot = new JButton("Forgot Password?");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgot.setForeground(Color.GRAY);
        btnForgot.setBackground(white);
        btnForgot.setBorder(null);
        btnForgot.setFocusPainted(false);
        btnForgot.setBounds(180, 510, 140, 20);
        btnForgot.addActionListener(e -> {
            Forgotpassword r = new Forgotpassword();
            r.setVisible(true);
            this.dispose();
        });
        panelRight.add(btnForgot);

        add(panelRight);
    }

    // Helper to style text fields
    private void styleField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt.setBorder(new MatteBorder(0, 0, 2, 0, primaryColor)); // Bottom border only
        txt.setBackground(Color.WHITE);
    }

    // Helper to style buttons
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LoginPage().setVisible(true));
    }

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnSignup;
}