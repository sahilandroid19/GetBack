package com.example.sahil.getback.backend;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sahil.getback.backend.Contract.Movies;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_MOVIE = "create table " + Movies.TABLE_NAME
            + "("
            + Movies._ID + " integer primary key autoincrement, "
            + Movies.MOVIE_TITLE + " text not null, "
            + Movies.MOVIE_POSTER + " text not null, "
            + Movies.MOVIE_BACKDROP + " text not null, "
            + Movies.MOVIE_OVERVIEW + " text not null, "
            + Movies.MOVIE_RATING + " text not null, "
            + Movies.MOVIE_DATE + " text not null " + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Future statements");
    }
}
