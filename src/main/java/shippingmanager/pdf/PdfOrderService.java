package shippingmanager.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.company.CompanyDto;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;
import shippingmanager.order.OrderDto;
import shippingmanager.order.OrderMapper;
import shippingmanager.utility.address.AddressDto;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.orderdriver.OrderDriver;
import shippingmanager.utility.orderdriver.OrderDriverDto;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.plate.PlateDto;


@AllArgsConstructor
@Service
public class PdfOrderService  {

    private OrderDao orderDao;
    private OrderMapper orderMapper;
    private final PdfService pdfService;

    //TODO add to method parameters Long orderId
    public ByteArrayInputStream generatePdf(Long id) throws Exception {
        PdfFontFactory.registerDirectory("src/main/resources/fonts/");
        Order order = orderDao.findById(id)
                .orElseThrow(Exception::new);
        OrderDto orderDto = orderMapper.toDto(order);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
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
        pdfService.addSignaturesFields(secondTable,
                "Podpisz i pieczątka zleceniodawcy",
                "Podpis i pieczątka zleceniobiorcy");
        document.add(secondTable);

        pdfService.addFooter(document);

        document.close();
        out.close();

        return new ByteArrayInputStream(out.toByteArray());
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
        String createdDate = pdfService.reformatDate(orderDto.getCreatedDate(), false);

        Paragraph header = createParagraph(orderDto.getIssuedIn() + " " + createdDate, MyFont.getRegularFont(), 10f);
        header.setTextAlignment(TextAlignment.RIGHT);
        document.add(header);
    }

    private void addOrderNumberHeader(Document document, OrderDto orderDto) throws IOException {
        Paragraph header = createParagraph("Zlecenie transportowe nr: " + orderDto.getOrderNumber(), MyFont.getBoldFont(), 16f);
        header.setTextAlignment(TextAlignment.CENTER);
        document.add(header);
    }

    private void addCompaniesInfo(Table table, OrderDto orderDto) throws IOException {
        Paragraph header = createParagraph("Zleceniodawca", MyFont.getBoldFont(), 10f);
        Cell cell = new Cell();
        cell.add(header);

        addCompanyInfo(table, cell, orderDto.getGivenBy());

        header = createParagraph("Zleceniobiorca", MyFont.getBoldFont(), 10f);
        cell = new Cell();
        cell.add(header);

        addCompanyInfo(table, cell, orderDto.getReceivedBy());
    }

    private void addCompanyInfo(Table table, Cell cell, CompanyDto company) throws IOException {
        String address = buildAddress(company);
        PdfFont regularFont = MyFont.getRegularFont();

        Paragraph paragraph = createParagraph(company.getCompanyName() +
                "\n" + address + "\nNIP " + company.getNip(), regularFont, 9f);
/*        paragraph.add(company.getCompanyName());
        paragraph.add("\n" + address);
        paragraph.add("\nNIP " + company.getNip());
        paragraph.setFont(MyFont.getRegularFont());
        paragraph.setFontSize(9f);*/
        cell.add(paragraph);

        for (PhoneNumberDto phoneNumber : company.getPhoneNumbers()) {
            paragraph = createParagraph(phoneNumber.getType() + ": " + phoneNumber.getNumber(), regularFont, 9f);
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

        Paragraph paragraph = createParagraph("Data i godzina załadunku", MyFont.getBoldFont(), 10f);
        cell.add(paragraph);

        String loadingDate = buildLoadingDate(loadingInformation);
        paragraph = createParagraph(loadingDate, MyFont.getRegularFont(), 9f);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private void addUnloadingDateInfo(Table table, LoadingInformationDto loadingInformation) throws IOException {
        Cell cell = new Cell();
        Paragraph paragraph = createParagraph("Data i godzina rozładunku", MyFont.getBoldFont(), 10f);
        cell.add(paragraph);

        String unloadingDate = buildUnloadingDate(loadingInformation);
        paragraph = createParagraph(unloadingDate, MyFont.getBoldFont(), 9f);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private String buildLoadingDate(LoadingInformationDto loadingInformation) {
        String result = "";
        result += pdfService.reformatDate(loadingInformation.getMinLoadingDate(), true) + " - ";
        result += pdfService.reformatDate(loadingInformation.getMaxLoadingDate(), true);

        return result;
    }

    private String buildUnloadingDate(LoadingInformationDto loadingInformation) {
        String result = "";
        result += pdfService.reformatDate(loadingInformation.getMinUnloadingDate(), true) + " - ";
        result += pdfService.reformatDate(loadingInformation.getMaxUnloadingDate(), true);

        return result;
    }

    private void addLoadingPlaceInfo(Table table, LoadingInformationDto loadingInformation) throws IOException {
        Cell cell = new Cell();
        Paragraph paragraph = createParagraph("Miejsce załadunku", MyFont.getBoldFont(), 10f);
        cell.add(paragraph);

        paragraph = createParagraph(loadingInformation.getLoadingPlace(), MyFont.getRegularFont(), 9f);
        cell.add(paragraph);

        table.addCell(cell);
    }

    private void addUnloadingPlaceInfo(Table table, LoadingInformationDto loadingInformationDto) throws IOException {
        Cell cell = new Cell();

        Paragraph paragraph = createParagraph("Miejsce rozładunku", MyFont.getBoldFont(), 10f);
        cell.add(paragraph);

        paragraph = createParagraph(loadingInformationDto.getUnloadingPlace(), MyFont.getRegularFont(), 9f);
        cell.add(paragraph);

        table.addCell(cell);

    }

    private void addDescriptionInfo(Table table, OrderDto orderDto) throws IOException {
        Cell cell = createRowWithText("Opis ładunku", orderDto.getDescription());
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

            String platesInfo = buildPlatesInfo(orderDriver);
            builder.append(platesInfo);

            builder.append(orderDriver.getName()).append(" ");
            builder.append(orderDriver.getSurname()).append(" ");

            String phonesInfo = buildPhonesInfo(orderDriver);
            builder.append(phonesInfo);

            if (orderDrivers.size() > 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private String buildPlatesInfo(OrderDriverDto orderDriver) {
        String result = "(";
        result += orderDriver.getFirstPlate();

        if (orderDriver.getSecondPlate() != null) {
            result += " " + orderDriver.getSecondPlate();
        }
        result += ") ";
        return result;
    }

    private String buildPhonesInfo(OrderDriverDto orderDriver) {
        String result = "";
        result += orderDriver.getFirstPhoneNumber();

        if (orderDriver.getSecondPhoneNumber() != null) {
            result += ", ";
            result += orderDriver.getSecondPhoneNumber();
        }

        return result;
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

    private Cell createRowWithText(String header, String text) throws IOException {
        Cell cell = new Cell(0, 2);

        Paragraph paragraph = createParagraph(header, MyFont.getBoldFont(), 10f);
        cell.add(paragraph);

        paragraph = createParagraph(text, MyFont.getRegularFont(), 9f);
        cell.add(paragraph);

        return cell;
    }

    private Paragraph createParagraph(String text, PdfFont font, float fontSize) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(font);
        paragraph.setFontSize(fontSize);

        return paragraph;
    }

}