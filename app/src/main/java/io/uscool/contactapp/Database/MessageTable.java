package io.uscool.contactapp.Database;

/**
 * Created by ujjawal on 16/7/17.
 * Interface used to Create and Populate Message in database
 */

public interface MessageTable {
    String NAME = "message";

    String COLUMN_ID = "_id";
    String COLUMN_NAME = "contact_name";
    String COLUMN_NUMBER = "contact_number";
    String COLUMN_TIMESTAMP = "timestamp";
    String COLUMN_OTP = "otp";

    String[] PROJECTION = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NUMBER,
            COLUMN_TIMESTAMP, COLUMN_OTP};

    String CREATE = "CREATE TABLE " + NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_NUMBER + " TEXT NOT NULL, "
            + COLUMN_TIMESTAMP + " TEXT NOT NULL, "
            + COLUMN_OTP + " TEXT NOT NULL);";
}
