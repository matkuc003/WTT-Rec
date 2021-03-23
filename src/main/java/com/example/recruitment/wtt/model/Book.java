package com.example.recruitment.wtt.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String isbn;
    private String title;
    private String subtitle;
    private String publisher;
    private String publishedDate;
    private String description;
    private Integer pageCount;
    private String thumbnailUrl;
    private String language;
    private String previewLink;
    private Double averageRating;
    private List<String> authors = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @JsonProperty("volumeInfo")
    private void unpackNested(Map<String,Object> volumeInfo) {
        List<Map<String,String>> industryIdentifiers = (ArrayList<Map<String,String>>)volumeInfo.get("industryIdentifiers");
        this.isbn = industryIdentifiers.stream().filter(o -> o.get("type").equals("ISBN_13")).map(id -> id.get("identifier")).collect(Collectors.joining());
        if(this.isbn.isEmpty())
        {
            this.isbn=id;
        }
        Map<String,String> imageLinks = (Map<String,String>)volumeInfo.get("imageLinks");
        this.thumbnailUrl = imageLinks.get("thumbnail");
        this.title = (String)volumeInfo.get("title");
        this.subtitle = (String)volumeInfo.get("subtitle");
        this.description = (String)volumeInfo.get("description");
        this.publisher = (String)volumeInfo.get("publisher");
        this.publishedDate = (String)volumeInfo.get("publishedDate");
        this.pageCount = (Integer)volumeInfo.get("pageCount");
        this.language = (String)volumeInfo.get("language");
        this.previewLink = (String)volumeInfo.get("previewLink");
        this.averageRating = (Double)volumeInfo.get("averageRating");
        this.authors = (List<String>)volumeInfo.get("authors");
        this.categories = (List<String>)volumeInfo.get("categories");
    }
    public boolean hasCategory(String category)
    {
        if(this.categories != null)
        {
            return this.getCategories().stream().anyMatch(cat->cat.equals(category));
        }
        else{
            return false;
        }
    }
    public boolean hasAuthor(String author)
    {
        if(this.authors != null)
        {
            return this.getAuthors().stream().anyMatch(auth->auth.equals(author));
        }
        else{
            return false;
        }
    }
    public boolean pageCountGreaterThan(Integer pageCount)
    {
        if(this.pageCount != null)
        {
            return this.pageCount > pageCount;
        }
        else{
            return false;
        }
    }
    public boolean pageCountLessThan(Double pageCount)
    {
        if(this.pageCount != null)
        {
            return this.pageCount < pageCount;
        }
        else{
            return false;
        }
    }
    public boolean avgRatingGreaterOREqual(Integer rate)
    {
        if(this.averageRating != null)
        {
            return this.averageRating >= rate;
        }
        else{
            return false;
        }
    }
}
