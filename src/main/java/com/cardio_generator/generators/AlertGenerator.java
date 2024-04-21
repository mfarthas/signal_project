package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The `AlertGenerator` class implements the `PatientDataGenerator` interface for generating
 * alert data for patients. It simulates the triggering and resolving of alerts for each patient
 * based on a specified probability.
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();

    // uppercase (A) in Alert, changed to lowercase (a)
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructs a new instance of `AlertGenerator` with the specified number of patients.
     *
     * @param patientCount The total number of patients for which to generate alert data.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert data for the specified patient ID and sends it to the provided output strategy.
     *
     * @param patientId The ID of the patient for whom to generate alert data.
     * @param outputStrategy The output strategy to which the generated data will be sent.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // deleted the "Output the alert" comments due to triviality
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // uppercase (L) changed to lowercase (l)
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
