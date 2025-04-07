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

import java.net.ConnectException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Page<Author> getAllAuthors(Pageable pageable) {
//        Page<Author> result = authorRepository.findAll(pageable);
        try {
            return authorRepository.findAll(pageable);
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Author> getAuthorById(int id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return author;
        } else {
            throw new AuthorNotFoundException("Author not found");
        }
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> updateAuthor(int id, Author authorDetails) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            author.setName(authorDetails.getName());
            return Optional.of(authorRepository.save(author));
        } else {
            throw new AuthorNotFoundException("Author not found");
        }
    }

    public boolean deleteAuthor(int id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            authorRepository.deleteById(id);
            return true;
        } else {
            throw new AuthorNotFoundException("Author not found");
        }
    }
}
