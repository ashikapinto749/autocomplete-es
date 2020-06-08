package com.example.demo.service.impl;

import com.example.demo.model.Book;
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
import java.util.List;


import com.example.demo.model.Book;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class HouseKeepingServiceImpl implements HouseKeepingService {

    @Autowired
    private BookRepository repo;
    @Autowired
    private RestHighLevelClient client;
    public void load() {
        try {

            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\HP\\Desktop\\books.csv"));

            String line = "";
            String splitBy = ",";

            line = br.readLine();
            int count = 0;
            while ((line = br.readLine()) != null && count < 6000) {
                count = count + 1;
                String[] row = line.split(splitBy);

                try {
                    XContentBuilder builder = jsonBuilder().startObject();
                    int i=Integer.parseInt(row[0]) + 100000;
                    String str1=Integer.toString(i);
                    System.out.println("Book Id=" + str1);
                    builder.field("bookId", row[0]);
                    builder.field("title", row[1]);
                    builder.field("authors", row[2]);
                    builder.field("average_rating", row[3]);
                    builder.endObject();
                    IndexRequest indexRequest = new IndexRequest("my_index","_doc",str1);
                    indexRequest.source(builder);
                    client.index(indexRequest, RequestOptions.DEFAULT);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


