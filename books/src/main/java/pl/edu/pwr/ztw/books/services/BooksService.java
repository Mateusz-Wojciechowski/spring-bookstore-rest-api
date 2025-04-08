package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import pl.edu.pwr.ztw.books.exceptions.BookNotFoundException;
import pl.edu.pwr.ztw.books.exceptions.DataIntegrityException;
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.repositories.BookRepository;

import java.util.Optional;

@Service
public class BooksService {

    @Autowired
    private BookRepository bookRepository;

    public Page<Book> getAllBooks(Pageable pageable) {
        try {
            return bookRepository.findAll(pageable);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Book> getBookById(int id) {
        try {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent()) {
                return book;
            } else {
                throw new BookNotFoundException("Book not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Book createBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Book> updateBook(int id, Book bookDetails) {
        try {
            Optional<Book> bookOptional = bookRepository.findById(id);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setTitle(bookDetails.getTitle());
                book.setPages(bookDetails.getPages());
                book.setAuthor(bookDetails.getAuthor());
                return Optional.of(bookRepository.save(book));
            } else {
                throw new BookNotFoundException("Book not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public boolean deleteBook(int id) {
        try {
            Optional<Book> bookOptional = bookRepository.findById(id);
            if (bookOptional.isPresent()) {
                bookRepository.deleteById(id);
                return true;
            } else {
                throw new BookNotFoundException("Book not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Cannot delete book that is lent");
        }
    }

    public void markBookAsLent(int id, boolean lent) {
        try {
            Optional<Book> bookOptional = bookRepository.findById(id);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setLent(lent);
                bookRepository.save(book);
            } else {
                throw new BookNotFoundException("Book not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }
}
