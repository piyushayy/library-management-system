

import java.sql.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Newstudent extends javax.swing.JFrame {

    Color sidebarColor = new Color(44, 62, 80);
    Color white = Color.WHITE;
    Color blue = new Color(52, 152, 219);

    public Newstudent() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(500, 650); // Compact vertical design
        setLayout(null);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel();
        header.setBackground(sidebarColor);
        header.setBounds(0, 0, 500, 70);
        header.setLayout(null);

        JLabel lblTitle = new JLabel("Add New Student");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(white);
        lblTitle.setBounds(130, 20, 300, 30);
        header.add(lblTitle);

        // Close
        JLabel lblClose = new JLabel("X");
        lblClose.setForeground(white);
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblClose.setBounds(460, 20, 30, 30);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { 
            new HomePage().setVisible(true); dispose(); 
        }});
        header.add(lblClose);
        add(header);

        // Body
        JPanel body = new JPanel();
        body.setBackground(white);
        body.setBounds(0, 70, 500, 580);
        body.setLayout(null);

        addInput(body, "Student ID", 0, txtId = new JTextField());
        addInput(body, "Name", 1, txtName = new JTextField());
        addInput(body, "Father Name", 2, txtFather = new JTextField());

        // Combo Boxes styled
        JLabel lblCourse = new JLabel("Course");
        lblCourse.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCourse.setForeground(sidebarColor);
        lblCourse.setBounds(50, 310, 100, 20);
        body.add(lblCourse);

        comboCourse = new JComboBox<>(new String[]{"B.Tech", "B.Sc", "MBA", "BCA"});
        comboCourse.setBounds(50, 340, 400, 35);
        comboCourse.setBackground(white);
        body.add(comboCourse);

        JLabel lblBranch = new JLabel("Branch");
        lblBranch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblBranch.setForeground(sidebarColor);
        lblBranch.setBounds(50, 390, 100, 20);
        body.add(lblBranch);

        comboBranch = new JComboBox<>(new String[]{"CS", "IT", "Civil", "Mech"});
        comboBranch.setBounds(50, 420, 400, 35);
        comboBranch.setBackground(white);
        body.add(comboBranch);

        // Save Button
        JButton btnSave = new JButton("SAVE RECORD");
        btnSave.setBackground(blue);
        btnSave.setForeground(white);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSave.setBounds(50, 500, 400, 50);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> saveStudent());
        body.add(btnSave);

        add(body);
    }

    private void addInput(JPanel p, String label, int index, JTextField field) {
        int y = 30 + (index * 90);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(sidebarColor);
        lbl.setBounds(50, y, 200, 20);
        p.add(lbl);

        field.setBounds(50, y + 30, 400, 35);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(new MatteBorder(0, 0, 2, 0, sidebarColor));
        p.add(field);
    }

    private void saveStudent() {
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement pst = con.prepareStatement("insert into student values(?,?,?,?,?)");
            pst.setString(1, txtId.getText());
            pst.setString(2, txtName.getText());
            pst.setString(3, txtFather.getText());
            pst.setString(4, comboCourse.getSelectedItem().toString());
            pst.setString(5, comboBranch.getSelectedItem().toString());
            if(pst.executeUpdate() > 0) { JOptionPane.showMessageDialog(this, "Success"); dispose(); new HomePage().setVisible(true);}
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String args[]) { java.awt.EventQueue.invokeLater(() -> new Newstudent().setVisible(true)); }
    private JTextField txtId, txtName, txtFather;
    private JComboBox<String> comboCourse, comboBranch;
}