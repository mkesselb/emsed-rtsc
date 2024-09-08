package com.demo.emsed_rtsc.incidents;

import java.util.Date;

public class IncidentSearchDto {

    private Location location;

    private String locationDistance;

    private String[] severityLevels;

    private String[] incidentTypes;

    private Date from;
    private Date to;

    public Location getLocation() {
        return this.location;
    }

    public String getLocationDistance() {
        return this.locationDistance;
    }

    public String[] getSeverityLevels() {
        return this.severityLevels;
    }

    public String[] getIncidentType() {
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

    public void setSeverityLevel(String[] level) {
        this.severityLevels = level;
    }

    public void setIncidentType(String[] type) {
        this.incidentTypes = type;
    }

    public void setFrom(Date date) {
        this.from = date;
    }
    public void setTo(Date date) {
        this.to = date;
    }
}

