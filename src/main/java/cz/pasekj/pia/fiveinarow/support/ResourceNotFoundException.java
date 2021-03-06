package cz.pasekj.pia.fiveinarow.support;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom resource not found exception
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

}