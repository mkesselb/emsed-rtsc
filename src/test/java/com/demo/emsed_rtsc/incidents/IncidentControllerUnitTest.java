package com.demo.emsed_rtsc.incidents;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import com.demo.emsed_rtsc.EmsedRtscApplication;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonpUtils;

@SpringBootTest(classes=EmsedRtscApplication.class)
public class IncidentControllerUnitTest {

    @MockBean
    private IncidentRepository incidentRepository;

    @Mock
    private ElasticsearchOperations operations;

    @Mock SearchHits searchHits;

    @Autowired
    private IncidentController incidentController;

    @Test
    public void test_createIncident_valid() {
        Mockito.when(this.incidentRepository.save(any(Incident.class))).thenAnswer(i -> {Incident in = i.getArgument(0); in.setId("new_id"); return in;});

        IncidentPostDto newIncident = new IncidentPostDto(new Location(1, 2), "HIGH", "FIRE");
        Incident savedIncident = this.incidentController.create(newIncident);

        assertEquals(newIncident.getLocation().getLongitude(), savedIncident.getLocation().getLongitude());
        assertEquals(newIncident.getLocation().getLatitude(), savedIncident.getLocation().getLatitude());
        assertEquals(newIncident.getIncidentType(), savedIncident.getIncidentType().name());
        assertEquals(newIncident.getSeverityLevel(), savedIncident.getSeverityLevel().name());
        assertNotEquals(null, savedIncident.getTimestamp());
    }

    @Test
    public void test_createIncident_invalidSeverityLevel() {
        Mockito.when(this.incidentRepository.save(any(Incident.class))).thenAnswer(i -> {Incident in = i.getArgument(0); in.setId("new_id"); return in;});

        IncidentPostDto newIncident = new IncidentPostDto(new Location(1, 2), "not_found", "FIRE");
        try {
            this.incidentController.create(newIncident);
            fail("IncidentDataException not thrown");
        } catch(IncidentDataException e) {
            assertEquals("Invalid data input", e.getMessage());
        }
    }

    @Test
    public void test_createIncident_invalidIncidentType() {
        Mockito.when(this.incidentRepository.save(any(Incident.class))).thenAnswer(i -> {Incident in = i.getArgument(0); in.setId("new_id"); return in;});

        IncidentPostDto newIncident = new IncidentPostDto(new Location(1, 2), "HIGH", "not_found");
        try {
            this.incidentController.create(newIncident);
            fail("IncidentDataException not thrown");
        } catch(IncidentDataException e) {
            assertEquals("Invalid data input", e.getMessage());
        }
    }

    @Test
    public void test_searchIncidents_noFilters() {
        Mockito.when(this.operations.search(any(NativeQuery.class), any(Class.class), any(IndexCoordinates.class)))
            .thenReturn(searchHits);

        IncidentSearchDto searchParams = new IncidentSearchDto();
        Page<Incident> foundIncidents = this.incidentController.search(searchParams, null);

        Map<String, Query> queries = this.incidentController.getQueries(foundIncidents.hashCode());

        assertEquals("Query: {\"range\":{\"timestamp\":{\"gte\":\"now-7d/d\",\"lte\":\"now/d\"}}}", queries.get("rqDaterange").toString());
        assertEquals("Query: {\"bool\":{\"filter\":[],\"must\":[{\"range\":{\"timestamp\":{\"gte\":\"now-7d/d\",\"lte\":\"now/d\"}}}]}}", queries.get("bq").toString());
    }

    @Test
    public void test_searchIncidents_allFilters() {
        Mockito.when(this.operations.search(any(NativeQuery.class), any(Class.class), any(IndexCoordinates.class)))
            .thenReturn(searchHits);

        Date dateFrom = new Date();
        Date dateTo = new Date();
        IncidentSearchDto searchParams = new IncidentSearchDto();
        searchParams.setLocation(new Location(1, 2));
        searchParams.setLocationDistance("234km");
        searchParams.setSeverityLevels(Arrays.asList("MEDIUM", "HIGH"));
        searchParams.setIncidentTypes(Arrays.asList("FIRE"));
        searchParams.setFrom(dateFrom);
        searchParams.setTo(dateTo);
        Page<Incident> foundIncidents = this.incidentController.search(searchParams, null);

        Map<String, Query> queries = this.incidentController.getQueries(foundIncidents.hashCode());

        String dateFromFormatted = this.incidentController.getDateFormat().format(dateFrom);
        String dateToFormatted = this.incidentController.getDateFormat().format(dateTo);
        assertEquals("Query: {\"range\":{\"timestamp\":{\"gte\":\"" + dateFromFormatted + "\",\"lte\":\"" + dateToFormatted + "\"}}}", JsonpUtils.toString(queries.get("rqDaterange")));
        assertEquals("Query: {\"terms\":{\"incidentType\":[\"FIRE\"]}}", JsonpUtils.toString(queries.get("tqIncidentTypes")));
        assertEquals("Query: {\"terms\":{\"severityLevel\":[\"MEDIUM\",\"HIGH\"]}}", JsonpUtils.toString(queries.get("tqSeverityLevels")));
        assertEquals("Query: {\"geo_distance\":{\"location\":{\"lat\":1.0,\"lon\":2.0},\"distance\":\"234km\"}}", JsonpUtils.toString(queries.get("gdq")));
        assertEquals("Query: {\"bool\":{\"filter\":[{\"geo_distance\":{\"location\":{\"lat\":1.0,\"lon\":2.0},\"distance\":\"234km\"}}],\"must\":[{\"range\":{\"timestamp\":{\"gte\":\"" + dateFromFormatted + "\",\"lte\":\"" + dateToFormatted + "\"}}},{\"terms\":{\"incidentType\":[\"FIRE\"]}},{\"terms\":{\"severityLevel\":[\"MEDIUM\",\"HIGH\"]}}]}}", JsonpUtils.toString(queries.get("bq")));
    }

}

