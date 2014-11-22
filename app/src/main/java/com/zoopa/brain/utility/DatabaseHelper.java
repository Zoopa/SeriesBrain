package com.zoopa.brain.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zoopa.brain.model.Series;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String DB_CREATE = "CREATE TABLE series (_id integer primary key autoincrement, "
            + "seriesName text not null, seriesSeason integer not null, seriesEpisode integer not null);";
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "series";
    private final static String ROW_ID = "_id";
    private final static String ROW_SNAME = "seriesName";
    private final static String ROW_SSEASON = "seriesSeason";
    private final static String ROW_SEPISODE = "seriesEpisode";
    private final static String DB_TABLE = "series";
    private SQLiteDatabase myDB;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        myDB = getWritableDatabase();
    }

    public void closeDatabase() {
        myDB.close();
    }

    public long insertSeries(Series series) {
        ContentValues cv = createContentValues(series);
        long insertedId = myDB.insert(DB_TABLE, null, cv);
        series.setId((int) insertedId);
        return insertedId;
    }

    public boolean updateSeries(Series series) {
        ContentValues cv = createContentValues(series);
        return myDB.update(DB_TABLE, cv, ROW_ID + "=" + series.getId(), null) > 0;
    }

    public boolean deleteSeries(Series series) {
        return myDB.delete(DB_TABLE, ROW_ID + "=" + series.getId(), null) > 0;
    }

    public List<Series> getAllSeries() {
        List<Series> seriesList = new ArrayList<Series>();
        String[] rows = {ROW_ID, ROW_SNAME, ROW_SSEASON, ROW_SEPISODE};
        Cursor cursor = myDB.query(DB_TABLE, rows, null, null, null, null, ROW_SNAME, null);
        ;

        while(cursor.moveToNext()) {
            seriesList.add(new Series(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
        }

        cursor.close();

        return seriesList;
    }

    private ContentValues createContentValues(Series series) {
        ContentValues cv = new ContentValues();

        cv.put(ROW_SNAME, series.getName());
        cv.put(ROW_SSEASON, series.getSeason());
        cv.put(ROW_SEPISODE, series.getEpisode());

        return cv;
    }
}
