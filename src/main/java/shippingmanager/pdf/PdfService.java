package shippingmanager.pdf;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Service
public class PdfService {


    public String reformatDate(LocalDateTime date, boolean appendTime) {
        if (appendTime) {
            return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(date);
        } else {
            return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
        }
    }

    public void addFooter(Document document) throws IOException {
        Paragraph paragraph = new Paragraph();

        Text footer = new Text("\u00a9 2020 Created by Forest Industry S.A.");
        footer.setFont(MyFont.getRegularFont());
        footer.setFontSize(8);

        paragraph.add(footer);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFixedPosition(8f, 10f, 580f);

        document.add(paragraph);
    }

    public void addSignaturesFields(Table table, String field1, String field2) throws IOException {
        table.setFixedPosition(48f, 30f, 500f);
        createSignatureCell(table, field1);

        Cell emptyCell = new Cell();
        emptyCell.setBorder(Border.NO_BORDER);
        table.addCell(emptyCell);

        createSignatureCell(table, field2);
    }

    private void createSignatureCell(Table table, String text) throws IOException {
        Cell cell = new Cell(0, 2);
        cell.add(text);
        cell.setFont(MyFont.getRegularFont());
        cell.setFontSize(8f);
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderTop(new SolidBorder(Color.BLACK, 1));
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
    }

}