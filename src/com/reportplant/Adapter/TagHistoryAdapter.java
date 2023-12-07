package com.reportplant.Adapter;

import com.google.gson.Gson;
import com.reportplant.Controller.RestConnection;
import com.reportplant.Controller.Utils;
import com.reportplant.Main;
import com.reportplant.Model.HistoryJson;
import jakarta.ws.rs.client.Client;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagHistoryAdapter {
    public static final String QUERY_SUFFIX = "FROM History";
    private static final String QUERY_PREFIX = "query=SELECT Timestamp";
    public static final String NEXT_PAGE_TOKEN = "&pageToken=";
    private final Logger logger;
    private final Client client;
    private final List<HistoryJson.Entry> entryList; // holds the data
    private HistoryJson currentHistoryJson;



    public TagHistoryAdapter(String readTimestamp, String tagName) {
        entryList = new LinkedList<>();
        logger = Logger.getLogger(this.getClass().getName());
        Utils.configureLogger(logger);
        this.client = RestConnection.getClient();
        run(QUERY_PREFIX + ", '" + readTimestamp + "', '" + tagName + "' " + QUERY_SUFFIX);
    }

    // Fills entries and paginationStats from one or more requests
    private void run(String initialQuery){
        String query = initialQuery;
        do {
            currentHistoryJson = getHistoryJson(query);
            entryList.addAll(currentHistoryJson.getValuesAsList()); // add all values to entries list
            if (currentHistoryJson.getNextPageToken() != null){
                logger.log(Level.ALL, "there's next page ");
                query = initialQuery + NEXT_PAGE_TOKEN + currentHistoryJson.getNextPageToken();
            }
        } while (currentHistoryJson.getNextPageToken() != null);
    }

    private HistoryJson getHistoryJson(String query) {
        String jsonResponseString = getJsonResponseString(client, query, logger);
        Gson gson = new Gson();
        return gson.fromJson(jsonResponseString, HistoryJson.class);

    }


    public List<HistoryJson.Entry> getEntryList() {
        return entryList;
    }

    // Get JSON response
    private String getJsonResponseString(Client client, String query, Logger logger) {
        String jsonResponseString = null;
        try {
            jsonResponseString = RestConnection.getJsonResponseString(client, query, logger);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Error caught in " + TagHistoryAdapter.class.getName(), this);
            exception.printStackTrace();
        }
        return jsonResponseString;
    }
}
