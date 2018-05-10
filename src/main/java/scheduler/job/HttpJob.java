package scheduler.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HttpJob implements Job {

    private final static Logger logger = Logger.getLogger(HttpJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Execute http job.");
    }

}
