package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import com.example.demo.RestClientConfig;
import com.example.demo.service.BookService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Book;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RestController
public class BookController {
    @Autowired
    BookService service;

    @PostMapping("/addBook")
    public int saveCustomer(@RequestBody List<Book> books) {
        return service.saveAll(books);

    }

    @GetMapping("/findAll")
    public Iterable<Book> findAllCustomers() {

        return service.findAll();
    }


    @GetMapping("/autocomplete/{search}")
    public Iterable<Book> autocomplete(@PathVariable String search) {

        return service.autocomplete(search);
    }

}
