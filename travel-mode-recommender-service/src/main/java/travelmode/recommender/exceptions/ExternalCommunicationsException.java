package travelmode.recommender.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class ExternalCommunicationsException extends RuntimeException {

    public ExternalCommunicationsException() {
    }

    public ExternalCommunicationsException(String message) {
        super(message);
    }

    public ExternalCommunicationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalCommunicationsException(Throwable cause) {
        super(cause);
    }

    public ExternalCommunicationsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
