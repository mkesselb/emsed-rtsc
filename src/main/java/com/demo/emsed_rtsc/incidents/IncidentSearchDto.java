package com.demo.emsed_rtsc.incidents;

import java.util.Date;

public class IncidentSearchDto {

    private Location location;

    private String[] severityLevels;

    private String[] incidentTypes;

    private Date timestamp;

    public Location getLocation() {
        return this.location;
    }

    public String[] getSeverityLevels() {
        return this.severityLevels;
    }

    public String[] getIncidentType() {
        return this.incidentTypes;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSeverityLevel(String[] level) {
        this.severityLevels = level;
    }

    public void setIncidentType(String[] type) {
        this.incidentTypes = type;
    }

    public void setTimestamp(Date date) {
        this.timestamp = date;
    }
}

