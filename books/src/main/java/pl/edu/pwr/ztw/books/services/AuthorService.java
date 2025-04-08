package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import pl.edu.pwr.ztw.books.exceptions.AuthorNotFoundException;
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
import pl.edu.pwr.ztw.books.models.Author;
import pl.edu.pwr.ztw.books.repositories.AuthorRepository;

import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Page<Author> getAllAuthors(Pageable pageable) {
        try {
            return authorRepository.findAll(pageable);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Author> getAuthorById(int id) {
        try {
            Optional<Author> author = authorRepository.findById(id);
            if (author.isPresent()) {
                return author;
            } else {
                throw new AuthorNotFoundException("Author not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Author createAuthor(Author author) {
        try {
            return authorRepository.save(author);
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Author> updateAuthor(int id, Author authorDetails) {
        try {
            Optional<Author> authorOptional = authorRepository.findById(id);
            if (authorOptional.isPresent()) {
                Author author = authorOptional.get();
                author.setName(authorDetails.getName());
                return Optional.of(authorRepository.save(author));
            } else {
                throw new AuthorNotFoundException("Author not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public boolean deleteAuthor(int id) {
        try {
            Optional<Author> authorOptional = authorRepository.findById(id);
            if (authorOptional.isPresent()) {
                authorRepository.deleteById(id);
                return true;
            } else {
                throw new AuthorNotFoundException("Author not found");
            }
        } catch (CannotCreateTransactionException e) {
            throw new DatabaseConnectionError("Database connection error");
        }
    }
}
