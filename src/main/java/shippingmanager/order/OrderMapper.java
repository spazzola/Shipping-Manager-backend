package shippingmanager.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import shippingmanager.company.CompanyDto;
import shippingmanager.company.CompanyMapper;
import shippingmanager.utility.loadinginformation.LoadingInformationDto;
import shippingmanager.utility.loadinginformation.LoadingInformationMapper;
import shippingmanager.utility.orderdriver.OrderDriverDto;
import shippingmanager.utility.orderdriver.OrderDriverMapper;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class OrderMapper {

    CompanyMapper companyMapper;
    OrderDriverMapper orderDriverMapper;
    LoadingInformationMapper loadingInformationMapper;

    public OrderDto toDto(Order order) {
        CompanyDto givenByDto = companyMapper.toDto(order.getGivenBy());
        CompanyDto receivedByDto = companyMapper.toDto(order.getReceivedBy());
        List<OrderDriverDto> orderDriversDto = orderDriverMapper.toDto(order.getOrderDrivers());
        LoadingInformationDto loadingInformationDto = loadingInformationMapper.toDto(order.getLoadingInformation());

        return OrderDto.builder()
                .id(order.getId())
                .createdDate(order.getCreatedDate())
                .paymentDate(order.getPaymentDate())
                .value(order.getValue())
                .weight(order.getWeight())
                .description(order.getDescription())
                .orderNumber(order.getOrderNumber())
                .isInvoiceCreated(order.isInvoiceCreated())
                .givenBy(givenByDto)
                .receivedBy(receivedByDto)
                .orderDrivers(orderDriversDto)
                .loadingInformation(loadingInformationDto)
                .build();
    }

    public List<OrderDto> toDto(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
