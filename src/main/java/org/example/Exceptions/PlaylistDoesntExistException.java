package org.example.Exceptions;

public class PlaylistDoesntExistException extends RuntimeException {
    public PlaylistDoesntExistException(String message) {
        super(message);
    }
}
