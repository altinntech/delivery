package microarch.delivery.adapters.in.job;

import microarch.delivery.core.application.usecases.courier_move_order.CourierMoveCommand;
import microarch.delivery.core.application.usecases.courier_move_order.CourierMoveOrder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveCouriersJob implements Job {

    private final CourierMoveOrder useCase;

    @Autowired
    public MoveCouriersJob(CourierMoveOrder useCase) {
        this.useCase = useCase;
    }

    @Override
    public void execute(JobExecutionContext context) {
        useCase.handle(new CourierMoveCommand());
        System.out.println("Job: Move Courier");
    }
}