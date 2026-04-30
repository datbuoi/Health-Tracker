package util;

import java.awt.*;
import javax.swing.*;

public class Theme {
    // Màu chính
    public static final Color BG_DARK       = new Color(15, 23, 42);       // nền chính
    public static final Color BG_CARD       = new Color(30, 41, 59);       // card
    public static final Color BG_CARD2      = new Color(51, 65, 85);       // card hover
    public static final Color ACCENT_BLUE   = new Color(99, 102, 241);     // tím xanh chính
    public static final Color ACCENT_GREEN  = new Color(34, 197, 94);      // xanh lá
    public static final Color ACCENT_ORANGE = new Color(251, 146, 60);     // cam
    public static final Color ACCENT_PINK   = new Color(236, 72, 153);     // hồng
    public static final Color ACCENT_CYAN   = new Color(6, 182, 212);      // cyan
    public static final Color TEXT_PRIMARY  = new Color(248, 250, 252);    // chữ chính
    public static final Color TEXT_SECONDARY= new Color(148, 163, 184);    // chữ phụ
    public static final Color BORDER        = new Color(51, 65, 85);       // viền

    // Font
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_LARGE   = new Font("Segoe UI", Font.BOLD, 32);

    public static void applyDarkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {}

        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("Frame.background", BG_DARK);
        UIManager.put("Button.background", ACCENT_BLUE);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", BG_CARD2);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("PasswordField.background", BG_CARD2);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", TEXT_PRIMARY);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", BG_CARD2);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("TextArea.background", BG_CARD2);
        UIManager.put("TextArea.foreground", TEXT_PRIMARY);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("Table.background", BG_CARD);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", BG_CARD2);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
    }

    // Tạo button đẹp
    public static javax.swing.JButton createButton(String text, Color bgColor) {
        javax.swing.JButton btn = new javax.swing.JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgColor.darker() :
                            getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(FONT_BODY.deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Tạo card panel bo góc
    public static javax.swing.JPanel createCard() {
        return new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
    }
}
