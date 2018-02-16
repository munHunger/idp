package se.munhunger.idp.exception;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Marcus Münger
 */
public class UserNotInDatabaseException extends Exception{
    private String message;

    public UserNotInDatabaseException() {

    }

    public UserNotInDatabaseException(String message) {
        this.message = message;
    }
}

