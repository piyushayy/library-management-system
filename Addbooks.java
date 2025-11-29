

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Addbooks extends javax.swing.JFrame {

    // Colors
    Color sidebarColor = new Color(44, 62, 80);
    Color white = Color.WHITE;
    Color red = new Color(231, 76, 60);

    public Addbooks() {
        initComponents();
        load();
    }

    PreparedStatement pst;
    ResultSet rs;

    public void load() {
        try {
            Connection con = DBconnection.getConnection();
            pst = con.prepareStatement("select * from books");
            rs = pst.executeQuery();
            
            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            d.setRowCount(0);

            ResultSetMetaData rsd = rs.getMetaData();
            int c = rsd.getColumnCount();

            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= c; i++) {
                    v.add(rs.getString("book_id"));
                    v.add(rs.getString("book_category"));
                    v.add(rs.getString("book_name"));
                    v.add(rs.getString("book_author"));
                    v.add(rs.getString("date_added"));
                }
                d.addRow(v);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(1200, 700);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- LEFT PANEL (Form) ---
        JPanel panelLeft = new JPanel();
        panelLeft.setBackground(sidebarColor);
        panelLeft.setBounds(0, 0, 450, 700);
        panelLeft.setLayout(null);

        // Back Button
        JLabel lblBack = new JLabel("<< Back");
        lblBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBack.setForeground(white);
        lblBack.setBounds(10, 10, 100, 30);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new HomePage().setVisible(true);
                dispose();
            }
        });
        panelLeft.add(lblBack);

        // Inputs
        addInput(panelLeft, "Book ID", 0, txtBookId = new JTextField());
        addInput(panelLeft, "Category", 1, txtCategory = new JTextField());
        addInput(panelLeft, "Book Name", 2, txtName = new JTextField());
        addInput(panelLeft, "Author Name", 3, txtAuthor = new JTextField());
        addInput(panelLeft, "Date (YYYY-MM-DD)", 4, txtDate = new JTextField());
        txtDate.setText("2024-01-01");

        // Buttons
        JButton btnAdd = createButton("ADD", new Color(46, 204, 113));
        btnAdd.setBounds(30, 500, 110, 40);
        btnAdd.addActionListener(e -> addBook());
        panelLeft.add(btnAdd);

        JButton btnUpdate = createButton("UPDATE", new Color(241, 196, 15));
        btnUpdate.setBounds(160, 500, 110, 40);
        btnUpdate.addActionListener(e -> updateBook());
        panelLeft.add(btnUpdate);

        JButton btnDelete = createButton("DELETE", red);
        btnDelete.setBounds(290, 500, 110, 40);
        btnDelete.addActionListener(e -> deleteBook());
        panelLeft.add(btnDelete);

        add(panelLeft);

        // --- RIGHT PANEL (Table) ---
        JPanel panelRight = new JPanel();
        panelRight.setBackground(white);
        panelRight.setBounds(450, 0, 750, 700);
        panelRight.setLayout(null);

        // Close Button
        JLabel lblClose = new JLabel("X");
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblClose.setForeground(sidebarColor);
        lblClose.setBounds(710, 10, 30, 30);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { System.exit(0); }});
        panelRight.add(lblClose);

        JLabel lblTitle = new JLabel("Manage Books");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(red);
        lblTitle.setBounds(250, 50, 300, 40);
        panelRight.add(lblTitle);

        // Table
        jTable1 = new JTable();
        jTable1.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Category", "Name", "Author", "Date"}));
        jTable1.setRowHeight(25);
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = jTable1.getSelectedRow();
                DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
                txtBookId.setText(d.getValueAt(row, 0).toString());
                txtCategory.setText(d.getValueAt(row, 1).toString());
                txtName.setText(d.getValueAt(row, 2).toString());
                txtAuthor.setText(d.getValueAt(row, 3).toString());
                txtDate.setText(d.getValueAt(row, 4).toString());
            }
        });

        JScrollPane sp = new JScrollPane(jTable1);
        sp.setBounds(20, 120, 700, 500);
        panelRight.add(sp);

        add(panelRight);
    }

    // Helpers
    private void addInput(JPanel p, String label, int index, JTextField field) {
        int y = 100 + (index * 80);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(white);
        lbl.setBounds(50, y, 200, 20);
        p.add(lbl);

        field.setBounds(50, y + 25, 300, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(sidebarColor);
        field.setForeground(white);
        field.setBorder(new MatteBorder(0, 0, 2, 0, white));
        p.add(field);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(white);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Logic
    private void addBook() {
        try {
            Connection con = DBconnection.getConnection();
            pst = con.prepareStatement("insert into books values(?,?,?,?,?)");
            pst.setString(1, txtBookId.getText());
            pst.setString(2, txtCategory.getText());
            pst.setString(3, txtName.getText());
            pst.setString(4, txtAuthor.getText());
            pst.setString(5, txtDate.getText());
            if (pst.executeUpdate() > 0) { JOptionPane.showMessageDialog(this, "Added"); load(); clear(); }
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void deleteBook() {
        try {
            Connection con = DBconnection.getConnection();
            pst = con.prepareStatement("delete from books where book_id=?");
            pst.setString(1, txtBookId.getText());
            if (pst.executeUpdate() > 0) { JOptionPane.showMessageDialog(this, "Deleted"); load(); clear(); }
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void updateBook() {
         try {
            Connection con = DBconnection.getConnection();
            pst = con.prepareStatement("update books set book_category=?, book_name=?, book_author=?, date_added=? where book_id=?");
            pst.setString(1, txtCategory.getText());
            pst.setString(2, txtName.getText());
            pst.setString(3, txtAuthor.getText());
            pst.setString(4, txtDate.getText());
            pst.setString(5, txtBookId.getText());
            if (pst.executeUpdate() > 0) { JOptionPane.showMessageDialog(this, "Updated"); load(); clear(); }
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void clear() { txtBookId.setText(""); txtName.setText(""); txtAuthor.setText(""); }

    public static void main(String args[]) { java.awt.EventQueue.invokeLater(() -> new Addbooks().setVisible(true)); }
    private JTextField txtBookId, txtCategory, txtName, txtAuthor, txtDate;
    private JTable jTable1;
}