package com.demo.emsed_rtsc.incidents;

import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

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

    @PostMapping("/search")
    public Iterable<Incident> findAll(@RequestBody IncidentSearchDto searchParams) {
        // TODO: build a query part for each non-empty / non-null field
        // plan is: build a field query for incidentType + severityLevel

        // geo location search variants:
        // https://www.elastic.co/de/blog/geo-location-and-search
        // for now, only support distance-based searching
        GeoDistanceQuery gdq;
        if (searchParams.getLocation() != null) { 
            Location location = searchParams.getLocation();
            if (searchParams.getLocationDistance() != null && searchParams.getLocationDistance().length() > 0) {
                gdq = QueryBuilders.geoDistance()
                    .field("location")
                    .distance(searchParams.getLocationDistance())
                    .location(loc -> loc.latlon(latlonLoc -> latlonLoc.lon(location.getLongitude()).lat(location.getLatitude()))).build();
            }
        }

        // TODO: build query based on available matchings and filters
        NativeQuery searchQuery = new NativeQueryBuilder()
        .withQuery(QueryBuilders.bool().build()._toQuery())
        .withQuery(QueryBuilders.match().field("id").query("").build()._toQuery())
        .build();

        SearchHits<Incident> incidents = 
            this.operations.search(searchQuery, Incident.class, IndexCoordinates.of("incidents"));
        List<SearchHit<Incident>> inc = incidents.getSearchHits();
        return inc.stream().map(SearchHit<Incident>::getContent).collect(toList());
    }

    private Incident createIncidentFromPostDto(IncidentPostDto postDto) {
        Incident newIncident = this.mapper.map(postDto, Incident.class);
        if (newIncident.getLocation() == null || newIncident.getIncidentType() == null || newIncident.getSeverityLevel() == null) {
            throw new IncidentDataException("Invalid data input");
        }
        return newIncident;
    }

}
