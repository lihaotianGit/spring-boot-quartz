package scheduler.api;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scheduler.domain.JobVo;
import scheduler.service.JobService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static scheduler.utils.URIHelper.createUri;

@RestController
@RequestMapping("/jobs")
public class JobsResource {

    private final static Logger logger = Logger.getLogger(JobsResource.class);

    @Resource
    private JobService jobService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody JobVo jobVo) throws SchedulerException {
        jobService.save(jobVo);
        return ResponseEntity.created(createUri("/{group}/{name}", jobVo.getJobDetailVo().getGroup(), jobVo.getJobDetailVo().getName()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<JobVo>> findAll() throws SchedulerException {
        return ResponseEntity.ok().body(jobService.findAll());
    }

    @GetMapping("/{group}/{name}")
    public ResponseEntity<JobVo> findJob(@PathVariable String name,
                                         @PathVariable String group) throws SchedulerException {
        return ResponseEntity.ok().body(jobService.findJob(new JobKey(name, group)));
    }

    /**
     * 立即触发一次任务
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    @PutMapping("/trigger/{group}/{name}")
    public ResponseEntity triggerJob(@PathVariable String name,
                                     @PathVariable String group) throws SchedulerException {
        jobService.triggerJob(new JobKey(name, group));
        return ResponseEntity.noContent().build();
    }

    /**
     * 暂停任务
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    @PutMapping("/pause/{group}/{name}")
    public ResponseEntity pauseJob(@PathVariable String name,
                                   @PathVariable String group) throws SchedulerException {
        jobService.pauseJob(new JobKey(name, group));
        return ResponseEntity.noContent().build();
    }

    /**
     * 恢复（已暂停）任务
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    @PutMapping("/resume/{group}/{name}")
    public ResponseEntity resumeJob(@PathVariable String name,
                                    @PathVariable String group) throws SchedulerException {
        jobService.resumeJob(new JobKey(name, group));
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除任务
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    @DeleteMapping("/{group}/{name}")
    public ResponseEntity deleteJob(@PathVariable String name,
                                    @PathVariable String group) throws SchedulerException {
        jobService.deleteJob(new JobKey(name, group));
        return ResponseEntity.noContent().build();
    }

}
