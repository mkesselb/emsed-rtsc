package com.demo.emsed_rtsc.incidents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.json.JsonData;
import io.micrometer.common.lang.Nullable;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    Logger logger = LoggerFactory.getLogger(IncidentController.class);

    private final SimpleDateFormat strictDateOptionalTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private  ElasticsearchOperations operations;
    private final IncidentRepository incidentRepository;
    private final ModelMapper mapper;

    public IncidentController(IncidentRepository incidentRepository, ModelMapper mapper, ElasticsearchOperations elasticsearchOperations) {
        this.incidentRepository = incidentRepository;
        this.mapper = mapper;
        this.operations = elasticsearchOperations;
    }

    @GetMapping
    public Iterable<Incident> findAll(Pageable pageable) {
        return incidentRepository.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Incident create(@RequestBody IncidentPostDto incident) {
        Incident newIncident = this.createIncidentFromPostDto(incident);
        newIncident.setTimestamp(new Date());
        return incidentRepository.save(newIncident);
    }

    @PostMapping("/search")
    public Page<Incident> findAll(@RequestBody IncidentSearchDto searchParams, @Nullable Pageable pageable) {
        List<Query> queries = new ArrayList<>();
        List<Query> filters = new ArrayList<>();

        Date lte = searchParams.getTo();
        Date gte = searchParams.getFrom();
        RangeQuery rqDaterange = QueryBuilders
            .range()
            .field("timestamp")
            .lte(lte == null ? JsonData.of("now/d") : JsonData.of(strictDateOptionalTimeFormat.format(lte)))
            .gte(gte == null ? JsonData.of("now-7d/d") : JsonData.of(strictDateOptionalTimeFormat.format(gte)))
            .build();
        logger.info(rqDaterange.toString());
        queries.add(rqDaterange._toQuery());

        TermsQuery tqIncidentTypes = null;
        if (searchParams.getIncidentTypes() != null && searchParams.getIncidentTypes().size() > 0) {
            tqIncidentTypes = QueryBuilders
                .terms()
                .field("incidentType")
                .terms(TermsQueryField.of(t -> t.value(searchParams.getIncidentTypes().stream().map(FieldValue::of).collect(toList()))))
                .build();
            queries.add(tqIncidentTypes._toQuery());
            logger.info(tqIncidentTypes.toString());
        }

        TermsQuery tqSeverityLevels = null;
        if (searchParams.getSeverityLevels() != null && searchParams.getSeverityLevels().size() > 0) {
            tqSeverityLevels = QueryBuilders
                .terms()
                .field("severityLevel")
                .terms(TermsQueryField.of(t -> t.value(searchParams.getSeverityLevels().stream().map(FieldValue::of).collect(toList()))))
                .build();
            queries.add(tqSeverityLevels._toQuery());
            logger.info(tqSeverityLevels.toString());
        }

        // geo location search variants:
        // https://www.elastic.co/de/blog/geo-location-and-search
        // for now, only support distance-based searching
        GeoDistanceQuery gdq = null;
        if (searchParams.getLocation() != null) { 
            Location location = searchParams.getLocation();
            if (searchParams.getLocationDistance() != null && searchParams.getLocationDistance().length() > 0) {
                gdq = QueryBuilders
                    .geoDistance()
                    .field("location")
                    .distance(searchParams.getLocationDistance())
                    .location(loc -> loc.latlon(latlonLoc -> latlonLoc.lon(location.getLongitude()).lat(location.getLatitude()))).build();
                filters.add(gdq._toQuery());
                logger.info(gdq.toString());
            }
        }

        BoolQuery bq = QueryBuilders.bool().must(queries).filter(filters).build();
        logger.info(bq.toString());
        NativeQuery searchQuery = new NativeQueryBuilder()
            .withQuery(bq._toQuery())
            .build();
        searchQuery.setPageable(pageable);

        SearchHits<Incident> incidents = 
            this.operations.search(searchQuery, Incident.class, IndexCoordinates.of("incidents"));
            SearchPage<Incident> searchPageIncidents = SearchHitSupport.searchPageFor(incidents, pageable);
        return (Page<Incident>) SearchHitSupport.unwrapSearchHits(searchPageIncidents);
    }

    private Incident createIncidentFromPostDto(IncidentPostDto postDto) {
        Incident newIncident = this.mapper.map(postDto, Incident.class);
        if (newIncident.getLocation() == null || newIncident.getIncidentType() == null || newIncident.getSeverityLevel() == null) {
            throw new IncidentDataException("Invalid data input");
        }
        return newIncident;
    }

}
