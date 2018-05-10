package scheduler;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @LocalServerPort
    private int port;

    private String host = "http://127.0.0.1:";

    @Before
    public void before() throws SQLException {
        String sql = "SET FOREIGN_KEY_CHECKS = 0;" +
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
    }

    @Test
    public void should_create_scheduler() {
        JobVo jobVo = new JobVo();
        jobVo.setName("myJob");
        jobVo.setGroup("myJobGroup");
        jobVo.setDescription("myDescription");
        jobVo.setExtraInfo(new HashMap<String, Object>() {{
            put("jobType", "HTTP_JOB");
        }});

        TriggerVo triggerVo = new TriggerVo();
        triggerVo.setName("myTrigger");
        triggerVo.setGroup("myTriggerGroup");
        triggerVo.setDescription("myTriggerDescription");
        triggerVo.setExpression("0/10 * * * * ?");

        JobDetailVo jobDetailVo = new JobDetailVo();
        jobDetailVo.setJobVo(jobVo);
        jobDetailVo.setTriggerVos(new HashSet<TriggerVo>() {{
            add(triggerVo);
        }});

        String postJson = toJson(jobDetailVo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(postJson, headers);
        restTemplate.postForEntity(host + port + "/schedulers", entity, String.class);
    }


}


