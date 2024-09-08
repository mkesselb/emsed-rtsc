package com.demo.emsed_rtsc.incidents;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    
    private  ElasticsearchOperations operations;
    private final IncidentRepository incidentRepository;
    private final ModelMapper mapper;

    public IncidentController(IncidentRepository incidentRepository, ModelMapper mapper, ElasticsearchOperations elasticsearchOperations) {
        this.incidentRepository = incidentRepository;
        this.mapper = mapper;
        this.operations = elasticsearchOperations;
    }

    @GetMapping
    public Iterable<Incident> findAll() {
        return incidentRepository.findAll(PageRequest.of(0, 10));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Incident create(@RequestBody IncidentPostDto incident) {
        Incident newIncident = this.createIncidentFromPostDto(incident);
        newIncident.setTimestamp(new Date());
        return incidentRepository.save(newIncident);
    }

    private Incident createIncidentFromPostDto(IncidentPostDto postDto) {
        Incident newIncident = this.mapper.map(postDto, Incident.class);
        return newIncident;
    }

}
