package JFrame;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Issuebook extends javax.swing.JFrame {

    Color panelColor = new Color(255, 51, 51); // Orange-Red for Issue
    Color white = Color.WHITE;

    public Issuebook() {
        initComponents();
        loadIds();
    }

    private void loadIds() {
        try {
            Connection con = DBconnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select book_id from books");
            while(rs.next()) comboBook.addItem(rs.getString(1));
            
            rs = con.createStatement().executeQuery("select student_id from student");
            while(rs.next()) comboStudent.addItem(rs.getString(1));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(1000, 600);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- LEFT PANEL (Details Display) ---
        JPanel panelLeft = new JPanel();
        panelLeft.setBackground(panelColor);
        panelLeft.setBounds(0, 0, 400, 600);
        panelLeft.setLayout(null);

        JLabel lblHead = new JLabel("  Issue Details");
        lblHead.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblHead.setForeground(white);
        lblHead.setBounds(40, 50, 300, 40);
        // Add an underline
        JPanel line = new JPanel();
        line.setBackground(white);
        line.setBounds(40, 90, 250, 3);
        panelLeft.add(line);
        panelLeft.add(lblHead);

        // Detail Labels
        lblBookName = addDetail(panelLeft, "Book Name:", 150);
        lblAuthor = addDetail(panelLeft, "Author:", 220);
        lblStudentName = addDetail(panelLeft, "Student Name:", 290);
        lblDept = addDetail(panelLeft, "Department:", 360);

        add(panelLeft);

        // --- RIGHT PANEL (Controls) ---
        JPanel panelRight = new JPanel();
        panelRight.setBackground(white);
        panelRight.setBounds(400, 0, 600, 600);
        panelRight.setLayout(null);

        // Close
        JLabel lblClose = new JLabel("X");
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblClose.setForeground(panelColor);
        lblClose.setBounds(560, 10, 30, 30);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { 
            new HomePage().setVisible(true); dispose(); 
        }});
        panelRight.add(lblClose);

        JLabel lblTitle = new JLabel("Issue Book");
        lblTitle.setForeground(panelColor);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTitle.setBounds(100, 50, 200, 30);
        panelRight.add(lblTitle);

        // Selectors
        addLabel(panelRight, "Select Book ID:", 120);
        comboBook = new JComboBox<>();
        comboBook.setBounds(100, 150, 300, 35);
        panelRight.add(comboBook);

        addLabel(panelRight, "Select Student ID:", 200);
        comboStudent = new JComboBox<>();
        comboStudent.setBounds(100, 230, 300, 35);
        panelRight.add(comboStudent);
        
        // Find Details Button
        JButton btnFind = new JButton("Find Details");
        btnFind.setBounds(420, 150, 120, 115);
        btnFind.setBackground(new Color(44, 62, 80));
        btnFind.setForeground(white);
        btnFind.addActionListener(e -> findDetails());
        panelRight.add(btnFind);

        addLabel(panelRight, "Issue Date (YYYY-MM-DD):", 280);
        txtDate = new JTextField("2024-01-01");
        txtDate.setBounds(100, 310, 300, 35);
        txtDate.setBorder(BorderFactory.createMatteBorder(0,0,2,0, panelColor));
        panelRight.add(txtDate);

        // Issue Button
        JButton btnIssue = new JButton("ISSUE BOOK");
        btnIssue.setBackground(panelColor);
        btnIssue.setForeground(white);
        btnIssue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnIssue.setBounds(100, 400, 400, 50);
        btnIssue.addActionListener(e -> issueBook());
        panelRight.add(btnIssue);

        add(panelRight);
    }

    private JLabel addDetail(JPanel p, String title, int y) {
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(white);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setBounds(40, y, 150, 20);
        p.add(lblTitle);

        JLabel lblValue = new JLabel("...");
        lblValue.setForeground(white);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setBounds(40, y + 25, 300, 20);
        p.add(lblValue);
        return lblValue;
    }

    private void addLabel(JPanel p, String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(panelColor);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setBounds(100, y, 200, 20);
        p.add(lbl);
    }

    private void findDetails() {
        try {
            Connection con = DBconnection.getConnection();
            // Get Book
            PreparedStatement pst = con.prepareStatement("select book_name, book_author from books where book_id=?");
            pst.setString(1, comboBook.getSelectedItem().toString());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lblBookName.setText(rs.getString(1));
                lblAuthor.setText(rs.getString(2));
            }
            // Get Student
            pst = con.prepareStatement("select name, branch from student where student_id=?");
            pst.setString(1, comboStudent.getSelectedItem().toString());
            rs = pst.executeQuery();
            if (rs.next()) {
                lblStudentName.setText(rs.getString(1));
                lblDept.setText(rs.getString(2));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void issueBook() {
        try {
            Connection con = DBconnection.getConnection();
            // Check if issued
            PreparedStatement chk = con.prepareStatement("select * from issue_book where book_id=? and status='Pending'");
            chk.setString(1, comboBook.getSelectedItem().toString());
            if(chk.executeQuery().next()) { JOptionPane.showMessageDialog(this, "Book already issued"); return; }

            PreparedStatement pst = con.prepareStatement("insert into issue_book(book_id, student_id, book_name, date_issued, status) values(?,?,?,?,?)");
            pst.setString(1, comboBook.getSelectedItem().toString());
            pst.setString(2, comboStudent.getSelectedItem().toString());
            pst.setString(3, lblBookName.getText());
            pst.setString(4, txtDate.getText());
            pst.setString(5, "Pending");
            if(pst.executeUpdate() > 0) JOptionPane.showMessageDialog(this, "Book Issued");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String args[]) { java.awt.EventQueue.invokeLater(() -> new Issuebook().setVisible(true)); }
    private JComboBox<String> comboBook, comboStudent;
    private JLabel lblBookName, lblAuthor, lblStudentName, lblDept;
    private JTextField txtDate;
}