package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class DataStorageTest {

    DataReader reader = new MockDataReader();
    DataStorage storage = new DataStorage();

    @Test
    void testAddAndGetRecords() {

        storage.setDataReader(reader);

        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size(), "Should retrieve two records");
        assertEquals(100.0, records.get(0).getMeasurementValue(), "First record value should be 100");
    }

    @Test
    public void testGetRecordsWithinTimeRange() {

        int patientId = 1;
        storage.addPatientData(patientId, 200, "WhiteBloodCells", 1625072400000L);
        storage.addPatientData(patientId, 210, "WhiteBloodCells", 1625158801000L);
        storage.addPatientData(patientId, 220, "WhiteBloodCells", 1625245202000L);

        // Define a time range that includes only the second and third records
        long startTime = 1625158800000L;
        long endTime = 1625331600000L;

        // Retrieve records within the time range
        List<PatientRecord> records = storage.getRecords(patientId, startTime, endTime);

        // Assertions
        assertEquals(2, records.size(), "Should retrieve two records");
        assertEquals(210, records.get(0).getMeasurementValue(), "First record value should be 200");
        assertEquals(220, records.get(1).getMeasurementValue(), "Second record value should be 210");
    }


    @Test
    public void testGetRecordsOutOfTimeRange() {

        int patientId = 1;
        storage.addPatientData(patientId, 200.0, "WhiteBloodCells", 1625072400000L);

        // Define a time range that does not include the added record
        long startTime = 1625072401000L;
        long endTime = 1625072402000L;

        List<PatientRecord> records = storage.getRecords(patientId, startTime, endTime);

        assertTrue(records.isEmpty(), "Should return an empty list");
    }

    @Test
    public void testGetRecordsForNullPatient() {

        long startTime = 1625072401000L; // After Timestamp 1
        long endTime = 1625250000000L;   // Before any new record

        List<PatientRecord> records = storage.getRecords(1, startTime, endTime);

        assertTrue(records.isEmpty(), "Should return an empty list");
    }

    @Test
    public void testGetRecordsForMultiplePatients() {

        int patientId1 = 1;
        int patientId2 = 2;

        storage.addPatientData(patientId1, 200.0, "WhiteBloodCells", 1625072400000L);
        storage.addPatientData(patientId2, 210.0, "WhiteBloodCells", 1625158801000L);
        storage.addPatientData(patientId1, 220.0, "WhiteBloodCells", 1625245202000L);

        // Define a time range that includes only the first and third records
        long startTime = 1625072400000L;
        long endTime = 1625331600000L;

        List<PatientRecord> records1 = storage.getRecords(patientId1, startTime, endTime);
        List<PatientRecord> records2 = storage.getRecords(patientId2, startTime, endTime);

        assertEquals(2, records1.size(), "Should retrieve two records for patient 1");
        assertEquals(1, records2.size(), "Should retrieve one record for patient 2");
        assertEquals(200.0, records1.get(0).getMeasurementValue(), "First record value for patient 1 should be 200.0");
        assertEquals(220.0, records1.get(1).getMeasurementValue(), "Second record value for patient 1 should be 220.0");
        assertEquals(210.0, records2.get(0).getMeasurementValue(), "First record value for patient 2 should be 210.0");
    }

    @Test
    public void testRecordBoundaries() {

        int patientId = 1;

        storage.addPatientData(patientId, 100.0, "WhiteBloodCells", 1625158800000L); // Exactly at startTime
        storage.addPatientData(patientId, 150.0, "WhiteBloodCells", 1625331600000L); // Exactly at endTime

        long startTime = 1625158800000L;
        long endTime = 1625331600000L;

        List<PatientRecord> records = storage.getRecords(patientId, startTime, endTime);

        assertEquals(2, records.size(), "Should include records at the time boundaries");
    }

    @Test
    public void testDataIntegrity() {
        int patientId = 1;
        storage.addPatientData(patientId, Double.MAX_VALUE, "WhiteBloodCells", Long.MAX_VALUE);
        storage.addPatientData(patientId, -Double.MAX_VALUE, "WhiteBloodCells", Long.MIN_VALUE);

        List<PatientRecord> records = storage.getRecords(patientId, Long.MIN_VALUE, Long.MAX_VALUE);

        assertEquals(2, records.size(), "Should correctly handle max/min data values");
        assertEquals(Double.MAX_VALUE, records.get(0).getMeasurementValue(), "Check max double value");
        assertEquals(-Double.MAX_VALUE, records.get(1).getMeasurementValue(), "Check min double value");
    }
    @Test
    public void testGetRecordsByTypeWithRecords() {

        Patient patient = new Patient(1);

        patient.addRecord(120.0, "HeartRate", 1625158800000L);
        patient.addRecord(80.0, "BloodPressure", 1625158800001L);
        patient.addRecord(130.0, "HeartRate", 1625158800002L);

        List<PatientRecord> heartRateRecords = patient.dataByLabel("HeartRate");

        assertEquals(2, heartRateRecords.size(), "Should retrieve two HeartRate records");
        assertEquals(120.0, heartRateRecords.get(0).getMeasurementValue(), "First record value should be 120");
        assertEquals(130.0, heartRateRecords.get(1).getMeasurementValue(), "Second record value should be 130");
    }

    @Test
    public void testGetRecordsByTypeWithNoRecordsOfThatType() {

        Patient patient = new Patient(1);

        patient.addRecord(120.0, "HeartRate", 1625158800000L);
        patient.addRecord(130.0, "HeartRate", 1625158800002L);

        List<PatientRecord> bloodPressureRecords = patient.dataByLabel("WhiteBloodCells");

        assertTrue(bloodPressureRecords.isEmpty(), "Should return an empty list");
    }

    @Test
    public void testGetRecordsByTypeCaseSensitivity() {

        Patient patient = new Patient(1);

        patient.addRecord(200.0, "wHiTeBlOoDcElLs", 1625158800000L);
        patient.addRecord(210.0, "WHITEBLOODCELLS", 1625158800001L);
        patient.addRecord(220.0, "WhiteBloodCells", 1625158800002L);
        
        List<PatientRecord> records = patient.dataByLabel("WhiteBloodCells");

        assertEquals(1, records.size(), "Should retrieve one record with exact casing");
        assertEquals(220.0, records.get(0).getMeasurementValue(), "Value should be 220");
        
        // ---

        List<PatientRecord> SwitchingCaseRecords = patient.dataByLabel("wHiTeBlOoDcElLs");
        
        assertEquals(1, SwitchingCaseRecords.size(), "Should retrieve one record with exact casing");
        assertEquals(200.0, SwitchingCaseRecords.get(0).getMeasurementValue(), "Value should be 200");

        // ---
        
        List<PatientRecord> upperCaseRecords = patient.dataByLabel("WHITEBLOODCELLS");

        assertEquals(1, upperCaseRecords.size(), "Should retrieve one record with exact casing");
        assertEquals(210.0, upperCaseRecords.get(0).getMeasurementValue(), "Value should be 210");
    }
}