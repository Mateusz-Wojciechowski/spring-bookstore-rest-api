package pl.edu.pwr.ztw.books.models;

import java.time.LocalDate;

public class Lending {
    private int id;
    private Book book;
    private Reader reader;
    private LocalDate lendingDate;

    public Lending() {
    }

    public Lending(int id, Book book, Reader reader, LocalDate lendingDate) {
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.lendingDate = lendingDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Reader getReader() {
        return reader;
    }
    public void setReader(Reader reader) {
        this.reader = reader;
    }
    public LocalDate getLendingDate() {
        return lendingDate;
    }
    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDate = lendingDate;
    }
}

