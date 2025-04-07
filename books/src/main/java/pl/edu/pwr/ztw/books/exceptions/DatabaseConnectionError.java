package pl.edu.pwr.ztw.books.exceptions;

public class DatabaseConnectionError extends RuntimeException {
    public DatabaseConnectionError(String message) {
        super(message);
    }
}
