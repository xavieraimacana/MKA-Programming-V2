package ec.espe.edu.coffeeshop.reports;

import java.io.IOException;

/**
 * Interface for generating and exporting system reports.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public interface Report {
    /**
     * Generates the report content as a formatted string.
     */
    String generate();

    /**
     * Exports the generated report to a physical text file.
     * 
     * @param filePath The absolute or relative path to save the report.
     * @throws IOException If file writing fails.
     */
    void exportToFile(String filePath) throws IOException;
}
