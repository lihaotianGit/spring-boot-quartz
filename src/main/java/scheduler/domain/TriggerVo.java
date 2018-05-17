package scheduler.domain;

import org.quartz.*;
import org.springframework.util.StringUtils;

import java.text.ParseException;

public class TriggerVo {

    private String name;
    private String group;
    private String description;
    private String expression;

    public TriggerVo() {
    }

    private TriggerVo(Builder builder) {
        setName(builder.name);
        setGroup(builder.group);
        setDescription(builder.description);
        setExpression(builder.expression);
    }

    public CronTrigger buildCronTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(this.getCronExpression()))
                .withIdentity(this.getName(), this.getGroup())
                .withDescription(this.getDescription())
                .build();
    }

    private CronExpression getCronExpression() {
        if (StringUtils.isEmpty(this.getExpression())) {
            throw new IllegalArgumentException("String expression is empty.");
        }
        try {
            return new CronExpression(this.getExpression());
        } catch (ParseException e) {
            throw new IllegalArgumentException("String expression can not parse to cron expression: " + this.getExpression());
        }
    }

    public static TriggerVo trigger2TriggerVo(Trigger trigger) {
        TriggerKey triggerKey = trigger.getKey();
        String expression = null;
        if (trigger instanceof CronTrigger) {
            expression = ((CronTrigger) trigger).getCronExpression();
        }
        return new Builder()
                .name(triggerKey.getName())
                .group(triggerKey.getGroup())
                .description(trigger.getDescription())
                .expression(expression)
                .build();
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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public static final class Builder {
        private String name;
        private String group;
        private String description;
        private String expression;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder group(String val) {
            group = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder expression(String val) {
            expression = val;
            return this;
        }

        public TriggerVo build() {
            return new TriggerVo(this);
        }
    }
}
