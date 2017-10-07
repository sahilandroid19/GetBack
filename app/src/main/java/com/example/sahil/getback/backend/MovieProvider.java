package com.example.sahil.getback.backend;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE_LIST = 1;
    private static final int MOVIE_ID = 2;

    private DatabaseHelper mHelper = null;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.Movies.TABLE_NAME, MOVIE_LIST);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.Movies.TABLE_NAME + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE_LIST:
                cursor = db.query(
                        Contract.Movies.TABLE_NAME,
                        Contract.Movies.PROJECTION_ALL,
                        selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_ID:
                cursor = db.query(
                        Contract.Movies.TABLE_NAME,
                        Contract.Movies.PROJECTION_ALL,
                        Contract.Movies._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case MOVIE_LIST:
                return Contract.Movies.CONTENT_TYPE;
            case MOVIE_ID:
                return Contract.Movies.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported Uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri uri1;
        switch (uriMatcher.match(uri)){
            case MOVIE_LIST:
                long id = db.insert(Contract.Movies.TABLE_NAME, null, values);
                if(id > 0){
                    uri1 = Contract.Movies.getUriForId(id);
                }else {
                    throw new UnsupportedOperationException("Unable to insert row");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delRow;
        switch (uriMatcher.match(uri)){
            case MOVIE_LIST:
                delRow = db.delete(Contract.Movies.TABLE_NAME, null, null);
                break;
            case MOVIE_ID:
                delRow = db.delete(Contract.Movies.TABLE_NAME,
                        Contract.Movies._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(delRow != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delRow;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int upRow;
        switch (uriMatcher.match(uri)){
            case MOVIE_LIST:
                upRow = db.update(Contract.Movies.TABLE_NAME, values, null, null);
                break;
            case MOVIE_ID:
                upRow = db.update(Contract.Movies.TABLE_NAME, values,
                        Contract.Movies._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(upRow != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return upRow;
    }
}
