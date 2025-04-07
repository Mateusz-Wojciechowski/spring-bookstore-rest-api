package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
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

    public Page<Lending> getAllLendings(Pageable pageable) {
        try {
            return lendingRepository.findAll(pageable);
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Lending> getLendingById(int id) {
        try {
            Optional<Lending> lending = lendingRepository.findById(id);
            if (lending.isPresent()) {
                return lending;
            } else {
                throw new LendingNotFoundException("Lending not found");
            }
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Lending> lendBook(int bookId, Reader reader) {
        try {
            Optional<Book> bookOpt = bookService.getBookById(bookId);
            if (bookOpt.isPresent() && !bookOpt.get().isLent()) {
                Book book = bookOpt.get();
                bookService.markBookAsLent(bookId, true);
                Lending lending = new Lending(book, reader, LocalDate.now());
                return Optional.of(lendingRepository.save(lending));
            }
            return Optional.empty();
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public boolean returnBook(int lendingId) {
        try {
            Optional<Lending> lendingOpt = getLendingById(lendingId);
            if (lendingOpt.isPresent()){
                Lending lending = lendingOpt.get();
                int bookId = lending.getBook().getId();
                bookService.markBookAsLent(bookId, false);
                lendingRepository.delete(lending);
                return true;
            }
            return false;
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }
}
