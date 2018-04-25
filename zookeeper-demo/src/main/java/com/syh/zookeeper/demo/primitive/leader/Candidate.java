package com.syh.zookeeper.demo.primitive.leader;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.syh.zookeeper.demo.primitive.IZkDataListener;
import com.syh.zookeeper.demo.primitive.ZkClient;

/**
 * A simple DEMO class for leader election, which support the base function to
 * elect.
 *
 * @author Yuhang Shen
 */
public abstract class Candidate {

    private static final String leaderPrefix = "leader_";

    private ZkClient zkClient;
    private String path;

    private ThreadPoolExecutor processThread;

    public Candidate(ZkClient zkClient, String path) {
        this.zkClient = zkClient;
        this.path = path;

        processThread = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>());
    }

    public void elect() {
        String node = zkClient.createNode(path + "/" + leaderPrefix, null,
                true, true, true);

        Optional<String> watchNode = getWatchNode(node);
        if (watchNode.isPresent()) {
            zkClient.subscribeDataChanged(watchNode.get(), new IZkDataListener() {
                @Override
                public void onNodeDataChanged(String path, Optional<String> data) {
                    //no need do anything
                }

                @Override
                public void onNodeDeleted(String path) {
                    doProcess(node);
                }
            });
        } else {
            doProcess(node);
        }
    }

    public void close() {
        processThread.shutdown();
    }

    private void doProcess(String node) {
        processThread.execute(() -> {
            process();
            zkClient.delete(node);
        });
    }

    /**
     * The processing when take leadership.
     */
    abstract void process();

    private Optional<String> getWatchNode(String node) {
        List<String> children =
                zkClient.getChildren(path, true)
                        .stream()
                        .sorted(((Comparator<String>) (s1, s2) -> {
                            int i1 = getEphemeralNumber(s1);
                            int i2 = getEphemeralNumber(s2);
                            if (i1 > i2) {
                                return 1;
                            } else if (i1 < i2) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }).reversed())
                        .collect(Collectors.toList());
        for (int i = 0; i < children.size(); i++) {
            if (node.equals(children.get(i))) {
                if (i == children.size() - 1) {
                    return Optional.empty();
                } else {
                    return Optional.of(children.get(i + 1));
                }
            }
        }

        throw new RuntimeException("Can not found node to watch");
    }

    private int getEphemeralNumber(String path) {
        return new Integer(path.substring(path.lastIndexOf("_") + 1));
    }
}
