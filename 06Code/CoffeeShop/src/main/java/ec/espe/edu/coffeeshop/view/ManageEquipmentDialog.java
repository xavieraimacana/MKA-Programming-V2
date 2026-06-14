package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Equipment;
import ec.espe.edu.coffeeshop.model.EquipmentStatus;
import ec.espe.edu.coffeeshop.repository.MongoEquipmentRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Administrative GUI to monitor and change the status of espresso machines, grinders, etc. (Light & Gold Theme).
 * Relates to FR-11 machinery blocks.
 * 
 * @author MKA Programmers, ESPE
 */
public class ManageEquipmentDialog extends JDialog {
    private final MongoEquipmentRepository equipmentRepo;
    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Stone 800
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    public ManageEquipmentDialog(Frame parent) {
        super(parent, I18nHelper.getMessage("menu.monitor_eq"), true);
        this.equipmentRepo = new MongoEquipmentRepository();

        setSize(700, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header
        JLabel titleLbl = new JLabel(I18nHelper.getMessage("eq.panel"), JLabel.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(goldSecondary);
        mainPanel.add(titleLbl, BorderLayout.NORTH);

        // Center Table
        tableModel = new DefaultTableModel(new Object[]{
                I18nHelper.getMessage("general.id"), 
                I18nHelper.getMessage("eq.name"), 
                I18nHelper.getMessage("eq.status"), 
                I18nHelper.getMessage("eq.last_maint")
            }, 0);
        equipmentTable = new JTable(tableModel);
        equipmentTable.setBackground(inputColor);
        equipmentTable.setForeground(textColor);
        equipmentTable.setGridColor(borderColor);
        equipmentTable.getTableHeader().setBackground(new Color(245, 242, 237));
        equipmentTable.getTableHeader().setForeground(textColor);

        JScrollPane scrollPane = new JScrollPane(equipmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.weightx = 1.0;

        JButton btnOn = createStyledButton(I18nHelper.getMessage("btn.turn_on"), new Color(5, 150, 105));
        btnOn.addActionListener(e -> updateStatus(EquipmentStatus.OPERATIONAL));
        gbc.gridx = 0;
        controlPanel.add(btnOn, gbc);

        JButton btnOff = createStyledButton(I18nHelper.getMessage("btn.turn_off"), new Color(220, 38, 38));
        btnOff.addActionListener(e -> updateStatus(EquipmentStatus.BROKEN));
        gbc.gridx = 1;
        controlPanel.add(btnOff, gbc);

        JButton btnMaint = createStyledButton(I18nHelper.getMessage("btn.maint"), new Color(217, 119, 6));
        btnMaint.addActionListener(e -> updateStatus(EquipmentStatus.MAINTENANCE_REQUIRED));
        gbc.gridx = 2;
        controlPanel.add(btnMaint, gbc);

        JButton btnMaintDate = createStyledButton(I18nHelper.getMessage("btn.record_maint"), new Color(120, 113, 108));
        btnMaintDate.addActionListener(e -> recordMaintenance());
        gbc.gridx = 3;
        controlPanel.add(btnMaintDate, gbc);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        refreshTableData();
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(0, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        List<Equipment> equipments = equipmentRepo.findAll();
        for (Equipment eq : equipments) {
            String dateStr = (eq.getLastMaintenanceDate() != null) ? dateFormat.format(eq.getLastMaintenanceDate()) : I18nHelper.getMessage("eq.never");
            tableModel.addRow(new Object[]{
                eq.getId(),
                eq.getName(),
                eq.getStatus().name(),
                dateStr
            });
        }
    }

    private void updateStatus(EquipmentStatus status) {
        int row = equipmentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, I18nHelper.getMessage("eq.warn_select"), I18nHelper.getMessage("eq.warn_title"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(row, 0).toString();
        Equipment eq = equipmentRepo.findById(id);
        if (eq != null) {
            eq.setStatus(status);
            equipmentRepo.save(eq);
            refreshTableData();
            JOptionPane.showMessageDialog(this, java.text.MessageFormat.format(I18nHelper.getMessage("eq.success_status"), status, eq.getName()), I18nHelper.getMessage("eq.success_title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void recordMaintenance() {
        int row = equipmentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, I18nHelper.getMessage("eq.warn_select"), I18nHelper.getMessage("eq.warn_title"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(row, 0).toString();
        Equipment eq = equipmentRepo.findById(id);
        if (eq != null) {
            eq.setLastMaintenanceDate(new Date());
            equipmentRepo.save(eq);
            refreshTableData();
            JOptionPane.showMessageDialog(this, java.text.MessageFormat.format(I18nHelper.getMessage("eq.success_maint"), eq.getName()), I18nHelper.getMessage("eq.success_title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
