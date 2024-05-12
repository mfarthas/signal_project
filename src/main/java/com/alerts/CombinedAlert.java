package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.ArrayList;

public class CombinedAlert implements AlertsList{

    /**
     * Check for Hypotensive Hypoxemia Alerts.
     *
     * @param patient The patient whose records need to be checked for Hypotensive Hypoxemia Alerts.
     * @return A list of Alert objects describing the Hypotensive Hypoxemia Alerts if triggered, otherwise an empty list.
     */
    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> bloodPressureRecords = patient.dataByLabel("SystolicPressure");
        List<PatientRecord> saturationRecords = patient.dataByLabel("Saturation");

        for (PatientRecord pressureRecord : bloodPressureRecords) {
            for (PatientRecord saturationRecord : saturationRecords) {
                if (pressureRecord.getMeasurementValue() < 90 && saturationRecord.getMeasurementValue() < 92) {
                    alerts.add(new Alert(Integer.toString(pressureRecord.getPatientId()), "Hypotensive Hypoxemia", pressureRecord.getTimestamp()));
                }
            }
        }
        return alerts;
    }

}
