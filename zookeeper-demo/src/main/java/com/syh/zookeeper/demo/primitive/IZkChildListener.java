package com.syh.zookeeper.demo.primitive;

import java.util.List;

public interface IZkChildListener {

    void onChildChanged(String path, List<String> children);
}
