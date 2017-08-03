package uk.co.savant.barry;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by steveo on 04/11/2016.
 */

public class ErrorHandling {

    public static void showErrorMessage(String errorMessage, Context thisContext)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(thisContext);
        dlgAlert.setMessage(errorMessage);
        dlgAlert.setTitle("Error");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
