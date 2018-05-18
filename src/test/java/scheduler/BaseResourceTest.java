package scheduler;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class BaseResourceTest {

    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

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

        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

}
