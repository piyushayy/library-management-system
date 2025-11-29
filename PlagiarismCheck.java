

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PlagiarismCheck extends javax.swing.JFrame {

    // Colors
    Color sidebarColor = new Color(44, 62, 80);
    Color white = Color.WHITE;
    Color red = new Color(231, 76, 60);
    Color green = new Color(46, 204, 113);

    File selectedFile;
    String fileContent = "";

    public PlagiarismCheck() {
        initComponents();
        loadStudentIds();
    }

    private void loadStudentIds() {
        try {
            Connection con = DBconnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select student_id from student");
            comboStudent.removeAllItems();
            while (rs.next()) {
                comboStudent.addItem(rs.getString("student_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- LOGIC: READ FILE ---
    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            lblFilename.setText(selectedFile.getName());
            
            try {
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                fileContent = sb.toString();
                
                // Display raw text initially
                txtPane.setText("");
                appendToPane(txtPane, fileContent, Color.BLACK);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reading file");
            }
        }
    }

    // --- LOGIC: CHECK PLAGIARISM ---
    private void checkPlagiarism() {
        if (fileContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please upload a text file first.");
            return;
        }

        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement pst = con.prepareStatement("select content from assignments");
            ResultSet rs = pst.executeQuery();

            // 1. Split input into sentences
            String[] inputSentences = fileContent.split("[.!?]");
            int totalSentences = inputSentences.length;
            int copiedSentences = 0;
            ArrayList<String> plagiarizedParts = new ArrayList<>();

            // 2. Compare against Database
            while (rs.next()) {
                String dbContent = rs.getString("content");
                
                for (String sentence : inputSentences) {
                    String cleanSentence = sentence.trim();
                    if (cleanSentence.length() > 10) { // Ignore very short phrases
                        if (dbContent.contains(cleanSentence)) {
                            if (!plagiarizedParts.contains(cleanSentence)) {
                                plagiarizedParts.add(cleanSentence);
                                copiedSentences++;
                            }
                        }
                    }
                }
            }

            // 3. Calculate Percentage
            int percentage = 0;
            if (totalSentences > 0) {
                percentage = (copiedSentences * 100) / totalSentences;
            }

            // 4. Update UI
            lblPercentage.setText(percentage + "% Plagiarized");
            if (percentage > 30) lblPercentage.setForeground(red);
            else lblPercentage.setForeground(green);

            // 5. Highlight Text
            highlightText(plagiarizedParts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- LOGIC: HIGHLIGHTING ---
    private void highlightText(ArrayList<String> badParts) {
        txtPane.setText(""); // Clear
        String[] sentences = fileContent.split("(?<=[.!?])"); // Split but keep delimiters

        for (String sentence : sentences) {
            boolean isCopied = false;
            for (String bad : badParts) {
                if (sentence.contains(bad)) {
                    isCopied = true;
                    break;
                }
            }

            if (isCopied) {
                appendToPane(txtPane, sentence, Color.RED);
            } else {
                appendToPane(txtPane, sentence, Color.BLACK);
            }
        }
    }

    // Helper to append colored text to JTextPane
    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Segoe UI");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 14);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    // --- LOGIC: SAVE ASSIGNMENT ---
    private void submitAssignment() {
        if (fileContent.isEmpty()) return;
        try {
            Connection con = DBconnection.getConnection();
            String sql = "insert into assignments(student_id, student_name, file_name, content) values(?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, comboStudent.getSelectedItem().toString());
            pst.setString(2, "Student Name"); // You can fetch real name if you want
            pst.setString(3, selectedFile.getName());
            pst.setString(4, fileContent);
            
            if(pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Assignment Submitted to Database!");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- GUI SETUP ---
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(1000, 700);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- LEFT PANEL (Controls) ---
        JPanel panelLeft = new JPanel();
        panelLeft.setBackground(sidebarColor);
        panelLeft.setBounds(0, 0, 350, 700);
        panelLeft.setLayout(null);

        JLabel lblBack = new JLabel("<< Back");
        lblBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBack.setForeground(white);
        lblBack.setBounds(20, 20, 100, 30);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new HomePage().setVisible(true); dispose();
            }
        });
        panelLeft.add(lblBack);

        JLabel title = new JLabel("<html>Check & Submit<br>Assignment</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(white);
        title.setBounds(30, 80, 300, 80);
        panelLeft.add(title);

        JLabel lblStu = new JLabel("Select Student ID:");
        lblStu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblStu.setForeground(white);
        lblStu.setBounds(30, 200, 200, 30);
        panelLeft.add(lblStu);

        comboStudent = new JComboBox<>();
        comboStudent.setBounds(30, 240, 280, 40);
        panelLeft.add(comboStudent);

        JButton btnUpload = new JButton("1. Upload File (.txt)");
        btnUpload.setBackground(new Color(52, 152, 219));
        btnUpload.setForeground(white);
        btnUpload.setBounds(30, 320, 280, 50);
        btnUpload.addActionListener(e -> uploadFile());
        panelLeft.add(btnUpload);

        lblFilename = new JLabel("No file selected");
        lblFilename.setForeground(Color.LIGHT_GRAY);
        lblFilename.setBounds(30, 380, 280, 20);
        panelLeft.add(lblFilename);

        JButton btnCheck = new JButton("2. Check Plagiarism");
        btnCheck.setBackground(new Color(243, 156, 18));
        btnCheck.setForeground(white);
        btnCheck.setBounds(30, 430, 280, 50);
        btnCheck.addActionListener(e -> checkPlagiarism());
        panelLeft.add(btnCheck);

        JButton btnSubmit = new JButton("3. Submit to DB");
        btnSubmit.setBackground(green);
        btnSubmit.setForeground(white);
        btnSubmit.setBounds(30, 580, 280, 50);
        btnSubmit.addActionListener(e -> submitAssignment());
        panelLeft.add(btnSubmit);

        add(panelLeft);

        // --- RIGHT PANEL (Results) ---
        JPanel panelRight = new JPanel();
        panelRight.setBackground(white);
        panelRight.setBounds(350, 0, 650, 700);
        panelRight.setLayout(null);

        JLabel lblResTitle = new JLabel("Plagiarism Report");
        lblResTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblResTitle.setForeground(sidebarColor);
        lblResTitle.setBounds(30, 30, 300, 40);
        panelRight.add(lblResTitle);

        lblPercentage = new JLabel("0% Plagiarized");
        lblPercentage.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblPercentage.setForeground(green);
        lblPercentage.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPercentage.setBounds(300, 30, 300, 50);
        panelRight.add(lblPercentage);

        // Text Pane for Highlighting
        txtPane = new JTextPane();
        txtPane.setEditable(false);
        JScrollPane sp = new JScrollPane(txtPane);
        sp.setBounds(30, 100, 580, 550);
        sp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelRight.add(sp);

        add(panelRight);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new PlagiarismCheck().setVisible(true));
    }

    private JComboBox<String> comboStudent;
    private JLabel lblFilename, lblPercentage;
    private JTextPane txtPane;
}