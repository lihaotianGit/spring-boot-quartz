package scheduler.application.val;

import org.springframework.stereotype.Component;

@Component
public class RuntimeVal {

    private volatile boolean isLeader;

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}
