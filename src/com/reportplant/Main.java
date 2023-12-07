package com.reportplant;

import com.reportplant.Adapter.TagHistoryAdapter;
import com.reportplant.Adapter.HistoryColumnsAdapter;
import com.reportplant.Controller.MainController;
import com.reportplant.Controller.MysqlConnection;
import com.reportplant.Controller.Utils;
import com.reportplant.Model.HistoryColumnsJson;
import com.reportplant.Model.HistoryJson;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starting ReportPlant ETL
 * 1. Implement Junit Tests
 */

public class Main {

    public static final String REPORT_PLANT_VERSION = "ReportPlant ETL v0.1";

    public static void main(String[] args)  {
        Logger logger = Logger.getLogger(Main.class.getName());
        Utils.configureLogger(logger);
        Utils.logToFile(logger);
        final long startTime = System.nanoTime();
        logger.log(Level.ALL, "Progarm starting " + REPORT_PLANT_VERSION);


        // MAIN CONTROLLER TO RUN THE PROGRAM
        MainController mainController = new MainController();
        mainController.run();

        logger.log(Level.ALL,"Total execution time is: " + String.valueOf(
                (double)( (System.nanoTime() - startTime) / 1000000 )) + " m/s");
        logger.log(Level.ALL,"Program finished. Exiting.");
    }
}
