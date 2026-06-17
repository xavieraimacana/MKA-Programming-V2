package ec.espe.edu.coffeeshop.view;
import ec.espe.edu.coffeeshop.controller.StockEntryController;
import ec.espe.edu.coffeeshop.model.StockEntry;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class StockEntryView extends JFrame {
    private final StockEntryController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public StockEntryView() {
        controller = new StockEntryController();
        setTitle("Gestión de Entradas de Stock - Ver Todos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.COLOR_BACKGROUND);
        ((javax.swing.JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(10, 10));
        String[] columns = {"ID Entrada", "Fecha", "Cantidad", "Unidad", "Costo Total", "Costo Unitario", "Artículo"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ThemeManager.COLOR_WHITE);
        JLabel titleLabel = new JLabel("ENTRADAS DE STOCK - SÓLO LECTURA");
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
        btnAdd.addActionListener(e -> new StockEntryAddView(this, controller).setVisible(true));
        JButton btnRefresh = new JButton("Actualizar Datos");
        btnRefresh.setBackground(ThemeManager.COLOR_GOLD);
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadTableData());
        actionPanel.add(btnAdd);
        actionPanel.add(btnRefresh);
        add(actionPanel, BorderLayout.SOUTH);
        loadTableData();
    }
    public void loadTableData() {
        tableModel.setRowCount(0);
        List<StockEntry> entries = controller.getAllStockEntries();
        for (StockEntry entry : entries) {
            tableModel.addRow(new Object[]{
                    entry.getEntryId(),
                    entry.getDateReceived() != null ? entry.getDateReceived().format(formatter) : "",
                    String.format("%.2f", entry.getQuantityReceived()),
                    entry.getUnitReceived().toString(),
                    String.format("$%.2f", entry.getTotalCost()),
                    String.format("$%.2f", entry.calculateUnitCost()),
                    entry.getItem() != null ? entry.getItem().getName() : "N/A"
            });
        }
    }
}
