package com.syh.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;

public class ProducerFactory {

    static Map<String, Object> props = new ImmutableMap.Builder<String, Object>()
            .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            .put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-transaction-id")
            .build();

    public static KafkaProducer<String, String> create() {
        return new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());
    }

    public static void main(String[] args) {
        KafkaProducer<String, String> producer = create();
        producer.initTransactions();
        try {
            producer.beginTransaction();
            producer.send(new ProducerRecord<>("test", "1", "1"));
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            producer.close();
        } catch (KafkaException e) {
            producer.abortTransaction();
        }

        producer.close();
    }
}
