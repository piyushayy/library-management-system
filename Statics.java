package JFrame;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;

public class Statics extends javax.swing.JFrame {

    public Statics() {
        initComponents();
        loadIssuedBooks();
        loadReturnedBooks();
    }

    // --- LOAD ISSUED BOOKS (Status = Pending) ---
    public void loadIssuedBooks() {
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement pst = con.prepareStatement("select * from issue_book where status = 'Pending'");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel d = (DefaultTableModel) tblIssued.getModel();
            d.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("book_id"));
                v.add(rs.getString("book_name"));
                v.add(rs.getString("student_id"));
                v.add(rs.getString("date_issued"));
                d.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- LOAD RETURNED BOOKS (Status = Returned) ---
    public void loadReturnedBooks() {
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement pst = con.prepareStatement("select * from issue_book where status = 'Returned'");
            ResultSet rs = pst.executeQuery();

            DefaultTableModel d = (DefaultTableModel) tblReturned.getModel();
            d.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("book_id"));
                v.add(rs.getString("book_name"));
                v.add(rs.getString("student_id"));
                v.add(rs.getString("date_issued")); // You could add a return_date column to DB if needed
                d.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- GUI SETUP ---
    private void initComponents() {
        jPanel1 = new JPanel(); // Header Issued
        jPanel2 = new JPanel(); // Header Returned
        
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        
        jScrollPane1 = new JScrollPane();
        tblIssued = new JTable();
        
        jScrollPane2 = new JScrollPane();
        tblReturned = new JTable();
        
        btnClose = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library Statistics");
        getContentPane().setLayout(null);

        // --- TOP SECTION: ISSUED BOOKS ---
        jLabel1.setFont(new Font("Segoe UI", 1, 24)); 
        jLabel1.setForeground(new Color(51, 51, 255));
        jLabel1.setText("Issue Details");
        jLabel1.setBounds(20, 10, 200, 30);
        getContentPane().add(jLabel1);

        // Underline / Divider
        JPanel line1 = new JPanel();
        line1.setBackground(new Color(51, 51, 255));
        line1.setBounds(20, 45, 300, 3);
        getContentPane().add(line1);

        // Table Issued
        tblIssued.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "Issue ID", "Book ID", "Book Name", "Student ID", "Date" }
        ));
        tblIssued.setRowHeight(25);
        tblIssued.getTableHeader().setBackground(new Color(102, 102, 255));
        tblIssued.getTableHeader().setForeground(Color.WHITE);
        
        jScrollPane1.setViewportView(tblIssued);
        jScrollPane1.setBounds(20, 60, 1150, 250);
        getContentPane().add(jScrollPane1);


        // --- BOTTOM SECTION: RETURNED BOOKS ---
        jLabel2.setFont(new Font("Segoe UI", 1, 24)); 
        jLabel2.setForeground(new Color(255, 51, 51));
        jLabel2.setText("Return Details");
        jLabel2.setBounds(20, 330, 200, 30);
        getContentPane().add(jLabel2);

        // Underline / Divider
        JPanel line2 = new JPanel();
        line2.setBackground(new Color(255, 51, 51));
        line2.setBounds(20, 365, 300, 3);
        getContentPane().add(line2);

        // Table Returned
        tblReturned.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "Issue ID", "Book ID", "Book Name", "Student ID", "Date" }
        ));
        tblReturned.setRowHeight(25);
        tblReturned.getTableHeader().setBackground(new Color(255, 102, 102));
        tblReturned.getTableHeader().setForeground(Color.WHITE);

        jScrollPane2.setViewportView(tblReturned);
        jScrollPane2.setBounds(20, 380, 1150, 250);
        getContentPane().add(jScrollPane2);

        // Close Button
        btnClose.setText("Back");
        btnClose.setBackground(Color.RED);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", 1, 18));
        btnClose.setBounds(1050, 650, 120, 40);
        btnClose.addActionListener(e -> {
            new HomePage().setVisible(true);
            this.dispose();
        });
        getContentPane().add(btnClose);

        // Frame Settings
        setSize(1200, 750);
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Statics().setVisible(true));
    }

    // Variables
    private JTable tblIssued, tblReturned;
    private JButton btnClose;
    private JLabel jLabel1, jLabel2;
    private JPanel jPanel1, jPanel2;
    private JScrollPane jScrollPane1, jScrollPane2;
}