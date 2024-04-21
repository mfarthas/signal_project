package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The `fileOutputStrategy` class implements the `OutputStrategy` interface for
 * outputting health data to files. Data is written to files located in a specified
 * base directory, with each data type (label) having its own file.
 */
public class fileOutputStrategy implements OutputStrategy {

    private String baseDirectory; // uppercase (B) changed to lowercase (b)

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * Constructs a new instance of `fileOutputStrategy` with the specified base directory.
     *
     * @param baseDirectory The base directory where output files will be stored.
     */
    public fileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs health data with the specified patient ID, timestamp, label, and data value
     * to a file in the base directory.
     *
     * @param patientId The ID of the patient whose data is being outputted.
     * @param timestamp The timestamp of when the data was generated.
     * @param label The label or type of the data.
     * @param data The actual data value to be outputted.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // uppercase (F) changed to lowercase (f) + line is too long, so an enter added
        String filePath = file_map.computeIfAbsent(label, k ->
                Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                    patientId, timestamp, label, data); // added an enter for cleanliness
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}