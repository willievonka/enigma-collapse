package ru.collapse.enigma.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import ru.collapse.enigma.kafka.KafkaTopic;

@Configuration
public class TicketProcessTaskMessage {

    @Bean
    public NewTopic ticketProcessTask() {
        return TopicBuilder.name(KafkaTopic.TICKET_PROCESS_TASK)
                .partitions(5)
                .replicas(1)
                .build();
    }

}
