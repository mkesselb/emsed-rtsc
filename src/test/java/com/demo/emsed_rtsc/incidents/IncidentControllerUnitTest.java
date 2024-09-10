package com.demo.emsed_rtsc.incidents;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import com.demo.emsed_rtsc.EmsedRtscApplication;

@SpringBootTest(classes=EmsedRtscApplication.class)
public class IncidentControllerUnitTest {

    @MockBean
    private IncidentRepository incidentRepository;

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
    public void test_searchIncidents_severityLevels() {
        Mockito.when(this.incidentRepository.save(any(Incident.class))).thenAnswer(i -> {Incident in = i.getArgument(0); in.setId("new_id"); return in;});

        // TODO: make the .search observable
        IncidentSearchDto searchParams = new IncidentSearchDto();
        searchParams.setSeverityLevels(Arrays.asList("MEDIUM", "HIGH"));
        Page<Incident> savedIncident = this.incidentController.search(searchParams, null);
    }

}

