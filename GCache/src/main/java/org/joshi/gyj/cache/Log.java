package org.joshi.gyj.cache;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private static void configureLogger() {
        // Create a file handler to append logs to a file
        try {
            Handler fileHandler = new FileHandler("app.log", true); // true indicates append mode
            fileHandler.setFormatter(new SimpleFormatter()); // Use simple text format

            // Add the file handler to the logger
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            // Handle IOException if unable to create file handler
            e.printStackTrace();
        }

        // Set logger level to INFO (configurable)
        logger.setLevel(Level.INFO);
    }
}
