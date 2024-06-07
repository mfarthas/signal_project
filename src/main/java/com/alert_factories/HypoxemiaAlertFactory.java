package com.alert_factories;

import com.alerts.Alert;

public class HypoxemiaAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(int patientId, long timestamp) {
        return new Alert(Integer.toString(patientId), "Hypotensive Hypoxemia", timestamp);
    }
}
