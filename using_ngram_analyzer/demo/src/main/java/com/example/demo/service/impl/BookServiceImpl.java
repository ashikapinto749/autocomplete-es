package com.example.demo.service.impl;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private BookRepository repo;

    public int saveAll(List<Book> books) {
        for (Book book : books) {
            try {
                XContentBuilder builder = jsonBuilder().startObject();
                builder.field("bookId", book.getBookId());
                builder.field("title", book.getTitle());
                builder.field("authors", book.getAuthors());
                builder.field("average_rating", book.getAverage_rating());
                builder.endObject();
                IndexRequest indexRequest = new IndexRequest("my_index", "_doc", book.getBookId());
                indexRequest.source(builder);
                client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return books.size();
    }

    public List<Book> findAll() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("my_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(200000);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            List<Book> bookList = new ArrayList<>();
            for (SearchHit hit : searchHit) {
                bookList.add(objectMapper.convertValue(hit.getSourceAsMap(), Book.class));
            }
            return bookList;
        }
        catch (IOException ex) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in ES search");
        }

    }

    public Iterable<Book> autocomplete(String prefixString) {
        System.out.println(prefixString);
        MatchQueryBuilder searchBook = QueryBuilders.matchQuery("title", prefixString);
        System.out.println(searchBook);
        return repo.search(searchBook);
  }

}







