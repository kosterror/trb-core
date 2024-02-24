package ru.hits.trb.trbcore.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public abstract class BaseEntity {

    @Id
    private UUID id;

}
