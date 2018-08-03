package scheduler;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.MediaType;
import scheduler.domain.JobDetailVo;
import scheduler.domain.JobVo;
import scheduler.domain.TriggerVo;

import java.util.HashMap;

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static scheduler.utils.JsonHelper.toJson;

public class JobsResourceTest extends BaseResourceTest {

    private final static Logger logger = Logger.getLogger(JobsResourceTest.class);

    @Test
    public void should_create_scheduler() throws Exception {
        String postJson = toJson(buildJobVo());

        mvc.perform(post("/jobs").contentType(MediaType.APPLICATION_JSON).content(postJson))
                .andExpect(status().isCreated());
    }

    private JobVo buildJobVo() {
        JobDetailVo jobDetailVo = new JobDetailVo.Builder()
                .name("myJob")
                .group("myJobGroup")
                .description("myDescription")
                .extraInfo(new HashMap<String, Object>() {{
                    put("jobType", "HTTP_JOB");
                    put("url", "https://www.baidu.com");
                }})
                .build();

        TriggerVo triggerVo = new TriggerVo.Builder()
                .name("myTrigger")
                .group("myTriggerGroup")
                .description("myTriggerDescription")
                .expression("0/10 * * * * ?")
                .build();

        return new JobVo.Builder()
                .jobVo(jobDetailVo)
                .triggerVos(asSet(triggerVo))
                .build();
    }

    @Test
    public void should_find_all_schedulers() throws Exception {
        should_create_scheduler();
        mvc.perform(get("/jobs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_find_job() throws Exception {
        should_create_scheduler();
        JobVo jobVo = buildJobVo();
        String url = "/jobs/" + jobVo.getJobDetailVo().getGroup() + "/" + jobVo.getJobDetailVo().getName();
        mvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println();
    }
}


