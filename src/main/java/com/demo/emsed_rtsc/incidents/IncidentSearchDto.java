package com.demo.emsed_rtsc.incidents;

import java.util.Date;
import java.util.List;

public class IncidentSearchDto {

    private Location location;

    private String locationDistance;

    private List<String> severityLevels;

    private List<String> incidentTypes;

    private Date from;
    private Date to;

    public Location getLocation() {
        return this.location;
    }

    public String getLocationDistance() {
        return this.locationDistance;
    }

    public List<String> getSeverityLevels() {
        return this.severityLevels;
    }

    public List<String> getIncidentTypes() {
        return this.incidentTypes;
    }

    public Date getFrom() {
        return this.from;
    }

    public Date getTo() {
        return this.to;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocationDistance(String locationDistance) {
        this.locationDistance = locationDistance;
    }

    public void setSeverityLevels(List<String> level) {
        this.severityLevels = level;
    }

    public void setIncidentTypes(List<String> type) {
        this.incidentTypes = type;
    }

    public void setFrom(Date date) {
        this.from = date;
    }
    public void setTo(Date date) {
        this.to = date;
    }
}

