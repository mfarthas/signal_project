package com.alerts;

import com.alert_factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class TrendAlert implements AlertLists {

    private final BloodPressureAlertFactory alertFactory;

    public TrendAlert() {
        this.alertFactory = new BloodPressureAlertFactory();
    }

    @Override
    public List<Alert> checkAlerts(Patient patient) {


        List<Alert> res = new ArrayList<>();

        List<PatientRecord> systolicRecords = patient.dataByLabel("SystolicPressure");
        List<PatientRecord> diastolicRecords = patient.dataByLabel("DiastolicPressure");

        checkTrendAlerts(res, systolicRecords);
        checkTrendAlerts(res, diastolicRecords);

        return res;
    }

    private void checkTrendAlerts(List<Alert> res, List<PatientRecord> records) {

        for (int i = 2; i < records.size(); i++) {

            long timestamp = records.get(i).getTimestamp();
            int patientId = records.get(0).getPatientId();
            double currentValue = records.get(i).getMeasurementValue();
            double previousValue = records.get(i - 1).getMeasurementValue();
            double prePreviousValue = records.get(i - 2).getMeasurementValue();

            boolean increaseTrend = prePreviousValue <= previousValue && previousValue <= currentValue;
            if (increaseTrend && (currentValue - prePreviousValue >= 10)) {

                res.add(alertFactory.createAlert(patientId, timestamp));
                continue;
            }

            boolean decreaseTrend = prePreviousValue >= previousValue && previousValue >= currentValue;
            if (decreaseTrend && (prePreviousValue - currentValue >= 10)) {
                res.add(alertFactory.createAlert(patientId, timestamp));
            }
        }
    }
}
