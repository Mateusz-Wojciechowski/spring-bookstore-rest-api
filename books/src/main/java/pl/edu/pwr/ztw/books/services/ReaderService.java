package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
import pl.edu.pwr.ztw.books.exceptions.ReaderNotFoundException;
import pl.edu.pwr.ztw.books.models.Reader;
import pl.edu.pwr.ztw.books.repositories.ReaderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    public Page<Reader> getAllReaders(Pageable pageable) {
        try {
            return readerRepository.findAll(pageable);
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Reader> getReaderById(int id) {
        try {
            Optional<Reader> reader = readerRepository.findById(id);
            if (reader.isPresent()) {
                return reader;
            } else {
                throw new ReaderNotFoundException("Reader not found");
            }
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Reader createReader(Reader reader) {
        try {
            return readerRepository.save(reader);
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public Optional<Reader> updateReader(int id, Reader readerDetails) {
        try {
            Optional<Reader> readerOptional = readerRepository.findById(id);
            if (readerOptional.isPresent()) {
                Reader reader = readerOptional.get();
                reader.setName(readerDetails.getName());
                return Optional.of(readerRepository.save(reader));
            } else {
                throw new ReaderNotFoundException("Reader not found");
            }
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }

    public boolean deleteReader(int id) {
        try {
            Optional<Reader> readerOptional = readerRepository.findById(id);
            if (readerOptional.isPresent()) {
                readerRepository.deleteById(id);
                return true;
            } else {
                throw new ReaderNotFoundException("Reader not found");
            }
        }catch (CannotCreateTransactionException e){
            throw new DatabaseConnectionError("Database connection error");
        }
    }
}
