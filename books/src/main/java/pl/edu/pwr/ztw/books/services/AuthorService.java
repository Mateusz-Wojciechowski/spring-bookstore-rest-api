package pl.edu.pwr.ztw.books.services;

import pl.edu.pwr.ztw.books.exceptions.AuthorNotFoundException;
import pl.edu.pwr.ztw.books.models.Author;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private static List<Author> authors = new ArrayList<>();
    private static int nextId = 1;



    static {
        authors.add(new Author(nextId++, "Henryk Sienkiewicz"));
        authors.add(new Author(nextId++, "Stanisław Reymont"));
        authors.add(new Author(nextId++, "Adam Mickiewicz"));
    }

    public List<Author> getAllAuthors() {
        return authors;
    }

    public Optional<Author> getAuthorById(int id) {
        Optional<Author> author =  authors.stream().filter(a -> a.getId() == id).findFirst();
        if (author.isPresent()) {
            return author;
        }else{
            throw new AuthorNotFoundException("Author not found");
        }
    }

    public Author createAuthor(Author author) {
        author.setId(nextId++);
        authors.add(author);
        return author;
    }

    public Optional<Author> updateAuthor(int id, Author authorDetails) {
        Optional<Author> authorOptional = getAuthorById(id);
        authorOptional.ifPresent(author -> author.setName(authorDetails.getName()));
        if (authorOptional.isEmpty()) {
            throw new AuthorNotFoundException("Author not found");
        }
        return authorOptional;
    }

    public boolean deleteAuthor(int id) {
        boolean result = authors.removeIf(a -> a.getId() == id);
        if (result) {
            return true;
        }else{
            throw new AuthorNotFoundException("Author not found");
        }
    }
}
