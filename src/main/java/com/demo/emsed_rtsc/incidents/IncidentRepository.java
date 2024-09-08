package com.demo.emsed_rtsc.incidents;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.demo.emsed_rtsc.article.Article;

public interface IncidentRepository extends ElasticsearchRepository<Incident, String> {
    Page<Article> findBySeverityLevel(SeverityLevel severity, Pageable pageable);
}
