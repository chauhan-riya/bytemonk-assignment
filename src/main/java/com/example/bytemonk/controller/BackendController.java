package com.example.bytemonk.controller;

import com.example.bytemonk.domain.Incident;
import com.example.bytemonk.domain.Severity;
import com.example.bytemonk.services.BackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class BackendController {

    @Autowired
    BackendService backendService;

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Running");
    }

    @PostMapping("new_incident")
    public ResponseEntity<String> createNewIncident(@RequestParam String title, @RequestParam String description, @RequestParam String severityLevel, @RequestParam String incidentDate) {

        if (title == null || title.isEmpty() || description == null || description.isEmpty()
                || severityLevel == null || severityLevel.isEmpty() || incidentDate == null || incidentDate.isEmpty()) {
            return ResponseEntity.badRequest().body("All fields must be provided and non-empty");
        }

        if (title.length() < 10) {
            return ResponseEntity.badRequest().body("Title must be at least 10 characters long");
        }

        boolean incidentIsPresent = backendService.checkIfIncidentIsPresent(title);

        if ( incidentIsPresent ) {
            return ResponseEntity.unprocessableEntity().body("Incident already present with given title");
        }

        Severity severityEnum = null;

        for ( Severity severityValue : Severity.values() ) {
            if (severityValue.name().equalsIgnoreCase(severityLevel)) {
                severityEnum = severityValue;
                break;
            }
        }
        if ( severityEnum == null ) {
            return ResponseEntity.badRequest().body("Severity level must be one of 'Low', 'Medium', or 'High'");
        }
        LocalDate parsedDate;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            parsedDate = LocalDate.parse(incidentDate, dateFormatter);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Incident date must be in the format YYYYMMDD");
        }

        if(parsedDate.isBefore(LocalDate.now().minusDays(30)) || parsedDate.isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Incidents cannot be created with a past date greater than 30 days or a future date.");
        }

        Incident newIncident = new Incident(title, description, severityEnum, parsedDate);

        boolean result = backendService.insertNewIncident(newIncident);

        if (result) {
            return ResponseEntity.ok("Incident created successfully");
        } else {
            return ResponseEntity.internalServerError().body("Incident failed to create, try again");
        }
    }


    @PostMapping("update_incident")
    public ResponseEntity<String> updateIncident(@RequestParam long id, @RequestParam String status, @RequestParam String notes) {

        if (status == null || status.isEmpty() || notes == null || notes.isEmpty()) {
            return ResponseEntity.badRequest().body("All fields must be provided and non-empty");
        }

        boolean incidentIsPresent = backendService.checkIfIncidentIsPresent(id);

        if ( !incidentIsPresent ) {
            return ResponseEntity.unprocessableEntity().body("Incident not present");
        }

        boolean result = backendService.updateIncident(id, status, notes);

        if (result) {
            return ResponseEntity.ok("Incident updated successfully");
        } else {
            return ResponseEntity.internalServerError().body("Incident failed to update, try again");
        }
    }

    @PostMapping("retrieve_incidents")
    public ResponseEntity<String> retrieveIncidents(@RequestParam(required = false) String severity,
                                                          @RequestParam(required = false) String incidentStartDate,
                                                          @RequestParam(required = false) String incidentEndDate) {

        Severity severityEnum = null;

        for ( Severity severityValue : Severity.values() ) {
            if (severityValue.name().equalsIgnoreCase(severity)) {
                severityEnum = severityValue;
                break;
            }
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate parsedIncidentStartDate = LocalDate.of(1900, 1, 1);
        if ( incidentStartDate != null && !incidentStartDate.isEmpty() ) {
            try {
                parsedIncidentStartDate = LocalDate.parse(incidentStartDate, dateFormatter);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Incident start date must be in the format YYYYMMDD");
            }
        }
        LocalDate parsedIncidentEndDate = LocalDate.now();
        if ( incidentEndDate != null && !incidentEndDate.isEmpty() ) {
            try {
                parsedIncidentStartDate = LocalDate.parse(incidentEndDate, dateFormatter);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Incident end date must be in the format YYYYMMDD");
            }
        }

        List<Incident> filteredIncidents = backendService.getFilteredIncidents(severityEnum, parsedIncidentStartDate, parsedIncidentEndDate);

        return ResponseEntity.ok(filteredIncidents.toString());

    }


    @PostMapping("retrieve_incident_by_id")
    public ResponseEntity<String> retrieveIncidentById(@RequestParam long id) {

        Incident incident = backendService.getIncidentById(id);

        if (incident != null) {
            return ResponseEntity.ok(incident.toString());
        } else {
            return ResponseEntity.internalServerError().body("Incident Not Available");
        }

    }
}

