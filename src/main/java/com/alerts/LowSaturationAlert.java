package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class LowSaturationAlert implements AlertsList {

    /**
     * Check for low saturation alerts.
     *
     * @param patient The patient whose records need to be checked for low saturation alerts.
     * @return A string describing the low saturation alert if triggered, otherwise null.
     */
    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<PatientRecord> saturationRecords = patient.dataByLabel("Saturation");

        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : saturationRecords) {
            if (record.getMeasurementValue() < 92) {
                alerts.add(new Alert(Integer.toString(record.getPatientId()), "Low Saturation", record.getTimestamp()));
            }
        }
        return alerts;
    }
}
