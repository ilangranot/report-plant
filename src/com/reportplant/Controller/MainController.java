package com.reportplant.Controller;

import com.reportplant.Adapter.HistoryColumnsAdapter;
import com.reportplant.Adapter.TagHistoryAdapter;
import com.reportplant.Model.HistoryColumnsJson;
import com.reportplant.Model.HistoryJson;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private final Logger logger;

    public MainController() {
        logger = Logger.getLogger(this.getClass().getName());
        Utils.configureLogger(logger);
    }

    /**
     * Reads from REST and stores in DB History for all tags
     */
    public void run(){
        // create new db connection
        MysqlConnection mysqlConnection = new MysqlConnection();
        // get history column names
        LinkedList<HistoryColumnsJson.ColumnEntry> historyColumnNames = getHistoryColumnNames();
        // load for each TIMESTAMP, VALUE/STATUS pair
        int columnIndex = 1; // the first tag appears at index 1 since at index 0 is the timestamp
        while (columnIndex < historyColumnNames.size()){
            try {
                // get column names
                String tagReadTimestampName = historyColumnNames.get(columnIndex).getColumnName(); // the read timestamp of the tag name to get
                String tagValueOrStatusName = historyColumnNames.get(columnIndex + 1).getColumnName(); // the tag name to get
                // assert column names match
                if (!tagReadTimestampName.replace(":StorageTimestamp","").equals(tagValueOrStatusName.replace(":Value",""))) // ASSERT THAT THE COLUMNS MATCH
                    throw new RuntimeException("Assertion error in match columns.");
                // make REST request
                List<HistoryJson.Entry> entryList = getTagHistoryEntries(tagReadTimestampName, tagValueOrStatusName);
                // write to db
                writeTagToDb(mysqlConnection, tagValueOrStatusName, entryList);
            } catch (RuntimeException runtimeException){
                logger.log(Level.SEVERE, runtimeException.getMessage());
            } finally {
                columnIndex = columnIndex + 2;
            }
        }
        // close the db connection
        mysqlConnection.closeConnection();
    }

    private LinkedList<HistoryColumnsJson.ColumnEntry> getHistoryColumnNames() {
        HistoryColumnsAdapter historyColumnsAdapter = new HistoryColumnsAdapter();
        return historyColumnsAdapter.getColumnEntries();
    }

    // get the history of a specific tag
    private List<HistoryJson.Entry> getTagHistoryEntries(String readTimestamp, String tagName) {
        TagHistoryAdapter tagHistoryAdapter = new TagHistoryAdapter(readTimestamp, tagName);
        return tagHistoryAdapter.getEntryList();
    }

    // write to db
    private void writeTagToDb(MysqlConnection mysqlConnection, String tagName, List<HistoryJson.Entry> entryList) {
        try {
            mysqlConnection.insertHistoryBatch(tagName, entryList);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
