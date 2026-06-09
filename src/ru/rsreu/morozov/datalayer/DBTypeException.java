package ru.rsreu.morozov.datalayer;

public class DBTypeException extends RuntimeException {

    public DBTypeException() {
        super();
    }

    public DBTypeException(String message) {
        super(message);
    }
}
