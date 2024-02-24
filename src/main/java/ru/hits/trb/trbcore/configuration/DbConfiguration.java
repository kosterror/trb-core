package ru.hits.trb.trbcore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import ru.hits.trb.trbcore.entity.BaseEntity;

import java.util.UUID;

@Configuration
public class DbConfiguration {

    @Bean
    BeforeConvertCallback<BaseEntity> beforeConvertCallback() {
        return entity -> {
            if (entity.getId() == null) {
                entity.setId(UUID.randomUUID());
            }
            return entity;
        };
    }

}
