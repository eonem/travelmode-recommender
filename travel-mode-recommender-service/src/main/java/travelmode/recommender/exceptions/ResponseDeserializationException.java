package travelmode.recommender.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class ResponseDeserializationException extends RuntimeException {

    public ResponseDeserializationException() {
    }

    public ResponseDeserializationException(String message) {
        super(message);
    }

    public ResponseDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseDeserializationException(Throwable cause) {
        super(cause);
    }

    public ResponseDeserializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
