package com.reportplant.Controller;


import com.reportplant.Model.HistoryJson;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlConnection {
    public static final String DB_NAME = "ReportPlant";
    public static final String HOST = "localhost";
    public static final String PORT = "3306";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Pentium13";
    public static final String TABLE_NAME = "History";
    private static Logger logger;
    private static Connection connection;


    public MysqlConnection(){
        logger = Logger.getLogger(this.getClass().getName());
        Utils.configureLogger(logger);
        run();
    }

    private void run() {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;

        logger.log(Level.ALL, "Connecting to database...");

        try {
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            logger.log(Level.ALL, "Database connected!");
        } catch (SQLException exception) {
            logger.log(Level.ALL, "Cannot connect the database!");
            throw new IllegalStateException("Cannot connect the database!", exception);
        }
    }

    public int[] insertHistoryBatch(String tagName, List<HistoryJson.Entry> historyList) throws SQLException {
        connection.setAutoCommit(true); //   otherwise may require use of:  connection.commit();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + DB_NAME + "." + TABLE_NAME + " " +
                "(timestamp, read_timestamp, tag_name, value, status) VALUES (?, ?, ?, ?, ?)");
        // indexes: 1-timestamp, 2-read_timestamp, 3-tag_name, 4-value, 5-status
        int TIMESTAMP = 1, READ_TIMESTAMP = 2, TAG_NAME = 3, VALUE = 4, STATUS = 5;

        for (HistoryJson.Entry historyEntry : historyList){
            preparedStatement.setTimestamp(TIMESTAMP, new Timestamp((long)(historyEntry.getTimestamp()*1000))); // multiply by 1000 to keep milliseconds
            if (historyEntry.getReadTimestamp() != null)
                preparedStatement.setTimestamp(READ_TIMESTAMP, new Timestamp(historyEntry.getReadTimestamp() * 1000)); // multiply by 1000 to conform to format
            else
                preparedStatement.setNull(READ_TIMESTAMP, Types.TIMESTAMP);
            preparedStatement.setString(TAG_NAME, tagName);
            // VALUES or STATUS CAN BE EMPTY AND ARE SET NULL IN MYSQL
            if (historyEntry.getValue() != null)
                preparedStatement.setDouble(VALUE, historyEntry.getValue());
            else
                preparedStatement.setNull(VALUE,Types.DOUBLE);
            if (historyEntry.getStatus() != null)
                preparedStatement.setString(STATUS, historyEntry.getStatus());
            else
                preparedStatement.setNull(STATUS,Types.VARCHAR);
            preparedStatement.addBatch();
        }
        int[] results = preparedStatement.executeBatch();
        preparedStatement.close();
        return results;
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }



//    public boolean initDB(){
//        boolean failed = false;
//        try {
//            Statement statement = connection.createStatement();
//
//            if (statement.execute("DROP DATABASE ReportPlant;"))
//                failed = true;
//
//            if (statement.execute("CREATE DATABASE ReportPlant;") )
//                failed = true;
//
//            if (statement.execute("CREATE TABLE 'ReportPlant'.'History' ( 'timestamp' TIMESTAMP(3) NOT NULL, 'read_timestamp' TIMESTAMP , 'tag_name' VARCHAR(150) NOT NULL , " +
//                    "'value' DOUBLE NULL, 'status' VARCHAR(50) , INDEX 'timestamp_index' ('timestamp')) ENGINE = InnoDB;") )
//                failed = true;
//
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
//        }
//        return failed;
//    }

//    // i.e. testQuery("SELECT * FROM ReportPlant.History");
//    public void testQuery(String query) {
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//            resultSet.getWarnings();
//            resultSet.close();
//            statement.close();
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        }
//    }
}
