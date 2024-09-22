package com.demo.emsed_rtsc.incidents;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IncidentRepository extends ElasticsearchRepository<Incident, String> {
    Page<Incident> findBySeverityLevel(SeverityLevel severity, Pageable pageable);
}
