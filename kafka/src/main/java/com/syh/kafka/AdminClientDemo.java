package com.syh.kafka;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.admin.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AdminClientFactory {

    static Map<String, Object> props = new ImmutableMap.Builder<String, Object>()
            .put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
            .build();

    public static AdminClient create() {
        return AdminClient.create(props);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        AdminClient client = AdminClientFactory.create();

        try {
            printTotics(client);

            NewTopic topic = new NewTopic("test_" + LocalDate.now().toString(), 2, (short) 1);
            CreateTopicsResult result = client.createTopics(Collections.singleton(topic));
            result.all().get();

            printTotics(client);
        } finally {
            client.close();
        }
    }

    public static void printTotics(AdminClient client) throws ExecutionException, InterruptedException {
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true); // includes internal topics such as __consumer_offsets
        ListTopicsResult topics = client.listTopics(options);
        Set<String> topicNames = topics.names().get();
        System.out.println("Current topics in this cluster: " + topicNames);
    }

}
