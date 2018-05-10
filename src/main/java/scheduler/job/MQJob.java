package scheduler.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MQJob implements Job {

    private final static Logger logger = Logger.getLogger(MQJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Execute MQ job.");
    }
}
