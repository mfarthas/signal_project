package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class AbnormalHeartRateAlert implements AlertsList{

    /**
     * Check for Abnormal Heart Rate Alerts.
     *
     * @param patient The patient whose records need to be checked for Abnormal Heart Rate Alerts.
     * @return A list of Alert objects describing the Abnormal Heart Rate Alerts if triggered, otherwise an empty list.
     */
    @Override
    public List<Alert> checkAlerts(Patient patient) {
        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> ecgRecords = patient.dataByLabel("ECG");

        for (PatientRecord record : ecgRecords) {
            double heartRate = calculateHeartRate(record.getMeasurementValue());

            if (heartRate < 50 || heartRate > 100) {
                alerts.add(new Alert(Integer.toString(record.getPatientId()), "Abnormal Heart Rate", record.getTimestamp()));
            }
        }
        return alerts;
    }

    /**
     * Calculate heart rate from ECG measurement (beats per minute).
     *
     * @param ecgValue The ECG measurement value.
     * @return The calculated heart rate.
     */
    private static double calculateHeartRate(double ecgValue) {
        // Assuming ECG value represents RR interval in seconds
        double rrIntervalInSeconds = ecgValue / 1000.0; // Convert milliseconds to seconds
        return 60.0 / rrIntervalInSeconds;
    }
}
