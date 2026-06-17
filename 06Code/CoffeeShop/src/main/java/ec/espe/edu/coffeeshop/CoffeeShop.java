package ec.espe.edu.coffeeshop;
import ec.espe.edu.coffeeshop.utils.ThemeManager;
import ec.espe.edu.coffeeshop.utils.MongoDBConnection;
import ec.espe.edu.coffeeshop.utils.DatabaseSeeder;
import javax.swing.SwingUtilities;
public class CoffeeShop {
    public static void main(String[] args) {
        System.out.println("Connecting to database...");
        MongoDBConnection.getDatabase(); 
        DatabaseSeeder.seed();
        System.setProperty("flatlaf.useNativeLibrary", "false");
        System.setProperty("flatlaf.useNativeWindowDecorations", "false");
        ThemeManager.setupTheme();
        SwingUtilities.invokeLater(() -> {
            ec.espe.edu.coffeeshop.view.LoginView loginView = new ec.espe.edu.coffeeshop.view.LoginView();
            loginView.setVisible(true);
        });
    }
}
