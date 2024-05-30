package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.ArrayList;

public class CombinedAlert implements AlertsList {

    private static final long TIME_WINDOW_MS = 1000;

    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> bloodPressureRecords = patient.dataByLabel("SystolicPressure");
        List<PatientRecord> saturationRecords = patient.dataByLabel("Saturation");

        for (PatientRecord pressureRecord : bloodPressureRecords) {
            for (PatientRecord saturationRecord : saturationRecords) {
                if (Math.abs(pressureRecord.getTimestamp() - saturationRecord.getTimestamp()) <= TIME_WINDOW_MS) {
                    if (pressureRecord.getMeasurementValue() < 90 && saturationRecord.getMeasurementValue() < 92) {
                        alerts.add(new Alert(Integer.toString(pressureRecord.getPatientId()), "Hypotensive Hypoxemia", pressureRecord.getTimestamp()));
                    }
                }
            }
        }
        return alerts;
    }
}
