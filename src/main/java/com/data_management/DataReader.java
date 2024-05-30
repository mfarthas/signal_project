package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Reads data from a WebSocket server and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @param websocketUrl the URL of the WebSocket server to connect to
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage, String websocketUrl) throws IOException;
}
