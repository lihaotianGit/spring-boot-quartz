package scheduler.api;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scheduler.domain.JobVo;
import scheduler.service.JobService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobsResource {

    private final static Logger logger = Logger.getLogger(JobsResource.class);

    @Resource
    private JobService jobService;

    @PostMapping
    public ResponseEntity create(@RequestBody JobVo jobVo) throws SchedulerException {
        jobService.save(jobVo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

}
