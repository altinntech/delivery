package microarch.delivery.core.domain.model.order;

import libs.errs.Result;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderStatus {

    CREATED(1), ASSIGNED(2), COMPLETED(3);

    private final int code;

    public static Result<OrderStatus, Error> getByName(String name) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.name().equalsIgnoreCase(name)) {
                return Result.success(orderStatus);
            }
        }
        return Result.failure(Errors.orderStatusIsWrong());
    }

    public static Result<OrderStatus, Error> getByCode(int code) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.code == code) {
                return Result.success(orderStatus);
            }
        }
        return Result.failure(Errors.orderStatusIsWrong());
    }

    public static class Errors {
        public static Error orderStatusIsWrong() {
            return Error.of("orderStatus.is.wrong", "Не верное значение. Допустимые значения: "
                    + String.join(", ", Arrays.stream(values()).map(OrderStatus::name).toList()));
        }
    }

}
