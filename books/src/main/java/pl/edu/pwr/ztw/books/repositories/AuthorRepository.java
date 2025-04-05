package pl.edu.pwr.ztw.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pwr.ztw.books.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
