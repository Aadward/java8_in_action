package com.syh.zookeeper.demo.curator.LeaderElection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;

public abstract class Candidate extends LeaderSelectorListenerAdapter
        implements Closeable {

    private LeaderSelector selector;

    public Candidate(CuratorFramework client, String leaderPath) {
        selector = new LeaderSelector(client, leaderPath, this);
    }

    abstract void process();

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        process();
    }

    public void start() {
        //Wait for taking next leadership after releasing leadership.
        selector.autoRequeue();
        selector.start();
    }

    @Override
    public void close() {
        selector.close();
    }

    public boolean isLeader() {
        return selector.hasLeadership();
    }
}
