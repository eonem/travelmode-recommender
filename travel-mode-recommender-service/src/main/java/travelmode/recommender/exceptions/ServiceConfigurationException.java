package travelmode.recommender.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceConfigurationException extends RuntimeException {

    public ServiceConfigurationException() {
    }

    public ServiceConfigurationException(String message) {
        super(message);
    }

    public ServiceConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceConfigurationException(Throwable cause) {
        super(cause);
    }

    public ServiceConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
