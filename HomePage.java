

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*; // Import SQL for database connection

public class HomePage extends javax.swing.JFrame {

    // Colors
    Color colorPrimary = new Color(52, 152, 219); // Blue
    Color colorSecondary = new Color(41, 128, 185); // Darker Blue
    Color bgColor = new Color(244, 247, 246); // Light Grey Background

    // Data Variables
    int bookCount = 0;
    int studentCount = 0;
    int issueCount = 0;
    int defaulterCount = 0;

    public HomePage() {
        initComponents();
        this.setLocationRelativeTo(null);
        startFadeInAnimation(); 
    }

    // --- DATABASE LOGIC ---
    public void setDataToCards() {
        try {
            Connection con = DBconnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs;

            // 1. Get Total Books
            rs = st.executeQuery("select count(*) from books");
            if (rs.next()) {
                bookCount = rs.getInt(1);
            }

            // 2. Get Total Students
            rs = st.executeQuery("select count(*) from student");
            if (rs.next()) {
                studentCount = rs.getInt(1);
            }

            // 3. Get Issued Books (Status = 'Pending')
            rs = st.executeQuery("select count(*) from issue_book where status = 'Pending'");
            if (rs.next()) {
                issueCount = rs.getInt(1);
            }

            // 4. Get Defaulter List 
            // Note: Since we don't have a 'Due Date' column yet, 
            // we will count all Pending books as currently checked out.
            // In a future update, you can change this query to check dates.
            defaulterCount = issueCount; 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- ANIMATION LOGIC ---
    private void startFadeInAnimation() {
        setOpacity(0.0f);
        Timer timer = new Timer(10, new ActionListener() {
            float opacity = 0.0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if(opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    private void initComponents() {
        // Fetch Data BEFORE drawing UI
        setDataToCards();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(1400, 800);
        setLayout(null);

        // --- 1. SIDEBAR WITH GRADIENT ---
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(75, 108, 183), 0, getHeight(), new Color(24, 40, 72));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setBounds(0, 0, 300, 800);
        sidebar.setLayout(null);

        JLabel title = new JLabel("LMS Pro");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setBounds(60, 40, 200, 50);
        sidebar.add(title);

        JLabel subtitle = new JLabel("Library System");
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setBounds(85, 90, 200, 30);
        sidebar.add(subtitle);

        int y = 180;
        int gap = 60;
        
        addMenuButton(sidebar, "📚 Manage Books", y, e -> openPage(new Addbooks()));
        addMenuButton(sidebar, "🎓 Manage Students", y + gap, e -> openPage(new Newstudent()));
        addMenuButton(sidebar, "📝 Issue Books", y + gap*2, e -> openPage(new Issuebook()));
        addMenuButton(sidebar, "↩ Return Books", y + gap*3, e -> openPage(new Returnbooks()));
        addMenuButton(sidebar, "📊 Statistics", y + gap*4, e -> openPage(new Statics()));
        addMenuButton(sidebar, "👨‍💼 Manage Staff", y + gap*5, e -> openPage(new AddStaff()));
        addMenuButton(sidebar, "🕵️ Plagiarism Check", y + gap*6, e -> openPage(new PlagiarismCheck()));

        ModernButton btnLogout = new ModernButton("LOGOUT", new Color(231, 76, 60));
        btnLogout.setBounds(50, 700, 200, 50);
        btnLogout.addActionListener(e -> {
            LoginPage r = new LoginPage();
            r.setVisible(true);
            this.dispose();
        });
        sidebar.add(btnLogout);

        add(sidebar);

        // --- 2. HEADER PANEL ---
        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setBounds(300, 0, 1100, 70);
        header.setLayout(null);

        JLabel lblWelcome = new JLabel("Welcome back, Admin!");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(new Color(100, 100, 100));
        lblWelcome.setBounds(30, 20, 300, 30);
        header.add(lblWelcome);

        JLabel close = new JLabel("X");
        close.setFont(new Font("Segoe UI", Font.BOLD, 22));
        close.setForeground(Color.BLACK);
        close.setBounds(1050, 20, 30, 30);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { System.exit(0); }
        });
        header.add(close);

        add(header);

        // --- 3. MAIN CONTENT (DASHBOARD CARDS) ---
        JPanel content = new JPanel();
        content.setBackground(bgColor);
        content.setBounds(300, 70, 1100, 730);
        content.setLayout(null);

        // --- Cards showing REAL Database Stats ---
        
        // Books Card (Red)
        createCard(content, "Total Books", String.valueOf(bookCount), new Color(255, 82, 82), 50, 50);
        
        // Students Card (Blue)
        createCard(content, "Students", String.valueOf(studentCount), new Color(68, 138, 255), 320, 50);
        
        // Issued Books Card (Orange)
        createCard(content, "Issued Books", String.valueOf(issueCount), new Color(255, 177, 66), 590, 50);
        
        // Defaulter List (Purple)
        createCard(content, "Defaulter List", String.valueOf(defaulterCount), new Color(102, 102, 255), 860, 50);

        JLabel lblDecor = new JLabel("<html>Manage your library efficiently.<br>Select an option from the menu to begin.</html>");
        lblDecor.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        lblDecor.setForeground(new Color(150, 150, 150));
        lblDecor.setBounds(350, 350, 500, 100);
        content.add(lblDecor);

        add(content);
    }

    // --- HELPER METHODS ---

    private void openPage(JFrame page) {
        page.setVisible(true);
    }

    private void addMenuButton(JPanel panel, String text, int y, ActionListener action) {
        ModernButton btn = new ModernButton(text, new Color(255, 255, 255, 30));
        btn.setBounds(30, y, 240, 45);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.addActionListener(action);
        panel.add(btn);
    }

    private void createCard(JPanel panel, String title, String value, Color color, int x, int y) {
        JPanel card = new JPanel();
        card.setBackground(color);
        card.setBounds(x, y, 220, 120);
        card.setLayout(null);
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(0,0,0,30)));

        JLabel lblIcon = new JLabel("📊");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        lblIcon.setForeground(new Color(255,255,255, 100));
        lblIcon.setBounds(150, 20, 60, 50);
        card.add(lblIcon);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValue.setForeground(Color.WHITE);
        lblValue.setBounds(20, 20, 150, 40);
        card.add(lblValue);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 65, 150, 20);
        card.add(lblTitle);

        panel.add(card);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new HomePage().setVisible(true));
    }
}