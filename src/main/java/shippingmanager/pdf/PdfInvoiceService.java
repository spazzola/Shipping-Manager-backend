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
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.company.Company;
import shippingmanager.invoice.Invoice;
import shippingmanager.invoice.InvoiceDao;
import shippingmanager.order.Order;
import shippingmanager.utility.product.ProductService;
import shippingmanager.utility.taxxinfo.TaxInfo;
import shippingmanager.utility.bankaccount.BankAccount;
import shippingmanager.utility.numberconverter.NumberConverter;
import shippingmanager.utility.phonenumber.PhoneNumber;
import shippingmanager.utility.product.Product;
import shippingmanager.utility.taxxinfo.TaxInfoService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PdfInvoiceService {

    private final InvoiceDao invoiceDao;
    private final ProductService productService;
    private final TaxInfoService taxInfoService;
    private final PdfService pdfService;


    //TODO add to method parameters Long invoiceId
    public ByteArrayInputStream generatePdf(Long id) throws Exception {
        //PdfFontFactory.registerDirectory("src/main/resources/fonts/");

        ClassLoader classLoader = getClass().getClassLoader();
        PdfFontFactory.registerDirectory(classLoader.getResource("fonts/").toString());

        Invoice invoice = invoiceDao.findById(id)
                .orElseThrow(Exception::new);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);



        Table firstTable = new Table(2);
        addCompanyLogo(firstTable);
        addIssuedDateAndPlace(firstTable, invoice);
        document.add(firstTable);

        addInvoiceNumber(document, invoice);

        Table secondTable = new Table(2);
        addGivenAndReceivedByInfo(secondTable, invoice.getOrder(), invoice);
        document.add(secondTable);

        Table thirdTable = new Table(24);
        addProductsAndValuesInfo(thirdTable, invoice);
        document.add(thirdTable);

        Table fourthTable = new Table(2);
        addPaymentAndFinalValuesInfo(fourthTable, invoice);
        document.add(fourthTable);

        Table fifthTable = new Table(2);
        addPaymentValueInWords(fifthTable, invoice);
        document.add(fifthTable);

        Table sixthTable = new Table(5);
        pdfService.addSignaturesFields(sixthTable,
                "Osoba upoważniona do odbioru",
                "Osoba upoważniona do wystawienia");
        document.add(sixthTable);

        pdfService.addFooter(document);

        document.close();
        out.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addCompanyLogo(Table table) throws IOException {
        Cell cell = new Cell();
        //ImageData imageData = ImageDataFactory.create("src/main/resources/pics/logo.png");
        ClassLoader classLoader = getClass().getClassLoader();
        ImageData imageData = ImageDataFactory.create(classLoader.getResource("pics/logo.png"));
        Image pdfImage = new Image(imageData);
        cell.setBorder(Border.NO_BORDER);
        cell.add(pdfImage);
        table.addCell(cell);
    }

    private void addIssuedDateAndPlace(Table table, Invoice invoice) throws IOException {
        Cell cell = new Cell();

        String formattedDate = pdfService.reformatDate(invoice.getIssuedDate(), false);

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
        textHeader.setFont(MyFont.getBoldFont());
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
        invoiceNumber.setFont(MyFont.getBoldFont());
        invoiceNumber.setFontSize(20);

        paragraph.add(invoiceNumber);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setMarginTop(10f);

        document.add(paragraph);
    }

    private void addGivenAndReceivedByInfo(Table table, Order order, Invoice invoice) throws IOException {
        table.setMarginTop(15);

        addMerchantsHeaders(table, "Sprzedawca");
        addMerchantsHeaders(table, "Nabywca");

        //TODO switch givenBy with receivedBy?
        if (order != null) {
            addMerchantInfo(table, order.getGivenBy());
            addMerchantInfo(table, order.getReceivedBy());
        } else {
            addMerchantInfo(table, invoice.getIssuedBy());
            addMerchantInfo(table, invoice.getReceivedBy());
        }

    }

    private void addMerchantsHeaders(Table table, String header) throws IOException {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph();
        paragraph.add(header);
        paragraph.setFont(MyFont.getBoldFont());
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

    private void addProductsAndValuesInfo(Table table, Invoice invoice) throws IOException {
        table.setMarginTop(30f);

        addHeaderCellsToThirdTable(table);
        List<Product> products = invoice.getProducts();
        for (int i = 0; i < products.size(); i++) {
            addDescriptionCellsToThirdTable(table, products.get(i), i + 1);
        }

    }

    private void addHeaderCellsToThirdTable(Table table) throws IOException {
        PdfFont boldFont = MyFont.getBoldFont();
        Color grey = new DeviceRgb(217, 215, 212);

        Cell counter = createCell("Lp", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(counter);

        Cell productName = createCell("Nazwa towaru/uslugi", boldFont, grey, TextAlignment.CENTER, 8);
        table.addCell(productName);

        Cell quantity = createCell("Ilość", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(quantity);

        Cell measureUnit = createCell("Jm", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(measureUnit);

        Cell priceWithoutTax = createCell("Cena netto", boldFont, grey, TextAlignment.CENTER, 3);
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

    private Cell createCell(String text, PdfFont font, Color color, TextAlignment alignment, int colspan) {
        Cell cell = new Cell(0, colspan);
        setCellProperties(cell, font, color, alignment);
        cell.add(new Paragraph(text));

        return cell;
    }

    private void setCellProperties(Cell cell, PdfFont font, Color color, TextAlignment alignment) {
        cell.setBackgroundColor(color);
        cell.setFont(font).setFontSize(8);
        cell.setTextAlignment(alignment);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private void addDescriptionCellsToThirdTable(Table table, Product product, int counter) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        Color white = Color.WHITE;

        Cell index = createCell(String.valueOf(counter), regularFont, white, TextAlignment.CENTER, 0);
        table.addCell(index);

        Cell productName = createCell(product.getProductName(), regularFont, white, TextAlignment.LEFT, 8);
        table.addCell(productName);

        Cell quantity = createCell(String.valueOf(product.getQuantity()), regularFont, white, TextAlignment.CENTER, 0);
        table.addCell(quantity);

        Cell measureUnit = createCell(product.getMeasureUnit(), regularFont, white, TextAlignment.CENTER, 0);
        table.addCell(measureUnit);

        Cell priceWithoutTax = createCell(product.getPriceWithoutTax().toString(), regularFont, white, TextAlignment.RIGHT, 3);
        table.addCell(priceWithoutTax);

        Cell percentageTaxValue = createCell(product.getTax().toString() + "%", regularFont, white, TextAlignment.CENTER, 0);
        table.addCell(percentageTaxValue);

        Cell totalPriceWithoutTax = createCell(product.getValueWithoutTax().toString(), regularFont, white, TextAlignment.RIGHT, 3);
        table.addCell(totalPriceWithoutTax);

        Cell totalTaxValue = createCell(product.getTaxValue().toString(), regularFont, white, TextAlignment.RIGHT, 3);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell(product.getValueWithTax().toString(), regularFont, white, TextAlignment.RIGHT, 3);
        table.addCell(totalValueWithTax);
    }

    private void addPaymentAndFinalValuesInfo(Table table, Invoice invoice) throws IOException {
        table.setMarginTop(10f);
        addFinalValuesInfo(table, invoice);
        addPaymentInfo(table, invoice);
    }

    private void addFinalValuesInfo(Table table, Invoice invoice) throws IOException {
        Cell cell = new Cell();
        Table fourthTable = new Table(4);

        addHeaderCellsToFourthTable(fourthTable);

        List<Product> productsWithTax23 = productService.getProductWithTax23(invoice.getProducts());
        List<Product> productsWithTax08 = productService.getProductWithTax08(invoice.getProducts());
        List<Product> productsWithTax05 = productService.getProductWithTax05(invoice.getProducts());


        TaxInfo tax23 = taxInfoService.createTaxInfo(productsWithTax23);
        TaxInfo tax08 = taxInfoService.createTaxInfo(productsWithTax08);
        TaxInfo tax05 = taxInfoService.createTaxInfo(productsWithTax05);

        if (tax23 != null) {
            addDescriptionCellsToFourthTable(fourthTable, tax23);
        }
        if (tax08 != null) {
            addDescriptionCellsToFourthTable(fourthTable, tax08);
        }
        if (tax05 != null) {
            addDescriptionCellsToFourthTable(fourthTable, tax05);
        }

        addSummarizeDescription(fourthTable, invoice);

        cell.add(fourthTable);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
    }

    private void addHeaderCellsToFourthTable(Table table) throws IOException {
        PdfFont boldFont = MyFont.getBoldFont();
        Color grey = new DeviceRgb(217, 215, 212);

        Cell percentageTaxValue = createCell("Stawka VAT", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(percentageTaxValue);

        Cell priceWithoutTax = createCell("Netto", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(priceWithoutTax);

        Cell totalTaxValue = createCell("Kwota VAT", boldFont, grey, TextAlignment.CENTER, 0);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell("Brutto", boldFont, grey, TextAlignment.CENTER, 3);
        table.addCell(totalValueWithTax);
    }

    private void addDescriptionCellsToFourthTable(Table table, TaxInfo taxInfo) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        Color white = Color.WHITE;

        Cell percentageTaxValue = createCell(taxInfo.getTax() + "%", regularFont, white, TextAlignment.CENTER, 0);
        table.addCell(percentageTaxValue);

        Cell priceWithoutTax = createCell(taxInfo.getTotalValueWithoutTax().toString(), regularFont, white, TextAlignment.RIGHT, 0);
        table.addCell(priceWithoutTax);

        Cell totalTaxValue = createCell(taxInfo.getTotalTaxValue().toString(), regularFont, white, TextAlignment.RIGHT, 0);
        table.addCell(totalTaxValue);

        Cell totalValueWithTax = createCell(taxInfo.getTotalValueWithTax().toString(), regularFont, white, TextAlignment.RIGHT, 0);
        table.addCell(totalValueWithTax);
    }

    private void addSummarizeDescription(Table table, Invoice invoice) throws IOException {
        PdfFont boldFont = MyFont.getBoldFont();
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

        Cell cell = new Cell();
        Table fifthTable = new Table(2);

        addFirstRow(fifthTable, invoice, white);
        addSecondRow(fifthTable, invoice, white);
        addThirdRow(fifthTable, invoice, white);
        addFourthRow(fifthTable, invoice, white);

        cell.add(fifthTable);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
    }

    private void addFirstRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBoldFont();

        Cell cell1 = createCell("Sposób zapłaty: ", regularFont, color, TextAlignment.RIGHT, 0);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Cell cell2 = createCell(invoice.getPaymentMethod(), boldFont, color, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);

    }

    private void addSecondRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBoldFont();

        Cell cell1 = createCell("Termin zapłaty: ", regularFont, color, TextAlignment.RIGHT, 0);
        cell1.setVerticalAlignment(VerticalAlignment.TOP);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        int daysTillPayment = invoice.getDaysTillPayment();
        LocalDateTime paymentDay = invoice.getIssuedDate().plusDays(daysTillPayment);
        String formattedPaymentDay = pdfService.reformatDate(paymentDay, false);
        String paymentDateInfo = daysTillPayment + " dni\n" + formattedPaymentDay;

        Cell cell2 = createCell(paymentDateInfo, boldFont, color, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);
    }

    private void addThirdRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBoldFont();

        Cell cell1 = createCell("Zapłacono: ", regularFont, color, TextAlignment.RIGHT, 0);
        cell1.setVerticalAlignment(VerticalAlignment.TOP);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Cell cell2 = createCell(invoice.getPaidAmount().toString(), boldFont, color, TextAlignment.LEFT, 0);
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);
    }

    private void addFourthRow(Table table, Invoice invoice, Color color) throws IOException {
        PdfFont boldFont = MyFont.getBoldFont();
        String toPayment = "Do zapłaty: " + invoice.getAmountToPay() + " " + invoice.getCurrency().toUpperCase();
        Cell cell = createCell(toPayment, boldFont, color, TextAlignment.CENTER, 2);
        cell.setUnderline();
        cell.setFontSize(12);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
    }

    private void addPaymentValueInWords(Table table, Invoice invoice) throws IOException {
        PdfFont regularFont = MyFont.getRegularFont();
        PdfFont boldFont = MyFont.getBoldFont();
        Color white = Color.WHITE;

        Cell cell1 = createCell("Kwota słownie: ", regularFont, white, TextAlignment.RIGHT, 0);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        String[] splittedNumber = splitNumber(invoice.getAmountToPay());

        String numberFirstPart = splittedNumber[0];
        String currency = " " + invoice.getCurrency().toUpperCase() + " ";
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

}