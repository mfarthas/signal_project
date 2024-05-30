package data_management;

import com.alerts.Alert;
import com.alerts.IrregularBeatsAlert;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IrregularBeatsTest {

    @Test
    public void testNoIrregularBeats() {
        Patient patient = new Patient(1);

        // Adding ECG records with consistent RR intervals
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now - 3000);
        patient.addRecord(2.0, "ECG", now - 2000);
        patient.addRecord(3.0, "ECG", now - 1000);

        IrregularBeatsAlert alert = new IrregularBeatsAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated for consistent RR intervals");
    }

    @Test
    public void testSingleIrregularBeat() {
        Patient patient = new Patient(1);

        // Adding ECG records with one irregular RR interval
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now - 3000);
        patient.addRecord(2.0, "ECG", now - 2000);
        patient.addRecord(2.7, "ECG", now - 1000); // Irregular interval

        IrregularBeatsAlert alert = new IrregularBeatsAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertEquals(1, alerts.size(), "One alert should be generated for the irregular RR interval");
    }

    @Test
    public void testMultipleIrregularBeats() {
        Patient patient = new Patient(1);

        // Adding ECG records with multiple irregular RR intervals
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now - 4000);
        patient.addRecord(2.0, "ECG", now - 3000);
        patient.addRecord(2.7, "ECG", now - 2000); // Irregular interval
        patient.addRecord(3.5, "ECG", now - 1000); // Irregular interval

        IrregularBeatsAlert alert = new IrregularBeatsAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertEquals(2, alerts.size(), "Two alerts should be generated for the irregular RR intervals");
    }

    @Test
    public void testEdgeCaseNoAlerts() {
        Patient patient = new Patient(1);

        // Adding ECG records with RR intervals at the threshold
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now - 3000);
        patient.addRecord(2.0, "ECG", now - 2000);
        patient.addRecord(3.0, "ECG", now - 1000);

        IrregularBeatsAlert alert = new IrregularBeatsAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated as variations are at the threshold");
    }

    @Test
    public void testJustBelowThreshold() {
        Patient patient = new Patient(1);

        // Adding ECG records with RR intervals just below the threshold
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now - 3000);
        patient.addRecord(2.0, "ECG", now - 2000);
        patient.addRecord(3.09, "ECG", now - 1000);

        IrregularBeatsAlert alert = new IrregularBeatsAlert();
        List<Alert> alerts = alert.checkAlerts(patient);
        assertTrue(alerts.isEmpty(), "No alerts should be generated as variations are just below the threshold");
    }
}
