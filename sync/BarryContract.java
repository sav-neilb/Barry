package uk.co.savant.barry.sync;

import android.net.Uri;

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
public class BarryContract {

    public static final String CONTENT_AUTHORITY = "com.savant.android.barry";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DOCUMENTARCHIVE = "document";
    public static final String PATH_SEARCH_SUGGESTION = "searchsuggestions";
    public static final String PATH_FAVOURITE_CODES = "favouritecodes";
    public static final String PATH_CODE_HISTORY = "codehistory";


}
