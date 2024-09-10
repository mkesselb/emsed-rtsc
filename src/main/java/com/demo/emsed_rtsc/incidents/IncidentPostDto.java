package com.demo.emsed_rtsc.incidents;

public class IncidentPostDto {

    private Location location;

    private String severityLevel;

    private String incidentType;

    public IncidentPostDto() {
        // empty constructor
    }

    public IncidentPostDto(Location location, String severityLevel, String incidentType) {
        this.location = location;
        this.severityLevel = severityLevel;
        this.incidentType = incidentType;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getSeverityLevel() {
        return this.severityLevel;
    }

    public String getIncidentType() {
        return this.incidentType;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSeverityLevel(String level) {
        this.severityLevel = level;
    }

    public void setIncidentType(String type) {
        this.incidentType = type;
    }
}

