package pl.edu.pwr.ztw.books.exceptions;

public class LendingNotFoundException extends RuntimeException {
    public LendingNotFoundException(String message) {
        super(message);
    }
}
