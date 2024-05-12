package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class IrregularBeatsAlert implements AlertsList {

    /**
     * Check for Irregular Beat Alerts.
     *
     * @param patient The patient whose records need to be checked for Irregular Beat Alerts.
     * @return A list of Alert objects describing the Irregular Beat Alerts if triggered, otherwise an empty list.
     */
    @Override
    public List<Alert> checkAlerts(Patient patient) {
        List<Alert> alerts = new ArrayList<>();

        List<PatientRecord> ecgRecords = patient.dataByLabel("ECG");

        for (int i = 2; i < ecgRecords.size(); i++) {
            double previousRRInterval = ecgRecords.get(i - 1).getMeasurementValue() - ecgRecords.get(i - 2).getMeasurementValue();
            double currentRRInterval = ecgRecords.get(i).getMeasurementValue() - ecgRecords.get(i - 1).getMeasurementValue();

            // Check for significant variations in time intervals between consecutive beats
            if (Math.abs(currentRRInterval - previousRRInterval) > 0.1 * previousRRInterval) {
                alerts.add(new Alert(Integer.toString(ecgRecords.get(i).getPatientId()), "Irregular Beat", ecgRecords.get(i).getTimestamp()));
            }
        }
        return alerts;
    }
}
