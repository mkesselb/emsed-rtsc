package com.demo.emsed_rtsc.article;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;

@Document(indexName = "blog")
public class Article {

    @Id
    private String id;
    
    private String title;
    
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Author> authors;
    
    // standard getters and setters
    public String getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public List<Author> getAuthors() {
        return this.authors;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
}
