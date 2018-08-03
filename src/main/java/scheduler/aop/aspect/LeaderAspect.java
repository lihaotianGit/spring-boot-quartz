package scheduler.aop.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import scheduler.application.val.RuntimeVal;

import javax.annotation.Resource;

@Aspect
@Component
public class LeaderAspect {

    private final static Logger logger = Logger.getLogger(LeaderAspect.class);

    @Resource
    private RuntimeVal runtimeVal;

    @Pointcut("@annotation(scheduler.aop.annotation.LeaderInterceptive)")
    public void leaderInterceptive() {}

    @Around("leaderInterceptive()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        if (runtimeVal.isLeader()) {
            logger.info("I'm leader, I do the job.");
            pjp.proceed();
        } else {
            logger.info("I'm not leader, I don't want to do job.");
        }
        return null;
    }

}
