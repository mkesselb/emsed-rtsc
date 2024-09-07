package com.demo.emsed_rtsc.article;

import static java.util.Arrays.asList;
import java.util.List;
import static java.util.stream.Collectors.toList;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;


@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private  ElasticsearchOperations operations;
    private final ArticleRepository articleRepository;
    private final ModelMapper mapper;

    public ArticleController(ArticleRepository articleRepository, ModelMapper mapper, ElasticsearchOperations elasticsearchOperations) {
        this.articleRepository = articleRepository;
        this.mapper = mapper;
        this.operations = elasticsearchOperations;
    }

    @GetMapping("/{authorName}")
    public List<Article> findArticlesOfAuthor(@PathVariable String authorName) {
        Page<Article> articleByAuthorName
            = articleRepository.findByAuthorsName(authorName, PageRequest.of(0, 10));
        return articleByAuthorName.getContent();
    }

    @GetMapping("/title/{titleFragment}")
    public List<Article> findByTitle(@PathVariable String titleFragment) {
        NativeQuery searchQuery = new NativeQueryBuilder()
            .withQuery(QueryBuilders.regexp().field("title").value(".*" + titleFragment + ".*").build()._toQuery())
            .build();
        SearchHits<Article> articles = 
            this.operations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
        List<SearchHit<Article>> sh = articles.getSearchHits();
        return sh.stream().map(SearchHit<Article>::getContent).collect(toList());
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Article create(@RequestBody String title) {
        Article article = new Article();
        article.setTitle(title);
        article.setAuthors(asList(new Author("John Smith"), new Author("John Doe")));
        return articleRepository.save(article);
    }

    @PostMapping("/{articleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Article update(@PathVariable String articleId, @RequestBody String newTitle) {
        /*
        Article article = this.articleRepository.findById(articleId).orElseThrow();
        */
        NativeQuery searchQuery = new NativeQueryBuilder()
        .withQuery(QueryBuilders.bool().build()._toQuery())
        .withQuery(QueryBuilders.match().field("id").query(articleId).build()._toQuery())
        .build();
        
        SearchHit<Article> articles = 
           this.operations.searchOne(searchQuery, Article.class);
        if (articles == null) {
            return null;
        }
        Article article = articles.getContent();
        article.setTitle(newTitle);
        article = this.articleRepository.save(article);
        return article;
    }

    // TODO: mapping for author fields (implement DTO)

}
