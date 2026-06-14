package ec.espe.edu.coffeeshop;

import ec.espe.edu.coffeeshop.model.*;
import ec.espe.edu.coffeeshop.repository.*;
import ec.espe.edu.coffeeshop.utils.PasswordHasher;
import ec.espe.edu.coffeeshop.view.LoginFrame;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main entry point for the Coffeeshop Management System.
 * Connects to MongoDB, seeds initial database records, and launches the GUI.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class CoffeeShop {

    public static void main(String[] args) {
        // 1. Connect and seed MongoDB collections via DatabaseSeeder
        ec.espe.edu.coffeeshop.utils.DatabaseSeeder.seed();

        // 2. Launch GUI
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fallback Look & Feel
            }
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
