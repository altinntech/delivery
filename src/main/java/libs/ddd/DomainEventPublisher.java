package libs.ddd;

public interface DomainEventPublisher {

    //void publish(Iterable<Aggregate<?>> aggregates);

    void publish(Iterable<? extends Aggregate<?>> aggregates);
}