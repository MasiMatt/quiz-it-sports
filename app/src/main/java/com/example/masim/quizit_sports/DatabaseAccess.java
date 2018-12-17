package com.example.masim.quizit_sports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DataBaseHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public String getName(int id) {
        String name = "n/a";
        Cursor cursor = database.rawQuery("SELECT * FROM players WHERE id=" + id, null);
        cursor.moveToFirst();
        name = cursor.getString(1);

        cursor.close();
        return name;
    }

    public String getImageLink(int id) {
        String link = "n/a";
        Cursor cursor = database.rawQuery("SELECT * FROM players WHERE id=" + id, null);
        cursor.moveToFirst();
        link = cursor.getString(3);

        cursor.close();
        return link;
    }

    public String getHint(int id) {
        String hint = "n/a";
        Cursor cursor = database.rawQuery("SELECT * FROM players WHERE id=" + id, null);
        cursor.moveToFirst();
        hint = cursor.getString(2);

        cursor.close();
        return hint;
    }
}
