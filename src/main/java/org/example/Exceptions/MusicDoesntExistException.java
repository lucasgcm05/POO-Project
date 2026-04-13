package org.example.Exceptions;

public class MusicDoesntExistException extends RuntimeException {
    public MusicDoesntExistException(String message) {
        super(message);
    }
}
