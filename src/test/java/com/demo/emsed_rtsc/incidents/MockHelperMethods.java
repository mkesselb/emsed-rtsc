package com.demo.emsed_rtsc.incidents;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class MockHelperMethods {

    public static Page<Incident> createPageOfIncidents(int page, int size, List<Incident> incidents) {
        Pageable pageRequest = PageRequest.of(page, size);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), incidents.size());

        List<Incident> pageContent = incidents.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, incidents.size());
    }

}
