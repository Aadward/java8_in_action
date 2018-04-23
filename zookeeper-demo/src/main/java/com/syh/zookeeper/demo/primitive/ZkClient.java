package com.syh.zookeeper.demo.primitive;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ZkClient {

    private static final int DEFAULT_SESSION_TIMEOUT = 10000;

    private ZooKeeper zooKeeper;

    private int retryCount;

    private Set<IZkStateListener> stateListeners;
    private Map<String, Set<IZkDataListener>> dataListeners;
    private Map<String, Set<IZkChildListener>> childListeners;

    public ZkClient(ZooKeeper zooKeeper, int retryCount) {
        this.zooKeeper = zooKeeper;
        this.retryCount = retryCount;

        stateListeners = Collections.synchronizedSet(new HashSet<>());
        dataListeners = new ConcurrentHashMap<>();
        childListeners = new ConcurrentHashMap<>();
    }

    public static ZkClient createClient(String servers) {
        Builder builder = new Builder(servers);
        builder.sessionTimeout(DEFAULT_SESSION_TIMEOUT)
                .defaultWatcher(new PrintWatcher())
                .retry(5);
        return builder.build();
    }

    public void subscribeStateChanged(IZkStateListener listener) {
        stateListeners.add(listener);
    }

    public void subscribeDataChanged(String path, IZkDataListener listener) {
        dataListeners
                .computeIfAbsent(path, key -> Collections.synchronizedSet(new HashSet<>()))
                .add(listener);
    }

    public void subscribeChildChanged(String path, IZkChildListener listener) {
        childListeners
                .computeIfAbsent(path, key -> Collections.synchronizedSet(new HashSet<>()))
                .add(listener);
    }

    private void handleStateChanged(Watcher.Event.KeeperState state) {
        stateListeners.forEach(listener -> {
            listener.onStateChanged(state);
        });
    }

    private void handleDataChanged(WatchedEvent event) {
        String path = event.getPath();
        boolean isNodeDeleted = event.getType() == Watcher.Event.EventType.NodeDeleted;

        if (isNodeDeleted) {
            dataListeners.getOrDefault(path, new HashSet<>())
                    .forEach(listener -> listener.onNodeDeleted(path));
        } else {
            dataListeners.getOrDefault(path, new HashSet<>())
                    .forEach(listener -> listener.onNodeDataChanged(path, readData(path)));
        }
    }

    private void handleChildChanged(String path) {
        childListeners.getOrDefault(path, new HashSet<>())
                .forEach(listener -> listener.onChildChanged(path, getChildren(path)));
    }

    private Optional<String> readData(String path) {
        Stat stat = new Stat();
        try {
            byte[] data = zooKeeper.getData(path, false, stat);
            return Optional.of(new String(data));
        } catch (KeeperException.NoNodeException e) {
            return Optional.empty();
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getChildren(String path) {
        try {
            return zooKeeper.getChildren(path, false);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ZooKeeper getPrimitiveClient() {
        return this.zooKeeper;
    }

    public void registerWatcherOnNode(String nodePath, Watcher watcher)
            throws KeeperException, InterruptedException {
        zooKeeper.exists(nodePath, watcher);
    }

    public void setData(String nodePath, byte[] data, boolean createIfAbsent)
            throws KeeperException, InterruptedException {
        try {
            zooKeeper.setData(nodePath, data, -1);
        } catch (KeeperException.NoNodeException e) {
            if (createIfAbsent) {
                zooKeeper.create(nodePath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            } else {
                throw e;
            }
        }
    }

    public static class Builder {
        private String servers;
        private int sessionTimeout;
        private Watcher defaultWatcher;
        private int retryCount;

        public Builder(String servers) {
            this.servers = servers;
        }

        public Builder sessionTimeout(int sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
            return this;
        }

        public Builder defaultWatcher(Watcher watcher) {
            this.defaultWatcher = watcher;
            return this;
        }

        public Builder retry(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public ZkClient build() {
            int retry = this.retryCount;
            ZooKeeper zooKeeper;
            while (true) {
                try {
                    zooKeeper = new ZooKeeper(servers, sessionTimeout, defaultWatcher);
                    return new ZkClient(zooKeeper, retryCount);
                } catch (IOException e) {
                    if (retry-- <= 0) {
                        throw new Error("Can not connect to zookeeper server");
                    }
                }
            }
        }
    }


    private static class PrintWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            System.out.println("=====Event Arrive====\n" + event);
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        ZkClient client = ZkClient.createClient("localhost:2181");
        TimeUnit.SECONDS.sleep(2);

        client.registerWatcherOnNode("/test/exist", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeCreated) {
                    System.out.println("/test/exist created:" + event.getState());
                } else if (event.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println("/test/exist changed:" + event.getState());
                }
            }
        });

        client.setData("/test/exist", "data".getBytes(), true);
        TimeUnit.SECONDS.sleep(2);
    }

}
