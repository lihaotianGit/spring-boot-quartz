package scheduler.domain;

import java.util.Set;

public class JobDetailVo {

    private JobVo jobVo;
    private Set<TriggerVo> triggerVos;

    public JobDetailVo() {
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
}
