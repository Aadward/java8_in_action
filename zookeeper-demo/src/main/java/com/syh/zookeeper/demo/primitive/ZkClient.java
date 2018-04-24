package com.syh.zookeeper.demo.primitive;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple zookeeper client for DEMO, many exceptions are not handled and just
 * throw them inside a RuntimeException.
 *
 * @author Yuhang Shen
 */
public class ZkClient {

    private static final int DEFAULT_SESSION_TIMEOUT = 10000;
    private static final int N_THREAD = Runtime.getRuntime().availableProcessors();

    private ZooKeeper zooKeeper;

    private Set<IZkStateListener> stateListeners;
    private Map<String, Set<IZkDataListener>> dataListeners;
    private Map<String, Set<IZkChildListener>> childListeners;

    private ThreadPoolExecutor eventThread;

    private ZkClient(String servers, int sessionTimeout) {
        try {
            this.zooKeeper = new ZooKeeper(servers, sessionTimeout, new DefaultWatcher());
        } catch (IOException e) {
            throw new Error("Connect fail");
        }

        stateListeners = Collections.synchronizedSet(new HashSet<>());
        dataListeners = new ConcurrentHashMap<>();
        childListeners = new ConcurrentHashMap<>();

        eventThread = new ThreadPoolExecutor(N_THREAD, N_THREAD, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>());
    }

    public static ZkClient createClient(String servers) {
        return new ZkClient(servers, DEFAULT_SESSION_TIMEOUT);
    }

    public void subscribeStateChanged(IZkStateListener listener) {
        stateListeners.add(listener);
        watchNode("/");
    }

    public void subscribeDataChanged(String path, IZkDataListener listener) {
        dataListeners
                .computeIfAbsent(path, key -> Collections.synchronizedSet(new HashSet<>()))
                .add(listener);
        watchNode(path);
    }

    public void subscribeChildChanged(String path, IZkChildListener listener) {
        childListeners
                .computeIfAbsent(path, key -> Collections.synchronizedSet(new HashSet<>()))
                .add(listener);
        watchChild(path);
    }

    private void handleStateChanged(Watcher.Event.KeeperState state) {
        stateListeners.forEach(listener -> {
            watchNode("/");
            listener.onStateChanged(state);
        });
    }

    private void handleNodeChanged(WatchedEvent event) {
        String path = event.getPath();
        boolean isNodeDeleted = event.getType() == Watcher.Event.EventType.NodeDeleted;

        if (isNodeDeleted) {
            dataListeners.getOrDefault(path, new HashSet<>())
                    .forEach(listener -> {
                        watchNode(path);
                        listener.onNodeDeleted(path);
                    });
        } else {
            dataListeners.getOrDefault(path, new HashSet<>())
                    .forEach(listener -> {
                        watchNode(path);
                        listener.onNodeDataChanged(path, readData(path));
                    });
        }
    }

    private void handleChildChanged(String path) {
        childListeners.getOrDefault(path, new HashSet<>())
                .forEach(listener -> {
                    watchChild(path);
                    listener.onChildChanged(path, getChildren(path));
                });
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

    private void watchNode(String path) {
        try {
            zooKeeper.exists(path, true);
        } catch (KeeperException | InterruptedException e) {
            throw new Error(e);
        }
    }

    private void watchChild(String path) {
        try {
            zooKeeper.getChildren(path, true);
        } catch (KeeperException | InterruptedException e) {
            throw new Error(e);
        }
    }

    public void close() {
        try {
            zooKeeper.close();
            eventThread.shutdown();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    private class DefaultWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            System.out.println("=====Event Arrive====\n" + event);

            if (event.getType() == Event.EventType.None) {
                eventThread.execute(() -> handleStateChanged(event.getState()));
            } else if (event.getType() == Event.EventType.NodeCreated
                    || event.getType() == Event.EventType.NodeDataChanged
                    || event.getType() == Event.EventType.NodeDeleted) {
                eventThread.execute(() -> handleNodeChanged(event));
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                eventThread.execute(() -> handleChildChanged(event.getPath()));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ZkClient client = ZkClient.createClient("localhost:2181");
        client.subscribeStateChanged(state -> {
            System.out.println("state changed: " + state);
        });
        TimeUnit.SECONDS.sleep(2);

        client.subscribeDataChanged("/test", new IZkDataListener() {
            @Override
            public void onNodeDataChanged(String path, Optional<String> data) {
                System.out.println("node changed, new data:" + data.orElse(""));
            }

            @Override
            public void onNodeDeleted(String path) {
                System.out.println("node deleted");
            }
        });

        client.subscribeChildChanged("/test", new IZkChildListener() {
            @Override
            public void onChildChanged(String path, List<String> children) {
                System.out.println("child changed, children=" + children);
            }
        });

        System.out.println("PRESS <ENTER> TO EXIT");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        client.close();

    }

}
