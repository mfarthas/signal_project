package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class TrendAlert implements AlertsList {

    /**
     * Check for trend alerts based on systolic and diastolic blood pressure readings.
     *
     * @param patient The patient whose records need to be checked for alerts.
     * @return A string describing the trend alert if triggered, otherwise null.
     */
    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<PatientRecord> systolicRecords = patient.dataByLabel("SystolicPressure");
        List<PatientRecord> diastolicRecords = patient.dataByLabel("DiastolicPressure");

        List<Alert> alerts = new ArrayList<>();

        if (systolicRecords.size() < 3 || diastolicRecords.size() < 3) {
            return null; // Insufficient data for trend analysis
        }

        if (checkTrend(systolicRecords)) {
            alerts.add(new Alert(Integer.toString(systolicRecords.get(0).getPatientId()), "Trend Alert", systolicRecords.get(3).getTimestamp()));
        }

        if (checkTrend(diastolicRecords)) {
            alerts.add(new Alert(Integer.toString(diastolicRecords.get(0).getPatientId()), "Trend Alert", diastolicRecords.get(3).getTimestamp()));
        }

        return alerts;
    }

    /**
     * Check if there is a consistent increase or decrease in blood pressure readings.
     *
     * @param records The list of blood pressure records to analyze.
     * @return True if a consistent trend is detected, otherwise false.
     */
    private static boolean checkTrend(List<PatientRecord> records) {


        for (int i = 2; i < records.size(); i++) {
            double diff1 = records.get(i).getMeasurementValue() - records.get(i - 1).getMeasurementValue();
            double diff2 = records.get(i - 1).getMeasurementValue() - records.get(i - 2).getMeasurementValue();

            if (Math.abs(diff1) > 10 && Math.abs(diff2) > 10) {
                // Check for consistent increase or decrease
                if ((diff1 > 0 && diff2 > 0) || (diff1 < 0 && diff2 < 0)) {
                    return true;
                }
            }
        }
        return false;
    }
}
