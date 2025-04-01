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

import pl.edu.pwr.ztw.books.models.Author;
import pl.edu.pwr.ztw.books.services.AuthorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Management System", description = "Operations pertaining to authors in Book Management System")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "View a list of available authors", description = "Returns a List of all authors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors(){
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }

    @Operation(summary = "Get an author by Id", description = "Fetch an author from the system using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved author"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @Parameter(description = "Author id from which author object will be retrieved", required = true)
            @PathVariable("id") int id){
        Optional<Author> authorOpt = authorService.getAuthorById(id);
        return authorOpt.map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Add an author", description = "Adds a new author to the system")
    @ApiResponse(responseCode = "201", description = "Author successfully created")
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Parameter(description = "Author object to store in the database", required = true)
            @RequestBody Author author){
        Author createdAuthor = authorService.createAuthor(author);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an author", description = "Updates an existing author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author successfully updated"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @Parameter(description = "Author Id to update author object", required = true)
            @PathVariable("id") int id,
            @Parameter(description = "Updated author object", required = true)
            @RequestBody Author authorDetails){
        Optional<Author> updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return updatedAuthor.map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete an author", description = "Deletes an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "Author Id to delete from the database", required = true)
            @PathVariable("id") int id){
        boolean deleted = authorService.deleteAuthor(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

