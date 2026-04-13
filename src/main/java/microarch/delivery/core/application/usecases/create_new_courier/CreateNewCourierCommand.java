package microarch.delivery.core.application.usecases.create_new_courier;

import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.Error;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.general.Speed;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateNewCourierCommand {

    private final String name;
    private final Speed speed;

    public static Result<CreateNewCourierCommand, Error> create (String name, Speed speed) {

        var err = Guard.combine(
                Guard.againstNullOrEmpty(name,"name"),
                Guard.againstNullValueObject(speed,"speed")
        );

        if (err != null)
            return Result.failure(err);

        return Result.success(new CreateNewCourierCommand(name,speed));
    }
}
