package pl.edu.pwr.ztw.books.services;

import pl.edu.pwr.ztw.books.exceptions.BookNotFoundException;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.models.Author;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private static List<Book> books = new ArrayList<>();
    private static int nextId = 1;

    static {
        Author author1 = new Author(1, "Henryk Sienkiewicz");
        Author author2 = new Author(2, "Stanisław Reymont");
        Author author3 = new Author(3, "Adam Mickiewicz");

        books.add(new Book(nextId++, "Potop", author1, 936));
        books.add(new Book(nextId++, "Wesele", author2, 150));
        books.add(new Book(nextId++, "Dziady", author3, 292));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(int id) {
        Optional<Book> book = books.stream().filter(b -> b.getId() == id).findAny();
        if (book.isPresent()) {
            return book;
        }else{
            throw new BookNotFoundException("book not found");
        }
    }

    public Book createBook(Book book) {
        book.setId(nextId++);
        books.add(book);
        return book;
    }

    public Optional<Book> updateBook(int id, Book bookDetails) {
        Optional<Book> bookOptional = getBookById(id);
        bookOptional.ifPresent(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPages(bookDetails.getPages());
        });
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("book not found");
        }
        return bookOptional;
    }

    public boolean deleteBook(int id) {
        boolean result = books.removeIf(b -> b.getId() == id);
        if (result) {
            return true;
        }else{
            throw new BookNotFoundException("book not found");
        }
    }

    public void markBookAsLent(int id, boolean lent) {
        getBookById(id).ifPresent(book -> book.setLent(lent));
    }
}
