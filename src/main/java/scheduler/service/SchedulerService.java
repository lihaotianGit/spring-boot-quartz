package scheduler.service;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scheduler.domain.JobDetailVo;
import scheduler.domain.JobVo;
import scheduler.domain.TriggerVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional(readOnly = true)
    public List<JobDetailVo> findAll() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        List<JobDetailVo> jobDetailVos = new ArrayList<>();
        for (JobKey jobKey: jobKeys) {
            JobVo jobVo = JobVo.JobDetail2JobVo(scheduler.getJobDetail(jobKey));
            Set<TriggerVo> triggerVos = ((List<Trigger>) scheduler.getTriggersOfJob(jobKey))
                    .stream()
                    .map(TriggerVo::trigger2TriggerVo)
                    .collect(toSet());
            jobDetailVos.add(new JobDetailVo.Builder().jobVo(jobVo).triggerVos(triggerVos).build());
        }
        return jobDetailVos;
    }
}
