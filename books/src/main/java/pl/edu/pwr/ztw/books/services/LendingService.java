package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.models.Lending;
import pl.edu.pwr.ztw.books.models.Reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LendingService {
    private static List<Lending> lendings = new ArrayList<>();
    private static int nextId = 1;

    @Autowired
    private BooksService bookService;

    public List<Lending> getAllLendings() {
        return lendings;
    }

    public Optional<Lending> getLendingById(int id) {
        return lendings.stream().filter(l -> l.getId() == id).findFirst();
    }

    public Optional<Lending> lendBook(int bookId, Reader reader) {
        Optional<Book> bookOpt = bookService.getBookById(bookId);
        if (bookOpt.isPresent() && !bookOpt.get().isLent()) {
            Book book = bookOpt.get();
            bookService.markBookAsLent(bookId, true);
            Lending lending = new Lending(nextId++, book, reader, LocalDate.now());
            lendings.add(lending);
            return Optional.of(lending);
        }
        return Optional.empty();
    }

    public boolean returnBook(int lendingId) {
        Optional<Lending> lendingOpt = getLendingById(lendingId);
        if(lendingOpt.isPresent()){
            Lending lending = lendingOpt.get();
            int bookId = lending.getBook().getId();
            bookService.markBookAsLent(bookId, false);
            return lendings.remove(lending);
        }
        return false;
    }
}
