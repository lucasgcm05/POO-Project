package org.example.Exceptions;

public class AlbumDoesntExistException extends RuntimeException {
    public AlbumDoesntExistException(String message) {
        super(message);
    }
}
