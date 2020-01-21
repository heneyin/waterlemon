package com.henvealf.watermelon.tokafka;

import com.google.common.base.Preconditions;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 *
 * Kafka 数据发送器。
 * @author hongliang.yin/Henvealf
 * @date 2019-06-20
 */
public class KafkaSender {

    private static KafkaProducer<String, String> producer;
    private static Logger logger = LoggerFactory.getLogger(KafkaSender.class);


    public static void init(Properties kafkaProducerProp) {
        producer = new KafkaProducer<String, String>(kafkaProducerProp);
    }

    public static void send(String topicName, String message) {
        Preconditions.checkNotNull(producer, "kafka producer is null");
        producer.send(new ProducerRecord<String, String>(topicName, message));
    }

    public static void stop() {
        if (producer != null) {
            producer.flush();
            producer.close();
        }
        logger.info("Stopped KafkaSender");
        System.out.println("Stopped KafkaSender");
    }

    private KafkaSender(){}

}
