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
import java.util.stream.Collectors;

/**
 * A simple zookeeper client for DEMO, many exceptions are not handled and just
 * throw them inside a RuntimeException.
 *
 * @author Yuhang Shen
 */
public class ZkClient {

    private static final Logger logger = LoggerFactory.getLogger(ZkClient.class);

    private static final int DEFAULT_SESSION_TIMEOUT = 1000000000;
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
            throw new RuntimeException("Connect fail");
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
                    listener.onChildChanged(path, getChildren(path, true));
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

    public List<String> getChildren(String path, boolean fullPath) {
        try {
            List<String> children = zooKeeper.getChildren(path, false);
            if (fullPath) {
                children = children.stream()
                                   .map(s -> {
                                       String childPath = path;
                                       if (!path.endsWith("/")) {
                                           childPath += "/";
                                       }
                                       childPath += s;
                                       return childPath;
                                   })
                                   .collect(Collectors.toList());
            }
            return children;
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ZooKeeper getPrimitiveClient() {
        return this.zooKeeper;
    }

    public void setData(String nodePath, byte[] data, boolean createIfAbsent)
            throws RuntimeException {
        try {
            zooKeeper.setData(nodePath, data, -1);
        } catch (KeeperException.NoNodeException e) {
            if (createIfAbsent) {
                try {
                    zooKeeper.create(nodePath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new RuntimeException(e);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String createNode(String nodePath, byte[] data, boolean ephemeral, boolean sequential,
                           boolean createParentIfAbsent) {
        CreateMode mode;
        if (ephemeral && sequential) {
            mode = CreateMode.EPHEMERAL_SEQUENTIAL;
        } else if (ephemeral) {
            mode = CreateMode.EPHEMERAL;
        } else if (sequential) {
            mode = CreateMode.PERSISTENT_SEQUENTIAL;
        } else {
            mode = CreateMode.PERSISTENT;
        }

        while (true) {
            try {
                return zooKeeper.create(nodePath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
            } catch (KeeperException.NoNodeException e) {
                if (createParentIfAbsent) {
                    try {
                        createNode(getParent(nodePath), null, false, false, true);
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (KeeperException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete(String path) {
        try {
            zooKeeper.delete(path, -1);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getParent(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    private void watchNode(String path) {
        try {
            zooKeeper.exists(path, true);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void watchChild(String path) {
        try {
            zooKeeper.getChildren(path, true);
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            zooKeeper.close();
            eventThread.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class DefaultWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            logger.debug("=====Event Arrive====\n" + event);

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


    /**
     * Base usage, just for test.
     */
    public static void main(String[] args) throws InterruptedException {
        ZkClient client = ZkClient.createClient("localhost:2181");
        client.subscribeStateChanged(state -> {
            System.out.println("state changed: " + state);
        });
        //wait for connect.
        while (!client.getPrimitiveClient().getState().isConnected()) {
            TimeUnit.SECONDS.sleep(1);
        }

        //init node value
        client.setData("/test", "init".getBytes(), true);

        //register node changed listener.
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

        //register child changed listener.
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
