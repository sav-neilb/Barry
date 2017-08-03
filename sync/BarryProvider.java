package uk.co.savant.barry.sync;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import uk.co.savant.barry.database.BarryDatabase;

/**
 * barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  Neilb
 * <p>
 * <p>
 * Details:      :-  Created by Neilb on 21/02/2017.
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * **************************************************************************
 * HISTORY RECORDS
 * DATE        WHO  ACTION
 * --------------------------------------------------------------------------
 */

public class BarryProvider extends ContentProvider {

    public static final String TAG = "BarryProvider";

    private BarryDatabase barryDatabase;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final String QUERY_PARAMETER_DISTINCT = "distinct";

    private static final int DOCUMENTARCHIVES = 100;
    private static final int DOCUMENTARCHIVE_CODE = 101;
    private static final int DOCUMENTARCHIVES_DISTINCT = 102;

    private static final int SEARCH_SUGGESTIONS = 200;
    private static final int SEARCH_SUGGESTIONS_QUERY = 201;

    private static final int FAVOURITECODES = 301;
    private static final int FAVOURITECODES_CODE = 302;

    private static final int CODEHISTORY = 400;
    private static final int CODEHISTORY_CODE = 401;

    @Override
    public boolean onCreate() {
        barryDatabase = new BarryDatabase(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = BarryContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BarryContract.PATH_DOCUMENTARCHIVE, DOCUMENTARCHIVES);
        matcher.addURI(authority, BarryContract.PATH_DOCUMENTARCHIVE + "/*", DOCUMENTARCHIVE_CODE);

        matcher.addURI(authority, BarryContract.PATH_SEARCH_SUGGESTION, SEARCH_SUGGESTIONS);

        matcher.addURI(authority, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGESTIONS);
        matcher.addURI(authority, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGESTIONS_QUERY);

        matcher.addURI(authority, BarryContract.PATH_FAVOURITE_CODES, FAVOURITECODES);
        matcher.addURI(authority, BarryContract.PATH_FAVOURITE_CODES + "/*", FAVOURITECODES_CODE);

        matcher.addURI(authority, BarryContract.PATH_CODE_HISTORY, CODEHISTORY);
        matcher.addURI(authority, BarryContract.PATH_CODE_HISTORY + "/*", CODEHISTORY_CODE);

        return matcher;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
