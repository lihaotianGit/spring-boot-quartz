package scheduler.config.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import scheduler.config.leader.LeaderLatchGetter;

import javax.annotation.Resource;

@Component
@Order(0)
public class LeaderRunner implements ApplicationRunner {

    @Resource
    private LeaderLatchGetter leaderLatchGetter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        leaderLatchGetter.tryLead();
    }

}
