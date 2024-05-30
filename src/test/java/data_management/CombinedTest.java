package data_management;

import com.alerts.Alert;
import com.alerts.CombinedAlert;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CombinedTest {

    @Test
    public void testJustAboveThresholds() {

        Patient patient = new Patient(1);

        patient.addRecord(93, "Saturation", 1100);
        patient.addRecord(91, "SystolicPressure", 1100);

        CombinedAlert alert = new CombinedAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated as values are just above thresholds");
    }

    @Test
    public void testNoMatchingBloodPressureRecords() {

        Patient patient = new Patient(1);

        patient.addRecord(89, "Saturation", 1200);

        CombinedAlert alert = new CombinedAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated without matching blood pressure records");
    }

    @Test
    public void testBloodPressureNotLowEnough() {

        Patient patient = new Patient(1);

        patient.addRecord(89, "Saturation", 1300);
        patient.addRecord(92, "SystolicPressure", 1300);

        CombinedAlert alert = new CombinedAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated as blood pressure is not low enough");
    }

    @Test
    public void testMultipleValidRecords() {

        Patient patient = new Patient(1);

        long now = System.currentTimeMillis();
        patient.addRecord(89, "Saturation", now - 5100);
        patient.addRecord(86, "SystolicPressure", now - 5100);
        patient.addRecord(88, "Saturation", now);
        patient.addRecord(85, "SystolicPressure", now);

        CombinedAlert alert = new CombinedAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertEquals(2, alerts.size(), "Alerts should be generated for each valid hypotensive hypoxemia condition");
    }

    @Test
    public void testTimeWindowForSystolicPressure() {

        Patient patient = new Patient(1);

        patient.addRecord(89, "Saturation", 1400);
        patient.addRecord(86, "SystolicPressure", 3400); // Outside the time window

        CombinedAlert alert = new CombinedAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alert should be generated as the blood pressure record is outside the time window");
    }
}
