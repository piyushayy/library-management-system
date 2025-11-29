

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {

    private Color color;
    private Color colorOver;
    private Color colorClick;
    private int radius = 30; // Rounded corners

    public ModernButton(String text, Color baseColor) {
        super(text);
        this.color = baseColor;
        this.colorOver = baseColor.brighter();
        this.colorClick = baseColor.darker();
        
        // Remove standard Swing looks
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add Animation Logic
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(colorOver);
                color = colorOver;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(baseColor);
                color = baseColor;
            }

            @Override
            public void mousePressed(MouseEvent me) {
                setBackground(colorClick);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                setBackground(colorOver);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint background
        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        // Paint Text
        super.paintComponent(g);
    }
}