package com.syh.springkafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = TestConfig.class)
@RunWith(SpringRunner.class)
@EmbeddedKafka(topics = "test-topic")
public class SpringKafkaTest {

    @Autowired
    private Listener listener;

    @Autowired
    private KafkaEmbedded embeddedKafka;

    @Autowired
    private KafkaTemplate<Integer, String> template;

    @Test
    public void test() throws Exception {
        template.send("test-topic", 0, "foo");
        template.flush();
        assertTrue(this.listener.latch.await(10, TimeUnit.SECONDS));
    }

}
