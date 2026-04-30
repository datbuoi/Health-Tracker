package ui;

import db.DAO;
import model.User;
import model.WaterLog;
import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class WaterPanel extends JPanel {

    private User user;
    private JLabel lblTotal, lblPercent;
    private WaterProgressBar progressBar;
    private JPanel logListPanel;
    private final int GOAL_ML = 2000;

    public WaterPanel(User user) {
        this.user = user;
        setLayout(null);
        setBackground(Theme.BG_DARK);
        initUI();
        loadData();
    }

    private void initUI() {
        JLabel title = new JLabel("Nuoc - Theo doi uong nuoc");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT_CYAN);
        title.setBounds(24, 20, 500, 36);
        add(title);

        // === PROGRESS CARD ===
        JPanel progressCard = Theme.createCard();
        progressCard.setLayout(null);
        progressCard.setBounds(24, 68, 340, 260);
        add(progressCard);

        JLabel lblGoal = new JLabel("Mục tiêu hôm nay: " + GOAL_ML + " ml");
        lblGoal.setFont(Theme.FONT_SMALL);
        lblGoal.setForeground(Theme.TEXT_SECONDARY);
        lblGoal.setBounds(16, 16, 300, 20);
        progressCard.add(lblGoal);

        lblTotal = new JLabel("0 ml");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotal.setForeground(Theme.ACCENT_CYAN);
        lblTotal.setBounds(16, 42, 300, 48);
        progressCard.add(lblTotal);

        lblPercent = new JLabel("0%");
        lblPercent.setFont(Theme.FONT_HEADING);
        lblPercent.setForeground(Theme.TEXT_SECONDARY);
        lblPercent.setBounds(16, 88, 100, 24);
        progressCard.add(lblPercent);

        progressBar = new WaterProgressBar();
        progressBar.setBounds(16, 120, 308, 36);
        progressCard.add(progressBar);

        JLabel lblTip = new JLabel("Uong du nuoc giup co the khoe manh!");
        lblTip.setFont(Theme.FONT_SMALL);
        lblTip.setForeground(Theme.TEXT_SECONDARY);
        lblTip.setBounds(16, 168, 308, 18);
        progressCard.add(lblTip);

        // Status emoji
        JLabel lblStatus = new JLabel("Hay nho uong nuoc nhe!");
        lblStatus.setFont(Theme.FONT_SMALL);
        lblStatus.setForeground(Theme.ACCENT_CYAN);
        lblStatus.setBounds(16, 196, 308, 20);
        progressCard.add(lblStatus);

        // === QUICK ADD BUTTONS ===
        JPanel quickCard = Theme.createCard();
        quickCard.setLayout(null);
        quickCard.setBounds(24, 344, 340, 200);
        add(quickCard);

        JLabel lblQuick = new JLabel("Thêm nhanh");
        lblQuick.setFont(Theme.FONT_HEADING);
        lblQuick.setForeground(Theme.TEXT_PRIMARY);
        lblQuick.setBounds(16, 14, 200, 24);
        quickCard.add(lblQuick);

        int[] amounts = { 150, 200, 330, 500 };
        String[] icons = { "Ca phe", "Ly nho", "Chai nho", "Chai lon" };
        int bx = 16;
        for (int i = 0; i < amounts.length; i++) {
            final int ml = amounts[i];
            JButton btn = makeQuickBtn(icons[i] + "\n" + ml + "ml", ml);
            btn.setBounds(bx, 50, 57, 60);
            quickCard.add(btn);
            bx += 62;
        }

        JTextField txtCustom = makeField("Tùy chỉnh (ml)");
        txtCustom.setBounds(16, 128, 200, 40);
        quickCard.add(txtCustom);

        JButton btnCustom = Theme.createButton("Thêm", Theme.ACCENT_CYAN);
        btnCustom.setBounds(224, 128, 100, 40);
        quickCard.add(btnCustom);
        btnCustom.addActionListener(e -> {
            try {
                int ml = Integer.parseInt(txtCustom.getText().trim());
                addWater(ml);
                txtCustom.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nhập số ml hợp lệ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        });

        // === LOG LIST ===
        JPanel listCard = Theme.createCard();
        listCard.setLayout(new BorderLayout());
        listCard.setBounds(380, 68, 450, 476);
        add(listCard);

        JLabel lblLog = new JLabel("  Lich su hom nay");
        lblLog.setFont(Theme.FONT_HEADING);
        lblLog.setForeground(Theme.TEXT_PRIMARY);
        lblLog.setPreferredSize(new Dimension(450, 40));
        listCard.add(lblLog, BorderLayout.NORTH);

        logListPanel = new JPanel();
        logListPanel.setLayout(new BoxLayout(logListPanel, BoxLayout.Y_AXIS));
        logListPanel.setBackground(Theme.BG_CARD);

        JScrollPane scroll = new JScrollPane(logListPanel);
        scroll.setBackground(Theme.BG_CARD);
        scroll.getViewport().setBackground(Theme.BG_CARD);
        scroll.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        listCard.add(scroll, BorderLayout.CENTER);
    }

    private JButton makeQuickBtn(String label, int ml) {
        JButton btn = new JButton("<html><center>" + label.replace("\n", "<br>") + "</center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Theme.ACCENT_CYAN.darker() : Theme.BG_CARD2);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.FONT_SMALL);
        btn.setForeground(Theme.TEXT_PRIMARY);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> addWater(ml));
        return btn;
    }

    private void addWater(int ml) {
        boolean ok = DAO.addWater(user.getId(), ml);
        if (ok)
            loadData();
        else
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void loadData() {
        int total = DAO.getTodayWater(user.getId());
        double pct = Math.min((double) total / GOAL_ML * 100, 100);

        lblTotal.setText(total + " ml");
        lblPercent.setText(String.format("%.0f%%", pct));
        progressBar.setPercent((int) pct);

        // Reload logs
        logListPanel.removeAll();
        List<WaterLog> logs = DAO.getTodayWaterLogs(user.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (WaterLog log : logs) {
            JPanel row = new JPanel(null);
            row.setMaximumSize(new Dimension(430, 44));
            row.setMinimumSize(new Dimension(430, 44));
            row.setPreferredSize(new Dimension(430, 44));
            row.setBackground(Theme.BG_CARD);

            JLabel timeLabel = new JLabel(sdf.format(log.getLogTime()));
            timeLabel.setFont(Theme.FONT_SMALL);
            timeLabel.setForeground(Theme.TEXT_SECONDARY);
            timeLabel.setBounds(12, 12, 60, 20);
            row.add(timeLabel);

            JLabel mlLabel = new JLabel(log.getAmountMl() + " ml");
            mlLabel.setFont(Theme.FONT_BODY.deriveFont(Font.BOLD));
            mlLabel.setForeground(Theme.ACCENT_CYAN);
            mlLabel.setBounds(80, 12, 140, 20);
            row.add(mlLabel);

            JSeparator sep = new JSeparator();
            sep.setForeground(Theme.BORDER);
            sep.setBounds(0, 43, 430, 1);
            row.add(sep);

            logListPanel.add(row);
        }

        if (logs.isEmpty()) {
            JLabel empty = new JLabel("  Chưa có ghi chép hôm nay", SwingConstants.CENTER);
            empty.setFont(Theme.FONT_BODY);
            empty.setForeground(Theme.TEXT_SECONDARY);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            logListPanel.add(Box.createVerticalStrut(20));
            logListPanel.add(empty);
        }

        logListPanel.revalidate();
        logListPanel.repaint();
    }

    // Custom progress bar
    class WaterProgressBar extends JPanel {
        private int percent = 0;

        WaterProgressBar() {
            setOpaque(false);
        }

        void setPercent(int p) {
            this.percent = p;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.BG_CARD2);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            int filled = (int) (getWidth() * percent / 100.0);
            if (filled > 0) {
                GradientPaint gp = new GradientPaint(0, 0, Theme.ACCENT_CYAN, filled, 0, Theme.ACCENT_BLUE);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, filled, getHeight(), getHeight(), getHeight());
            }
            g2.dispose();
        }
    }

    private JTextField makeField(String hint) {
        JTextField tf = new JTextField(hint);
        tf.setFont(Theme.FONT_BODY);
        tf.setForeground(Theme.TEXT_SECONDARY);
        tf.setBackground(Theme.BG_CARD2);
        tf.setCaretColor(Theme.TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(hint)) {
                    tf.setText("");
                    tf.setForeground(Theme.TEXT_PRIMARY);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(hint);
                    tf.setForeground(Theme.TEXT_SECONDARY);
                }
            }
        });
        return tf;
    }
}
