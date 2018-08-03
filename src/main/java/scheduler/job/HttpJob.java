package scheduler.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import scheduler.aop.annotation.LeaderInterceptive;

public class HttpJob implements Job {

    private final static Logger logger = Logger.getLogger(HttpJob.class);

    @LeaderInterceptive
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Execute http job.");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        logger.info("Requesting to " + jobDataMap.get("url"));
    }

}
