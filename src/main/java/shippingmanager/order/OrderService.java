package shippingmanager.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shippingmanager.company.Company;
import shippingmanager.company.CompanyDao;
import shippingmanager.utility.driver.Driver;
import shippingmanager.utility.driver.DriverService;
import shippingmanager.utility.loadinginformation.LoadingInformation;
import shippingmanager.utility.loadinginformation.LoadingInformationMapper;
import shippingmanager.utility.orderdriver.OrderDriver;
import shippingmanager.utility.orderdriver.OrderDriverService;

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
    public Order createOrder(CreateOrderRequest createOrderRequest) {
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
            default:
                throw new RuntimeException("Wrong order type");
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
        String result = orderNumber.substring( 0, orderNumber.indexOf("/"));

        return Integer.valueOf(result);
    }

}
