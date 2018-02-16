package se.munhunger.idp.exception;

public class ClientNotInDatabaseException extends Exception{
    private String message;

    public ClientNotInDatabaseException() {

    }

    public ClientNotInDatabaseException(String message) {
        this.message = message;
    }
}
