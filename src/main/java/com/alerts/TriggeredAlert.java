package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class TriggeredAlert implements AlertsList {

    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<PatientRecord> records = patient.dataByLabel("Alert");

        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord alert : records) {
            if (alert.getMeasurementValue() == 0) {
                int id = alert.getPatientId();
                long timestamp = alert.getTimestamp();
                boolean resolved = false;

                for (PatientRecord resolution : records) {
                    if (alert.getMeasurementValue() == 1) {
                        if (resolution.getPatientId() == id && resolution.getTimestamp() == timestamp) {
                            resolved = true;
                            break;
                        }
                    }
                }

                if (!resolved) {
                    alerts.add(new Alert(Integer.toString(id), "Triggered alert", timestamp));
                }
            }
        }

        return alerts;
    }

}
