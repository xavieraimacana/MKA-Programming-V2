package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.EmployeeController;
import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class EmployeeView extends JFrame {
    private final EmployeeController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    public EmployeeView() {
        controller = new EmployeeController();
        setTitle("Gestión de Empleados - Ver Todos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        ((javax.swing.JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 10));
        String[] columns = {"ID", "Nombre", "Usuario", "Rol"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeManager.COLOR_WHITE);
        JLabel titleLabel = new JLabel("MÓDULO DE RRHH DE EMPLEADOS - SOLO LECTURA");
        titleLabel.setFont(ThemeManager.TITLE_FONT);
        titleLabel.setForeground(ThemeManager.COLOR_GOLD);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(ThemeManager.COLOR_WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        JTextField txtSearch = new JTextField(20);
        txtSearch.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Buscar...");
        txtSearch.putClientProperty(com.formdev.flatlaf.FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(txtSearch);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            private void search() {
                String text = txtSearch.getText();
                javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(tableModel);
                table.setRowSorter(sorter);
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        add(topPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(ThemeManager.COLOR_WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton btnAdd = new JButton("Abrir Ventana de Añadir");
        btnAdd.setBackground(ThemeManager.COLOR_GOLD);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.addActionListener(e -> new EmployeeAddView(this, controller).setVisible(true));
        JButton btnDelete = new JButton("Abrir Ventana de Eliminar");
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) tableModel.getValueAt(row, 0);
                if (controller.deleteEmployee(id)) {
                    JOptionPane.showMessageDialog(this, "Empleado eliminado");
                    loadTableData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar");
            }
        });
        JButton btnRefresh = new JButton("Actualizar Datos");
        btnRefresh.setBackground(ThemeManager.COLOR_GOLD);
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadTableData());
        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);
        add(actionPanel, BorderLayout.SOUTH);
        loadTableData();
    }
    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Employee> employees = controller.getAllEmployees();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    emp.getUsername(),
                    emp.getRole()
            });
        }
    }
}
