package com.example.demo.service;
import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.List;


public interface BookService {

    int saveAll(List<Book> books);

    List<Book> findAll();

    Iterable<Book> autocomplete(String prefixString);

}



