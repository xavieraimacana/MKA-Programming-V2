package ec.espe.edu.coffeeshop.utils;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
public class ThemeManager {
    public static final Color COLOR_WHITE = new Color(255, 255, 255);
    public static final Color COLOR_BACKGROUND = new Color(248, 249, 250); 
    public static final Color COLOR_GOLD = new Color(212, 175, 55); 
    public static final Color COLOR_TEXT = new Color(32, 33, 36); 
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static void setupTheme() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            UIManager.put("Button.arc", 20); 
            UIManager.put("Component.arc", 10); 
            UIManager.put("CheckBox.arc", 5);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.innerFocusWidth", 1);
            UIManager.put("Button.margin", new Insets(8, 16, 8, 16)); 
            UIManager.put("Component.accentColor", "#D4AF37"); 
            UIManager.put("Component.focusColor", "#D4AF37");
            UIManager.put("Panel.background", COLOR_WHITE);
            UIManager.put("RootPane.background", COLOR_BACKGROUND);
            UIManager.put("Viewport.background", COLOR_WHITE); 
            UIManager.put("defaultFont", MAIN_FONT);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.gridColor", new Color(230, 230, 230));
            UIManager.put("Table.selectionBackground", COLOR_GOLD);
            UIManager.put("Table.selectionForeground", COLOR_WHITE);
            UIManager.put("Table.rowHeight", 35); 
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 15));
            UIManager.put("TableHeader.separatorColor", COLOR_WHITE);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize FlatLaf. Falling back to default.");
        }
    }
}
