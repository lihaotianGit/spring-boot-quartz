package scheduler.service;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scheduler.domain.JobDetailVo;

import javax.annotation.Resource;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class SchedulerService {

    private final static Logger logger = Logger.getLogger(SchedulerService.class);

    @Resource
    private Scheduler scheduler;

    @Transactional
    public void save(JobDetailVo jobDetailVo) throws SchedulerException {
        JobDetail jobDetail = jobDetailVo.getJobVo().buildJobDetail();
        Set<Trigger> triggers = jobDetailVo.getTriggerVos()
                .stream().map(v -> v.buildCronTrigger(jobDetail))
                .collect(toSet());
        scheduler.scheduleJob(jobDetail, triggers, true);
    }
}
