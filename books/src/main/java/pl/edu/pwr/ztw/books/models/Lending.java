package pl.edu.pwr.ztw.books.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lendings")
public class Lending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    private LocalDate lendingDate;

    public Lending() {
    }

    public Lending(Book book, Reader reader, LocalDate lendingDate) {
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

    @Override
    public String toString() {
        return "Lending{" +
                "id=" + id +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", reader=" + (reader != null ? reader.getName() : "null") +
                ", lendingDate=" + lendingDate +
                '}';
    }
}
