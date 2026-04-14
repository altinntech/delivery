package microarch.delivery.core.application.view.all_couriers;

import microarch.delivery.core.application.view.dto.CourierDto;

import java.util.List;

public record GetAllCouriersResponse(List<CourierDto> couriers) {
}
