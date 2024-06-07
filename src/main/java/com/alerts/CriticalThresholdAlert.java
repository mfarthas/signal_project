package com.alerts;

import com.alert_factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.ArrayList;

public class CriticalThresholdAlert implements AlertLists {

    private final BloodPressureAlertFactory alertFactory;

    public CriticalThresholdAlert() {
        this.alertFactory = new BloodPressureAlertFactory();
    }

    /**
     * Check for critical threshold alerts based on systolic and diastolic blood pressure readings.
     *
     * @param patient The patient whose records need to be checked for alerts.
     * @return A string describing the alert if triggered, otherwise null.
     */
    @Override
    public List<Alert> checkAlerts(Patient patient) {

        List<PatientRecord> systolicRecords = patient.dataByLabel("SystolicPressure");
        List<PatientRecord> diastolicRecords = patient.dataByLabel("DiastolicPressure");

        List<Alert> alerts =  new ArrayList<>();

        for (PatientRecord systolicRecord : systolicRecords) {
            if (systolicRecord.getMeasurementValue() > 180 || systolicRecord.getMeasurementValue() < 90) {
                alerts.add(alertFactory.createAlert(systolicRecord.getPatientId(), systolicRecord.getTimestamp()));
            }
        }

        for (PatientRecord diastolicRecord : diastolicRecords) {
            if (diastolicRecord.getMeasurementValue() > 120 || diastolicRecord.getMeasurementValue() < 60) {
                alerts.add(alertFactory.createAlert(diastolicRecord.getPatientId(), diastolicRecord.getTimestamp()));
            }
        }

        return alerts;
    }
}
