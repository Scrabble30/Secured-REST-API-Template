package app.controllers;

import app.dtos.HttpMessageDTO;
import app.exceptions.APIException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    public void handleAPIExceptions(APIException e, Context ctx) {
        if (e.getCause() != null && !e.getMessage().equals(e.getCause().toString())) {
            logger.error("Status: {} - Message: {} - Cause: {}", HttpStatus.forStatus(e.getStatusCode()), e.getMessage(), e.getCause().toString());
        } else {
            logger.error("Status: {} - Message: {}", HttpStatus.forStatus(e.getStatusCode()), e.getMessage());
        }

        ctx.status(e.getStatusCode());
        ctx.json(new HttpMessageDTO(e.getStatusCode(), e.getMessage()));
    }

    public void handleExceptions(Exception e, Context ctx) {
        logger.error("Unhandled exception: {}", e.toString());

        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ctx.json(new HttpMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
