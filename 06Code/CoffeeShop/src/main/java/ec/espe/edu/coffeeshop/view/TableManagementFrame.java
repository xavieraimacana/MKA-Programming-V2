package ec.espe.edu.coffeeshop.view;

import ec.espe.edu.coffeeshop.model.Reservation;
import ec.espe.edu.coffeeshop.model.Table;
import ec.espe.edu.coffeeshop.model.TableStatus;
import ec.espe.edu.coffeeshop.repository.MongoReservationRepository;
import ec.espe.edu.coffeeshop.repository.MongoTableRepository;
import ec.espe.edu.coffeeshop.utils.I18nHelper;
import ec.espe.edu.coffeeshop.utils.NavigationMenuHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Waiter/Staff GUI to monitor table allocation and make/check reservations (Light & Gold Theme).
 * Represents spatial mapping and state machines.
 * 
 * @author MKA Programmers, ESPE
 */
public class TableManagementFrame extends JFrame {
    private final MongoTableRepository tableRepo;
    private final MongoReservationRepository reservationRepo;
    private JPanel gridPanel;

    // Colors
    private final Color bgColor = new Color(250, 248, 245); // Warm White
    private final Color textColor = new Color(41, 37, 36); // Stone 800
    private final Color goldPrimary = new Color(197, 160, 89); // Gold
    private final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private final Color borderColor = new Color(224, 220, 214); // Sand Border

    // Table state colors
    private final Color freeColor = new Color(16, 185, 129); // Emerald 500
    private final Color occupiedColor = new Color(239, 68, 68); // Red 500
    private final Color reservedColor = new Color(245, 158, 11); // Amber 500

    public TableManagementFrame() {
        this.tableRepo = new MongoTableRepository();
        this.reservationRepo = new MongoReservationRepository();

        setTitle(I18nHelper.getMessage("menu.table_manage"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 1. Native JMenuBar Navigation
        setJMenuBar(NavigationMenuHelper.createMenuBar(this, "TABLES"));

        // 2. Add Content Panel to Center
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(bgColor);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Subheader Panel (Title and Legend side-by-side)
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(I18nHelper.getMessage("table.layout_title"));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(goldSecondary);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Legend panel
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        legendPanel.setOpaque(false);
        legendPanel.add(createLegendItem(I18nHelper.getMessage("table.status_free"), freeColor));
        legendPanel.add(createLegendItem(I18nHelper.getMessage("table.status_occupied"), occupiedColor));
        legendPanel.add(createLegendItem(I18nHelper.getMessage("table.status_reserved"), reservedColor));
        headerPanel.add(legendPanel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Grid Panel for tables
        gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        gridPanel.setBackground(bgColor);
        
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(bgColor);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        refreshTables();
    }

    private JPanel createLegendItem(String labelText, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        
        JPanel colorIndicator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        colorIndicator.setPreferredSize(new Dimension(12, 12));
        colorIndicator.setOpaque(false);
        p.add(colorIndicator);

        JLabel label = new JLabel(labelText);
        label.setForeground(textColor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(label);

        return p;
    }

    private void refreshTables() {
        gridPanel.removeAll();
        List<Table> tables = tableRepo.findAll();
        
        for (Table table : tables) {
            gridPanel.add(createTableWidget(table));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createTableWidget(Table table) {
        JPanel widget = new JPanel();
        widget.setLayout(new BorderLayout(10, 10));
        widget.setPreferredSize(new Dimension(160, 160));
        widget.setBackground(Color.WHITE);
        
        Color statusColor;
        String statusText;
        switch (table.getStatus()) {
            case FREE:
                statusColor = freeColor;
                statusText = I18nHelper.getMessage("table.status_free");
                break;
            case OCCUPIED:
                statusColor = occupiedColor;
                statusText = I18nHelper.getMessage("table.status_occupied");
                break;
            case RESERVED:
                statusColor = reservedColor;
                statusText = I18nHelper.getMessage("table.status_reserved");
                break;
            default:
                statusColor = Color.GRAY;
                statusText = I18nHelper.getMessage("table.status_unknown");
        }
        
        // Compound border: solid colored left border, sand border for other sides
        widget.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, statusColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        ));

        // Center Content: Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setOpaque(false);
        
        JLabel numLbl = new JLabel(I18nHelper.getMessage("table.number") + " #" + table.getNumber());
        numLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        numLbl.setForeground(textColor);
        infoPanel.add(numLbl);

        JLabel capLbl = new JLabel(I18nHelper.getMessage("table.capacity") + ": " + table.getCapacity() + " pax");
        capLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        capLbl.setForeground(new Color(120, 113, 108)); // Muted Gray
        infoPanel.add(capLbl);

        // Status Badge (Pill style)
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgePanel.setOpaque(false);
        JLabel badgeLabel = new JLabel(" " + statusText + " ");
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badgeLabel.setForeground(statusColor);
        badgeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(statusColor, 1),
            BorderFactory.createEmptyBorder(1, 4, 1, 4)
        ));
        badgePanel.add(badgeLabel);
        infoPanel.add(badgePanel);

        widget.add(infoPanel, BorderLayout.CENTER);

        // State Action Buttons
        JButton actionBtn = new JButton();
        actionBtn.setFocusPainted(false);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setBackground(goldPrimary);
        actionBtn.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        switch (table.getStatus()) {
            case FREE:
                actionBtn.setText(I18nHelper.getMessage("table.btn_reserve_occupy"));
                actionBtn.addActionListener(e -> handleFreeTableActions(table));
                break;
            case RESERVED:
                actionBtn.setText(I18nHelper.getMessage("table.btn_arrive_cancel"));
                actionBtn.addActionListener(e -> handleReservedTableActions(table));
                break;
            case OCCUPIED:
                actionBtn.setText(I18nHelper.getMessage("table.btn_free"));
                actionBtn.setBackground(new Color(120, 113, 108)); // Gray for freeing
                actionBtn.addActionListener(e -> updateTableStatus(table, TableStatus.FREE));
                break;
        }

        // Hover animation for action button
        final Color originalBg = actionBtn.getBackground();
        actionBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (table.getStatus() == TableStatus.OCCUPIED) {
                    actionBtn.setBackground(textColor);
                } else {
                    actionBtn.setBackground(goldSecondary);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                actionBtn.setBackground(originalBg);
            }
        });

        widget.add(actionBtn, BorderLayout.SOUTH);

        return widget;
    }

    private void updateTableStatus(Table table, TableStatus status) {
        table.setStatus(status);
        tableRepo.save(table);
        refreshTables();
    }

    private void handleFreeTableActions(Table table) {
        String[] options = {I18nHelper.getMessage("table.action_occupy"), I18nHelper.getMessage("table.action_reserve"), I18nHelper.getMessage("table.action_cancel")};
        int choice = JOptionPane.showOptionDialog(this,
                I18nHelper.getMessage("table.msg_select_action") + " #" + table.getNumber(),
                I18nHelper.getMessage("table.action_title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) { // Occupy Immediately
            updateTableStatus(table, TableStatus.OCCUPIED);
        } else if (choice == 1) { // Reserve
            String customerName = JOptionPane.showInputDialog(this, I18nHelper.getMessage("table.input_name"));
            if (customerName == null || customerName.trim().isEmpty()) return;

            String paxInput = JOptionPane.showInputDialog(this, I18nHelper.getMessage("table.input_pax") + " (" + table.getCapacity() + "):");
            if (paxInput == null || paxInput.trim().isEmpty()) return;

            try {
                int pax = Integer.parseInt(paxInput);
                if (pax > table.getCapacity()) {
                    JOptionPane.showMessageDialog(this, I18nHelper.getMessage("table.warn_capacity"), I18nHelper.getMessage("table.warn_title"), JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Crear reservacion
                Reservation reservation = new Reservation(
                        UUID.randomUUID().toString(),
                        table.getId(),
                        customerName.trim(),
                        LocalDateTime.now().plusHours(2), // Reservar por defecto dentro de 2 horas
                        pax
                );
                reservationRepo.save(reservation);

                // Actualizar mesa a reservada
                updateTableStatus(table, TableStatus.RESERVED);
                boolean es = I18nHelper.getLocale().getLanguage().equals("es");
                String msg = es ? "Mesa #" + table.getNumber() + " reservada exitosamente para: " + customerName
                                : "Table #" + table.getNumber() + " successfully reserved for: " + customerName;
                JOptionPane.showMessageDialog(this, msg, es ? "Éxito" : "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                boolean es = I18nHelper.getLocale().getLanguage().equals("es");
                JOptionPane.showMessageDialog(this, es ? "Formato de número inválido para comensales." : "Invalid number format for guests.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleReservedTableActions(Table table) {
        // Buscar reservacion activa para esta mesa
        List<Reservation> reservations = reservationRepo.findAll();
        Reservation active = null;
        for (Reservation res : reservations) {
            if (res.getTableId().equals(table.getId())) {
                active = res;
                break;
            }
        }

        String message = "This table is reserved.";
        if (active != null) {
            message = "Reserved for: " + active.getCustomerName() + "\nGuests: " + active.getNumberOfPeople() + "\nTime: " + active.getReservationTime();
        }

        String[] options = {"Arrived (Occupy)", "Cancel Reservation", "Back"};
        int choice = JOptionPane.showOptionDialog(this,
                message + "\n\nSelect action:",
                "Reserved Table Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) { // Arrived
            if (active != null) reservationRepo.delete(active.getId());
            updateTableStatus(table, TableStatus.OCCUPIED);
        } else if (choice == 1) { // Cancel
            if (active != null) reservationRepo.delete(active.getId());
            boolean es = I18nHelper.getLocale().getLanguage().equals("es");
            JOptionPane.showMessageDialog(this, es ? "Reserva cancelada. Mesa libre." : "Reservation cancelled. Table is free.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
