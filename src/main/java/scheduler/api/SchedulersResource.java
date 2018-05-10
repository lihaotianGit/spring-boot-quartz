package scheduler.api;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import scheduler.domain.JobDetailVo;
import scheduler.service.SchedulerService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/schedulers")
public class SchedulersResource {

    private final static Logger logger = Logger.getLogger(SchedulersResource.class);

    @Resource
    private SchedulerService schedulerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody JobDetailVo jobDetailVo) {
        try {
            schedulerService.save(jobDetailVo);
        } catch (Exception e) {
            logger.error("Create scheduler error.", e);
        }
    }

}
