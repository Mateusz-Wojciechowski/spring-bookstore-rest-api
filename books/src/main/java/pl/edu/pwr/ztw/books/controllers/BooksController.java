package pl.edu.pwr.ztw.books.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.errors.ErrorResponseImpl;
import pl.edu.pwr.ztw.books.exceptions.BookNotFoundException;
import pl.edu.pwr.ztw.books.exceptions.DataIntegrityException;
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
import pl.edu.pwr.ztw.books.models.Author;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.services.AuthorService;
import pl.edu.pwr.ztw.books.services.BooksService;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management System", description = "Operations pertaining to books in Book Management System")
public class BooksController {

    @Autowired
    private BooksService bookService;

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "View a paginated list of available books", description = "Returns a paginated list of all books")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<Object> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Book> booksPage = bookService.getAllBooks(pageable);
            return new ResponseEntity<>(booksPage, HttpStatus.OK);
        } catch (DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a book by Id", description = "Fetch a book from the system using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(
            @Parameter(description = "Book id from which book object will be retrieved", required = true)
            @PathVariable("id") int id) {
        try {
            Book book = bookService.getBookById(id).get();
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a book", description = "Adds a new book to the system")
    @ApiResponse(responseCode = "201", description = "Book successfully created")
    @PostMapping
    public ResponseEntity<Object> createBook(
            @Parameter(description = "Book object to store in the database", required = true)
            @RequestBody Book book) {
        try {
            // Upewnij się, że author jest ustawiony – jeśli nie, spróbuj go utworzyć
            if (book.getAuthor() != null) {
                Optional<Author> authorOpt = authorService.getAuthorById(book.getAuthor().getId());
                if (authorOpt.isPresent()) {
                    book.setAuthor(authorOpt.get());
                } else {
                    Author newAuthor = authorService.createAuthor(book.getAuthor());
                    book.setAuthor(newAuthor);
                }
            }
            Book createdBook = bookService.createBook(book);
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update a book", description = "Updates an existing book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(
            @Parameter(description = "Book Id to update book object", required = true)
            @PathVariable("id") int id,
            @Parameter(description = "Updated book object", required = true)
            @RequestBody Book bookDetails) {
        try {
            if (bookDetails.getAuthor() != null) {
                Optional<Author> authorOpt = authorService.getAuthorById(bookDetails.getAuthor().getId());
                if (authorOpt.isPresent()) {
                    bookDetails.setAuthor(authorOpt.get());
                } else {
                    Author newAuthor = authorService.createAuthor(bookDetails.getAuthor());
                    bookDetails.setAuthor(newAuthor);
                }
            }
            Book updatedBook = bookService.updateBook(id, bookDetails).get();
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (BookNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a book", description = "Deletes a book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(
            @Parameter(description = "Book Id to delete from the database", required = true)
            @PathVariable("id") int id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookNotFoundException | DatabaseConnectionError | DataIntegrityException e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
