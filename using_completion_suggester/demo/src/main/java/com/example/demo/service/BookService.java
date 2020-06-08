package com.example.demo.service;
import com.example.demo.dto.SearchResultDto;
import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.List;


public interface BookService {

    int saveAll(List<Book> books);

    Iterable<Book> findAll();

    SearchResultDto autocomplete(String prefixString, int size);

}



