package com.syh.springkafka;

import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

public class Listener {

    public final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(id = "test", groupId = "test-group", topics = "test-topic")
    public void listen1(String foo) {
        this.latch.countDown();
    }
}
