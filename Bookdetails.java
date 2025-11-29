package JFrame;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;

public class Bookdetails extends javax.swing.JFrame {

    public Bookdetails() {
        initComponents();
        load();
    }

    // --- LOAD DATA FROM DATABASE ---
    public void load() {
        try {
            Connection con = DBconnection.getConnection();
            if (con == null) return;

            PreparedStatement pst = con.prepareStatement("select * from books");
            ResultSet rs = pst.executeQuery();

            ResultSetMetaData rsd = rs.getMetaData();
            int c = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            d.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= c; i++) {
                    // Make sure these match the columns in your MySQL Table
                    v.add(rs.getString("book_id"));
                    v.add(rs.getString("book_category"));
                    v.add(rs.getString("book_name"));
                    v.add(rs.getString("book_author"));
                    // We used 'date_added' in the AddBooks.java fix, so we use it here too
                    v.add(rs.getString("date_added")); 
                }
                d.addRow(v);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data");
        }
    }

    // --- GUI SETUP ---
    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton2 = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("View All Books");

        // Main Layout (Absolute)
        getContentPane().setLayout(null);

        // Header Panel
        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.setLayout(null);
        jPanel1.setBounds(0, 0, 1100, 70);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); 
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Available Books Details");
        jLabel1.setBounds(350, 10, 500, 50);
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1);

        // Table Setup
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Book ID", "Category", "Name", "Author", "Date Added" }
        ));
        
        jScrollPane1.setViewportView(jTable1);
        jScrollPane1.setBounds(20, 90, 1000, 480);
        getContentPane().add(jScrollPane1);

        // Back Button
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); 
        jButton2.setText("Back");
        jButton2.setBackground(Color.RED);
        jButton2.setForeground(Color.WHITE);
        jButton2.setBounds(880, 590, 140, 50);
        jButton2.addActionListener(e -> {
            HomePage r = new HomePage();
            r.setVisible(true);
            this.dispose();
        });
        getContentPane().add(jButton2);

        // Frame Size
        setSize(1060, 700);
        setLocationRelativeTo(null); // Center on screen
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Bookdetails().setVisible(true));
    }

    // Variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}