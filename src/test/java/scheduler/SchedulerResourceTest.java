package scheduler;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import scheduler.domain.JobDetailVo;
import scheduler.domain.JobVo;
import scheduler.domain.TriggerVo;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static scheduler.utils.JsonHelper.toJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulerResourceTest {

    private final static Logger logger = Logger.getLogger(SchedulerResourceTest.class);

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void before() throws SQLException {
        String sql =
                "SET FOREIGN_KEY_CHECKS = 0;" +
                "TRUNCATE TABLE QRTZ_BLOB_TRIGGERS;" +
                "TRUNCATE TABLE QRTZ_CALENDARS;" +
                "TRUNCATE TABLE QRTZ_CRON_TRIGGERS;" +
                "TRUNCATE TABLE QRTZ_FIRED_TRIGGERS;" +
                "TRUNCATE TABLE QRTZ_JOB_DETAILS;" +
                "TRUNCATE TABLE QRTZ_LOCKS;" +
                "TRUNCATE TABLE QRTZ_PAUSED_TRIGGER_GRPS;" +
                "TRUNCATE TABLE QRTZ_SCHEDULER_STATE;" +
                "TRUNCATE TABLE QRTZ_SIMPLE_TRIGGERS;" +
                "TRUNCATE TABLE QRTZ_SIMPROP_TRIGGERS;" +
                "TRUNCATE TABLE QRTZ_TRIGGERS;" +
                "SET FOREIGN_KEY_CHECKS = 1;";
        SqlSession session = sqlSessionFactory.openSession();
        session.getConnection().prepareStatement(sql).execute();
        session.commit();
        session.close();

        String host = "http://127.0.0.1:" + port;
        url = host + "/jobs";
    }

    @Test
    public void should_create_scheduler() {
        String postJson = toJson(buildJobVo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(postJson, headers);
        restTemplate.postForEntity(url, entity, String.class);
    }

    private JobVo buildJobVo() {
        JobDetailVo jobDetailVo = new JobDetailVo.Builder()
                .name("myJob")
                .group("myJobGroup")
                .description("myDescription")
                .extraInfo(new HashMap<String, Object>() {{
                    put("jobType", "HTTP_JOB");
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
    public void should_find_all_schedulers() {
        should_create_scheduler();
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        logger.info("HttpResponse Body: " + result.getBody());
    }

    @Test
    public void should_find_job() {
        JobVo jobVo = buildJobVo();
        String url = this.url + "/" + jobVo.getJobDetailVo().getGroup() + "/" + jobVo.getJobDetailVo().getName();
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        logger.info("HttpResponse Body: " + result.getBody());
    }
}


