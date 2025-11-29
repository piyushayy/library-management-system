package JFrame;

import java.sql.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

public class Returnbooks extends javax.swing.JFrame {

    public Returnbooks() {
        initComponents();
    }

    // --- FETCH ISSUE DETAILS ---
    private void searchIssue() {
        String bookId = txtBookId.getText();
        String studentId = txtStudentId.getText();

        if(bookId.isEmpty() || studentId.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter Book ID and Student ID");
            return;
        }

        try {
            Connection con = DBconnection.getConnection();
            // Find book that is issued and PENDING return
            String sql = "select * from issue_book where book_id = ? and student_id = ? and status = 'Pending'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);
            pst.setString(2, studentId);
            
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtBookName.setText(rs.getString("book_name"));
                // You can add more fields here if needed
                JOptionPane.showMessageDialog(this, "Book Found! Click Return to proceed.");
            } else {
                JOptionPane.showMessageDialog(this, "No pending issue found for this Book and Student.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error");
        }
    }

    // --- RETURN BOOK LOGIC ---
    private void returnBook() {
        String bookId = txtBookId.getText();
        String studentId = txtStudentId.getText();

        try {
            Connection con = DBconnection.getConnection();
            
            // Update status to 'Returned'
            String sql = "update issue_book set status = 'Returned' where book_id = ? and student_id = ? and status = 'Pending'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookId);
            pst.setString(2, studentId);

            int k = pst.executeUpdate();
            if (k > 0) {
                JOptionPane.showMessageDialog(this, "Book Returned Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error returning book. Please check IDs.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void clearFields(){
        txtBookId.setText("");
        txtStudentId.setText("");
        txtBookName.setText("");
    }

    // --- GUI SETUP ---
    private void initComponents() {
        jPanel1 = new JPanel();
        jLabelTitle = new JLabel();

        jLabelBookId = new JLabel("Book ID");
        jLabelStudentId = new JLabel("Student ID");
        jLabelBookName = new JLabel("Book Name");

        txtBookId = new JTextField();
        txtStudentId = new JTextField();
        txtBookName = new JTextField();

        btnSearch = new JButton("Find");
        btnReturn = new JButton("RETURN BOOK");
        btnClose = new JButton("Close");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Return Book");
        getContentPane().setLayout(null);

        // Header
        jPanel1.setBackground(new Color(255, 51, 51));
        jPanel1.setLayout(null);
        jPanel1.setBounds(0, 0, 800, 70);

        jLabelTitle.setFont(new Font("Segoe UI", 1, 30)); 
        jLabelTitle.setForeground(Color.WHITE);
        jLabelTitle.setText("Return Book");
        jLabelTitle.setBounds(300, 10, 300, 50);
        jPanel1.add(jLabelTitle);
        getContentPane().add(jPanel1);

        // Layout
        int x = 100;
        int y = 120;
        int gap = 60;
        int w = 250;

        // Book ID
        jLabelBookId.setFont(new Font("Segoe UI", 1, 18));
        jLabelBookId.setBounds(x, y, 150, 30);
        getContentPane().add(jLabelBookId);

        txtBookId.setBounds(x + 160, y, w, 35);
        getContentPane().add(txtBookId);

        // Student ID
        jLabelStudentId.setFont(new Font("Segoe UI", 1, 18));
        jLabelStudentId.setBounds(x, y + gap, 150, 30);
        getContentPane().add(jLabelStudentId);

        txtStudentId.setBounds(x + 160, y + gap, w, 35);
        getContentPane().add(txtStudentId);

        // Search Button
        btnSearch.setBounds(x + 160 + w + 20, y + gap/2, 100, 35);
        btnSearch.setText("Find Details");
        btnSearch.addActionListener(e -> searchIssue());
        getContentPane().add(btnSearch);

        // Book Name (Read Only)
        jLabelBookName.setFont(new Font("Segoe UI", 1, 18));
        jLabelBookName.setBounds(x, y + gap*2, 150, 30);
        getContentPane().add(jLabelBookName);

        txtBookName.setBounds(x + 160, y + gap*2, w, 35);
        txtBookName.setEditable(false);
        getContentPane().add(txtBookName);

        // Return Button
        btnReturn.setBackground(new Color(255, 51, 51));
        btnReturn.setForeground(Color.WHITE);
        btnReturn.setText("RETURN");
        btnReturn.setFont(new Font("Segoe UI", 1, 20));
        btnReturn.setBounds(180, 400, 150, 50);
        btnReturn.addActionListener(e -> returnBook());
        getContentPane().add(btnReturn);

        // Close Button
        btnClose.setBackground(new Color(102, 102, 255));
        btnClose.setForeground(Color.WHITE);
        btnClose.setText("Close");
        btnClose.setBounds(380, 400, 100, 50);
        btnClose.addActionListener(e -> {
            new HomePage().setVisible(true);
            this.dispose();
        });
        getContentPane().add(btnClose);

        // Frame
        setSize(750, 600);
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Returnbooks().setVisible(true));
    }

    // Variables
    private JTextField txtBookId, txtStudentId, txtBookName;
    private JButton btnSearch, btnReturn, btnClose;
    private JLabel jLabelTitle, jLabelBookId, jLabelStudentId, jLabelBookName;
    private JPanel jPanel1;
}