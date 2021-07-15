package shippingmanager.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.company.Company;
import shippingmanager.company.CompanyDao;
import shippingmanager.utility.driver.Driver;
import shippingmanager.utility.driver.DriverDto;
import shippingmanager.utility.driver.DriverService;
import shippingmanager.utility.generalnumber.GeneralNumberService;
import shippingmanager.utility.loadinginformation.LoadingInformation;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.loadinginformation.LoadingInformationMapper;
import shippingmanager.utility.orderdriver.OrderDriver;
import shippingmanager.utility.orderdriver.OrderDriverDao;
import shippingmanager.utility.orderdriver.OrderDriverMapper;
import shippingmanager.utility.orderdriver.OrderDriverService;

import javax.management.BadAttributeValueExpException;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final OrderDriverDao orderDriverDao;
    private final DriverService driverService;
    private final OrderDriverService orderDriverService;
    private final OrderDriverMapper orderDriverMapper;
    private final CompanyDao companyDao;
    private final LoadingInformationMapper loadingInformationMapper;
    private final GeneralNumberService generalNumberService;

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) throws BadAttributeValueExpException {
        validateOrder(createOrderRequest);
        LoadingInformation loadingInformation = loadingInformationMapper.fromDto(createOrderRequest.getLoadingInformation());
        List<Driver> drivers = driverService.createAndSaveDriversIfNotExists(createOrderRequest);

        String orderNumber = createOrderNumber(createOrderRequest.getCreatedDate());

        Order order = Order.builder()
                .createdDate(createOrderRequest.getCreatedDate())
                .paymentDate(createOrderRequest.getPaymentDate())
                .value(createOrderRequest.getValue())
                .weight(createOrderRequest.getWeight())
                .daysTillPayment(createOrderRequest.getDaysTillPayment())
                .issuedIn(createOrderRequest.getIssuedIn())
                .currency(createOrderRequest.getCurrency())
                .description(createOrderRequest.getDescription())
                .comment(createOrderRequest.getComment())
                .orderType(createOrderRequest.getOrderType().toUpperCase())
                .orderNumber(orderNumber)
                .isInvoiceCreated(false)
                .issuedIn(createOrderRequest.getIssuedIn())
                .shipper(createOrderRequest.getShipper())
                .loadingInformation(loadingInformation)
                .build();

        appendCompanies(order, createOrderRequest);

        List<OrderDriver> orderDrivers = orderDriverService.createAndSaveOrderDrivers(drivers, order);
        order.setOrderDrivers(orderDrivers);

        return orderDao.save(order);
    }

    @Transactional
    public Order updateOrder(UpdateOrderRequest updateOrderRequest) throws Exception {
        Order order = orderDao.findById(updateOrderRequest.getId())
                .orElseThrow(Exception::new);

        Company givenByCompany = companyDao.findById(updateOrderRequest.getGivenById())
                .orElseThrow(Exception::new);

        Company receivedByCompany = companyDao.findById(updateOrderRequest.getReceivedById())
                .orElseThrow(Exception::new);


        List<OrderDriver> orderDrivers = updateOrderDrivers(order, updateOrderRequest);

        LoadingInformation loadingInformation = loadingInformationMapper.fromDto(updateOrderRequest.getLoadingInformation());

        order.setValue(updateOrderRequest.getValue());
        order.setWeight(updateOrderRequest.getWeight());
        order.setIssuedIn(updateOrderRequest.getIssuedIn());
        order.setCurrency(updateOrderRequest.getCurrency());
        order.setDescription(updateOrderRequest.getDescription());
        order.setComment(updateOrderRequest.getComment());
        order.setOrderType(updateOrderRequest.getOrderType());
        order.setGivenBy(givenByCompany);
        order.setReceivedBy(receivedByCompany);
        order.setShipper(updateOrderRequest.getShipper());
        order.setOrderDrivers(orderDrivers);
        order.setLoadingInformation(loadingInformation);

        return orderDao.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) throws Exception {
        Order order = orderDao.findById(id)
                .orElseThrow(Exception::new);

        List<OrderDriver> orderDrivers = orderDriverDao.findByOrderId(order.getId());

        orderDriverDao.deleteAll(orderDrivers);
        orderDao.delete(order);
    }

    @Transactional
    public Order getOrder(Long id) throws Exception {
        return orderDao.findById(id)
                .orElseThrow(Exception::new);
    }


    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    @Transactional
    public List<Order> getMonthOrders(int month, int year) {
        return orderDao.getMonthOrders(month, year);
    }

    private void validateOrder(CreateOrderRequest createOrderRequest) throws BadAttributeValueExpException {
        String exceptionMessage = "";

        if (createOrderRequest.getCreatedDate() == null) {
            exceptionMessage += "Bad value of createdDate: " + createOrderRequest.getCreatedDate() + " ";
        }
        if (isInvalidNumber(createOrderRequest.getValue())) {
            exceptionMessage += "Bad value of value: " + createOrderRequest.getValue() + " ";
        }
        if (isInvalidNumber(createOrderRequest.getWeight())) {
            exceptionMessage += "Bad value of weight: " + createOrderRequest.getWeight() + " ";
        }
        if (isInvalidOrderType(createOrderRequest)) {
            exceptionMessage += "Bad value of orderType: " + createOrderRequest.getOrderType() + " ";
        }
        if (isInvalidMerchant(createOrderRequest)) {
            exceptionMessage += "Bad value of merchantTypes: " + "givenById: " + createOrderRequest.getGivenById() +
                    " receivedById: " + createOrderRequest.getReceivedById() + " ";
        }
        if (areInvalidDrivers(createOrderRequest)) {
            exceptionMessage += "Bad value of drivers: " + createOrderRequest.getDrivers() + " ";
        }
        if (isInvalidLoadingInformation(createOrderRequest.getLoadingInformation())) {
            exceptionMessage += "Bad value of loadingInformation: " + createOrderRequest.getLoadingInformation();
        }

        if (exceptionMessage.length() > 0) {
            throw new BadAttributeValueExpException(exceptionMessage);
        }

    }

    private String createOrderNumber(LocalDateTime createdDate) {
        Optional<Order> order = orderDao.findByMonthAndYear(createdDate.getMonth().getValue(), createdDate.getYear());
        if (order.isPresent()) {
            int slashIndex = order.get().getOrderNumber().indexOf("/");
            int number = Integer.valueOf(order.get().getOrderNumber().substring(0, slashIndex));
            return generalNumberService.generateNumber(createdDate, number + 1);
        } else {
            return generalNumberService.generateNumber(createdDate, 1);
        }
    }

    private boolean isInvalidDate(LocalDateTime localDateTime) {
        return localDateTime == null;
    }

    private boolean isInvalidNumber(BigDecimal number) {
        if (number == null) {
            return true;
        }
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            return true;
        }
        return false;
    }

    private boolean isInvalidOrderType(CreateOrderRequest createOrderRequest) {
        return !orderTypeToUpperCase(createOrderRequest).equals("GIVEN") && !orderTypeToUpperCase(createOrderRequest).equals("RECEIVED");
    }

    private String orderTypeToUpperCase(CreateOrderRequest createOrderRequest) {
        return createOrderRequest.getOrderType().toUpperCase();
    }

    private boolean isInvalidMerchant(CreateOrderRequest createOrderRequest) {
        return orderTypeToUpperCase(createOrderRequest).equals("GIVEN") && createOrderRequest.getReceivedById() != null
                && orderTypeToUpperCase(createOrderRequest).equals("RECEIVED") && createOrderRequest.getGivenById() != null;
    }

    private boolean areInvalidDrivers(CreateOrderRequest createOrderRequest) {
        if (createOrderRequest.getDrivers() == null || createOrderRequest.getDrivers().size() == 0) {
            return true;
        }
        for (DriverDto driver : createOrderRequest.getDrivers()) {
            if (driver.getName() == null || driver.getName().equals("")) {
                return true;
            }
            if (driver.getSurname() == null || driver.getSurname().equals("")) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidLoadingInformation(LoadingInformationDto loadingInformation) {
        if (loadingInformation == null) {
            return true;
        }
        if (loadingInformation.getLoadingPlace() == null || loadingInformation.getLoadingPlace().equals("")) {
            return true;
        }
        if (loadingInformation.getUnloadingPlace() == null || loadingInformation.getUnloadingPlace().equals("")) {
            return true;
        }
        if (isInvalidDate(loadingInformation.getMinLoadingDate())) {
            return true;
        }
        if (isInvalidDate(loadingInformation.getMaxLoadingDate())) {
            return true;
        }
        if (isInvalidDate(loadingInformation.getMinUnloadingDate())) {
            return true;
        }
        if (isInvalidDate(loadingInformation.getMaxUnloadingDate())) {
            return true;
        }

        return false;
    }

    private Order appendCompanies(Order order, CreateOrderRequest createOrderRequest) {
        switch (createOrderRequest.getOrderType().toUpperCase()) {
            case "GIVEN":
                Company receivedBy = companyDao.findById(createOrderRequest.getReceivedById())
                        .orElseThrow(NoSuchElementException::new);
                Company givenBy = companyDao.findByIsMainCompanyTrue();
                order.setReceivedBy(receivedBy);
                order.setGivenBy(givenBy);
                break;
            case "RECEIVED":
                receivedBy = companyDao.findByIsMainCompanyTrue();
                givenBy = companyDao.findById(createOrderRequest.getGivenById())
                        .orElseThrow(NoSuchElementException::new);
                order.setReceivedBy(receivedBy);
                order.setGivenBy(givenBy);
                break;
        }
        return order;
    }

    private List<OrderDriver> updateOrderDrivers(Order order, UpdateOrderRequest updateOrderRequest) {
        deleteOldOrderDrivers(order);
        return setNewOrderDrivers(order, updateOrderRequest);
    }

    private void deleteOldOrderDrivers(Order order) {
        for (OrderDriver orderDriver : order.getOrderDrivers()) {
            orderDriverDao.deleteById(orderDriver.getId());
        }
    }

    private List<OrderDriver> setNewOrderDrivers(Order order, UpdateOrderRequest updateOrderRequest) {
        List<OrderDriver> orderDrivers = orderDriverMapper.fromDto(updateOrderRequest.getOrderDrivers());
        for (OrderDriver orderDriver : orderDrivers) {
            orderDriver.setOrder(order);
        }

        return orderDrivers;
    }

}