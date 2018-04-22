package com.syh.zookeeper.demo.primitive.ps;

/**
 * Custom interface for handling data changed event.
 *
 * @author syh
 */

@FunctionalInterface
public interface IZkDataListener {

    void handleDataChanged(String nodePath, byte[] data);
}
