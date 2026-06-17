package ec.espe.edu.coffeeshop.utils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class PdfGenerator {
    public static void generateReceipt(List<String[]> items, double total, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(200, 750);
            contentStream.showText("COFFEE SHOP RECEIPT");
            contentStream.endText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            contentStream.showText("Date: " + dtf.format(LocalDateTime.now()));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("----------------------------------------------------------------------------------");
            contentStream.endText();
            int yPosition = 650;
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (String[] item : items) {
                if (yPosition < 50) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    yPosition = 750;
                }
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                String name = item[0] != null ? item[0].replaceAll("[^\\x20-\\x7E]", "") : "";
                contentStream.showText(name);
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(450, yPosition);
                String price = item[1] != null ? item[1].replaceAll("[^\\x20-\\x7E]", "") : "";
                contentStream.showText(price);
                contentStream.endText();
                yPosition -= 20;
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("----------------------------------------------------------------------------------");
            contentStream.endText();
            yPosition -= 20;
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(350, yPosition);
            contentStream.showText("TOTAL: $" + String.format("%.2f", total));
            contentStream.endText();
            contentStream.close();
            document.save(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
