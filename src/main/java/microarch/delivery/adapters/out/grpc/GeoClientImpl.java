package microarch.delivery.adapters.out.grpc;

import clients.geo.GeoGrpc;
import clients.geo.GeoProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.ApplicationProperties;
import microarch.delivery.core.domain.model.general.Address;
import microarch.delivery.core.domain.model.general.Location;
import microarch.delivery.core.ports.GeoClient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GeoClientImpl implements GeoClient {

    private final ManagedChannel channel;
    private final GeoGrpc.GeoBlockingStub stub;

    public GeoClientImpl(ApplicationProperties properties) {
        this.channel = ManagedChannelBuilder.forAddress(properties.getGrpc().getGeoService().getHost(),
                properties.getGrpc().getGeoService().getPort()).usePlaintext().build();
        this.stub = GeoGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdown() {
        if (!channel.isShutdown()) {
            channel.shutdown();
        }
    }

    @Override
    public Result<Location, Error> getLocation(Address address) {

        Objects.requireNonNull(address, "address");

        var request = GeoProto.GetGeolocationRequest.newBuilder().setStreet(address.getStreet()).build();

        // Вызов Geo Service
        var response = stub.getGeolocation(request);

        // Создаем Value Object
        return Result.success(Location.create(response.getLocation().getX(),response.getLocation().getY()).getValue());
    }
}
