package com.example.demo.service.impl;
import com.example.demo.service.BookService;
import com.github.javafaker.Faker;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.HouseKeepingService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Locale;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class HouseKeepingServiceImpl implements HouseKeepingService {
    private Faker faker = new Faker(new Locale("en-IND"));

    @Autowired
    private BookRepository repo;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private BookService bookService;

    public void load() {
        System.out.println("loading...");
        try{
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\HP\\Desktop\\books.csv"));

        String line = "";
        String splitBy = ",";

        line = br.readLine();
        int count = 0;

        while ((line = br.readLine()) != null  ) {


            String[] row = line.split(splitBy);

            try {
                XContentBuilder builder = jsonBuilder().startObject();
                builder.field("BookId", row[0]);
                builder.field("title", row[1]);
                builder.field("authors", row[2]);
                builder.field("average_rating", row[4]);
                builder.endObject();
                IndexRequest indexRequest = new IndexRequest("data", "_doc", row[0]);
                indexRequest.source(builder);
                client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
    }
         catch(IOException e) {
            e.printStackTrace();
        }



    }


    }



