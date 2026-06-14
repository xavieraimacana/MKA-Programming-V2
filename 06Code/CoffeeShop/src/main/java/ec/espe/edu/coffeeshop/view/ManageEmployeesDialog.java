package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.repository.MongoEmployeeRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Administrative GUI to monitor employees (Light & Gold Theme).
 * 
 * @author MKA Programmers, ESPE
 */
public class ManageEmployeesDialog extends JDialog {
    private final MongoEmployeeRepository employeeRepo;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Stone 800
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    public ManageEmployeesDialog(Frame parent) {
        super(parent, I18nHelper.getMessage("menu.manage_emp"), true);
        this.employeeRepo = new MongoEmployeeRepository();

        setSize(700, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header
        JLabel titleLbl = new JLabel(I18nHelper.getMessage("emp.panel"), JLabel.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(goldSecondary);
        mainPanel.add(titleLbl, BorderLayout.NORTH);

        // Center Table
        tableModel = new DefaultTableModel(new Object[]{
                I18nHelper.getMessage("general.id"), 
                I18nHelper.getMessage("general.name"), 
                I18nHelper.getMessage("emp.role"), 
                I18nHelper.getMessage("emp.username"), 
                I18nHelper.getMessage("emp.must_change_pass")
            }, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setBackground(inputColor);
        employeeTable.setForeground(textColor);
        employeeTable.setGridColor(borderColor);
        employeeTable.getTableHeader().setBackground(new Color(245, 242, 237));
        employeeTable.getTableHeader().setForeground(textColor);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(bgColor);
        
        JButton btnDelete = createStyledButton(I18nHelper.getMessage("btn.delete_emp"), new Color(220, 38, 38));
        btnDelete.addActionListener(e -> {
            int row = employeeTable.getSelectedRow();
            if (row >= 0) {
                String id = tableModel.getValueAt(row, 0).toString();
                employeeRepo.delete(id);
                refreshTableData();
            }
        });
        controlPanel.add(btnDelete);

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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        List<Employee> employees = employeeRepo.findAll();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                emp.getId(),
                emp.getName(),
                emp.getRole().name(),
                emp.getUsername(),
                emp.isChangePasswordRequired() ? I18nHelper.getMessage("general.yes") : I18nHelper.getMessage("general.no")
            });
        }
    }
}
