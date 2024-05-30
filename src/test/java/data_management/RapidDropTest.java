package data_management;

import com.alerts.Alert;
import com.alerts.RapidDropAlert;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RapidDropTest {

    @Test
    public void testNoRapidDrop() {
        Patient patient = new Patient(1);

        // Adding records with no significant drop
        long now = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", now - 1200000); // 20 minutes ago
        patient.addRecord(94, "Saturation", now - 600000);  // 10 minutes ago
        patient.addRecord(93, "Saturation", now);           // Now

        RapidDropAlert alert = new RapidDropAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated as there is no rapid drop");
    }

    @Test
    public void testRapidDropWithinTimeFrame() {
        Patient patient = new Patient(1);

        // Adding records with a rapid drop within 10 minutes
        long now = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", now - 600000); // 10 minutes ago
        patient.addRecord(85, "Saturation", now);          // Now

        RapidDropAlert alert = new RapidDropAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertEquals(1, alerts.size(), "One alert should be generated for the rapid drop within 10 minutes");
    }

    @Test
    public void testRapidDropOutsideTimeFrame() {
        Patient patient = new Patient(1);

        // Adding records with a rapid drop outside 10 minutes
        long now = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", now - 1800000); // 30 minutes ago
        patient.addRecord(85, "Saturation", now);           // Now

        RapidDropAlert alert = new RapidDropAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated as the rapid drop is outside 10 minutes");
    }

    @Test
    public void testMultipleRapidDrops() {
        Patient patient = new Patient(1);

        // Adding records with multiple rapid drops within 10 minutes
        long now = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", now - 600000); // 10 minutes ago
        patient.addRecord(85, "Saturation", now - 300000); // 5 minutes ago
        patient.addRecord(80, "Saturation", now);          // Now

        RapidDropAlert alert = new RapidDropAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertEquals(2, alerts.size(), "Two alerts should be generated for multiple rapid drops within 10 minutes");
    }

    @Test
    public void testEdgeCaseJustAboveThreshold() {
        Patient patient = new Patient(1);

        // Adding records with a drop just above the 5% threshold
        long now = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", now - 600000); // 10 minutes ago
        patient.addRecord(90, "Saturation", now);          // Now

        RapidDropAlert alert = new RapidDropAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertEquals(1, alerts.size(), "One alert should be generated for the drop just above the 5% threshold");
    }

    @Test
    public void testEdgeCaseJustBelowThreshold() {
        Patient patient = new Patient(1);

        // Adding records with a drop just below the 5% threshold
        long now = System.currentTimeMillis();
        patient.addRecord(95, "Saturation", now - 600000); // 10 minutes ago
        patient.addRecord(91, "Saturation", now);          // Now

        RapidDropAlert alert = new RapidDropAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated for the drop just below the 5% threshold");
    }
}
