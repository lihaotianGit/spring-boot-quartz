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
import java.util.HashSet;

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

    private String host;

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
        host = "http://127.0.0.1:" + port;
        url = host + "/jobs";
    }

    @Test
    public void should_create_scheduler() {
        JobDetailVo jobDetailVo = new JobDetailVo();
        jobDetailVo.setName("myJob");
        jobDetailVo.setGroup("myJobGroup");
        jobDetailVo.setDescription("myDescription");
        jobDetailVo.setExtraInfo(new HashMap<String, Object>() {{
            put("jobType", "HTTP_JOB");
        }});

        TriggerVo triggerVo = new TriggerVo();
        triggerVo.setName("myTrigger");
        triggerVo.setGroup("myTriggerGroup");
        triggerVo.setDescription("myTriggerDescription");
        triggerVo.setExpression("0/10 * * * * ?");

        JobVo jobVo = new JobVo();
        jobVo.setJobDetailVo(jobDetailVo);
        jobVo.setTriggerVos(new HashSet<TriggerVo>() {{
            add(triggerVo);
        }});

        String postJson = toJson(jobVo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(postJson, headers);
        restTemplate.postForEntity(url, entity, String.class);
    }

    @Test
    public void should_find_all_schedulers() {
        should_create_scheduler();
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        logger.info("HttpResponse Body: " + result.getBody());
    }
}


