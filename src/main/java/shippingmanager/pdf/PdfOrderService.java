package shippingmanager.pdf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.company.CompanyDto;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;
import shippingmanager.order.OrderDto;
import shippingmanager.order.OrderMapper;
import shippingmanager.utility.address.AddressDto;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.orderdriver.OrderDriverDto;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.plate.PlateDto;


@AllArgsConstructor
@Service
public class PdfOrderService  {

    private OrderDao orderDao;
    private OrderMapper orderMapper;

    //TODO add to method parameters Long orderId
    public void generatePdf() throws Exception {
        PdfFontFactory.registerDirectory("src/main/resources/fonts/");
        Order order = orderDao.findById(1L)
                .orElseThrow(Exception::new);
        OrderDto orderDto = orderMapper.toDto(order);

        String pdfName = generatePdfName(orderDto);

        PdfWriter writer = new PdfWriter(pdfName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        addHeaders(document, orderDto);

        Table firstTable = new Table(2);

        addCompaniesInfo(firstTable, orderDto);
        addLoadingsInfo(firstTable, orderDto.getLoadingInformation());
        addDescriptionInfo(firstTable, orderDto);
        addDriverInfo(firstTable, orderDto);
        addPriceInfo(firstTable, orderDto);
        addPaymentInfo(firstTable, orderDto);
        addComment(firstTable, orderDto);
        addRegulationsInfo(firstTable);

        document.add(firstTable);

        Table secondTable = new Table(5);
        addSignaturesFields(secondTable);
        document.add(secondTable);

        addFooter(document);

        document.close();
    }

    private String generatePdfName(OrderDto orderDto) {
        String orderNumber = orderDto.getOrderNumber().replace("/", "-");
        return "Zamówienie" + orderNumber + ".pdf";
    }

    private void addHeaders(Document document, OrderDto orderDto) throws IOException {
        addPlaceAndDateHeader(document, orderDto);
        addOrderNumberHeader(document, orderDto);
    }

    private void addPlaceAndDateHeader(Document document, OrderDto orderDto) throws IOException {
        String createdDate = rebuildDateFormat(orderDto.getCreatedDate());
        int spaceIndex = createdDate.indexOf(" ");
        createdDate = createdDate.substring(0, spaceIndex);

        Paragraph header = new Paragraph(orderDto.getIssuedIn() + " " + createdDate);
        header.setFont(MyFont.getRegularFont());
        header.setTextAlignment(TextAlignment.RIGHT);
        document.add(header);
    }

    private void addOrderNumberHeader(Document document, OrderDto orderDto) throws IOException {
        Paragraph header = new Paragraph("Zlecenie transportowe nr: " + orderDto.getOrderNumber());
        header.setFont(MyFont.getBolderFont());
        header.setFontSize(16f);
        header.setTextAlignment(TextAlignment.CENTER);
        document.add(header);
    }

    private void addCompaniesInfo(Table table, OrderDto orderDto) throws IOException {
        Paragraph header = new Paragraph("Zleceniodawca");
        header.setFont(MyFont.getBolderFont());
        header.setFontSize(10);
        Cell cell = new Cell();
        cell.add(header);
        addCompanyInfo(table, cell, orderDto.getGivenBy());

        header = new Paragraph("Zleceniobiorca");
        header.setFont(MyFont.getBolderFont());
        header.setFontSize(10);
        cell = new Cell();
        cell.add(header);
        addCompanyInfo(table, cell, orderDto.getReceivedBy());
    }

    private void addCompanyInfo(Table table, Cell cell, CompanyDto company) throws IOException {
        String address = buildAddress(company);

        Paragraph paragraph = new Paragraph();
        paragraph.add(company.getCompanyName());
        paragraph.add("\n" + address);
        paragraph.add("\nNIP " + company.getNip());
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);
        cell.add(paragraph);

        for (PhoneNumberDto phoneNumber : company.getPhoneNumbers()) {
            paragraph = new Paragraph();
            paragraph.add(phoneNumber.getType() + ": " + phoneNumber.getNumber());
            paragraph.setFont(MyFont.getRegularFont());
            paragraph.setFontSize(9f);
            cell.add(paragraph);
        }

        table.addCell(cell);
    }

    private String buildAddress(CompanyDto companyDto) {
        AddressDto address = companyDto.getAddress();

        return "ul." + address.getStreet() + " " + address.getHouseNumber() + ", " + address.getPostalCode() + " "
                + address.getCity();
    }

    private void addLoadingsInfo(Table table, LoadingInformationDto loadingInformation) throws IOException {
        addLoadingDateInfo(table, loadingInformation);
        addUnloadingDateInfo(table, loadingInformation);

        addLoadingPlaceInfo(table, loadingInformation);
        addUnloadingPlaceInfo(table, loadingInformation);

    }

    private void addLoadingDateInfo(Table table, LoadingInformationDto loadingInformation) throws IOException {
        Cell cell = new Cell();

        Paragraph paragraph = new Paragraph("Data i godzina załadunku");
        paragraph.setFont(MyFont.getBolderFont());
        paragraph.setFontSize(10f);
        cell.add(paragraph);

        String loadingDate = buildLoadingDate(loadingInformation);
        paragraph = new Paragraph(loadingDate);
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private void addUnloadingDateInfo(Table table, LoadingInformationDto loadingInformation) throws IOException {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph("Data i godzina rozładunku");
        paragraph.setFont(MyFont.getBolderFont());
        paragraph.setFontSize(10);
        cell.add(paragraph);

        String unloadingDate = buildUnloadingDate(loadingInformation);
        paragraph = new Paragraph(unloadingDate);
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private String buildLoadingDate(LoadingInformationDto loadingInformation) {
        String result = "";
        result += rebuildDateFormat(loadingInformation.getMinLoadingDate()) + " - ";
        result += rebuildDateFormat(loadingInformation.getMaxLoadingDate());

        return result;
    }

    private String buildUnloadingDate(LoadingInformationDto loadingInformation) {
        String result = "";
        result += rebuildDateFormat(loadingInformation.getMinUnloadingDate()) + " - ";
        result += rebuildDateFormat(loadingInformation.getMaxUnloadingDate());

        return result;
    }

    private void addLoadingPlaceInfo(Table table, LoadingInformationDto loadingInformation) throws IOException {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph("Miejsce załadunku");
        paragraph.setFont(MyFont.getBolderFont());
        paragraph.setFontSize(10f);
        cell.add(paragraph);

        paragraph = new Paragraph(loadingInformation.getLoadingPlace());
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private void addUnloadingPlaceInfo(Table table, LoadingInformationDto loadingInformationDto) throws IOException {
        Cell cell = new Cell();

        Paragraph paragraph = new Paragraph("Miejsce rozładunku");
        paragraph.setFont(MyFont.getBolderFont());
        paragraph.setFontSize(10f);
        cell.add(paragraph);

        paragraph = new Paragraph(loadingInformationDto.getUnloadingPlace());
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);
        cell.add(paragraph);
        table.addCell(cell);

    }

    private void addDescriptionInfo(Table table, OrderDto orderDto) throws IOException {
        Cell cell = createRowWithText("Opis ładunku", orderDto.getOrderDescription());
        table.addCell(cell);
    }

    private void addDriverInfo(Table table, OrderDto orderDto) throws IOException {
        String driversInfo = buildDriversInfoString(orderDto.getOrderDrivers());
        Cell cell = createRowWithText("Dane kierowcy", driversInfo);
        table.addCell(cell);
    }


    private String buildDriversInfoString(List<OrderDriverDto> orderDrivers) {
        StringBuilder builder = new StringBuilder();
        for (OrderDriverDto orderDriver : orderDrivers) {

            String plateInfo = buildPlateInfo(orderDriver.getDriver().getPlates());
            builder.append(plateInfo);

            builder.append(orderDriver.getDriver().getName()).append(" ");
            builder.append(orderDriver.getDriver().getSurname()).append(" ");

            for (PhoneNumberDto phoneNumber : orderDriver.getDriver().getPhoneNumbers()) {
                builder.append(phoneNumber.getNumber());
            }

            if (orderDrivers.size() > 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private String buildPlateInfo(List<PlateDto> plates) {
        StringBuilder builder = new StringBuilder();

        if (plates.size() > 0) {
            builder.append("(");
            for (PlateDto plate : plates) {
                builder.append(plate.getPlateNumber()).append(" ");
            }
            builder.append(") ");
        }

        return builder.toString();
    }

    private void addComment(Table table, OrderDto orderDto) throws IOException {
        Cell cell = createRowWithText("Uwagi", orderDto.getComment());
        table.addCell(cell);
    }

    private void addPriceInfo(Table table, OrderDto orderDto) throws IOException {
        Cell cell = createRowWithText("Stawka: ", orderDto.getValue().toString() + orderDto.getCurrency());
        table.addCell(cell);
    }

    private void addPaymentInfo(Table table, OrderDto orderDto) throws IOException {
        String daysTillPayment = orderDto.getDaysTillPayment() + " dni od otrzymania FV";
        Cell cell = createRowWithText("Warunku płatności", daysTillPayment);
        table.addCell(cell);
    }

    private void addRegulationsInfo(Table table) throws IOException {
        Cell cell = createRowWithText("Regulamin", "* Wymagamy aby na powyższe zlecenie podstawiony był samochod:  sprawny, czysty, bez obcych zapachów, posiadający aktualną\n" +
                "  polise  OC i OCP, natomiast zleceniobiorca posiadał wymagane prawem  zezwolenia na wykonywanie transportu.   \n" +
                "* Prosimy o sprawdzenie poprawności zlecenia i potwierdzenie  w ciągu 30 min:  telefonicznie, faksem lub e-mail,   po tym czasie\n" +
                "  uznajemy zlecenie za przyjęte.\n" +
                "* Dokumenty przewozowe (potwierdzone oryginały dokumentów: list przewozowy, dowód dostawy) i fakture wraz z nr zlecenia               należy dostarczyć max do 14 dni po rozładunku ( w przypadku dostaw do klienta ŻABKA max do 7 dni po rozładunku ), po tym terminie zastrzegamy  sobie prawo obniżenia frachtu o 10 %,  na fakturze           obowiązkowo powinien być umieszczony nr listu przewozowego.\n" +
                "* Zastrzegamy sobie możliwość dochodzenia odszkodowania, przekraczającego kary umowne w przypadku nienależytego lub też  \n" +
                "niewykonania usługi, lub reklamacji klienta       \n" +
                "* Przestoje pod załadunkiem i rozładunkiem do 24 godzin są wolne od opłat.\n" +
                "* W przypadku nieterminowego podstawienia pojazdu zastrzegamy możliwość nałożenia kary umownej w wysokości nie mniejszej\n" +
                "  niż 100 zł z frachtu\n" +
                "•\tPrzyjmując niniejsze zlecenie Zleceniobiorca akceptuje i przyjmuje powyższe warunki.\n" +
                "•\tW przypadku odmowy przyjęcia towaru przez odbiorce istnieje mozliwosc zwrotu towaru do miejsca załadunku lub do najbliższego magazynu załadowcy\n" +
                "Niniejsze zlecenie jest umową o ochronie klienta. Podjęcie jakichkolwiek pertraktacji z klientem jest prawnie zabronione pod rygorem kary finansowej (10.000 EUR) oraz wstrzymaniem wszelkich płatności wobec przewoźnika.\n" +
                "W sprawach nieuregulowanych obowiązują przepisy Kodeksu Cywilnego. Spory sądowe rozstrzygane będą dla obu Stron w Żywcu.\n");
        table.addCell(cell);
    }

    private void addSignaturesFields(Table table) throws IOException {
        table.setFixedPosition(48f, 30f, 500f);
        createSignatureCell(table, "Podpisz i pieczątka zleceniodawcy");

        Cell emptyCell = new Cell();
        emptyCell.setBorder(Border.NO_BORDER);
        table.addCell(emptyCell);

        createSignatureCell(table, "Podpis i pieczątka zleceniobiorcy");
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
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFixedPosition(8f, 10f, 580f);

        document.add(paragraph);
    }

    private Cell createRowWithText(String header, String text) throws IOException {
        Cell cell = new Cell(0, 2);

        Paragraph paragraph = new Paragraph(header);
        paragraph.setFont(MyFont.getBolderFont());
        paragraph.setFontSize(10f);
        cell.add(paragraph);

        paragraph = new Paragraph(text);
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);
        cell.add(paragraph);

        return cell;
    }

    private String rebuildDateFormat(LocalDateTime localDateTime) {
        return localDateTime.toString().replace("T", " ");
    }

}