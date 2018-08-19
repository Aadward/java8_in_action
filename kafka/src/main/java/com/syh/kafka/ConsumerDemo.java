package com.syh.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsumerDemo {

    static final String topic = "test";

    static Map<String, Object> props = new ImmutableMap.Builder<String, Object>()
            .put("bootstrap.servers", "localhost:9092")
            .put("group.id", "test")
            .put("enable.auto.commit", "true")
            .put("auto.commit.interval.ms", "1000")
            .put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            .build();

    public static KafkaConsumer<String, Long> create() {
        return new KafkaConsumer<>(props, new StringDeserializer(), new LongDeserializer());
    }

    public static void main(String[] args) {
        KafkaConsumer<String, Long> consumer = create();

        try {
            Collection<TopicPartition> topicPartitions = consumer.partitionsFor(topic)
                    .stream()
                    .map(info -> new TopicPartition(topic, info.partition()))
                    .collect(Collectors.toList());

            consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    // 重置topic中所有partition的offset，读取所有的message
                    consumer.seekToBeginning(topicPartitions);
                }
            });
            while (true) {
                ConsumerRecords<String, Long> records = consumer.poll(10000);
                records.iterator().forEachRemaining(record -> System.out.println("key=" + record.key() + ",value=" + record.value()));
            }
        } catch (WakeupException e) {

        } finally {
            consumer.close();
        }

    }


}
