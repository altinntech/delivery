package microarch.delivery.adapters.in.job;

import microarch.delivery.core.application.usecases.assign_courier.AssignCourier;
import microarch.delivery.core.application.usecases.assign_courier.AssignCourierCommand;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignOrdersJob implements Job {

    private final AssignCourier useCase;

    @Autowired
    public AssignOrdersJob(AssignCourier useCase) {
        this.useCase = useCase;
    }

    @Override
    public void execute(JobExecutionContext context) {
        useCase.handle(new AssignCourierCommand());
        System.out.println("Job: Assign Order");
    }
}