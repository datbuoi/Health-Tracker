package ui;

import db.DAO;
import model.User;
import model.WeightLog;
import util.Theme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class WeightPanel extends JPanel {

    private User user;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtWeight, txtDate, txtNote;
    private JLabel lblBMI, lblCategory;

    public WeightPanel(User user) {
        this.user = user;
        setLayout(null);
        setBackground(Theme.BG_DARK);
        initUI();
        loadData();
    }

    private void initUI() {
        // Title
        JLabel title = new JLabel("[=]  Theo dõi cân nặng");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setBounds(24, 20, 400, 36);
        add(title);

        // === INPUT CARD ===
        JPanel inputCard = Theme.createCard();
        inputCard.setLayout(null);
        inputCard.setBounds(24, 68, 320, 400);
        add(inputCard);

        JLabel lblInput = new JLabel("Ghi cân nặng mới");
        lblInput.setFont(Theme.FONT_HEADING);
        lblInput.setForeground(Theme.TEXT_PRIMARY);
        lblInput.setBounds(16, 16, 280, 24);
        inputCard.add(lblInput);

        // Weight
        addLabel(inputCard, "Cân nặng (kg)", 16, 50);
        txtWeight = makeField("0,00");
        txtWeight.setBounds(16, 72, 285, 40);
        inputCard.add(txtWeight);

        // BMI display
        lblBMI = new JLabel("BMI: —");
        lblBMI.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBMI.setForeground(Theme.ACCENT_BLUE);
        lblBMI.setBounds(16, 118, 200, 28);
        inputCard.add(lblBMI);

        lblCategory = new JLabel("Phân loại: —");
        lblCategory.setFont(Theme.FONT_SMALL);
        lblCategory.setForeground(Theme.TEXT_SECONDARY);
        lblCategory.setBounds(16, 146, 285, 20);
        inputCard.add(lblCategory);

        txtWeight.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateBMI();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateBMI();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateBMI();
            }
        });

        // Date
        addLabel(inputCard, "Ngày (yyyy-MM-dd)", 16, 172);
        txtDate = makeField(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        txtDate.setBounds(16, 194, 285, 40);
        inputCard.add(txtDate);

        // Note
        addLabel(inputCard, "Ghi chú", 16, 240);
        txtNote = makeField("Ghi chú...");
        txtNote.setBounds(16, 262, 285, 40);
        inputCard.add(txtNote);

        JButton btnAdd = Theme.createButton("  Lưu cân nặng", Theme.ACCENT_BLUE);
        btnAdd.setBounds(16, 310, 285, 44);
        inputCard.add(btnAdd);
        btnAdd.addActionListener(e -> saveWeight());

        // === TABLE ===
        JPanel tableCard = Theme.createCard();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBounds(360, 68, 480, 530);
        add(tableCard);

        JLabel lblHist = new JLabel("  [=]  Lịch sử cân nặng");
        lblHist.setFont(Theme.FONT_HEADING);
        lblHist.setForeground(Theme.TEXT_PRIMARY);
        lblHist.setPreferredSize(new Dimension(480, 40));
        tableCard.add(lblHist, BorderLayout.NORTH);

        String[] cols = { "Ngày", "Cân nặng (kg)", "BMI", "Phân loại", "Ghi chú" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(Theme.BG_CARD);
        scroll.getViewport().setBackground(Theme.BG_CARD);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        tableCard.add(scroll, BorderLayout.CENTER);
    }

    private void updateBMI() {
        try {
            float w = Float.parseFloat(txtWeight.getText().trim());
            float h = user.getHeightCm() / 100f;
            if (h > 0) {
                float bmi = w / (h * h);
                lblBMI.setText(String.format("BMI: %.1f", bmi));
                WeightLog temp = new WeightLog();
                temp.setBmi(bmi);
                String cat = temp.getBmiCategory();
                lblCategory.setText("Phân loại: " + cat);
                lblBMI.setForeground(bmi < 18.5 ? Theme.ACCENT_CYAN
                        : bmi < 25 ? Theme.ACCENT_GREEN : bmi < 30 ? Theme.ACCENT_ORANGE : new Color(248, 113, 113));
            }
        } catch (NumberFormatException ex) {
            lblBMI.setText("BMI: —");
            lblCategory.setText("Phân loại: —");
        }
    }

    private void saveWeight() {
        try {
            float weight = Float.parseFloat(txtWeight.getText().trim());
            String date = txtDate.getText().trim();
            String note = txtNote.getText().trim();
            float h = user.getHeightCm() / 100f;
            float bmi = weight / (h * h);

            boolean ok = DAO.addWeight(user.getId(), weight, bmi, date, note);
            if (ok) {
                JOptionPane.showMessageDialog(this, "[v] Đã lưu cân nặng!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "[x] Lỗi khi lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cân nặng không hợp lệ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<WeightLog> list = DAO.getWeightHistory(user.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (WeightLog w : list) {
            tableModel.addRow(new Object[] {
                    sdf.format(w.getLogDate()),
                    w.getWeightKg() + " kg",
                    String.format("%.1f", w.getBmi()),
                    w.getBmiCategory(),
                    w.getNote() != null ? w.getNote() : ""
            });
        }
    }

    private void styleTable() {
        table.setFont(Theme.FONT_BODY);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setBackground(Theme.BG_CARD);
        table.setRowHeight(36);
        table.setGridColor(Theme.BORDER);
        table.setSelectionBackground(new Color(99, 102, 241, 80));
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.getTableHeader().setFont(Theme.FONT_SMALL.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(Theme.BG_CARD2);
        table.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        table.setShowGrid(true);

        // Color BMI category column
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r,
                    int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                String v = val != null ? val.toString() : "";
                lbl.setForeground(v.equals("Gầy") ? Theme.ACCENT_CYAN
                        : v.equals("Bình thường") ? Theme.ACCENT_GREEN
                                : v.equals("Thừa cân") ? Theme.ACCENT_ORANGE : new Color(248, 113, 113));
                lbl.setBackground(sel ? new Color(99, 102, 241, 80) : Theme.BG_CARD);
                return lbl;
            }
        });
    }

    private JTextField makeField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(Theme.FONT_BODY);
        tf.setForeground(Theme.TEXT_PRIMARY);
        tf.setBackground(Theme.BG_CARD2);
        tf.setCaretColor(Theme.TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        return tf;
    }

    private void addLabel(JPanel p, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setBounds(x, y, 285, 20);
        p.add(lbl);
    }
}
