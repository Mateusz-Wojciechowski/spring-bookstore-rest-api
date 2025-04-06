package pl.edu.pwr.ztw.books.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.errors.ErrorResponseImpl;
import pl.edu.pwr.ztw.books.exceptions.ReaderNotFoundException;
import pl.edu.pwr.ztw.books.models.Author;
import pl.edu.pwr.ztw.books.models.Reader;
import pl.edu.pwr.ztw.books.services.ReaderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/readers")
@Tag(name = "Reader Management System", description = "Operations pertaining to readers in Book Management System")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @Operation(summary = "View a list of registered readers", description = "Returns a List of all readers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<Reader>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<Reader> readers = readerService.getAllReaders(pageable).getContent();
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }

    @Operation(summary = "Get a reader by Id", description = "Fetch a reader from the system using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reader", content = @Content(schema = @Schema(implementation = Reader.class))),
            @ApiResponse(responseCode = "404", description = "Reader not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getReaderById(
            @Parameter(description = "Reader id from which reader object will be retrieved", required = true)
            @PathVariable("id") int id){
        try {
            Optional<Reader> readerOpt = readerService.getReaderById(id);
            return new ResponseEntity<>(readerOpt.get(), HttpStatus.OK);
        }catch (ReaderNotFoundException e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a reader", description = "Adds a new reader to the system")
    @ApiResponse(responseCode = "201", description = "Reader successfully created")
    @PostMapping
    public ResponseEntity<Reader> createAuthor(
            @Parameter(description = "Reader object to store in the database", required = true)
            @RequestBody Reader reader){
        Reader createdReader = readerService.createReader(reader);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a reader", description = "Updates an existing reader by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reader successfully updated", content = @Content(schema = @Schema(implementation = Reader.class))),
            @ApiResponse(responseCode = "404", description = "Reader not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReader(
            @Parameter(description = "Reader Id to update reader object", required = true)
            @PathVariable("id") int id,
            @Parameter(description = "Updated reader object", required = true)
            @RequestBody Reader readerDetails){
        try {
            Optional<Reader> updatedReader = readerService.updateReader(id, readerDetails);
            return new ResponseEntity<>(updatedReader.get(), HttpStatus.OK);
        }catch (ReaderNotFoundException e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a reader", description = "Deletes a reader by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reader successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Reader not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReader(
            @Parameter(description = "Reader Id to delete from the database", required = true)
            @PathVariable("id") int id){
        try {
            boolean deleted = readerService.deleteReader(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (ReaderNotFoundException e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
