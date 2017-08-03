/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.savant.barry;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static uk.co.savant.barry.LogHelper.makeLogTag;

/**
 * Utilities and constants related to app preferences.
 */
public class PrefUtils {
    private static final String TAG = makeLogTag("PrefUtils");

    /**
     * Boolean indicating whether ToS has been accepted
     */
    private static final String PREF_DECLINED_WIFI_SETUP = "pref_declined_wifi_setup";

    /**
     * Long indicating when a sync was last ATTEMPTED (not necessarily succeeded)
     */
    private static final String PREF_LAST_SYNC_SPREADSHEET_ATTEMPTED = "pref_last_sync_sp_attempted";

    /**
     * Long indicating when a sync last SUCCEEDED
     */
    private static final String PREF_LAST_SYNC_SPREADSHEET_SUCCEEDED = "pref_last_sync_sp_succeeded";


    /**
     * Sync interval that's currently configured
     */
    private static final String PREF_CUR_SYNC_INTERVAL = "pref_cur_sync_interval";


    /**
     * Boolean indicating whether we performed the (one-time) welcome.
     */
    private static final String PREF_WELCOME_DONE = "pref_welcome_done";

    /**
     * Boolean indicating if we can collect and Analytics
     */
    private static final String PREF_ANALYTICS_ENABLED = "pref_analytics_enabled";

    private static final String PREF_SERVER_ADDRESS = "pref_server_address";
    private static final String PREF_SERVER_DOMAIN = "pref_server_domain";
    private static final String PREF_SERVER_USER = "pref_server_user";
    private static final String PREF_SERVER_PASS = "pref_server_pass";
    private static final String PREF_SERVER_FOLDERPATH = "pref_server_folderpath";
    private static final String PREF_SERVER_FILENAME = "pref_server_filename";
    private static final String PREF_SERVER_WORKBOOK_INDEX = "pref_server_workbook_index";
    private static final String PREF_SERVER_CODE_COL_INDEX = "pref_server_code_column_index";
    private static final String PREF_SERVER_DESC_COL_INDEX = "pref_server_desc_column_index";
    private static final String PREF_PRINTER_ID = "pref_printer_id";
    private static final String PREF_PRINTER_DEFAULT_NUMBER_OF_LABELS = "pref_printer_default_number_of_labels";
    private static final String PREF_PRINTER_MAX_NUMBER_OF_LABELS = "pref_printer_max_number_of_labels";
    private static final String PREF_PRINTER_LABEL_XPOS = "pref_printer_label_xpos";
    private static final String PREF_PRINTER_LABEL_YPOS = "pref_printer_label_ypos";
    private static final String PREF_PRINTER_LABEL_SIZE = "pref_printer_label_size";
    private static final String PREF_PRINTER_TEXT_XPOS = "pref_printer_text_xpos";
    private static final String PREF_PRINTER_TEXT_YPOS = "pref_printer_text_ypos";
    private static final String PREF_PRINTER_TEXT_SIZE = "pref_printer_text_size";
    private static final String PREF_SYNC_FREQUENCY_KEY = "pref_sync_frequency_key";
    private static final String PREF_SYNC_FREQUENCY_PROMPT = "pref_sync_frequency_prompt";
    private static final String PREF_GENERAL_PASSCODE = "pref_general_passcode";
    private static final String PREF_GENERAL_STARTPAGE = "pref_general_startpage";
    private static final String PREF_GENERAL_KEEP_ARCHIVE_DAYS = "pref_general_keep_days";
    private static final String PREF_GENERAL_ENABLE_SCAN_TONES = "check_box_preference_enable_scan_tones";
    private static final String RESET_DATABASE_PASSWORD = "xxxyyy";

    private static SharedPreferences getDefaultSharedPreferencesMultiProcess(
            Context context) {
        return context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    public static TimeZone getDisplayTimeZone(Context context) {
        return TimeZone.getDefault();
    }

    public static String getResetDatabasePassword() {
        return RESET_DATABASE_PASSWORD;
    }

    public static void setResetDatabasePassword(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString("edit_text_reset_database", value).apply();
    }

    public static boolean hasDeclinedWifiSetup(Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getBoolean(PREF_DECLINED_WIFI_SETUP, false);
    }

    public static void markDeclinedWifiSetup(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putBoolean(PREF_DECLINED_WIFI_SETUP, true).apply();
    }

    public static boolean isWelcomeDone(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markWelcomeDone(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putBoolean(PREF_WELCOME_DONE, true).apply();
    }

    public static long getLastSyncAttemptedTime(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getLong(PREF_LAST_SYNC_SPREADSHEET_ATTEMPTED, 0L);
    }

    public static void markSyncAttemptedNow(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putLong(PREF_LAST_SYNC_SPREADSHEET_ATTEMPTED, TimeUtils.getCurrentTime(context)).apply();
    }

    public static String getLastSyncSucceededTime(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_LAST_SYNC_SPREADSHEET_SUCCEEDED, "");
    }

    public static void markSyncSucceededNow(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        sp.edit().putString(PREF_LAST_SYNC_SPREADSHEET_SUCCEEDED, currentTime).apply();
    }


    public static boolean isAnalyticsEnabled(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getBoolean(PREF_ANALYTICS_ENABLED, true);
    }


    public static long getCurSyncInterval(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getLong(PREF_CUR_SYNC_INTERVAL, 0L);
    }

    public static void setCurSyncInterval(final Context context, long interval) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putLong(PREF_CUR_SYNC_INTERVAL, interval).apply();
    }


    public static String getServerDomain(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_DOMAIN, null);
    }

    public static void setServerDomain(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_DOMAIN, value).apply();
    }


    public static String getServerUsername(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_USER, null);
    }

    public static void setServerUsername(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_USER, value).apply();
    }

    public static String getServerPassword(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_PASS, null);
    }

    public static void setServerPassword(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_PASS, value).apply();
    }

    public static String getServerAddress(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_ADDRESS, null);
    }

    public static void setServerAddress(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_ADDRESS, value).apply();
    }


    public static String getServerFolderpath(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_FOLDERPATH, null);
    }

    public static void setServerFolderpath(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_FOLDERPATH, value).apply();
    }

    public static String getServerFilename(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_FILENAME, null);
    }

    public static void setServerFilename(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_FILENAME, value).apply();
    }

    public static String getWorkbookIndex(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_WORKBOOK_INDEX, "-1");
    }

    public static void setWorkbookIndex(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_WORKBOOK_INDEX, value).apply();
    }

    public static String getServerCodeColIndex(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_CODE_COL_INDEX, null);
    }

    public static void setServerCodeColIndex(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_CODE_COL_INDEX, value).apply();
    }

    public static String getServerDescColIndex(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_SERVER_DESC_COL_INDEX, null);
    }

    public static void setServerDescColIndex(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SERVER_DESC_COL_INDEX, value).apply();
    }

    public static String getPrinterID(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_ID, null);
    }

    public static void setPrinterID(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_ID, value).apply();
    }

    public static String getPrefPrinterDefaultNumberOfLabels(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_DEFAULT_NUMBER_OF_LABELS, "1");
    }

    public static void setPrefPrinterDefaultNumberOfLabels(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_DEFAULT_NUMBER_OF_LABELS, value).apply();
    }

    public static String getPrefPrinterMaxNumberOfLabels(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_MAX_NUMBER_OF_LABELS, "0");
    }

    public static void setPrefPrinterMaxNumberOfLabels(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_MAX_NUMBER_OF_LABELS, value).apply();
    }

    public static String getSyncFrequency(final Context context) {
        try {
            SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
            return sp.getString(PREF_SYNC_FREQUENCY_KEY, "-1");
        } catch (Exception e) {
            return "-1";
        }
    }

    public static void setSyncFrequency(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_SYNC_FREQUENCY_KEY, value).apply();
    }

    public static Boolean getSyncFrequencyPrompt(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getBoolean(PREF_SYNC_FREQUENCY_PROMPT, false);
    }

    public static void setSyncFrequencyPrompt(final Context context, Boolean value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putBoolean(PREF_SYNC_FREQUENCY_PROMPT, value).apply();
    }

    public static String getPrinterLabelXPos(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_LABEL_XPOS, "");
    }

    public static void setPrinterLabelXPos(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_LABEL_XPOS, value).apply();
    }

    public static String getPrinterLabelYPos(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_LABEL_YPOS, "");
    }

    public static void setPrinterLabelYPos(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_LABEL_YPOS, value).apply();
    }

    public static String getPrinterLabelSize(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_LABEL_SIZE, "");
    }

    public static void setPrinterLabelSize(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_LABEL_SIZE, value).apply();
    }


    public static String getPrinterTextXPos(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_TEXT_XPOS, "");
    }

    public static void setPrinterTextXPos(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_TEXT_XPOS, value).apply();
    }

    public static String getPrinterTextYPos(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_TEXT_YPOS, "");
    }

    public static void setPrinterTextYPos(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_TEXT_YPOS, value).apply();
    }

    public static String getPrinterTextSize(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_PRINTER_TEXT_SIZE, "");
    }

    public static void setPrinterTextSize(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_PRINTER_TEXT_SIZE, value).apply();
    }

    public static void setGeneralPasscode(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_GENERAL_PASSCODE, value).apply();
    }

    public static String getGeneralPasscode(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_GENERAL_PASSCODE, "");
    }

    public static boolean getBarcodeBeep(final Context context) {

        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getBoolean(PREF_GENERAL_ENABLE_SCAN_TONES, true);
    }

    public static int getKeepDays(final Context context) {
        int result = 100;
        try {
            SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
            String strKeepDays = sp.getString(PREF_GENERAL_KEEP_ARCHIVE_DAYS, "100");

            result = Integer.valueOf(strKeepDays);
        } catch (Exception e) {
            // Throw away number convert exception and default to ititial value
        }
        return result;
    }

    public static void setGeneralStartPage(final Context context, String value) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.edit().putString(PREF_GENERAL_STARTPAGE, value).apply();
    }

    public static String getGeneralStartPage(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString(PREF_GENERAL_STARTPAGE, "M");
    }


    public static void registerOnSharedPreferenceChangeListener(final Context context,
                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(final Context context,
                                                                  SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static String getResetDatabase(final Context context) {
        SharedPreferences sp = getDefaultSharedPreferencesMultiProcess(context);
        return sp.getString("reset_database", "");
    }

    public static boolean gotEnoughInfoToSync(Context thisContext) {

        String errorList = "";

//        if ((getServerAddress(thisContext) == null) ||
//                (getServerAddress(thisContext).trim().equals(""))) {
//            errorList = errorList + "    - Server Address\n";
//        }

        if ((getServerFolderpath(thisContext) == null) ||
                (getServerFolderpath(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Folder Path\n";
        }

        if ((getServerFilename(thisContext) == null) ||
                (getServerFilename(thisContext).trim().equals(""))) {
            errorList = errorList + "    - File Name\n";
        }
//
//        if ((getServerCodeColIndex(thisContext) == null) ||
//                (getServerCodeColIndex(thisContext).trim().equals(""))) {
//            errorList = errorList + "    - Code Column Index\n";
//        }
//
//        if ((getServerDescColIndex(thisContext) == null) ||
//                (getServerDescColIndex(thisContext).trim().equals(""))) {
//            errorList = errorList + "    - Description Column Index\n";
//        }
//
        if ((getWorkbookIndex(thisContext) == null) ||
                (getWorkbookIndex(thisContext).trim().equals("")) ||
                (getWorkbookIndex(thisContext).trim() == "-1")) {
            errorList = errorList + "    - Workbook Index\n";
        }

        if (errorList.equals("")) {
            return gotValidInfoToSync(thisContext);
        } else {
            // Not got enough info to sync
            ErrorHandling.showErrorMessage("Not enough file information has been entered in settings to perform a sync.\n\n" +
                    "The following data is required : \n\n" + errorList, thisContext);
            return false;
        }
    }

    public static boolean gotValidInfoToSync(Context thisContext) {

        String errorList = "";

//        // Code Column index must be a letter
//        if (!(isAlphaString(getServerCodeColIndex(thisContext)))) {
//            errorList = errorList + "    - Code Column Index must be an alpha character\n";
//        }
//
//        // Description Column index must be a letter
//        if (!(isAlphaString(getServerDescColIndex(thisContext)))) {
//            errorList = errorList + "    - Description Column Index must be an alpha character\n";
//        }
//
//        // Workbook Index index must be a number - validated in the field
//        if (!(isNumericString(getWorkbookIndex(thisContext)))) {
//            errorList = errorList + "    - Workbook Index must be a number\n";
//        }

        if (errorList.equals("")) {
            return true;
        } else {
            // Not got enough info to sync
            ErrorHandling.showErrorMessage("The following data is invalid : \n\n" + errorList, thisContext);
            return false;
        }
    }

    public static boolean isAlphaString(String s) {
        String pattern = "^[a-zA-Z]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public static boolean isNumericString(String s) {
        String pattern = "^[0-9]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public static boolean gotEnoughInfoToPrint(Context thisContext) {

        String errorList = "";

        if ((getPrinterID(thisContext) == null) ||
                (getPrinterID(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Printer ID\n";
        }

        if ((getPrinterLabelSize(thisContext) == null) ||
                (getPrinterLabelSize(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Label Size\n";
        }

        if ((getPrinterLabelYPos(thisContext) == null) ||
                (getPrinterLabelYPos(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Label Y Position\n";
        }

        if ((getPrinterLabelXPos(thisContext) == null) ||
                (getPrinterLabelXPos(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Label X Position\n";
        }

        if ((getPrinterTextSize(thisContext) == null) ||
                (getPrinterTextSize(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Text Size\n";
        }

        if ((getPrinterTextYPos(thisContext) == null) ||
                (getPrinterTextYPos(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Text Y Position\n";
        }

        if ((getPrinterTextXPos(thisContext) == null) ||
                (getPrinterTextXPos(thisContext).trim().equals(""))) {
            errorList = errorList + "    - Text X Position\n";
        }

        if (errorList.equals("")) {
            return true;
        } else {
            // Not got enough info to sync
            ErrorHandling.showErrorMessage("Not enough file information has been entered in settings to print a barcode.\n\n" +
                    "The following data is required : \n\n" + errorList, thisContext);
            return false;
        }
    }

}
