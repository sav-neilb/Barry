package uk.co.savant.barry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  Neilb
 * <p>
 * <p>
 * Details:      :-  Created by Neilb on 26/01/2017.
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

class DateCalculation {


    String addDaysToDate(String strdate, int iDays) {
        String result;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.UK);
            Calendar c = Calendar.getInstance();

            c.setTime(sdf.parse(strdate));

            c.add(Calendar.DATE, iDays);
            result = sdf.format(c.getTime());  // dt is now the new date
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }

    String subtractDaysFromDate(String strDate, int iDays) {
        String result;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.UK);
            Calendar c = Calendar.getInstance();

            c.setTime(sdf.parse(strDate));

            c.add(Calendar.DATE, -iDays);
            result = sdf.format(c.getTime());  // dt is now the new date
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }
}
