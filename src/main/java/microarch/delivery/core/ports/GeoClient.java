package microarch.delivery.core.ports;

import libs.errs.Result;
import libs.errs.Error;
import microarch.delivery.core.domain.model.general.Address;
import microarch.delivery.core.domain.model.general.Location;

public interface GeoClient {

    Result<Location,Error> getLocation (Address address);

}
