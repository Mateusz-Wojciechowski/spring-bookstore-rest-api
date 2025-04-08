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
import pl.edu.pwr.ztw.books.exceptions.DatabaseConnectionError;
import pl.edu.pwr.ztw.books.exceptions.LendingNotFoundException;
import pl.edu.pwr.ztw.books.models.Lending;
import pl.edu.pwr.ztw.books.models.Reader;
import pl.edu.pwr.ztw.books.services.LendingService;

import org.springframework.data.domain.Page;
import java.util.Optional;

@RestController
@RequestMapping("/api/lendings")
@Tag(name = "Lending Management System", description = "Operations pertaining to lending books to readers")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @Operation(summary = "View a paginated list of lendings", description = "Returns a paginated list of all lending records")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<Object> getAllLendings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Lending> lendingsPage = lendingService.getAllLendings(pageable);
            return new ResponseEntity<>(lendingsPage, HttpStatus.OK);
        } catch (DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a lending by Id", description = "Fetch a lending record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lending", content = @Content(schema = @Schema(implementation = Lending.class))),
            @ApiResponse(responseCode = "404", description = "Lending not found", content = @Content(schema = @Schema(implementation = LendingNotFoundException.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getLendingById(
            @Parameter(description = "Lending ID to retrieve the lending record", required = true)
            @PathVariable("id") int id) {
        try {
            Optional<Lending> lendingOpt = lendingService.getLendingById(id);
            return new ResponseEntity<>(lendingOpt.get(), HttpStatus.OK);
        } catch (LendingNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Lend a book to a reader", description = "Creates a new lending record for a book and a reader")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully lent", content = @Content(schema = @Schema(implementation = Lending.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or book not available", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @PostMapping("/lend")
    public ResponseEntity<Object> lendBook(
            @Parameter(description = "ID of the book to lend", required = true)
            @RequestParam int bookId,
            @Parameter(description = "Reader information", required = true)
            @RequestBody Reader reader) {
        try {
            Optional<Lending> lendingOpt = lendingService.lendBook(bookId, reader);
            return new ResponseEntity<>(lendingOpt.get(), HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Return a lent book", description = "Marks a book as returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully returned"),
            @ApiResponse(responseCode = "400", description = "Invalid request or lending record not found", content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class)))
    })
    @PostMapping("/return")
    public ResponseEntity<Object> returnBook(
            @Parameter(description = "ID of the lending record", required = true)
            @RequestParam int lendingId) {
        try {
            lendingService.returnBook(lendingId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LendingNotFoundException | DatabaseConnectionError e) {
            ErrorResponseImpl error = new ErrorResponseImpl();
            error.setMessage(e.getMessage());
            error.setStatus(404);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
