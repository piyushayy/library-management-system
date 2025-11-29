package JFrame;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddStaff extends javax.swing.JFrame {

    public AddStaff() {
        initComponents();
        load();
    }

    PreparedStatement pst;
    ResultSet rs;

    // --- LOAD DATA FUNCTION ---
    public void load() {
        try {
            Connection con = DBconnection.getConnection();
            if (con == null) return;

            pst = con.prepareStatement("select * from staffs");
            rs = pst.executeQuery();

            ResultSetMetaData rsd = rs.getMetaData();
            int c = rsd.getColumnCount();
            
            DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
            d.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= c; i++) {
                    v.add(rs.getString("staff_id"));
                    v.add(rs.getString("staff_name"));
                    v.add(rs.getString("join_date"));
                    v.add(rs.getString("staff_mobile"));
                    v.add(rs.getString("staff_address"));
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

        jLabelId = new JLabel("Staff ID");
        jLabelName = new JLabel("Staff Name");
        jLabelDate = new JLabel("Join Date");
        jLabelMobile = new JLabel("Mobile");
        jLabelAddress = new JLabel("Address");

        txtId = new JTextField();
        txtName = new JTextField();
        txtDate = new JTextField();
        txtMobile = new JTextField();
        txtAddress = new JTextField();

        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnReset = new JButton("Reset");
        btnBack = new JButton("Back");

        jScrollPane2 = new JScrollPane();
        jTable1 = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manage Staff");

        // Top Panel
        jPanel1.setBackground(new Color(51, 51, 255));
        jPanel1.setLayout(null);

        jLabel1.setFont(new Font("Segoe UI", 1, 36)); 
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setText("Manage Staff");
        jLabel1.setBounds(400, 10, 300, 50);
        jPanel1.add(jLabel1);

        // Layout settings
        int leftX = 50;
        int labelY = 100;
        int inputY = 100;
        int gap = 60;
        int labelW = 150;
        int inputW = 250;

        // Fields
        jLabelId.setFont(new Font("Mangal", 1, 18));
        jLabelId.setBounds(leftX, labelY, labelW, 30);
        add(jLabelId);
        txtId.setBounds(leftX + 150, inputY, inputW, 30);
        add(txtId);

        jLabelName.setFont(new Font("Mangal", 1, 18));
        jLabelName.setBounds(leftX, labelY + gap, labelW, 30);
        add(jLabelName);
        txtName.setBounds(leftX + 150, inputY + gap, inputW, 30);
        add(txtName);

        jLabelDate.setFont(new Font("Mangal", 1, 18));
        jLabelDate.setBounds(leftX, labelY + gap*2, labelW, 30);
        add(jLabelDate);
        txtDate.setBounds(leftX + 150, inputY + gap*2, inputW, 30);
        txtDate.setText("YYYY-MM-DD"); // Helper text
        add(txtDate);

        jLabelMobile.setFont(new Font("Mangal", 1, 18));
        jLabelMobile.setBounds(leftX, labelY + gap*3, labelW, 30);
        add(jLabelMobile);
        txtMobile.setBounds(leftX + 150, inputY + gap*3, inputW, 30);
        add(txtMobile);

        jLabelAddress.setFont(new Font("Mangal", 1, 18));
        jLabelAddress.setBounds(leftX, labelY + gap*4, labelW, 30);
        add(jLabelAddress);
        txtAddress.setBounds(leftX + 150, inputY + gap*4, inputW, 30);
        add(txtAddress);

        // Table
        jTable1.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Name", "Date", "Mobile", "Address" }
        ));
        
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int selectIndex = jTable1.getSelectedRow();
                DefaultTableModel d = (DefaultTableModel) jTable1.getModel();
                
                txtId.setText(d.getValueAt(selectIndex, 0).toString());
                txtName.setText(d.getValueAt(selectIndex, 1).toString());
                txtDate.setText(d.getValueAt(selectIndex, 2).toString());
                txtMobile.setText(d.getValueAt(selectIndex, 3).toString());
                txtAddress.setText(d.getValueAt(selectIndex, 4).toString());
            }
        });

        jScrollPane2.setViewportView(jTable1);
        jScrollPane2.setBounds(500, 100, 600, 400);
        add(jScrollPane2);

        // Buttons
        int btnY = 550;
        int startX = 100;
        int btnW = 120;
        int btnGap = 140;

        btnAdd.setBounds(startX, btnY, btnW, 40);
        btnAdd.addActionListener(e -> addStaff());
        add(btnAdd);

        btnEdit.setBounds(startX + btnGap, btnY, btnW, 40);
        btnEdit.addActionListener(e -> editStaff());
        add(btnEdit);

        btnDelete.setBounds(startX + btnGap*2, btnY, btnW, 40);
        btnDelete.addActionListener(e -> deleteStaff());
        add(btnDelete);

        btnReset.setBounds(startX + btnGap*3, btnY, btnW, 40);
        btnReset.addActionListener(e -> resetFields());
        add(btnReset);

        btnBack.setBounds(startX + btnGap*4, btnY, btnW, 40);
        btnBack.setBackground(Color.RED);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            HomePage r = new HomePage();
            r.setVisible(true);
            this.dispose();
        });
        add(btnBack);

        setLayout(null);
        add(jPanel1);
        jPanel1.setBounds(0, 0, 1200, 70);
        
        setSize(1200, 700);
        setLocationRelativeTo(null);
    }

    // --- LOGIC METHODS ---

    private void addStaff() {
        try {
            Connection con = DBconnection.getConnection();
            String sql = "insert into staffs (staff_id, staff_name, join_date, staff_mobile, staff_address) values(?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            
            pst.setString(1, txtId.getText());
            pst.setString(2, txtName.getText());
            pst.setString(3, txtDate.getText());
            pst.setString(4, txtMobile.getText());
            pst.setString(5, txtAddress.getText());

            if (pst.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(this, "Staff Added Successfully");
                load();
                resetFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editStaff() {
        try {
            Connection con = DBconnection.getConnection();
            String sql = "update staffs set staff_name=?, join_date=?, staff_mobile=?, staff_address=? where staff_id=?";
            pst = con.prepareStatement(sql);
            
            pst.setString(1, txtName.getText());
            pst.setString(2, txtDate.getText());
            pst.setString(3, txtMobile.getText());
            pst.setString(4, txtAddress.getText());
            pst.setString(5, txtId.getText());

            if (pst.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(this, "Updated Successfully");
                load();
                resetFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteStaff() {
        try {
            Connection con = DBconnection.getConnection();
            pst = con.prepareStatement("delete from staffs where staff_id=?");
            pst.setString(1, txtId.getText());

            if (pst.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(this, "Deleted Successfully");
                load();
                resetFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void resetFields() {
        txtId.setText("");
        txtName.setText("");
        txtDate.setText("");
        txtMobile.setText("");
        txtAddress.setText("");
        txtId.requestFocus();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new AddStaff().setVisible(true));
    }

    // Components
    private JPanel jPanel1;
    private JLabel jLabel1, jLabelId, jLabelName, jLabelDate, jLabelMobile, jLabelAddress;
    private JTextField txtId, txtName, txtDate, txtMobile, txtAddress;
    private JButton btnAdd, btnEdit, btnDelete, btnReset, btnBack;
    private JScrollPane jScrollPane2;
    private JTable jTable1;
}