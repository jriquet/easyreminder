package fr.free.riquet.jeancharles.easyreminder;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Jean-Charles on 14/09/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "easyReminderDB.db";

    private static final String TABLE_USER = "users";
    private static final String TABLE_TASK = "task";

    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_FIRSTNAME = "firstname";
    public static final String COLUMN_USER_LASTNAME = "lastname";
    public static final String COLUMN_USER_EMAIL = "email";

    // Post Table Columns
    private static final String COLUMN_TASK_ID = "id";
    private static final String COLUMN_TASK_USER_ID_FK = "userId";
    private static final String COLUMN_TASK_NAME = "name";
    private static final String COLUMN_TASK_DESC = "desc";
    private static final String COLUMN_TASK_CREATED_AT = "createdAt";
    private static final String COLUMN_TASK_UNTIL_DATE = "untilDate";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY," + COLUMN_USER_USERNAME
                + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_FIRSTNAME
                + " TEXT,"  + COLUMN_USER_LASTNAME + " TEXT,"  + COLUMN_USER_EMAIL + " TEXT" + ")";

        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_TASK +
                "(" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                COLUMN_TASK_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USER + "," + // Define a foreign key
                COLUMN_TASK_NAME + " TEXT," + COLUMN_TASK_DESC + " TEXT,"
                + COLUMN_TASK_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP," + COLUMN_TASK_UNTIL_DATE + " DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public boolean checkUser(String username) {
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public User findUser(String username, String password) {
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_USERNAME + " =  \"" + username + "\" AND " + COLUMN_USER_PASSWORD + " =  \"" + password + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setID(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    public boolean deleteUser(String username) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();
        if (cursor.moveToFirst()) {
            user.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                    new String[] { String.valueOf(user.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }


    public void saveSettings(String username, String firstname, String lastname, String email) {
        String query = "Update " + TABLE_USER + " SET " + COLUMN_USER_FIRSTNAME + " = \"" + firstname +"\", " + COLUMN_USER_LASTNAME + " = \"" + lastname + "\", " + COLUMN_USER_EMAIL + " = " + "\"" + email + "\" WHERE " + COLUMN_USER_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        db.rawQuery(query, null);
        db.close();
    }


    public void addTask(String name, String desc, String untilDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, name);
        values.put(COLUMN_TASK_DESC, desc);
        values.put(COLUMN_TASK_UNTIL_DATE, untilDate);
        values.put(COLUMN_TASK_CREATED_AT, Utilities.getDateTime());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_TASK, null, values);
        db.close();
    }

}