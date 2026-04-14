package microarch.delivery.core.application.view.uncompleted_orders;

import microarch.delivery.core.application.view.dto.OrderDto;

import java.util.List;

public record GetUncompletedOrdersResponse (List<OrderDto> orderDtoList){
}
