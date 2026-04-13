package org.example.Exceptions;

public class MusicAlreadyExistsException extends RuntimeException {
    public MusicAlreadyExistsException(String message) {
        super(message);
    }
}
