package microarch.delivery.core.application.view.dto;

import java.util.UUID;

public record CourierDto (UUID id, String name, LocationDto locationDto) {
}
