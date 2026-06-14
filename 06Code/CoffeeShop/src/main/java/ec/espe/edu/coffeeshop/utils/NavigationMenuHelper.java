package ec.espe.edu.coffeeshop.utils;

import ec.espe.edu.coffeeshop.model.Employee;
import ec.espe.edu.coffeeshop.model.EmployeeRole;
import ec.espe.edu.coffeeshop.reports.InventoryReport;
import ec.espe.edu.coffeeshop.reports.SalesReport;
import ec.espe.edu.coffeeshop.reports.ZReport;
import ec.espe.edu.coffeeshop.utils.I18nHelper;
import ec.espe.edu.coffeeshop.view.*;

import javax.swing.*;
import java.awt.*;

/**
 * Utility helper to build a consistent JMenuBar (native Java Swing menus)
 * across all operational frames, styled in the Light & Gold theme.
 * 
 * @author MKA Programmers, ESPE
 */
public class NavigationMenuHelper {

    // Theme Colors
    private static final Color menuBg = Color.WHITE;
    private static final Color textColor = new Color(41, 37, 36); // Stone 800
    private static final Color goldPrimary = new Color(197, 160, 89); // Gold
    private static final Color goldSecondary = new Color(163, 128, 62); // Darker Gold
    private static final Color borderColor = new Color(224, 220, 214); // Sand Border
    private static final Color logoutRed = new Color(220, 38, 38); // Red 600

    // Fonts
    private static final Font menuFont = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font itemFont = new Font("Segoe UI", Font.PLAIN, 12);

    public static JMenuBar createMenuBar(JFrame currentFrame, String activeTab) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(menuBg);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));

        // 1. Left Side: Brand Logo Menu
        JMenu brandMenu = new JMenu("MKA COFFEE");
        brandMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brandMenu.setForeground(goldSecondary);
        menuBar.add(brandMenu);

        // 2. Navigation Menus
        JMenu opsMenu = new JMenu(I18nHelper.getMessage("menu.operations"));
        opsMenu.setFont(menuFont);
        opsMenu.setForeground(textColor);

        JMenuItem posItem = createStyledMenuItem(I18nHelper.getMessage("menu.pos_terminal"));
        posItem.addActionListener(e -> {
            currentFrame.dispose();
            Employee current = SystemContext.getInstance().getCurrentUser();
            if (current == null) {
                current = new Employee("temp", "Admin Cashier", EmployeeRole.CASHIER, "admin", "123", false);
            }
            new PosFrame(current).setVisible(true);
        });
        opsMenu.add(posItem);

        JMenuItem kdsItem = createStyledMenuItem(I18nHelper.getMessage("menu.kds_screen"));
        kdsItem.addActionListener(e -> {
            currentFrame.dispose();
            new KdsFrame(SystemContext.getInstance().getKitchenManager()).setVisible(true);
        });
        opsMenu.add(kdsItem);

        JMenuItem tablesItem = createStyledMenuItem(I18nHelper.getMessage("menu.table_manage"));
        tablesItem.addActionListener(e -> {
            currentFrame.dispose();
            new TableManagementFrame().setVisible(true);
        });
        opsMenu.add(tablesItem);

        menuBar.add(opsMenu);

        // Backoffice Menu (Only visible to certain roles, but the user requested "el admin tiene que poder usar todo")
        Employee currentEmp = SystemContext.getInstance().getCurrentUser();
        boolean isAdmin = (currentEmp != null && currentEmp.getRole() == EmployeeRole.MANAGER);

        if (isAdmin) {
            JMenu backofficeMenu = new JMenu(I18nHelper.getMessage("menu.backoffice"));
            backofficeMenu.setFont(menuFont);
            backofficeMenu.setForeground(textColor);

            JMenuItem inventoryItem = createStyledMenuItem(I18nHelper.getMessage("menu.manage_inv"));
            inventoryItem.addActionListener(e -> new ManageInventoryDialog(currentFrame).setVisible(true));
            backofficeMenu.add(inventoryItem);

            JMenuItem equipmentItem = createStyledMenuItem(I18nHelper.getMessage("menu.monitor_eq"));
            equipmentItem.addActionListener(e -> new ManageEquipmentDialog(currentFrame).setVisible(true));
            backofficeMenu.add(equipmentItem);
            
            JMenuItem employeesItem = createStyledMenuItem(I18nHelper.getMessage("menu.manage_emp"));
            employeesItem.addActionListener(e -> new ManageEmployeesDialog(currentFrame).setVisible(true));
            backofficeMenu.add(employeesItem);
            
            JMenuItem reservationsItem = createStyledMenuItem(I18nHelper.getMessage("menu.manage_res"));
            reservationsItem.addActionListener(e -> new ManageReservationsDialog(currentFrame).setVisible(true));
            backofficeMenu.add(reservationsItem);

            menuBar.add(backofficeMenu);

            // Reports Menu
            JMenu reportsMenu = new JMenu(I18nHelper.getMessage("menu.reports"));
            reportsMenu.setFont(menuFont);
            reportsMenu.setForeground(textColor);

            JMenuItem zReportItem = createStyledMenuItem(I18nHelper.getMessage("menu.z_report"));
            zReportItem.addActionListener(e -> runReport(currentFrame, new ZReport()));
            reportsMenu.add(zReportItem);

            JMenuItem invReportItem = createStyledMenuItem(I18nHelper.getMessage("menu.inv_report"));
            invReportItem.addActionListener(e -> runReport(currentFrame, new InventoryReport()));
            reportsMenu.add(invReportItem);

            JMenuItem salesReportItem = createStyledMenuItem(I18nHelper.getMessage("menu.sales_report"));
            salesReportItem.addActionListener(e -> runReport(currentFrame, new SalesReport()));
            reportsMenu.add(salesReportItem);

            menuBar.add(reportsMenu);
        }

        // 3. Spacing Glue
        menuBar.add(Box.createHorizontalGlue());

        // 4. Right Side: Employee Profile Menu
        String name = (currentEmp != null) ? currentEmp.getName() : "Guest";
        String role = (currentEmp != null) ? currentEmp.getRole().name() : "TEST";

        JMenu userMenu = new JMenu(name + " (" + role.toLowerCase() + ")");
        userMenu.setFont(menuFont);
        userMenu.setForeground(textColor);

        JMenuItem logoutItem = new JMenuItem(I18nHelper.getMessage("menu.logout"));
        logoutItem.setFont(itemFont);
        logoutItem.setForeground(logoutRed);
        logoutItem.setBackground(menuBg);
        logoutItem.addActionListener(e -> {
            currentFrame.dispose();
            SystemContext.getInstance().setCurrentUser(null);
            new LoginFrame().setVisible(true);
        });
        userMenu.add(logoutItem);

        menuBar.add(userMenu);

        return menuBar;
    }

    private static JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(itemFont);
        item.setBackground(menuBg);
        item.setForeground(textColor);
        return item;
    }
    
    private static void runReport(JFrame parent, ec.espe.edu.coffeeshop.reports.Report report) {
        report.generateReport();
        new ReportViewerDialog(parent, report.getClass().getSimpleName(), report).setVisible(true);
    }
}
