package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The `PatientDataGenerator` interface defines a method for generating health data for a patient.
 */
public interface PatientDataGenerator {

    /**
     * Generates health data for the specified patient ID and sends it to the provided output strategy.
     *
     * @param patientId The ID of the patient for whom to generate data.
     * @param outputStrategy The output strategy to which the generated data will be sent.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
