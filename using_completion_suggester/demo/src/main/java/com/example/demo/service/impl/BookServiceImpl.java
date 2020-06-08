package com.example.demo.service.impl;

import com.example.demo.dto.SearchResultDto;
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
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.search.suggest.term.TermSuggestionBuilder.SUGGESTION_NAME;
import org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private BookRepository repo;

    public int saveAll(List<Book> books){
        for (Book book : books) {
            try {
                System.out.println("adding.."+book.getTitle());
                XContentBuilder builder = jsonBuilder().startObject();
                builder.field("BookId", book.getBookId());
                builder.field("title", book.getTitle());
                builder.field("authors", book.getAuthors());
                builder.field("average_rating", book.getAverage_rating());
                builder.endObject();
                IndexRequest indexRequest = new IndexRequest("data", "_doc",book.getBookId());
                indexRequest.source(builder);
                client.index(indexRequest, RequestOptions.DEFAULT);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return books.size();
    }



    public Iterable<Book> findAll(){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("data");
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



    private SearchResultDto getSuggestions(SearchResponse response) {
        SearchResultDto dto = new SearchResultDto();
        Suggest suggest = response.getSuggest();
        Suggest.Suggestion<Entry<Entry.Option>> suggestion = suggest.getSuggestion(SUGGESTION_NAME);
        List<String> res = new ArrayList<String>();
        for(Entry<Entry.Option> entry: suggestion.getEntries()) {
            for (Entry.Option option: entry.getOptions()) {
                res.add(option.getText().toString());
                System.out.println(option.getText().toString());
            }
        }
        dto.setSuggestedBooks(res);
        return dto;
    }


    public SearchResultDto autocomplete(String prefixString, int size){

        SearchRequest searchRequest = new SearchRequest("data");
        CompletionSuggestionBuilder suggestBuilder = new CompletionSuggestionBuilder("title");

        suggestBuilder.size(size)
                .prefix(prefixString, Fuzziness.ONE)
                .skipDuplicates(true)
                .analyzer("standard");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(new SuggestBuilder().addSuggestion(SUGGESTION_NAME, suggestBuilder));
        searchRequest.source(sourceBuilder);

        SearchResponse response;
        try {
            response = client.search(searchRequest,RequestOptions.DEFAULT);
            return getSuggestions(response);
        } catch (IOException ex) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in ES search");
        }
    }
    }






