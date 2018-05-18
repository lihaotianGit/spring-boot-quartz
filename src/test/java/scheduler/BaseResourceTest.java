package scheduler;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Sql("classpath:clean_database.sql")
public class BaseResourceTest {

    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void before() throws SQLException {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

}
