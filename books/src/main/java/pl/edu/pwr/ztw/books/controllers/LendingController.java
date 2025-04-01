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

import pl.edu.pwr.ztw.books.models.Lending;
import pl.edu.pwr.ztw.books.models.Reader;
import pl.edu.pwr.ztw.books.services.LendingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lendings")
@Tag(name = "Lending Management System", description = "Operations pertaining to lending books to readers")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @Operation(summary = "View a list of lendings", description = "Returns a list of all lending records")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<Lending>> getAllLendings(){
        return new ResponseEntity<>(lendingService.getAllLendings(), HttpStatus.OK);
    }

    @Operation(summary = "Get a lending by Id", description = "Fetch a lending record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lending"),
            @ApiResponse(responseCode = "404", description = "Lending not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Lending> getLendingById(
            @Parameter(description = "Lending ID to retrieve the lending record", required = true)
            @PathVariable("id") int id){
        Optional<Lending> lendingOpt = lendingService.getLendingById(id);
        return lendingOpt.map(lending -> new ResponseEntity<>(lending, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lend a book to a reader", description = "Creates a new lending record for a book and a reader")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully lent"),
            @ApiResponse(responseCode = "400", description = "Invalid request or book not available")
    })
    @PostMapping("/lend")
    public ResponseEntity<Lending> lendBook(
            @Parameter(description = "ID of the book to lend", required = true)
            @RequestParam int bookId,
            @Parameter(description = "Reader information", required = true)
            @RequestBody Reader reader){
        Optional<Lending> lendingOpt = lendingService.lendBook(bookId, reader);
        return lendingOpt.map(lending -> new ResponseEntity<>(lending, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Return a lent book", description = "Marks a book as returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully returned"),
            @ApiResponse(responseCode = "400", description = "Invalid request or lending record not found")
    })
    @PostMapping("/return")
    public ResponseEntity<Void> returnBook(
            @Parameter(description = "ID of the lending record", required = true)
            @RequestParam int lendingId){
        boolean returned = lendingService.returnBook(lendingId);
        if(returned){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

