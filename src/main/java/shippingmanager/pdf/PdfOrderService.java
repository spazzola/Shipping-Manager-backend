package shippingmanager.pdf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import shippingmanager.company.CompanyDto;
import shippingmanager.order.Order;
import shippingmanager.order.OrderDao;
import shippingmanager.order.OrderDto;
import shippingmanager.order.OrderMapper;
import shippingmanager.utility.address.AddressDto;
import shippingmanager.utility.driver.Driver;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.orderdriver.OrderDriver;
import shippingmanager.utility.orderdriver.OrderDriverDto;
import shippingmanager.utility.phonenumber.PhoneNumberDto;
import shippingmanager.utility.plate.Plate;
import shippingmanager.utility.plate.PlateDto;

import java.io.FileOutputStream;
import java.util.Arrays;

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
        /*OrderDto orderDto = new OrderDto();
        List<PhoneNumberDto> phoneNumbers = Arrays.asList(new PhoneNumberDto(1L, "kom", "23124141"),
                                                          new PhoneNumberDto(1L, "kom", "892752789"),
                                                          new PhoneNumberDto(1L, "kom", "123131437"));
        orderDto.setOrderNumber("12/12/2020");
        CompanyDto companyDto = CompanyDto.builder()
                .address(AddressDto.builder()
                        .street("Kabatow")
                        .houseNumber("20a")
                        .city("Zywiec")
                        .postalCode("34-300")
                        .build())
                .phoneNumbers(phoneNumbers)
                .nip("5531004598")
                .companyName("MAX-TRANS")
                .build();
        orderDto.setLoadingInformation(LoadingInformationDto.builder()
                .loadingPlace("Browar Żywiec ul.Jakas tam 12")
                .unloadingPlace("Jamaica Wyspa Buchów 420")
                .minLoadingDate(LocalDateTime.of(2020, 12, 20, 13, 20))
                .maxLoadingDate(LocalDateTime.of(2020, 12, 20, 13, 20))
                .minUnloadingDate(LocalDateTime.of(2020, 12, 20, 13, 20))
                .maxUnloadingDate(LocalDateTime.of(2020, 12, 20, 13, 20))
                .build());

        orderDto.setDescription("Załadunek adfklafjalkjlad lad;kad;adas dadasda");
        orderDto.setGivenBy(companyDto);
        orderDto.setReceivedBy(companyDto);*/

        String pdfName = generatePdfName(orderDto);

        Document document = new Document(PageSize.A4, 20, 20, 30, 50);
        PdfWriter.getInstance(document, new FileOutputStream(pdfName));

        document.open();

        addHeader(orderDto, document);
        //document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(20f);

        addCompaniesInfo(table, orderDto);
        addLoadingsInfo(table, orderDto.getLoadingInformation());
        addDescriptionInfo(table, orderDto);
        addDriverInfo(table, orderDto);


        document.add(table);

        //PdfPTable secondTable = new PdfPTable(1);


        document.close();
    }

    private String generatePdfName(OrderDto orderDto) {
        String orderNumber = orderDto.getOrderNumber().replace("/", "-");
        return "Zamówienie" + orderNumber + ".pdf";
    }

    private void addHeader(OrderDto orderDto, Document document) throws DocumentException, IOException {
        Paragraph header = new Paragraph("Zlecenie transportowe nr: " + orderDto.getOrderNumber(), MyFont.getBoldFont(16));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
    }

    private void addCompaniesInfo(PdfPTable table, OrderDto orderDto) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();

        cell.addElement(new Phrase("Zleceniodawca", MyFont.getBoldFont(12)));
        addCompanyInfo(table, cell, orderDto.getGivenBy());

        PdfPCell cell2 = new PdfPCell();
        cell2.addElement(new Phrase("Zleceniobiorca", MyFont.getBoldFont(12)));
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

        cell.addElement((new Phrase("Data i godzina załadunku", MyFont.getBoldFont(12))));
        String loadingDate = buildLoadingDate(loadingInformation);
        cell.addElement(new Phrase(loadingDate, MyFont.getNormalFont()));

        table.addCell(cell);
    }

    private void addUnloadingDateInfo(PdfPTable table, LoadingInformationDto loadingInformation) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement((new Phrase("Data i godzina rozładunku", MyFont.getBoldFont(12))));
        String unloadingDate = buildUnloadingDate(loadingInformation);
        cell.addElement(new Phrase(unloadingDate, MyFont.getNormalFont()));

        table.addCell(cell);
    }

    private String buildLoadingDate(LoadingInformationDto loadingInformation) {
        String result = "";
        result += loadingInformation.getMinLoadingDate().toString().replace("T", " ") + " - ";
        result += loadingInformation.getMaxLoadingDate().toString().replace("T", " ");

        return result;
    }

    private String buildUnloadingDate(LoadingInformationDto loadingInformation) {
        String result = "";
        result += loadingInformation.getMinUnloadingDate().toString().replace("T", " ") + " - ";
        result += loadingInformation.getMaxUnloadingDate().toString().replace("T", " ");

        return result;
    }

    private void addLoadingPlaceInfo(PdfPTable table, LoadingInformationDto loadingInformation) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement(new Phrase("Miejsce załadunku", MyFont.getBoldFont(12)));
        cell.addElement(new Phrase(loadingInformation.getLoadingPlace(), MyFont.getNormalFont()));

        table.addCell(cell);
    }

    private void addUnloadingPlaceInfo(PdfPTable table, LoadingInformationDto loadingInformationDto) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        ridBlankSpace(cell);
        setPadding(cell, 2, 5, 5, 5);

        cell.addElement((new Phrase("Miejsce rozładunku", MyFont.getBoldFont(12))));
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

    private PdfPCell createRowWithText(String header, String text) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(2);
        setPadding(cell, 5, 5, 5, 5);
        ridBlankSpace(cell);

        cell.addElement(new Phrase(header, MyFont.getBoldFont(12)));
        cell.addElement(new Phrase(text, MyFont.getNormalFont()));

        return cell;
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