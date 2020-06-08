package com.example.demo.controller;
import java.util.List;
import com.example.demo.dto.SearchResultDto;
import com.example.demo.service.BookService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Book;

@RestController
public class BookController {

    @Autowired
    BookService service;

    @PostMapping("/addBook")
    public int saveCustomer(@RequestBody List<Book> books) {

        return service.saveAll(books);
    }

    @GetMapping("/count")
    public  int getCount(){
       Iterable<Book> books = service.findAll();
        return Lists.newArrayList(books).size();
    }

    @GetMapping("/findAll")
    public Iterable<Book> findAllCustomers() {

        return service.findAll();
    }

    @GetMapping("/autocomplete/{search}")
    public SearchResultDto autocomplete(@PathVariable String search, @RequestParam(defaultValue = "100") int size) {
        return service.autocomplete(search, size);
    }

}
