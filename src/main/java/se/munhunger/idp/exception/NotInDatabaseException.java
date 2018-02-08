package se.munhunger.idp.exception;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Marcus Münger
 */
public class NotInDatabaseException extends Exception {
    @ApiModelProperty(value = "An overview of what went wrong")
    public String message;
    @ApiModelProperty(value = "The details of what went wrong")
    public String details;

    public NotInDatabaseException() {
    }

    public NotInDatabaseException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
