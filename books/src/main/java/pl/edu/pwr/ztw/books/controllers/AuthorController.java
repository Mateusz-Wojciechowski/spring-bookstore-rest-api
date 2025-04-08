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
import pl.edu.pwr.ztw.books.exceptions.AuthorNotFoundException;
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
import pl.edu.pwr.ztw.books.models.Author;
import pl.edu.pwr.ztw.books.services.AuthorService;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Management System", description = "Operations pertaining to authors in Book Management System")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "View a paginated list of available authors", description = "Returns a paginated list of all authors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<Object> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Author> authorsPage = authorService.getAllAuthors(pageable);
            return new ResponseEntity<>(authorsPage, HttpStatus.OK);
        } catch (DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get an author by Id", description = "Fetch an author from the system using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved author", content = @Content(schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAuthorById(
            @Parameter(description = "Author id from which author object will be retrieved", required = true)
            @PathVariable("id") int id) {
        try {
            Author author = authorService.getAuthorById(id).get();
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (AuthorNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add an author", description = "Adds a new author to the system")
    @ApiResponse(responseCode = "201", description = "Author successfully created")
    @PostMapping
    public ResponseEntity<Object> createAuthor(
            @Parameter(description = "Author object to store in the database", required = true)
            @RequestBody Author author) {
        try {
            Author createdAuthor = authorService.createAuthor(author);
            return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
        } catch (DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update an author", description = "Updates an existing author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author successfully updated", content = @Content(schema = @Schema(implementation = Author.class))),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAuthor(
            @Parameter(description = "Author Id to update author object", required = true)
            @PathVariable("id") int id,
            @Parameter(description = "Updated author object", required = true)
            @RequestBody Author authorDetails) {
        try {
            Author updatedAuthor = authorService.updateAuthor(id, authorDetails).get();
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } catch (AuthorNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete an author", description = "Deletes an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthor(
            @Parameter(description = "Author Id to delete from the database", required = true)
            @PathVariable("id") int id) {
        try {
            authorService.deleteAuthor(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AuthorNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
