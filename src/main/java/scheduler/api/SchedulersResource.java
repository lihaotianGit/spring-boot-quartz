package scheduler.api;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import scheduler.domain.JobDetailVo;
import scheduler.service.SchedulerService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/schedulers")
public class SchedulersResource {

    private final static Logger logger = Logger.getLogger(SchedulersResource.class);

    @Resource
    private SchedulerService schedulerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody JobDetailVo jobDetailVo) throws SchedulerException {
        schedulerService.save(jobDetailVo);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<JobDetailVo> findAll() throws SchedulerException {
        return schedulerService.findAll();
    }

}
