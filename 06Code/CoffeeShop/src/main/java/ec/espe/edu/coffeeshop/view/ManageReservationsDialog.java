package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Reservation;
import ec.espe.edu.coffeeshop.repository.MongoReservationRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Administrative GUI to monitor reservations.
 * 
 * @author MKA Programmers, ESPE
 */
public class ManageReservationsDialog extends JDialog {
    private final MongoReservationRepository reservationRepo;
    private JTable resTable;
    private DefaultTableModel tableModel;

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color inputColor = new Color(255, 255, 255); // Pure White
    private final Color textColor = new Color(41, 37, 36); // Stone 800
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    public ManageReservationsDialog(Frame parent) {
        super(parent, I18nHelper.getMessage("menu.manage_res"), true);
        this.reservationRepo = new MongoReservationRepository();

        setSize(700, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header
        JLabel titleLbl = new JLabel(I18nHelper.getMessage("res.panel"), JLabel.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(goldSecondary);
        mainPanel.add(titleLbl, BorderLayout.NORTH);

        // Center Table
        tableModel = new DefaultTableModel(new Object[]{
                I18nHelper.getMessage("general.id"),
                I18nHelper.getMessage("res.table_id"),
                I18nHelper.getMessage("res.client"),
                I18nHelper.getMessage("res.datetime"),
                I18nHelper.getMessage("res.guests")
            }, 0);
        resTable = new JTable(tableModel);
        resTable.setBackground(inputColor);
        resTable.setForeground(textColor);
        resTable.setGridColor(borderColor);
        resTable.getTableHeader().setBackground(new Color(245, 242, 237));
        resTable.getTableHeader().setForeground(textColor);

        JScrollPane scrollPane = new JScrollPane(resTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(bgColor);
        
        JButton btnDelete = createStyledButton(I18nHelper.getMessage("btn.cancel_res"), new Color(220, 38, 38));
        btnDelete.addActionListener(e -> {
            int row = resTable.getSelectedRow();
            if (row >= 0) {
                String id = tableModel.getValueAt(row, 0).toString();
                reservationRepo.delete(id);
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
        List<Reservation> list = reservationRepo.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Reservation r : list) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getTableId(),
                r.getCustomerName(),
                r.getReservationTime().format(formatter),
                r.getNumberOfPeople()
            });
        }
    }
}
