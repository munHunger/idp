package se.munhunger.idp.exception;

public class OrphanageException extends Exception{
    private String message;

    public OrphanageException() {

    }

    public OrphanageException(String message) {
        this.message = message;
    }
}

