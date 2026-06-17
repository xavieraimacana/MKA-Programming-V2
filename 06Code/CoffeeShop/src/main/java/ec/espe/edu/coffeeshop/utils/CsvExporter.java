package ec.espe.edu.coffeeshop.utils;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.FileWriter;
import java.io.IOException;
public class CsvExporter {
    public static void exportTableToCsv(JTable table, String filePath) {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            TableModel model = table.getModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                csvWriter.append(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        String strValue = value.toString();
                        if (strValue.contains(",") || strValue.contains("\"") || strValue.contains("\n")) {
                            strValue = "\"" + strValue.replace("\"", "\"\"") + "\"";
                        }
                        csvWriter.append(strValue);
                    }
                    if (j < model.getColumnCount() - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error exporting to CSV: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
