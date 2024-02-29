package ru.hits.trb.trbcore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder(builderMethodName = "Builder")
public class ErrorResponse {

    @Schema(requiredMode = REQUIRED, description = "Задекларированный код ошибки")
    private int code;

    @Schema(requiredMode = REQUIRED, description = "Текст ошибки")
    private String message;

    @Schema(requiredMode = NOT_REQUIRED,
            description = "Слова с информацией о нарушении валидации, " +
                    "ключ - параметр тела запроса, значение - описание нарушения")
    private Map<String, String> requestValidationMessages;

}
