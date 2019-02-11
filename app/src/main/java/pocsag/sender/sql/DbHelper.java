package pocsag.sender.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import pocsag.sender.Contact;
import pocsag.sender.Message;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "pocsag.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Schematic.SQL_CREATE_CONTACTS);
        db.execSQL(Schematic.SQL_CREATE_HISTORY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(Schematic.SQL_DELETE_CONTACTS);
        db.execSQL(Schematic.SQL_DELETE_HISTORY);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public List<Contact> getListOfContacts() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                Schematic.ContactsTable.COLUMN_NAME_NAME,
                Schematic.ContactsTable.COLUMN_NAME_NUMBER
        };

        String sortOrder =
                Schematic.ContactsTable.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                Schematic.ContactsTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Schematic.ContactsTable._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Schematic.ContactsTable.COLUMN_NAME_NAME));
            String number = cursor.getString(cursor.getColumnIndexOrThrow(Schematic.ContactsTable.COLUMN_NAME_NUMBER));
            contacts.add(new Contact(name, id, number));
        }
        cursor.close();
        return contacts;
    }

    public List<Message> getListOfMessage(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                Schematic.HistoryTable.COLUMN_NAME_CONTACT,
                Schematic.HistoryTable.COLUMN_NAME_OUT,
                Schematic.HistoryTable.COLUMN_NAME_MESSAGE
        };

        String sortOrder =
                Schematic.HistoryTable._ID + " ASC";

        String selection = Schematic.HistoryTable.COLUMN_NAME_CONTACT + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                Schematic.HistoryTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        List<Message> messages = new ArrayList<>();
        while (cursor.moveToNext()) {
            long messageId = cursor.getLong(cursor.getColumnIndexOrThrow(Schematic.HistoryTable._ID));
            String message = cursor.getString(cursor.getColumnIndexOrThrow(Schematic.HistoryTable.COLUMN_NAME_MESSAGE));
            int flag = cursor.getInt(cursor.getColumnIndexOrThrow(Schematic.HistoryTable.COLUMN_NAME_OUT));
            messages.add(new Message(messageId, message, flag > 0));
        }
        cursor.close();
        return messages;
    }

    public Message addMessage(long userId, String message) {
        ContentValues values = new ContentValues();
        values.put(Schematic.HistoryTable.COLUMN_NAME_MESSAGE, message);
        values.put(Schematic.HistoryTable.COLUMN_NAME_CONTACT, userId);
        values.put(Schematic.HistoryTable.COLUMN_NAME_OUT, 1);
        long id = getWritableDatabase().insert(Schematic.HistoryTable.TABLE_NAME, null, values);
        return new Message(id, message, true);
    }

    public Contact addContact(String name, String number) {
        ContentValues values = new ContentValues();
        values.put(Schematic.ContactsTable.COLUMN_NAME_NAME, name);
        values.put(Schematic.ContactsTable.COLUMN_NAME_NUMBER, number);
        long id = getWritableDatabase().insert(Schematic.ContactsTable.TABLE_NAME, null, values);
        return new Contact(name, id, number);
    }
}
