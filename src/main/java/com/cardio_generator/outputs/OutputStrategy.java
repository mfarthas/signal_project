package com.cardio_generator.outputs;

/**
 * The `OutputStrategy` interface defines a method for outputting health data.
 */
public interface OutputStrategy {

    /**
     * Outputs health data with the specified patient ID, timestamp, label, and data value.
     *
     * @param patientId The ID of the patient whose data is being outputted.
     * @param timestamp The timestamp of when the data was generated.
     * @param label The label or type of the data.
     * @param data The actual data value to be outputted.
     */
    void output(int patientId, long timestamp, String label, String data);
}
