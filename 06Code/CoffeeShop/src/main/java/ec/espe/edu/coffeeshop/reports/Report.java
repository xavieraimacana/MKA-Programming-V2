package ec.espe.edu.coffeeshop.reports;

/**
 * Interface for generating and exporting system reports.
 * 
 * @author Kevin Albán, MKA Programer, @ESPE
 */
public interface Report {
    /**
     * Generates the report and displays it or saves it.
     */
    void generateReport();

    /**
     * Generates and returns the report content as formatted text.
     */
    String getReportText();
}
