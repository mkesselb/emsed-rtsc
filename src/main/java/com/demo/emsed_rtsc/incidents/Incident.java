package com.demo.emsed_rtsc.incidents;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import jakarta.persistence.Id;

@Document(indexName = "incidents")
public class Incident {

    @Id
    private String id;

    @GeoPointField
    @Field(type = FieldType.Nested, includeInParent = true)
    private Location location;

    @Field(type = FieldType.Keyword, includeInParent = true)
    private SeverityLevel severityLevel;

    @Field(type = FieldType.Keyword, includeInParent = true)
    private IncidentType incidentType;
    
    @Field(type = FieldType.Date, includeInParent = true)
    private Date timestamp;

    public String getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public SeverityLevel getSeverityLevel() {
        return this.severityLevel;
    }

    public IncidentType getIncidentType() {
        return this.incidentType;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSeverityLevel(SeverityLevel level) {
        this.severityLevel = level;
    }

    public void setIncidentType(IncidentType type) {
        this.incidentType = type;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
