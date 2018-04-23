package com.syh.zookeeper.demo.primitive;

import static org.apache.zookeeper.Watcher.Event.KeeperState;

public interface IZkStateListener {

    void onStateChanged(KeeperState state);
}
