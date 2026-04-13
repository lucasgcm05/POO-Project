package org.example.Exceptions;

public class NoPlayListException extends RuntimeException {
    public NoPlayListException(String message) {
        super(message);
    }
}
