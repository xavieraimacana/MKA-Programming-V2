package ec.espe.edu.coffeeshop.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class to securely hash and verify passwords using BCrypt.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public class PasswordHasher {

    /**
     * Hashes a plain text password using BCrypt.
     * 
     * @param password The plain text password.
     * @return The hashed password.
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifies a plain text password against a stored BCrypt hash.
     * Supports backward compatibility: returns false if the hash is null or is not in BCrypt format.
     * 
     * @param plainPassword The plain text password.
     * @param hashedPassword The stored hashed password.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
