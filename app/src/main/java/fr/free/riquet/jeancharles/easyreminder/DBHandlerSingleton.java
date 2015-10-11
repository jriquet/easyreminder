package fr.free.riquet.jeancharles.easyreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;


public class DBHandlerSingleton extends SQLiteOpenHelper {

    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_FIRSTNAME = "firstname";
    public static final String COLUMN_USER_LASTNAME = "lastname";
    public static final String COLUMN_USER_EMAIL = "email";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "easyReminderDB.db";
    private static final String TABLE_USER = "users";
    private static final String TABLE_TASK = "task";
    // Post Table Columns
    private static final String COLUMN_TASK_ID = "id";
    private static final String COLUMN_TASK_USER_ID_FK = "userId";
    private static final String COLUMN_TASK_NAME = "name";
    private static final String COLUMN_TASK_DESC = "desc";
    private static final String COLUMN_TASK_CREATED_AT = "createdAt";
    private static final String COLUMN_TASK_UNTIL_DATE = "untilDate";
    private static final String COLUMN_TASK_DONE = "done";
    private static final String COLUMN_TASK_COLOR = "color";
    private static DBHandlerSingleton _instance = null;
    private Context _context;

    private DBHandlerSingleton(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._context = context;
    }

    public static DBHandlerSingleton getInstance(Context ctx) {
        if (_instance == null) {
            _instance = new DBHandlerSingleton(ctx.getApplicationContext());
        }
        return _instance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN){
            db.setForeignKeyConstraintsEnabled(true);
        }

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
                + COLUMN_TASK_CREATED_AT + " DEFAULT CURRENT_TIMESTAMP," + COLUMN_TASK_UNTIL_DATE + " DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_TASK_DONE + " INTEGER, " + COLUMN_TASK_COLOR + " INTEGER " +
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

        if (cursor.moveToFirst()) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
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
            user.setFirstname(cursor.getString(3));
            user.setLastname(cursor.getString(4));
            user.setEmailAddress(cursor.getString(5));
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

    public void saveSettings(long id, String firstname, String lastname, String email, String password) {
        String query = "Update " + TABLE_USER + " SET " + COLUMN_USER_FIRSTNAME + " = \"" + firstname + "\", " +
                COLUMN_USER_LASTNAME + " = \"" + lastname + "\", " +
                COLUMN_USER_PASSWORD + " = \"" + password + "\", " +
                COLUMN_USER_EMAIL + " = " + "\"" + email + "\" WHERE " +
                COLUMN_USER_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public void modifyPassword(long id, String password) {
        String query = "Update " + TABLE_USER + " SET " + COLUMN_USER_PASSWORD + " = \"" + password + "\", WHERE " +
                COLUMN_USER_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public long addTask(Task taskToAdd) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskToAdd.get_name());
        values.put(COLUMN_TASK_DESC, taskToAdd.get_desc());
        values.put(COLUMN_TASK_USER_ID_FK, taskToAdd.get_user_id());
        values.put(COLUMN_TASK_UNTIL_DATE, taskToAdd.get_limiteDate());
        values.put(COLUMN_TASK_CREATED_AT, Utilities.getDateTime());
        values.put(COLUMN_TASK_DONE, 0);
        values.put(COLUMN_TASK_COLOR, taskToAdd.get_color());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(TABLE_TASK, null, values);
        db.close();

        return id;
    }

    public List getTasksList(User user) {
        List<Task> tasksList = new ArrayList<Task>();
        String query = "Select * FROM " + TABLE_TASK + " WHERE " + COLUMN_TASK_USER_ID_FK + " =  \"" + user.getID() + "\"" +
                "ORDER BY " + COLUMN_TASK_DONE + ", " + COLUMN_TASK_CREATED_AT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(Integer.parseInt(cursor.getString(0)));
                task.set_user_id(Integer.parseInt(cursor.getString(1)));
                task.set_name(cursor.getString(2));
                task.set_desc(cursor.getString(3));
                task.set_createdAt(cursor.getString(4));
                task.set_limiteDate(cursor.getString(5));
                int id = Integer.parseInt(cursor.getString(0));
                String state = cursor.getString(6);
                if (cursor.getString(6).equals("1")) {
                    task.set_checked(true);
                }
                else {
                    task.set_checked(false);
                }
                task.set_color(Integer.parseInt(cursor.getString(7)));

                tasksList.add(task);
            } while (cursor.moveToNext());
            db.close();
            return tasksList;
        } else {
            db.close();
            return null;
        }
    }

    public boolean deleteTask(long id) {
        boolean result;
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TASK, COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(id)});
        result = true;
        db.close();

        return result;
    }

    public void updateTask(long id, String name, String desc, String limiteDate) {
        String query = "Update " + TABLE_TASK + " SET " + COLUMN_TASK_NAME + " = \"" + name + "\", " +
                COLUMN_TASK_DESC + " = \"" + desc + "\", " +
                COLUMN_TASK_UNTIL_DATE + " = " + "\"" + limiteDate + "\" WHERE " +
                COLUMN_TASK_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public void setColorTask(long id, int color) {
        String query = "Update " + TABLE_TASK + " SET " + COLUMN_TASK_COLOR + " = \"" + color + "\" " +
                " WHERE " + COLUMN_TASK_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }


    public void setTaskDone(long id, boolean done) {
        int flag = (done)? 1 : 0;
        String query = "Update " + TABLE_TASK + " SET " + COLUMN_TASK_DONE + " = \"" + flag + "\" WHERE " +
                COLUMN_TASK_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public void truncateTaskTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_TASK;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        query = "VACUUM";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        cursor.close();
        db.close();
    }
}