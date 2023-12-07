package com.reportplant.Model;

/**
 * Class to represent the common fields in all VTScada JSON reads
 */

public class GeneralJson {
    private int returnCode;
    private String errorMessage;
    private String nextPageToken;
    private PaginationStats paginationStats;

    public String getNextPageToken(){
        return nextPageToken;
    }

    public PaginationStats getPaginationStats() {
        return paginationStats;
    }

    public class PaginationStats{
        private int numRetrieved;
        private int numResults;
        private double timestampRetrieved;
    }

    public class Results{
        private String[] fieldNames;
        private String[] fieldTypeNames;
    }
}
