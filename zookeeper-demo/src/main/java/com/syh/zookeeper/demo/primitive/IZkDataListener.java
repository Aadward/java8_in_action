package com.syh.zookeeper.demo.primitive;

import java.util.Optional;

public interface IZkDataListener {

    void onNodeDataChanged(String path, Optional<String> data);

    void onNodeDeleted(String path);
}
