package pl.edu.pwr.ztw.books.services;

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
        return authors.stream().filter(a -> a.getId() == id).findFirst();
    }

    public Author createAuthor(Author author) {
        author.setId(nextId++);
        authors.add(author);
        return author;
    }

    public Optional<Author> updateAuthor(int id, Author authorDetails) {
        Optional<Author> authorOptional = getAuthorById(id);
        authorOptional.ifPresent(author -> author.setName(authorDetails.getName()));
        return authorOptional;
    }

    public boolean deleteAuthor(int id) {
        return authors.removeIf(a -> a.getId() == id);
    }
}
