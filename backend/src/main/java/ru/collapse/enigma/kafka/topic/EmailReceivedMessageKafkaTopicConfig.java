package ru.collapse.enigma.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import ru.collapse.enigma.kafka.KafkaTopic;

@Configuration
public class EmailReceivedMessageKafkaTopicConfig {

    @Bean
    public NewTopic presentationSavedOrUpdated() {
        return TopicBuilder.name(KafkaTopic.EMAIL_RECEIVED)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
