package com.alert_factories;

import com.alerts.Alert;

public interface AlertFactory {
    Alert createAlert(int patientId, long timestamp);
}