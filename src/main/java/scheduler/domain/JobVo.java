package scheduler.domain;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.util.ClassUtils;
import scheduler.enums.JobType;
import scheduler.utils.MapHelper;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class JobVo {

    @NotNull
    private String name;

    @NotNull
    private String group;

    @NotNull
    private String description;

    @NotNull
    private Map<String, Object> extraInfo;

    private final static String JOB_TYPE = "jobType";

    public JobVo() {
    }

    public JobDetail buildJobDetail() {
        if (MapHelper.isBlank(extraInfo) || !extraInfo.containsKey(JOB_TYPE)) {
            throw new IllegalArgumentException("Job extraInfo is empty, or extraInfo do not contains key 'jobType'.");
        }
        return JobBuilder.newJob()
                .ofType(getClassType())
                .withIdentity(this.getName(), this.getGroup())
                .withDescription(this.getDescription())
                .setJobData(getJobDataMap())
                .build();
    }

    private JobDataMap getJobDataMap() {
        JobDataMap jobDataMap = new JobDataMap();
        extraInfo.forEach(jobDataMap::put);
        return jobDataMap;
    }

    private Class<Job> getClassType() {
        return (Class<Job>) ClassUtils.resolveClassName(JobType.map.get(extraInfo.get(JOB_TYPE)).getClassPath(), this.getClass().getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
