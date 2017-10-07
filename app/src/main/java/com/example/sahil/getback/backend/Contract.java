package com.example.sahil.getback.backend;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    static final String AUTHORITY =
            "com.example.sahil.getback.backend.provider";

    private static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final class Movies implements BaseColumns{

        static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(Contract.CONTENT_URI, "movies");

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_URI.toString();

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_URI.toString();

        public static final String MOVIE_TITLE = "title";

        public static final String MOVIE_POSTER = "poster_path";

        public static final String MOVIE_BACKDROP = "backdrop_path";

        public static final String MOVIE_OVERVIEW = "overview";

        public static final String MOVIE_RATING = "rating";

        public static final String MOVIE_DATE = "release_date";

        public static final String[] PROJECTION_ALL =
                {_ID, MOVIE_TITLE, MOVIE_POSTER, MOVIE_BACKDROP, MOVIE_OVERVIEW, MOVIE_RATING, MOVIE_DATE};

        public static Uri getUriForId(long id){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString((int)id)).build();
        }

    }

}
