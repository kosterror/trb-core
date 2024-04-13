package ru.hits.trb.trbcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.hits.trb.trbcore.configuration.KafkaProducerTopics;
import ru.hits.trb.trbcore.configuration.RandomErrorProperties;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProducerTopics.class, RandomErrorProperties.class})
public class TrbCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrbCoreApplication.class, args);
    }

}
