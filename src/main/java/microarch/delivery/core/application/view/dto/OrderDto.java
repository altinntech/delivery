package microarch.delivery.core.application.view.dto;

import java.util.UUID;

public record OrderDto(UUID id, LocationDto locationDto) {
}
