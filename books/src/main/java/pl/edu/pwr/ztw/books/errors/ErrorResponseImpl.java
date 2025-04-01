package pl.edu.pwr.ztw.books.errors;

public class ErrorResponseImpl {
    private String message;
    private int status;

    // Constructors
    public ErrorResponseImpl() {}

    public ErrorResponseImpl(String message, int status) {
        this.message = message;
        this.status = status;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

}
