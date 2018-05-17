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
public class JobService {

    private final static Logger logger = Logger.getLogger(JobService.class);

    @Resource
    private Scheduler scheduler;

    @Transactional
    public void save(JobVo jobVo) throws SchedulerException {
        JobDetail jobDetail = jobVo.getJobDetailVo().buildJobDetail();
        Set<Trigger> triggers = jobVo.getTriggerVos()
                .stream().map(v -> v.buildCronTrigger(jobDetail))
                .collect(toSet());
        scheduler.scheduleJob(jobDetail, triggers, true);
    }

    @Transactional(readOnly = true)
    public List<JobVo> findAll() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        List<JobVo> jobVos = new ArrayList<>();
        for (JobKey jobKey: jobKeys) {
            jobVos.add(findJob(jobKey));
        }
        return jobVos;
    }

    @Transactional(readOnly = true)
    public JobVo findJob(JobKey jobKey) throws SchedulerException {
        JobDetailVo jobDetailVo = JobDetailVo.JobDetail2JobDetailVo(scheduler.getJobDetail(jobKey));
        Set<TriggerVo> triggerVos = ((List<Trigger>) scheduler.getTriggersOfJob(jobKey))
                .stream()
                .map(TriggerVo::trigger2TriggerVo)
                .collect(toSet());
        return new JobVo.Builder().jobVo(jobDetailVo).triggerVos(triggerVos).build();
    }
}
