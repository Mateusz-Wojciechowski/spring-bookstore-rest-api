package pl.edu.pwr.ztw.books.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pwr.ztw.books.models.Lending;

public interface LendingRepository extends JpaRepository<Lending, Integer> {
}
