package pl.edu.pwr.ztw.books.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Book Management System", description = "Operations pertaining to books in Book Management System")
public class BooksController {

    @Autowired
    private BooksService bookService;

    @Autowired
    private AuthorService authorService;

    @ApiOperation(value = "View a list of available books", response = List.class)
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a book by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @ApiParam(value = "Book id from which book object will retrieve", required = true)
            @PathVariable("id") int id){
        Optional<Book> bookOpt = bookService.getBookById(id);
        return bookOpt.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Add a book")
    @PostMapping
    public ResponseEntity<Book> createBook(
            @ApiParam(value = "Book object store in database table", required = true)
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

    @ApiOperation(value = "Update a book")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @ApiParam(value = "Book Id to update book object", required = true)
            @PathVariable("id") int id,
            @ApiParam(value = "Update book object", required = true)
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

    @ApiOperation(value = "Delete a book")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @ApiParam(value = "Book Id from which book object will delete from database table", required = true)
            @PathVariable("id") int id){
        boolean deleted = bookService.deleteBook(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
