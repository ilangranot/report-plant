package com.reportplant.Model;

// field names order in entries array:
//"TABLE_CAT"
//"TABLE_SCHEM"
//"TABLE_NAME"
//"COLUMN_NAME"   <------- only interested in this one
//"DATA_TYPE"
//"TYPE_NAME"
//"COLUMN_SIZE"
//"BUFFER_LENGTH"
//"DECIMAL_DIGITS"
//"NUM_PREC_RADIX"
//"NULLABLE"
//"REMARKS"
//"COLUMN_DEF"
//"SQL_DATA_TYPE"
//"SQL_DATETIME_SUB"
//"CHAR_OCTET_LENGTH"
//"ORDINAL_POSITION"
//"IS_NULLABLE"


public class HistoryColumnsJson extends GeneralJson {
    private HistoryColumnsResults results; //class extends JsonResponse.Results see below;

    // Holds the values corresponding to JSON field "values"
    private class HistoryColumnsResults extends GeneralJson.Results{
        // values[n][m], m = 17, some could be NULL
        // Field has to be names values to conform to json and class inheritance
        private String[][] values; // use type class instead of primitives
    }

    public String[][] getValues(){
        return this.results.values;
    }

    public static class ColumnEntry {
        String columnName; //complies with VTScada naming scheme

        public ColumnEntry(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

}
