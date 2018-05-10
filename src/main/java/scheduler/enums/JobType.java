package scheduler.enums;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum JobType {

    HTTP_JOB("scheduler.job.HttpJob"),
    MQ_JOB("scheduler.job.MQJob");

    private String classPath;

    JobType(String classPath) {
        this.classPath = classPath;
    }

    public String getClassPath() {
        return classPath;
    }

    public final static Map<String, JobType> map = ImmutableMap.<String, JobType>builder()
            .put("HTTP_JOB", HTTP_JOB)
            .put("MQ_JOB", MQ_JOB)
            .build();
}
