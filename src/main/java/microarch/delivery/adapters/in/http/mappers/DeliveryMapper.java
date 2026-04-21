package microarch.delivery.adapters.in.http.mappers;

import microarch.delivery.adapters.in.http.model.Courier;
import microarch.delivery.adapters.in.http.model.Location;
import microarch.delivery.adapters.in.http.model.Order;

import microarch.delivery.core.application.view.dto.CourierDto;
import microarch.delivery.core.application.view.dto.LocationDto;
import microarch.delivery.core.application.view.dto.OrderDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeliveryMapper {

    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    Location locationToHttp (LocationDto locationDto);

    @Mapping(target = "location", source = "courierDto.locationDto")
    Courier courierToHttp(CourierDto courierDto);
    List<Courier> couriersToHttp(List<CourierDto> courierDtoList);

    @Mapping(target = "location", source = "orderDto.locationDto")
    Order orderToHttp (OrderDto orderDto);
    List<Order> ordersToHttp(List<OrderDto> orderDtoList);

}
