package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * The `TcpOutputStrategy` class implements the `OutputStrategy` interface for
 * outputting health data via TCP socket. Data is sent to a connected client
 * through a TCP server running on a specified port.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Constructs a new instance of `TcpOutputStrategy` with the specified port.
     *
     * @param port The port on which the TCP server will listen for connections.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs health data with the specified patient ID, timestamp, label, and data value
     * via TCP socket to the connected client.
     *
     * @param patientId The ID of the patient whose data is being outputted.
     * @param timestamp The timestamp of when the data was generated.
     * @param label The label or type of the data.
     * @param data The actual data value to be outputted.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
