package se.munhunger.idp.exception;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Marcus Münger
 */
public class NotInDatabaseException extends Exception {
    @ApiModelProperty(value = "Entity not found in DB")
    public String message;
    @ApiModelProperty(value = "The details on what type of object or field was not found")
    public String details;

    public NotInDatabaseException() {
    }

    public NotInDatabaseException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
