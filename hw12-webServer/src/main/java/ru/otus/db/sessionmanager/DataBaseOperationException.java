package ru.otus.db.sessionmanager;

public class DataBaseOperationException extends RuntimeException {

    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
