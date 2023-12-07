package com.reportplant.Controller;

import java.io.IOException;
import java.util.logging.*;

public class Utils {
    public static void configureLogger(Logger logger) {
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.ALL);
        logger.addHandler(handlerObj);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    public static void logToFile(Logger logger){
        try {
            // This block configure the logger with handler and simpleFormatter
            FileHandler fileHandler = new FileHandler("reportplant.log");
            logger.addHandler(fileHandler);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
