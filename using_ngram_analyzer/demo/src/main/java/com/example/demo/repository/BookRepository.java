package com.example.demo.repository;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.model.Book;

public interface BookRepository extends ElasticsearchRepository<Book, String>{

}
