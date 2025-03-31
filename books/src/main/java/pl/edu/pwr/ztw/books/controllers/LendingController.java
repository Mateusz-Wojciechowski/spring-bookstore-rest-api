package pl.edu.pwr.ztw.books.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Lending Management System", description = "Operations pertaining to lending books to readers")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @ApiOperation(value = "View a list of lendings", response = List.class)
    @GetMapping
    public ResponseEntity<List<Lending>> getAllLendings(){
        return new ResponseEntity<>(lendingService.getAllLendings(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get a lending by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Lending> getLendingById(
            @ApiParam(value = "Lending id from which lending object will retrieve", required = true)
            @PathVariable("id") int id){
        Optional<Lending> lendingOpt = lendingService.getLendingById(id);
        return lendingOpt.map(lending -> new ResponseEntity<>(lending, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Lend a book to a reader")
    @PostMapping("/lend")
    public ResponseEntity<Lending> lendBook(
            @ApiParam(value = "ID of the book to lend", required = true)
            @RequestParam int bookId,
            @ApiParam(value = "Reader information", required = true)
            @RequestBody Reader reader){
        Optional<Lending> lendingOpt = lendingService.lendBook(bookId, reader);
        return lendingOpt.map(lending -> new ResponseEntity<>(lending, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @ApiOperation(value = "Return a lent book")
    @PostMapping("/return")
    public ResponseEntity<Void> returnBook(
            @ApiParam(value = "ID of the lending record", required = true)
            @RequestParam int lendingId){
        boolean returned = lendingService.returnBook(lendingId);
        if(returned){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
