package app.exceptions;

import io.javalin.http.HttpStatus;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {

    private final int statusCode;

    public APIException(int statusCode) {
        super();

        this.statusCode = statusCode;
    }

    public APIException(int statusCode, String message) {
        super(message);

        this.statusCode = statusCode;
    }

    public APIException(int statusCode, String message, Throwable cause) {
        super(message, cause);

        this.statusCode = statusCode;
    }

    public APIException(int statusCode, Throwable cause) {
        super(cause);

        this.statusCode = statusCode;
    }

    public APIException(HttpStatus status) {
        this(status.getCode());
    }

    public APIException(HttpStatus status, String message) {
        this(status.getCode(), message);
    }

    public APIException(HttpStatus status, String message, Throwable cause) {
        this(status.getCode(), message, cause);
    }

    public APIException(HttpStatus status, Throwable cause) {
        this(status.getCode(), cause);
    }
}
