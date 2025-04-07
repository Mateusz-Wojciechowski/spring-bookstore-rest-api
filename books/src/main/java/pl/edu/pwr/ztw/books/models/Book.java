package pl.edu.pwr.ztw.books.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@JsonIgnoreProperties({"lendings"}) // ignorujemy wypożyczenia, aby nie powodowały cyklicznych referencji
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private int pages;

    private boolean isLent;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lending> lendings = new ArrayList<>();

    public Book() {
    }

    public Book(String title, Author author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.isLent = false;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
    public boolean isLent() {
        return isLent;
    }
    public void setLent(boolean lent) {
        isLent = lent;
    }
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }
    public List<Lending> getLendings() {
        return lendings;
    }
    public void setLendings(List<Lending> lendings) {
        this.lendings = lendings;
    }
}
