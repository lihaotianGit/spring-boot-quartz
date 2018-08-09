package scheduler.config.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;
import scheduler.val.RuntimeVal;
import scheduler.val.ZooKeeperVal;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
public class LeaderLatchGetter {

    private final static Logger logger = Logger.getLogger(LeaderLatchGetter.class);

    @Resource
    private CuratorFramework zkCurator;

    @Resource
    private ZooKeeperVal zooKeeperVal;

    @Resource
    private RuntimeVal runtimeVal;

    @Resource
    private ServerProperties serverProperties;

    private LeaderLatch leaderLatch = null;

    public void tryLead() throws Exception {
        leaderLatch = new LeaderLatch(zkCurator, zooKeeperVal.getLeaderPath(), zooKeeperVal.getClientId());
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                runtimeVal.setLeader(true);
                try {
                    zkCurator.setData().forPath(zooKeeperVal.getLeaderPath(), String.valueOf(serverProperties.getPort()).getBytes());
                    logger.info("Data of Leader znode: " + new String(zkCurator.getData().forPath(zooKeeperVal.getLeaderPath()), "UTF-8"));
                } catch (Exception e) {
                    throw new RuntimeException("Set port data to leader znode error.", e);
                }
                logger.info("Leader is me, path: " + zooKeeperVal.getLeaderPath() + ", clientId: " + zooKeeperVal.getClientId());
            }

            @Override
            public void notLeader() {
                runtimeVal.setLeader(false);
                logger.info("Leader is not me, path: " + zooKeeperVal.getLeaderPath() + ",  clientId: " + zooKeeperVal.getClientId());
            }
        });

        leaderLatch.start();
    }

    @PreDestroy
    public void destroyLeaderLatch() {
        CloseableUtils.closeQuietly(leaderLatch);
        logger.info("LeaderLatch closed.");
    }

}
