package shippingmanager.pdf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
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

import java.io.FileOutputStream;

@AllArgsConstructor
@Service
public class PdfOrderService  {

    private OrderDao orderDao;
    private OrderMapper orderMapper;

    public void generatePdf() throws Exception {
        FontFactory.registerDirectory("src/main/resources/fonts/");
        Order order = orderDao.findById(1L)
                .orElseThrow(Exception::new);
        OrderDto orderDto = orderMapper.toDto(order);

        String pdfName = generatePdfName(orderDto);

        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        PdfWriter.getInstance(document, new FileOutputStream(pdfName));

        document.open();

        addHeaders(document, orderDto);

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(20f);

        addCompaniesInfo(table, orderDto);
        addLoadingsInfo(table, orderDto.getLoadingInformation());
        addDescriptionInfo(table, orderDto);
        addDriverInfo(table, orderDto);
        addPriceInfo(table, orderDto);
        addPaymentInfo(table, orderDto);
        addComment(table, orderDto);
        addRegulationsInfo(table, orderDto);

        document.add(table);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        addSignaturesFields(document);
        addFooter(document);

        document.close();
    }

    private String generatePdfName(OrderDto orderDto) {
        String orderNumber = orderDto.getOrderNumber().replace("/", "-");
        return "Zamówienie" + orderNumber + ".pdf";
    }

    private void addHeaders(Document document, OrderDto orderDto) throws DocumentException, IOException {
        addPlaceAndDateHeader(document, orderDto);
        addOrderNumberHeader(document, orderDto);
    }

    private void addPlaceAndDateHeader(Document document, OrderDto orderDto) throws IOException, DocumentException {
        String createdDate = rebuildDateFormat(orderDto.getCreatedDate());
        int spaceIndex = createdDate.indexOf(" ");
        createdDate = createdDate.substring(0, spaceIndex);

        Paragraph header = new Paragraph(orderDto.getCreatedPlace() + " " + createdDate, MyFont.getNormalFont());
        header.setAlignment(Element.ALIGN_RIGHT);
        document.add(header);
        document.add(Chunk.NEWLINE);
    }

    private void addOrderNumberHeader(Document document, OrderDto orderDto) throws DocumentException, IOException {
        Paragraph header = new Paragraph("Zlecenie transportowe nr: " + orderDto.getOrderNumber(), MyFont.getBoldFont(16));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
    }

    private void addCompaniesInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();

        cell.addElement(new Phrase("Zleceniodawca", MyFont.getBoldFont(10)));
        addCompanyInfo(table, cell, orderDto.getGivenBy());

        PdfPCell cell2 = new PdfPCell();
        cell2.addElement(new Phrase("Zleceniobiorca", MyFont.getBoldFont(10)));
        addCompanyInfo(table, cell2, orderDto.getReceivedBy());
    }

    private void addCompanyInfo(PdfPTable table, PdfPCell cell, CompanyDto company) throws IOException, DocumentException {
        String address = buildAddress(company);

        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement(new Phrase(company.getCompanyName(), MyFont.getNormalFont()));
        cell.addElement(new Phrase(address, MyFont.getNormalFont()));
        cell.addElement(new Phrase("NIP " + company.getNip(), MyFont.getNormalFont()));

        for (PhoneNumberDto phoneNumber : company.getPhoneNumbers()) {
            cell.addElement(new Phrase("tel: " + phoneNumber.getNumber(), MyFont.getNormalFont()));
        }

        table.addCell(cell);
    }

    private String buildAddress(CompanyDto companyDto) {
        AddressDto address = companyDto.getAddress();

        return "ul." + address.getStreet() + " " + address.getHouseNumber() + ", " + address.getPostalCode() + " "
                + address.getCity();
    }

    private void addLoadingsInfo(PdfPTable table, LoadingInformationDto loadingInformation) throws IOException, DocumentException {
        addLoadingDateInfo(table, loadingInformation);
        addUnloadingDateInfo(table, loadingInformation);

        addLoadingPlaceInfo(table, loadingInformation);
        addUnloadingPlaceInfo(table, loadingInformation);

    }

    private void addLoadingDateInfo(PdfPTable table, LoadingInformationDto loadingInformation) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement((new Phrase("Data i godzina załadunku", MyFont.getBoldFont(10))));
        String loadingDate = buildLoadingDate(loadingInformation);
        cell.addElement(new Phrase(loadingDate, MyFont.getNormalFont()));

        table.addCell(cell);
    }

    private void addUnloadingDateInfo(PdfPTable table, LoadingInformationDto loadingInformation) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement((new Phrase("Data i godzina rozładunku", MyFont.getBoldFont(10))));
        String unloadingDate = buildUnloadingDate(loadingInformation);
        cell.addElement(new Phrase(unloadingDate, MyFont.getNormalFont()));

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

    private void addLoadingPlaceInfo(PdfPTable table, LoadingInformationDto loadingInformation) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement(new Phrase("Miejsce załadunku", MyFont.getBoldFont(10)));
        cell.addElement(new Phrase(loadingInformation.getLoadingPlace(), MyFont.getNormalFont()));

        table.addCell(cell);
    }

    private void addUnloadingPlaceInfo(PdfPTable table, LoadingInformationDto loadingInformationDto) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement((new Phrase("Miejsce rozładunku", MyFont.getBoldFont(10))));
        cell.addElement(new Phrase(loadingInformationDto.getUnloadingPlace(), MyFont.getNormalFont()));
        table.addCell(cell);

    }

    private void addDescriptionInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        PdfPCell cell = createRowWithText("Opis ładunku", orderDto.getDescription());
        table.addCell(cell);
    }

    private void addDriverInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        String driversInfo = buildDriversInfoString(orderDto.getOrderDrivers());
        PdfPCell cell = createRowWithText("Dane kierowcy", driversInfo);
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

    private void addComment(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        PdfPCell cell = createRowWithText("Uwagi", orderDto.getComment());
        table.addCell(cell);
    }

    private void addPriceInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        PdfPCell cell = createRowWithText("Stawka: ", orderDto.getValue().toString() + orderDto.getCurrency());
        table.addCell(cell);
    }

    private void addPaymentInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        String daysTillPayment = orderDto.getDaysTillPayment() + " dni od otrzymania FV";
        PdfPCell cell = createRowWithText("Warunku płatności", daysTillPayment);
        table.addCell(cell);
    }

    private void addRegulationsInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        PdfPCell cell = createRowWithText("Regulamin", "* Wymagamy aby na powyższe zlecenie podstawiony był samochod:  sprawny, czysty, bez obcych zapachów, posiadający aktualną\n" +
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

    private void addSignaturesFields(Document document) throws DocumentException, IOException {
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Podpis i pieczątka zleceniodawcy", MyFont.getNormalFont());
        p.setAlignment(Element.ALIGN_BOTTOM);
        p.add(new Chunk(glue));
        p.add("Podpis i pieczątka zleceniobiorcy");
        document.add(p);
    }

    private void addFooter(Document document) throws IOException, DocumentException {
        Paragraph p = new Paragraph("\u00a9 2020 Created by Forest Industry S.A.", MyFont.getNormalFont());
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
    }

    private PdfPCell createRowWithText(String header, String text) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        setPadding(cell, 2, 5, 5, 5);
        ridBlankSpace(cell);

        cell.addElement(new Phrase(header, MyFont.getBoldFont(10)));
        cell.addElement(new Phrase(text, MyFont.getNormalFont()));

        return cell;
    }

    private String rebuildDateFormat(LocalDateTime localDateTime) {
        return localDateTime.toString().replace("T", " ");
    }

    private void setPadding(PdfPCell cell, int top, int right, int bottom, int left) {
        cell.setPaddingTop(top);
        cell.setPaddingRight(right);
        cell.setPaddingBottom(bottom);
        cell.setPaddingLeft(left);
    }

    private void ridBlankSpace(PdfPCell cell) {
        cell.setUseAscender(true);
        cell.setUseDescender(true);
    }

}