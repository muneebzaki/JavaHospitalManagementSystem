package com.hospital.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoggingUtils {
    private static final Logger LOGGER = Logger.getLogger("HospitalSystem");
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "hospital.log";

    static {
        try {
            // Create logs directory if it doesn't exist
            if (!Files.exists(Paths.get(LOG_DIR))) {
                Files.createDirectories(Paths.get(LOG_DIR));
            }

            // Configure file handler
            FileHandler fileHandler = new FileHandler(LOG_DIR + "/" + LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize logging: " + e.getMessage());
        }
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void error(String message, Throwable thrown) {
        LOGGER.log(Level.SEVERE, message, thrown);
    }

    public static void debug(String message) {
        LOGGER.fine(message);
    }

    public static void trace(String message) {
        LOGGER.finest(message);
    }
} 