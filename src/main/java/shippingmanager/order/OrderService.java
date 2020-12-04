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
import shippingmanager.utility.loadinginformation.LoadingInformation;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.loadinginformation.LoadingInformationMapper;
import shippingmanager.utility.orderdriver.OrderDriver;
import shippingmanager.utility.orderdriver.OrderDriverService;
import shippingmanager.utility.plate.PlateDto;

import javax.management.BadAttributeValueExpException;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final DriverService driverService;
    private final OrderDriverService orderDriverService;
    private final CompanyDao companyDao;
    private final LoadingInformationMapper loadingInformationMapper;

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) throws BadAttributeValueExpException {
        validateOrder(createOrderRequest);
        LoadingInformation loadingInformation = loadingInformationMapper.fromDto(createOrderRequest.getLoadingInformation());
        List<Driver> drivers = driverService.createDrivers(createOrderRequest);

        String orderNumber = generateOrderNumber(createOrderRequest.getCreatedDate());

        Order order = Order.builder()
                .createdDate(createOrderRequest.getCreatedDate())
                .paymentDate(createOrderRequest.getPaymentDate())
                .value(createOrderRequest.getValue())
                .weight(createOrderRequest.getWeight())
                .description(createOrderRequest.getDescription())
                .orderType(createOrderRequest.getOrderType())
                .orderNumber(orderNumber)
                .loadingInformation(loadingInformation)
                .build();

        order = appendCompanies(order, createOrderRequest);

        List<OrderDriver> orderDrivers = orderDriverService.createOrderDrivers(drivers, order);
        order.setOrderDrivers(orderDrivers);

        return orderDao.save(order);
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
            if (driver.getPlates() == null || driver.getPlates().size() == 0) {
                return true;
            } else if (driver.getPlates().size() > 0) {
                for (PlateDto plate : driver.getPlates()) {
                    if (plate.getPlateNumber().equals("")) {
                        return true;
                    }
                }
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

    private String generateOrderNumber(LocalDateTime localDateTime) {
        Optional<Order> previousOrder = orderDao.findTopByOrderByIdDesc();
        int newNumber;

        if (previousOrder.isPresent()) {
            newNumber = extractOrderNumber(previousOrder.get()) + 1;
        } else {
            newNumber = 1;
        }

        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();

        return newNumber + "/" + month + "/" + year;
    }

    private int extractOrderNumber(Order order) {
        String orderNumber = order.getOrderNumber();
        String result = orderNumber.substring(0, orderNumber.indexOf("/"));

        return Integer.valueOf(result);
    }

}
