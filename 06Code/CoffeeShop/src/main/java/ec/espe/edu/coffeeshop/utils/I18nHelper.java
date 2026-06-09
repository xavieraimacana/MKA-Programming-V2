package ec.espe.edu.coffeeshop.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility helper to manage dynamic system localization (English/Spanish).
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class I18nHelper {
    private static ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault());

    /**
     * Changes the current locale of the application.
     * 
     * @param locale The target locale (e.g., Locale.ENGLISH or new Locale("es")).
     */
    public static void setLocale(Locale locale) {
        messages = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Retrieves a localized message by its key.
     * 
     * @param key The property key.
     * @return The localized string, or the key itself if not found.
     */
    public static String getMessage(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key; // Retorna la llave como respaldo en caso de error
        }
    }
}
