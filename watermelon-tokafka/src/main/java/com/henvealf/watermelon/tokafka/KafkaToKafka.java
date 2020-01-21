package com.henvealf.watermelon.tokafka;

import com.henvealf.watermelon.common.ConfigWm;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author hongliang.yin/Henvealf
 * @date 2019-12-23
 */
public class KafkaToKafka {

    public static void main(String[] args) {
        String topic = "Asap-Event";

        List<String> topics = new ArrayList<>();
        topics.add(topic);

        Properties kafkaProducerProp = ConfigWm.getPropertiesByClassPath("kafka-producer.properties");
        Properties kafkaConsumerProp = ConfigWm.getPropertiesByClassPath("kafka-consumer.properties");

        KafkaSender.init(kafkaProducerProp);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaConsumerProp);
        System.out.println("------subscribe");
        consumer.subscribe(topics);
        System.out.println("subscribe");
        int count = 0;
        try {
            while (true) {
                ConsumerRecords<String, String> records;
                records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();
                    KafkaSender.send("coalesce-v3-performance", value);
                    count ++;
                    if (count % 1000 == 0) {
                        System.out.println(count);
                    }
                }
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }

}
