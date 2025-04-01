package pl.edu.pwr.ztw.books.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.models.Author;
import pl.edu.pwr.ztw.books.services.BooksService;
import pl.edu.pwr.ztw.books.services.AuthorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management System", description = "Operations pertaining to books in Book Management System")
public class BooksController {

    @Autowired
    private BooksService bookService;

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "View a list of available books", description = "Returns a List of all books")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @Operation(summary = "Get a book by Id", description = "Fetch a book from the system using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved book"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "Book id from which book object will be retrieved", required = true)
            @PathVariable("id") int id){
        Optional<Book> bookOpt = bookService.getBookById(id);
        return bookOpt.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Add a book", description = "Adds a new book to the system")
    @ApiResponse(responseCode = "201", description = "Book successfully created")
    @PostMapping
    public ResponseEntity<Book> createBook(
            @Parameter(description = "Book object to store in the database", required = true)
            @RequestBody Book book){
        if(book.getAuthor() != null) {
            Optional<Author> authorOpt = authorService.getAuthorById(book.getAuthor().getId());
            if(authorOpt.isPresent()){
                book.setAuthor(authorOpt.get());
            } else {
                Author newAuthor = authorService.createAuthor(book.getAuthor());
                book.setAuthor(newAuthor);
            }
        }
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a book", description = "Updates an existing book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "Book Id to update book object", required = true)
            @PathVariable("id") int id,
            @Parameter(description = "Updated book object", required = true)
            @RequestBody Book bookDetails){
        if(bookDetails.getAuthor() != null) {
            Optional<Author> authorOpt = authorService.getAuthorById(bookDetails.getAuthor().getId());
            if(authorOpt.isPresent()){
                bookDetails.setAuthor(authorOpt.get());
            } else {
                Author newAuthor = authorService.createAuthor(bookDetails.getAuthor());
                bookDetails.setAuthor(newAuthor);
            }
        }
        Optional<Book> updatedBook = bookService.updateBook(id, bookDetails);
        return updatedBook.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete a book", description = "Deletes a book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book Id to delete from the database", required = true)
            @PathVariable("id") int id){
        boolean deleted = bookService.deleteBook(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
