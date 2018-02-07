package se.munhunger.idp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Marcus Münger
 */
@ApiModel(value = "An error message describing something that went wrong")
public class ErrorMessage extends Exception {
    @ApiModelProperty(value = "An overview of what went wrong")
    public String message;
    @ApiModelProperty(value = "The details of what went wrong")
    public String details;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
