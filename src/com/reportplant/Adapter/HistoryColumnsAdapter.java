package com.reportplant.Adapter;

import com.google.gson.Gson;
import com.reportplant.Controller.RestConnection;
import com.reportplant.Controller.Utils;
import com.reportplant.Main;
import com.reportplant.Model.HistoryColumnsJson;
import jakarta.ws.rs.client.Client;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoryColumnsAdapter {
    private static final String QUERY = "query=GET COLUMNS History";
    private static final int COLUMN_NAME_AT_INDEX = 3;
    private final Logger logger;
    private final Client client;
    private LinkedList<HistoryColumnsJson.ColumnEntry> columnEntries;

    /**
     * Initializes with it's own client and logger
     * Holds columnEntries in its state
     */
    public HistoryColumnsAdapter() {
        logger = Logger.getLogger(this.getClass().getName());
        Utils.configureLogger(logger);
        this.client = RestConnection.getClient();
        run();
    }


    private void run(){
        String jsonResponseString = getJsonResponseString();
        HistoryColumnsJson historyColumnsJson = getHistoryColumnsJson(jsonResponseString);
        columnEntries = getListFromValues(historyColumnsJson.getValues());
    }

    public LinkedList<HistoryColumnsJson.ColumnEntry> getColumnEntries() {
        return columnEntries;
    }

    // Get JSON response
    private String getJsonResponseString() {
        String jsonResponseString = null;
        try {
            jsonResponseString = RestConnection.getJsonResponseString(client, QUERY, logger);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Error caught in " + HistoryColumnsAdapter.class.getName(), this);
            exception.printStackTrace();
        }
        return jsonResponseString;
    }

    private static HistoryColumnsJson getHistoryColumnsJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, HistoryColumnsJson.class);
    }

    private static LinkedList<HistoryColumnsJson.ColumnEntry> getListFromValues(String[][] values) {
        LinkedList<HistoryColumnsJson.ColumnEntry> resultsList = new LinkedList<>();
        for (String[] columns : values){
            resultsList.add(new HistoryColumnsJson.ColumnEntry(columns[COLUMN_NAME_AT_INDEX]));
        }
        return resultsList;
    }
}
