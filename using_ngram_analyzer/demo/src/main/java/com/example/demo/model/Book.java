package com.example.demo.model;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName="my_index",type="book",shards=2)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	private String bookId;

	@Field(type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "autocomplete_search")
	private String title;

	private String authors;
	private String average_rating;

}
