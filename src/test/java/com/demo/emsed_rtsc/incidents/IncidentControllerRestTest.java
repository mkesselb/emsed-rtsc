package com.demo.emsed_rtsc.incidents;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.emsed_rtsc.EmsedRtscApplication;


@SpringBootTest(classes=EmsedRtscApplication.class)
@AutoConfigureMockMvc
public class IncidentControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentRepository incidentRepository;

    @Test
    public void test_get_findAll_returnList_status200() throws Exception {
        List<Incident> incidents = Arrays.asList(new Incident(), new Incident());
        Page<Incident> pageOfIncidents = MockHelperMethods.createPageOfIncidents(0, 3, incidents);
        Mockito.when(this.incidentRepository.findAll(any(Pageable.class))).thenReturn(pageOfIncidents);

        MvcResult result = this.mockMvc.perform(get("/api/incidents")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        MockHttpServletResponse response = result.getResponse();

        JSONObject jsonObject = new JSONObject(response.getContentAsString());
        assertEquals(2, jsonObject.get("totalElements"));
        assertEquals(2, jsonObject.getJSONArray("content").length());
    }    
}
