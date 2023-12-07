package com.reportplant.Model;

import java.util.LinkedList;
import java.util.List;

public class HistoryJson extends GeneralJson {
    private HistoryResults results;

    private class HistoryResults extends GeneralJson.Results{
        // values[n][m], m = 3 = [TIMESTAMP, READ TIMESTAMP, VALUE], VALUE could be NULL
        private String[][] values; // use type class instead of primitives, i.e. String, Double, Integer, etc.
    }


    public List<Entry> getValuesAsList() {
        List<HistoryJson.Entry> resultsList = new LinkedList<>();
        for (String[] values : results.values){
            resultsList.add(new HistoryJson.Entry(values));
        }
        return resultsList;
    }

    public static class Entry {
        private Double timestamp; //complies with VTScada naming scheme
        private Long readTimestamp;
        private Double value;
        private String status;

        /**
         * Constructor to parse the values as an entry
         * values[m], m = 3 = [TIMESTAMP, READ TIMESTAMP, VALUE], VALUE could be NULL
         * @param values an array of strings
         */
        public Entry(String[] values) {
            this.timestamp = Double.valueOf(values[0]);
            if (values[1] != null)
                this.readTimestamp = Long.valueOf(values[1]);
            // initialize value, status, or neither
            if (values[2] != null){
                try {
                    this.value = Double.parseDouble(values[2]);
                } catch (NumberFormatException numberFormatException) {
                    this.status = values[2];
                }
            }
        }

        public Double getTimestamp() {
            return timestamp;
        }

        public Double getValue() {
            return value;
        }

        public Long getReadTimestamp() {
            return readTimestamp;
        }

        public String getStatus() {
            return status;
        }
    }
}
