package com.example.bytemonk.dao;

import com.example.bytemonk.domain.Incident;
import com.example.bytemonk.domain.Severity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BackendDao {

    @Autowired
    JdbcTemplate incidentDb;

    private static final String INSERT_NEW_INCIDENT = "insert into incident ( id, title, description, severity_level, incident_date ) values ( ?, ?, ?, ?, ?)";
    private static final String CHECK_ID_IS_PRESENT = "select count(*) as incident_counts from incident where id = ?";
    private static final String CHECK_TITLE_IS_PRESENT = "select count(*) as incident_counts from incident where title = ?";
    private static final String UPDATE_INCIDENT = "update incident set status = ?, notes = ? where id = ?";
    private static final String GET_ALL_INCIDENTS = "select id, title, description, severity_level, incident_date, status, notes from incident";
    private static final String GET_INCIDENT_BY_ID = "select id, title, description, severity_level, incident_date, status, notes from incident where id = ?";

    public boolean insertNewIncident(Incident newIncident) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int rowsInserted = incidentDb.update(INSERT_NEW_INCIDENT, newIncident.getId(), newIncident.getTitle(), newIncident.getDescription(), newIncident.getSeverityLevel().name(), newIncident.getIncidentDate().format(dateFormatter));
        return rowsInserted > 0;
    }

    public boolean checkIfIncidentIsPresent(long id) {
        List<Map<String, Object>> result = incidentDb.queryForList(CHECK_ID_IS_PRESENT, id);
        return Integer.parseInt(String.valueOf(result.get(0).get("incident_counts"))) > 0;
    }

    public boolean checkIfIncidentIsPresent(String title) {
        List<Map<String, Object>> result = incidentDb.queryForList(CHECK_TITLE_IS_PRESENT, title.trim());
        return Integer.parseInt(String.valueOf(result.get(0).get("incident_counts"))) > 0;
    }

    public boolean updateIncident(long id, String status, String notes) {
        int rowsInserted = incidentDb.update(UPDATE_INCIDENT, status, notes, id);
        return rowsInserted > 0;
    }

    public List<Incident> getAllIncidents() {

        List<Map<String, Object>> result = incidentDb.queryForList(GET_ALL_INCIDENTS);
        List<Incident> allIncidents = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        for( Map<String, Object> row : result) {
            Incident incident = new Incident();
            incident.setId(Long.parseLong(String.valueOf(row.get("id"))));
            incident.setTitle(String.valueOf(row.get("title")));
            incident.setDescription(String.valueOf(row.get("description")));
            incident.setSeverityLevel(Severity.getValue(String.valueOf(row.get("severity_level"))));
            incident.setIncidentDate(LocalDate.parse(String.valueOf(row.get("incident_date")), dateFormatter));
            incident.setStatus(String.valueOf(row.get("status")));
            incident.setNotes(String.valueOf(row.get("notes")));
            allIncidents.add(incident);
        }
        return allIncidents;
    }

    public Incident getIncidentById(long id) {

        List<Map<String, Object>> result = incidentDb.queryForList(GET_INCIDENT_BY_ID, id);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        if (result.isEmpty()) {
            return null;
        }

        Incident incident = new Incident();
        incident.setId(Long.parseLong(String.valueOf(result.get(0).get("id"))));
        incident.setTitle(String.valueOf(result.get(0).get("title")));
        incident.setDescription(String.valueOf(result.get(0).get("description")));
        incident.setSeverityLevel(Severity.getValue(String.valueOf(result.get(0).get("severity_level"))));
        incident.setIncidentDate(LocalDate.parse(String.valueOf(result.get(0).get("incident_date")), dateFormatter));
        incident.setStatus(String.valueOf(result.get(0).get("status")));
        incident.setNotes(String.valueOf(result.get(0).get("notes")));
        return incident;
    }
}
