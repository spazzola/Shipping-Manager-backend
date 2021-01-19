package shippingmanager.pdf;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.AllArgsConstructor;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;
import shippingmanager.company.Company;
import shippingmanager.invoice.Invoice;
import shippingmanager.invoice.InvoiceDao;
import shippingmanager.order.Order;
import shippingmanager.utility.phonenumber.PhoneNumber;

import java.io.FileOutputStream;
import java.io.IOException;

@AllArgsConstructor
@Service
public class PdfInvoiceService {

    private final InvoiceDao invoiceDao;


    //TODO add to method parameters Long invoiceId
    public void generatePdf() throws Exception {
        PdfFontFactory.registerDirectory("src/main/resources/fonts/");

        Invoice invoice = invoiceDao.findById(1L)
                .orElseThrow(Exception::new);

        String pdfName = generatePdfName(invoice);

        PdfWriter writer = new PdfWriter(pdfName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        //PdfWriter.getInstance(document, new FileOutputStream(pdfName));


        Table firstTable = new Table(2);
        addCompanyLogo(firstTable);
        addIssuedDateAndPlace(firstTable, invoice);
        document.add(firstTable);

        addInvoiceNumber(document, invoice);

        Table secondTable = new Table(2);
        addGivenAndReceivedByInfo(secondTable, invoice.getOrder());
        document.add(secondTable);

        document.close();
    }

    private String generatePdfName(Invoice invoice) {
        String invoiceNumber = invoice.getOrder().getOrderNumber().replace("/", "-");
        return "Faktura" + invoiceNumber + ".pdf";
    }

    private void addCompanyLogo(Table table) throws IOException {
        Cell cell = new Cell();
        ImageData imageData = ImageDataFactory.create("src/main/resources/pics/logo.png");
        Image pdfImage = new Image(imageData);
        cell.setBorder(Border.NO_BORDER);
        cell.add(pdfImage);
        table.addCell(cell);
    }

    private void addIssuedDateAndPlace(Table table, Invoice invoice) throws IOException {
        Cell cell = new Cell();

        Paragraph placeParagraph = addToCellIssuedInfo("Miejsce wystawienia: ", invoice.getIssuedIn());
        Paragraph dateParagraph = addToCellIssuedInfo("Data wystawienia: ", invoice.getIssuedDate().toString().replace("T", " "));

        cell.add(placeParagraph);
        cell.add(dateParagraph);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
    }

    private Paragraph addToCellIssuedInfo(String header, String description) throws IOException {
        Paragraph paragraph = new Paragraph();

        Text textHeader = new Text(header);
        textHeader.setFont(MyFont.getBolderFont());
        textHeader.setFontSize(8);
        paragraph.add(textHeader);

        Text textDescription = new Text(description);
        textDescription.setFont(MyFont.getRegularFont());
        textDescription.setFontSize(8);
        paragraph.add(textDescription);

        paragraph.setTextAlignment(TextAlignment.RIGHT);

        return paragraph;
    }

    private void addInvoiceNumber(Document document, Invoice invoice) throws IOException {
        Paragraph paragraph = new Paragraph();

        Text invoiceNumber = new Text("Faktura VAT nr: " + invoice.getInvoiceNumber());
        invoiceNumber.setFont(MyFont.getBolderFont());
        invoiceNumber.setFontSize(20);

        paragraph.add(invoiceNumber);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setMarginTop(30f);

        document.add(paragraph);
    }

    private void addGivenAndReceivedByInfo(Table table, Order order) throws IOException {
        table.setMarginTop(30);

        addMerchantsHeaders(table, "Sprzedawca");
        addMerchantsHeaders(table, "Nabywca");

        addMerchantInfo(table, order.getGivenBy());
        addMerchantInfo(table, order.getReceivedBy());

    }

    private void addMerchantsHeaders(Table table, String header) throws IOException {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph();
        paragraph.add(header);
        paragraph.setFont(MyFont.getBolderFont());
        cell.add(paragraph);
        cell.setBorderTop(Border.NO_BORDER);
        cell.setBorderLeft(Border.NO_BORDER);
        cell.setBorderRight(Border.NO_BORDER);
        table.addCell(cell);
    }

    private void addMerchantInfo(Table table, Company company) throws IOException {
        Cell cell = new Cell();
/*
        Text companyName = new Text(company.getCompanyName() + "\n");
        Text street = new Text("ul. " + company.getAddress().getStreet() + " " + company.getAddress().getHouseNumber() + "\n");
        Text postalInfo = new Text(company.getAddress().getPostalCode() + " " + company.getAddress().getCity() + "\n");
        Text nip = new Text("NIP: " + company.getNip() + "\n");
        Text email = new Text("email: " + company.getEmail() + "\n");
*/

        Paragraph paragraph = new Paragraph();
        paragraph.add(company.getCompanyName() + "\n");
        paragraph.add("ul. " + company.getAddress().getStreet() + " " + company.getAddress().getHouseNumber() + "\n");
        paragraph.add(company.getAddress().getPostalCode() + " " + company.getAddress().getCity() + "\n");
        paragraph.add("NIP: " + company.getNip() + "\n");
        paragraph.add("email: " + company.getEmail() + "\n");

        for (PhoneNumber phoneNumber : company.getPhoneNumbers()) {
            Text phone = new Text(phoneNumber.getType() + ": " + phoneNumber.getNumber() + "\n");
            paragraph.add(phone);
        }

        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(8);

        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);

        table.addCell(cell);
    }

}