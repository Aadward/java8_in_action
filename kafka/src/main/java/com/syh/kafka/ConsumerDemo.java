package com.syh.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;

public class ConsumerDemo {

    static Map<String, Object> props = new ImmutableMap.Builder<String, Object>()
            .put("bootstrap.servers", "localhost:9092")
            .put("group.id", "test")
            .put("enable.auto.commit", "true")
            .put("auto.commit.interval.ms", "1000")
            .put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            .build();

    public static KafkaConsumer<String, String> create() {
        return new KafkaConsumer<>(props, new StringDeserializer(), new StringDeserializer());
    }

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = create();

        try {
            consumer.subscribe(Collections.singletonList("test"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                records.iterator().forEachRemaining(record -> System.out.println("key=" + record.key() + ",value=" + record.value()));
            }
        } catch (WakeupException e) {

        } finally {
            consumer.close();
        }

    }


}
