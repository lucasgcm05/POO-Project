package org.example.Exceptions;

public class OutOfBoundsPlaylistException extends RuntimeException {
    public OutOfBoundsPlaylistException(String message) {
        super(message);
    }
}
