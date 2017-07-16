package io.uscool.contactapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.uscool.contactapp.model.Contact;
import io.uscool.contactapp.model.Message;

/**
 * Created by ujjawal on 16/7/17.
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabasHelper";
    private static final String DB_NAME = "fuelFriend";
    private static final String DB_SUFFIX = ".db";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper mInstance;

    private static List<Message> mMessageList;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME+DB_SUFFIX, null, DB_VERSION);
    }

    /**
     * Database access point
     * Singleton instance
     * @param context context of the activity
     * @return database instance
     */
    private static DatabaseHelper getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // leaving it blank for now
    }

    /**
     * public methods to get list of messages {@link Message} from database
     * @param context context in which this is running in
     * @param fromDatabase <code>true</code> if database refresh is required, else <code>false</code>
     * @return All messages stored in database
     */
    public static List<Message> getMessages(Context context, boolean fromDatabase) {
        if(mMessageList == null || fromDatabase) {
            mMessageList = loadMessages(context);
        }
        return mMessageList;
    }


    /**
     * Loads messages from database
     * @param context context in which this is running in
     * @return message list if database is not empty, else null
     */
    private static List<Message> loadMessages(Context context) {
        Cursor data = getMessageCursor(context);
        // handle empty database case
        if(data != null && data.getCount() > 0) {
            List<Message> tmpMessageList = new ArrayList<>(data.getCount());

            do {
                final Message message = readMessages(data);
                tmpMessageList.add(message);
            } while (data.moveToNext());
            return tmpMessageList;
        }
        return null;
    }


    /**
     * Gets all messages wrapped in a {@link Cursor} positioned at it's first element
     * @param context The context this is running in
     * @return All categories stored in database
     */
    private static Cursor getMessageCursor(Context context) {
        SQLiteDatabase database = getReadableDatabase(context);
        // Query the database
        Cursor data = database.query(MessageTable.NAME,
                MessageTable.PROJECTION, null, null,
                null, null, null);
        // handles empty cursor case
        if(data != null) {
            data.moveToFirst();
        }
        return data;
    }

    /**
     * Gets a message from the given position of cursor provided
     * magic number based on table projection, see {@link MessageTable}
     * @param data The cursor containing the data
     * @return The found message
     */
    private static Message readMessages(Cursor data) {
        final String name = data.getString(1); //name of the message sender
        final String number = data.getString(2); // number of the message sender
        final String timestamp = data.getString(3); // time at which message was sent
        final String otpCode = data.getString(4); // OTP code, sent in message

        // creates contact for message
        Contact contact = new Contact(name, number);
        return new Message(contact, otpCode, timestamp);
    }

    /**
     * public method used to write message in database table
     * @param context context in which this is running in
     * @param message message to be stored in database
     */
    public static void writeMessage(Context context, Message message) {
        SQLiteDatabase writableDatabase = getWritableDatabase(context);
        try {
            writableDatabase.beginTransaction();
            try {
                fillMessage(writableDatabase, message);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        } catch (IOException e) {
            Log.e(TAG, "FillDatabase", e);
        }
    }

    /**
     * fill the given message in database table
     * @param db instance of db
     * @param message message to be stored in database
     * @throws IOException in case of error
     */
    private static void fillMessage(SQLiteDatabase db, Message message) throws IOException {
        ContentValues values = createContentValues(message);
        db.insert(MessageTable.NAME, null, values);
    }

    /**
     * Creates the content values to fill a message in database
     * @param message message to be filled
     * @return ContentValues containing writable data
     */
    private static ContentValues createContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put(MessageTable.COLUMN_NAME, message.getContact().getFullName());
        values.put(MessageTable.COLUMN_NUMBER, message.getContact().getNumber());
        values.put(MessageTable.COLUMN_TIMESTAMP, message.getTimestamp());
        values.put(MessageTable.COLUMN_OTP, message.getOtpCode());
        return values;
    }

    private static SQLiteDatabase getReadableDatabase(Context context) {
        return getInstance(context).getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDatabase(Context context) {
        return getInstance(context).getWritableDatabase();
    }
}
