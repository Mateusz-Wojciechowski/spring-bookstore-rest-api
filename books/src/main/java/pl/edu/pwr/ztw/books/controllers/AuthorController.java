package pl.edu.pwr.ztw.books.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Author Management System", description = "Operations pertaining to authors in Book Management System")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @ApiOperation(value = "View a list of available authors", response = List.class)
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors(){
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get an author by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @ApiParam(value = "Author id from which author object will retrieve", required = true)
            @PathVariable("id") int id){
        Optional<Author> authorOpt = authorService.getAuthorById(id);
        return authorOpt.map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Add an author")
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @ApiParam(value = "Author object store in database table", required = true)
            @RequestBody Author author){
        Author createdAuthor = authorService.createAuthor(author);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update an author")
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @ApiParam(value = "Author Id to update author object", required = true)
            @PathVariable("id") int id,
            @ApiParam(value = "Update author object", required = true)
            @RequestBody Author authorDetails){
        Optional<Author> updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return updatedAuthor.map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Delete an author")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @ApiParam(value = "Author Id from which author object will delete from database table", required = true)
            @PathVariable("id") int id){
        boolean deleted = authorService.deleteAuthor(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
