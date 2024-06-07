package com.alerts;

import com.alert_factories.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class RapidDropAlert implements AlertLists {

    private final BloodOxygenAlertFactory alertFactory;

    public RapidDropAlert() {
        this.alertFactory = new BloodOxygenAlertFactory();
    }

    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<PatientRecord> saturationRecords = patient.dataByLabel("Saturation");

        List<Alert> alerts = new ArrayList<>();

        for (int i = 1; i < saturationRecords.size(); i++) {
            PatientRecord currentRecord = saturationRecords.get(i);
            PatientRecord previousRecord = saturationRecords.get(i - 1);

            double dropPercentage = (previousRecord.getMeasurementValue() - currentRecord.getMeasurementValue()) / previousRecord.getMeasurementValue() * 100;
            long timeDifference = currentRecord.getTimestamp() - previousRecord.getTimestamp();
            long tenMinutesInMillis = 10 * 60 * 1000; // 10 minutes in milliseconds

            if (dropPercentage >= 5 && timeDifference <= tenMinutesInMillis) {
                alerts.add(alertFactory.createAlert(currentRecord.getPatientId(), currentRecord.getTimestamp()));
            }
        }
        return alerts;
    }

}
