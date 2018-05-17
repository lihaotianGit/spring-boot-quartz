package scheduler.domain;

import java.util.Set;

public class JobDetailVo {

    private JobVo jobVo;
    private Set<TriggerVo> triggerVos;

    public JobDetailVo() {
    }

    private JobDetailVo(Builder builder) {
        setJobVo(builder.jobVo);
        setTriggerVos(builder.triggerVos);
    }

    public JobVo getJobVo() {
        return jobVo;
    }

    public void setJobVo(JobVo jobVo) {
        this.jobVo = jobVo;
    }

    public Set<TriggerVo> getTriggerVos() {
        return triggerVos;
    }

    public void setTriggerVos(Set<TriggerVo> triggerVos) {
        this.triggerVos = triggerVos;
    }

    public static final class Builder {
        private JobVo jobVo;
        private Set<TriggerVo> triggerVos;

        public Builder() {
        }

        public Builder jobVo(JobVo val) {
            jobVo = val;
            return this;
        }

        public Builder triggerVos(Set<TriggerVo> val) {
            triggerVos = val;
            return this;
        }

        public JobDetailVo build() {
            return new JobDetailVo(this);
        }
    }
}
