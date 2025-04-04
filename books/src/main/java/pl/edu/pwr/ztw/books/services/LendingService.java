package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.exceptions.LendingNotFoundException;
import pl.edu.pwr.ztw.books.exceptions.BookNotFoundException;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.models.Lending;
import pl.edu.pwr.ztw.books.models.Reader;
import pl.edu.pwr.ztw.books.repositories.LendingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LendingService {

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private BooksService bookService;

    public List<Lending> getAllLendings() {
        return lendingRepository.findAll();
    }

    public Optional<Lending> getLendingById(int id) {
        Optional<Lending> lending = lendingRepository.findById(id);
        if (lending.isPresent()) {
            return lending;
        } else {
            throw new LendingNotFoundException("Lending not found");
        }
    }

    public Optional<Lending> lendBook(int bookId, Reader reader) {
        Optional<Book> bookOpt = bookService.getBookById(bookId);
        if (bookOpt.isPresent() && !bookOpt.get().isLent()) {
            Book book = bookOpt.get();
            bookService.markBookAsLent(bookId, true);
            Lending lending = new Lending(book, reader, LocalDate.now());
            return Optional.of(lendingRepository.save(lending));
        }
        return Optional.empty();
    }

    public boolean returnBook(int lendingId) {
        Optional<Lending> lendingOpt = getLendingById(lendingId);
        if (lendingOpt.isPresent()){
            Lending lending = lendingOpt.get();
            int bookId = lending.getBook().getId();
            bookService.markBookAsLent(bookId, false);
            lendingRepository.delete(lending);
            return true;
        }
        return false;
    }
}
