package com.example.bytemonk.services;

import com.example.bytemonk.domain.Incident;
import com.example.bytemonk.dao.BackendDao;
import com.example.bytemonk.domain.Severity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BackendService {


    @Autowired
    BackendDao backendDao;

    public boolean insertNewIncident(Incident newIncident) {

        // call dao to insert the entry
        long randomIncidentNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

        newIncident.setId(randomIncidentNumber);
        return backendDao.insertNewIncident(newIncident);

    }

    public boolean checkIfIncidentIsPresent(long id) {

        return backendDao.checkIfIncidentIsPresent(id);

    }

    public boolean checkIfIncidentIsPresent(String title) {

        return backendDao.checkIfIncidentIsPresent(title);

    }

    public boolean updateIncident(long id, String status, String notes) {

        return backendDao.updateIncident(id, status, notes);

    }


    public List<Incident> getFilteredIncidents(Severity severityEnum, LocalDate parsedIncidentStartDate, LocalDate parsedIncidentEndDate) {

        List<Incident> allIncidents = backendDao.getAllIncidents();

        return allIncidents.stream().filter(incident ->
                incident.getIncidentDate().isBefore(parsedIncidentEndDate.plusDays(1))
                        && incident.getIncidentDate().isAfter(parsedIncidentStartDate.minusDays(1)))
                .filter(incident -> {
                    if(severityEnum != null) {
                        return incident.getSeverityLevel().equals(severityEnum);
                    }
                    return true;
                }).collect(Collectors.toList());

    }


    public Incident getIncidentById(long id) {

        return backendDao.getIncidentById(id);

    }

}
