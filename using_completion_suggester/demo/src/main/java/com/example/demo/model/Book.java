package com.example.demo.model;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;


@Document(indexName="data",type="book",shards=2)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Lazy(value = false)
public class Book {
	
	@Id
	private String bookId;
	
	@Field(index=true)
	private String title;

	private String authors;

	private String average_rating;

}
