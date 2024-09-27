package com.example.bytemonk.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class Incident {

    private long id;

    private String title;

    private String description;

    private Severity severityLevel;

    private LocalDate incidentDate;

    private String status;

    private String notes;

    public Incident() {

    }

    public Incident(String title, String description, Severity severityLevel, LocalDate incidentDate) {
        this.title = title;
        this.description = description;
        this.severityLevel = severityLevel;
        this.incidentDate = incidentDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(Severity severityLevel) {
        this.severityLevel = severityLevel;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return id == incident.id && Objects.equals(title, incident.title) && Objects.equals(description, incident.description) && severityLevel == incident.severityLevel && Objects.equals(incidentDate, incident.incidentDate) && Objects.equals(status, incident.status) && Objects.equals(notes, incident.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, severityLevel, incidentDate, status, notes);
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\"=" + id +
                ", \"title\"=\"" + title + '\"' +
                ", \"description\"=\"" + description + '\"' +
                ", \"severityLevel\"=\"" + severityLevel + '\"' +
                ", \"incidentDate\"=\"" + incidentDate + '\"' +
                ", \"status\"=\"" + status + '\"' +
                ", \"notes\"=\"" + notes + '\"' +
                '}';
    }
}
