package com.alert_factories;

import com.alerts.Alert;

public class TriggeredAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(int patientId, long timestamp) {
        return new Alert(Integer.toString(patientId), "Triggered", timestamp);
    }
}
