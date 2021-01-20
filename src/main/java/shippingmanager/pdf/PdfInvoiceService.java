package shippingmanager.pdf;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DottedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.company.Company;
import shippingmanager.invoice.Invoice;
import shippingmanager.invoice.InvoiceDao;
import shippingmanager.order.Order;
import shippingmanager.utility.bankaccount.BankAccount;
import shippingmanager.utility.numberconverter.NumberConverter;
import shippingmanager.utility.phonenumber.PhoneNumber;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        Table firstTable = new Table(2);
        addCompanyLogo(firstTable);
        addIssuedDateAndPlace(firstTable, invoice);
        document.add(firstTable);

        addInvoiceNumber(document, invoice);

        Table secondTable = new Table(2);
        addGivenAndReceivedByInfo(secondTable, invoice.getOrder());
        document.add(secondTable);

        Table thirdTable = new Table(24);
        addProductsAndValuesInfo(thirdTable, invoice);
        document.add(thirdTable);

        Table fourthTable = new Table(4);
        addFinalValuesInfo(fourthTable, invoice);
        document.add(fourthTable);

        Table fifthTable = new Table(2);
        addPaymentInfo(fifthTable, invoice);
        document.add(fifthTable);

        Table sixthTable = new Table(2);
        addPaymentValueInWords(sixthTable, invoice);
        document.add(sixthTable);

        Table seventhTable = new Table(5);
        addSignaturesFields(seventhTable);
        document.add(seventhTable);

        addFooter(document);

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

        String formattedDate = reformatDate(invoice.getIssuedDate());

        Paragraph placeParagraph = addToCellIssuedInfo("Miejsce wystawienia: ", invoice.getIssuedIn());
        Paragraph dateParagraph = addToCellIssuedInfo("Data wystawienia: ", formattedDate);

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
        paragraph.setMarginTop(20f);

        document.add(paragraph);
    }

    private void addGivenAndReceivedByInfo(Table table, Order order) throws IOException {
        table.setMarginTop(30);

        addMerchantsHeaders(table, "Sprzedawca");
        addMerchantsHeaders(table, "Nabywca");

        //TODO switch givenBy with receivedBy?
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

        Paragraph paragraph = new Paragraph();
        paragraph.add(company.getCompanyName() + "\n");
        paragraph.add("ul. " + company.getAddress().getStreet() + " " + company.getAddress().getHouseNumber() + "\n");
        paragraph.add(company.getAddress().getPostalCode() + " " + company.getAddress().getCity() + "\n");
        paragraph.add("NIP: " + company.getNip() + "\n");
        paragraph.add("Email: " + company.getEmail() + "\n");

        for (PhoneNumber phoneNumber : company.getPhoneNumbers()) {
            Text phone = new Text(phoneNumber.getType() + ": " + phoneNumber.getNumber() + "\n");
            paragraph.add(phone);
        }

        for (BankAccount bankAccount : company.getBankAccounts()) {
            paragraph.add("Bank: " + bankAccount.getAccountName() + "\n");
            paragraph.add("Nr konta: " + bankAccount.getAccountNumber() + "\n");
        }

        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(8);

        cell.setBorder(Border.NO_BORDER);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private void addProductsAndValuesInfo(Table table, Invoice invoice) throws IOException, DocumentException {
        table.setMarginTop(30f);

        addHeaderCellsToThirdTable(table);
        addDescriptionCellsToThirdTable(table, invoice);
    }

    private void addHeaderCellsToThirdTable(Table table) throws IOException {
        PdfFont boldFont = MyFont.getBolderFont();
        Color grey = new DeviceRgb(217, 215, 212);

        Cell counter = createCell("Lp", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(counter);

        Cell productName = createCell("Nazwa towaru/uslugi", boldFont, grey, TextAlignment.CENTER,8);
        table.addCell(productName);

        Cell quantity = createCell("Ilość", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(quantity);

        Cell measureUnit = createCell("Jm", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(measureUnit);

        Cell priceWithoutTax = createCell("Cena netto", boldFont, grey, TextAlignment.CENTER,3);
        table.addCell(priceWithoutTax);

        Cell percentageTaxValue = createCell("VAT", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(percentageTaxValue);

        Cell totalPriceWithoutTax = createCell("Kwota netto", boldFont, grey, TextAlignment.CENTER, 3);
        table.addCell(totalPriceWithoutTax);

        Cell totalTaxValue = createCell("Kwota VAT", boldFont, grey, TextAlignment.CENTER, 3);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell("Kwota brutto", boldFont, grey, TextAlignment.CENTER, 3);
        table.addCell(totalValueWithTax);
    }

    private Cell createCell(String text, PdfFont font, Color color, TextAlignment alignment, int colspan) throws IOException {
        Cell cell = new Cell(0, colspan);
        setCellProperties(cell, font, color, alignment);
        cell.add(new Paragraph(text));

        return cell;
    }

    private void setCellProperties(Cell cell, PdfFont font, Color color, TextAlignment alignment) throws IOException {
        cell.setBackgroundColor(color);
        cell.setFont(font).setFontSize(8);
        cell.setTextAlignment(alignment);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private void addDescriptionCellsToThirdTable(Table table, Invoice invoice) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        Color white = Color.WHITE;

        Cell counter = createCell("1", regularFont, white, TextAlignment.CENTER,0);
        table.addCell(counter);

        Cell productName = createCell(invoice.getProductName(), regularFont, white, TextAlignment.LEFT,8);
        table.addCell(productName);

        Cell quantity = createCell("1", regularFont, white, TextAlignment.CENTER,0);
        table.addCell(quantity);

        Cell measureUnit = createCell("szt", regularFont, white, TextAlignment.CENTER,0);
        table.addCell(measureUnit);

        Cell priceWithoutTax = createCell(invoice.getValueWithoutTax().toString(), regularFont, white, TextAlignment.RIGHT,3);
        table.addCell(priceWithoutTax);

        Cell percentageTaxValue = createCell("23%", regularFont, white, TextAlignment.CENTER,0);
        table.addCell(percentageTaxValue);

        Cell totalPriceWithoutTax = createCell(invoice.getValueWithoutTax().toString(), regularFont, white, TextAlignment.RIGHT,3);
        table.addCell(totalPriceWithoutTax);

        BigDecimal taxValue = invoice.getValueWithTax().subtract(invoice.getValueWithoutTax());
        Cell totalTaxValue = createCell(taxValue.toString(), regularFont, white, TextAlignment.RIGHT,3);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell(invoice.getValueWithTax().toString(), regularFont, white, TextAlignment.RIGHT,3);
        table.addCell(totalValueWithTax);
    }

    private void addFinalValuesInfo(Table table, Invoice invoice) throws IOException {
        table.setMarginTop(20f);
        table.setWidth(260f);
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        addHeaderCellsToFourthTable(table);
        addDescriptionCellsToFourthTable(table, invoice);
        addSummarizeDescription(table, invoice);
    }

    private void addHeaderCellsToFourthTable(Table table) throws IOException {
        PdfFont boldFont = MyFont.getBolderFont();
        Color grey = new DeviceRgb(217, 215, 212);

        Cell percentageTaxValue = createCell("Stawka VAT", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(percentageTaxValue);

        Cell priceWithoutTax = createCell("Netto", boldFont, grey, TextAlignment.CENTER,0);
        table.addCell(priceWithoutTax);

        Cell totalTaxValue = createCell("Kwota VAT", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell("Brutto", boldFont, grey, TextAlignment.CENTER, 3);
        table.addCell(totalValueWithTax);
    }

    private void addDescriptionCellsToFourthTable(Table table, Invoice invoice) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        Color white = Color.WHITE;

        Cell percentageTaxValue = createCell("23%", regularFont, white, TextAlignment.CENTER,0);
        table.addCell(percentageTaxValue);

        Cell priceWithoutTax = createCell(invoice.getValueWithoutTax().toString(), regularFont, white, TextAlignment.RIGHT,0);
        table.addCell(priceWithoutTax);

        BigDecimal taxValue = invoice.getValueWithTax().subtract(invoice.getValueWithoutTax());
        Cell totalTaxValue = createCell(taxValue.toString(), regularFont, white, TextAlignment.RIGHT,0);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell(invoice.getValueWithTax().toString(), regularFont, white, TextAlignment.RIGHT,0);
        table.addCell(totalValueWithTax);
    }

    private void addSummarizeDescription(Table table, Invoice invoice) throws IOException {
        PdfFont boldFont = MyFont.getBolderFont();
        Color grey = new DeviceRgb(217, 215, 212);

        Cell summarize = createCell("Razem", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(summarize);

        Cell priceWithoutTax = createCell(invoice.getValueWithoutTax().toString(), boldFont, grey, TextAlignment.RIGHT, 0);
        table.addCell(priceWithoutTax);

        BigDecimal taxValue = invoice.getValueWithTax().subtract(invoice.getValueWithoutTax());
        Cell totalTaxValue = createCell(taxValue.toString(), boldFont, grey, TextAlignment.RIGHT, 0);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell(invoice.getValueWithTax().toString(), boldFont, grey, TextAlignment.RIGHT, 0);
        table.addCell(totalValueWithTax);
    }

    private void addPaymentInfo(Table table, Invoice invoice) throws IOException {
        Color white = Color.WHITE;

        table.setMarginTop(20f);
        table.setWidth(260f);
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        addFirstRow(table, invoice, white);
        addSecondRow(table, invoice, white);
        addThirdRow(table, invoice, white);
        addFourthRow(table, invoice, white);
    }

    private void addFirstRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBolderFont();

        Cell cell1 = createCell("Sposób zapłaty: ", regularFont, color, TextAlignment.RIGHT, 0);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Cell cell2 = createCell(invoice.getPaymentMethod(), boldFont, color, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);

    }

    private void addSecondRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBolderFont();

        Cell cell1 = createCell("Termin zapłaty: ", regularFont, color, TextAlignment.RIGHT, 0);
        cell1.setVerticalAlignment(VerticalAlignment.TOP);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Long daysTillPayment = Long.valueOf(invoice.getOrder().getDaysTillPayment());
        LocalDateTime paymentDay = invoice.getIssuedDate().plusDays(daysTillPayment);
        String formattedPaymentDay = reformatDate(paymentDay);
        String paymentDateInfo = daysTillPayment + " dni\n" + formattedPaymentDay;

        Cell cell2 = createCell(paymentDateInfo, boldFont, color, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);
    }

    private void addThirdRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBolderFont();

        Cell cell1 = createCell("Zapłacono: ", regularFont, color, TextAlignment.RIGHT, 0);
        cell1.setVerticalAlignment(VerticalAlignment.TOP);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Cell cell2 = createCell("0.00", boldFont, color, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);
    }

    private void addFourthRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont boldFont = MyFont.getBolderFont();
        String toPayment = "Do zapłaty: " + invoice.getValueWithTax() + " " + invoice.getOrder().getCurrency().toUpperCase();
        Cell cell = createCell(toPayment, boldFont, color, TextAlignment.CENTER, 2);
        cell.setUnderline();
        cell.setFontSize(12);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
    }

    private void addPaymentValueInWords(Table table, Invoice invoice) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBolderFont();
        Color white = Color.WHITE;

        Cell cell1 = createCell("Kwota słownie: ", regularFont, white, TextAlignment.RIGHT, 0);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        String[] splittedNumber = splitNumber(invoice.getValueWithTax());

        String numberFirstPart = splittedNumber[0];
        String currency = " " + invoice.getOrder().getCurrency() + " ";
        String pennies = splittedNumber[1] + "/100";

        Cell cell2 = createCell(NumberConverter.speakNumber(numberFirstPart) + currency + pennies, boldFont, white, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);

    }

    private String[] splitNumber(BigDecimal number) {
        String[] result = new String[2];

        String stringNumber = number.toString();
        int dotIndex = stringNumber.indexOf(".");

        result[0] = stringNumber.substring(0, dotIndex);
        result[1] = stringNumber.substring(dotIndex).replace(".", "");

        return result;
    }

    private void addSignaturesFields(Table table) throws IOException {
        table.setMarginTop(70f);

        createSignatureCell(table, "Osoba upoważniona do odbioru");

        Cell emptyCell = new Cell();
        emptyCell.setBorder(Border.NO_BORDER);
        table.addCell(emptyCell);

        createSignatureCell(table, "Osoba upoważniona do wystawienia");

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

    private void addFooter(Document document) throws IOException {
        Paragraph paragraph = new Paragraph();

        Text footer = new Text("\u00a9 2020 Created by Forest Industry S.A.");
        footer.setFont(MyFont.getRegularFont());
        footer.setFontSize(8);

        paragraph.add(footer);
        paragraph.setMarginTop(20f);
        paragraph.setTextAlignment(TextAlignment.CENTER);

        document.add(paragraph);
    }

    private String reformatDate(LocalDateTime date) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
    }

}