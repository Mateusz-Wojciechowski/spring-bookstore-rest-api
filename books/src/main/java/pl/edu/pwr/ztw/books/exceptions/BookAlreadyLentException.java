package pl.edu.pwr.ztw.books.exceptions;

public class BookAlreadyLentException extends RuntimeException {
    public BookAlreadyLentException(String message) {
        super(message);
    }
}
