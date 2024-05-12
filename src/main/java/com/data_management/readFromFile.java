package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readFromFile implements DataReader {
    private final String filePath;

    /**
     * Constructor
     *
     * @param filePath the path to the file containing data to read
     */
    public readFromFile(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads data from the specified file and stores it in the given DataStorage instance.
     *
     * @param dataStorage the data storage where data will be stored
     * @throws IOException if there is an error reading the data from the file
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read each line in the file
            while ((line = reader.readLine()) != null) {
                // Split the line by comma to get the fields
                String[] fields = line.split(",");

                // Ensure the data is of the right format
                if (fields.length == 4) {

                    int patientId = Integer.parseInt(fields[0]);
                    double measurementValue = Double.parseDouble(fields[1]);
                    String recordType = fields[2];
                    long timestamp = Long.parseLong(fields[3]);

                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                } else {
                    // Handle lines with wrong format
                    System.err.println("Ignored: " + line);
                }
            }
        }
    }
}

