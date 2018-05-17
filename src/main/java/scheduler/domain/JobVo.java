package scheduler.domain;

import java.util.Set;

public class JobVo {

    private JobDetailVo jobDetailVo;
    private Set<TriggerVo> triggerVos;

    public JobVo() {
    }

    private JobVo(Builder builder) {
        setJobDetailVo(builder.jobDetailVo);
        setTriggerVos(builder.triggerVos);
    }

    public JobDetailVo getJobDetailVo() {
        return jobDetailVo;
    }

    public void setJobDetailVo(JobDetailVo jobDetailVo) {
        this.jobDetailVo = jobDetailVo;
    }

    public Set<TriggerVo> getTriggerVos() {
        return triggerVos;
    }

    public void setTriggerVos(Set<TriggerVo> triggerVos) {
        this.triggerVos = triggerVos;
    }

    public static final class Builder {
        private JobDetailVo jobDetailVo;
        private Set<TriggerVo> triggerVos;

        public Builder() {
        }

        public Builder jobVo(JobDetailVo val) {
            jobDetailVo = val;
            return this;
        }

        public Builder triggerVos(Set<TriggerVo> val) {
            triggerVos = val;
            return this;
        }

        public JobVo build() {
            return new JobVo(this);
        }
    }
}
