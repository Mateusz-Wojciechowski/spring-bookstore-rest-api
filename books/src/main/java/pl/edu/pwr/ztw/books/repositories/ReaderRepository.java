package pl.edu.pwr.ztw.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pwr.ztw.books.models.Reader;

public interface ReaderRepository extends JpaRepository<Reader, Integer> {
}
