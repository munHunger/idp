package se.munhunger.idp.exception;

public class EmailNotValidException extends Exception {
    private String message;

    public EmailNotValidException() {

    }

    public EmailNotValidException(String message) {
        this.message = message;
    }
}
