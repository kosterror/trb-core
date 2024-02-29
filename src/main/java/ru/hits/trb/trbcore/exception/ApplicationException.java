package ru.hits.trb.trbcore.exception;

import lombok.Getter;
import ru.hits.trb.trbcore.util.Error;

@Getter
public class ApplicationException extends RuntimeException {

    private final Error error;

    public ApplicationException(Error error, String message) {
        super(message);
        this.error = error;
    }

}
