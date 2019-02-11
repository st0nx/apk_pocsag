package pocsag.sender.sql;

import android.provider.BaseColumns;

public class Schematic {

    /* Inner class that defines the table contents */
    public static class HistoryTable implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_CONTACT = "contact";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_OUT = "out";
    }

    /* Inner class that defines the table contents */
    public static class ContactsTable implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NUMBER = "number";
    }

    public static final String SQL_CREATE_CONTACTS =
            "CREATE TABLE " + ContactsTable.TABLE_NAME + " (" +
                    ContactsTable._ID + " INTEGER PRIMARY KEY," +
                    ContactsTable.COLUMN_NAME_NAME + " TEXT," +
                    ContactsTable.COLUMN_NAME_NUMBER + " TEXT)";

    public static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + ContactsTable.TABLE_NAME;

    public static final String SQL_CREATE_HISTORY =
            "CREATE TABLE " + HistoryTable.TABLE_NAME + " (" +
                    HistoryTable._ID + " INTEGER PRIMARY KEY," +
                    HistoryTable.COLUMN_NAME_CONTACT + " INTEGER," +
                    HistoryTable.COLUMN_NAME_OUT + " INTEGER," +
                    HistoryTable.COLUMN_NAME_MESSAGE + " TEXT)";

    public static final String SQL_DELETE_HISTORY =
            "DROP TABLE IF EXISTS " + HistoryTable.TABLE_NAME;
}
