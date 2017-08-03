package uk.co.savant.barry;

/**
 *
 *    Barry –
 *    *************************************************
 *
 *    Copyright     :-  National Blood Authority, 2017
 *
 *    Author        :-  Neilb
 *
 *
 *    Details:      :-  Created by Neilb on 26/01/2017.
 *
 *
 *
 *  Expiration Date [Data Structure 004]
 *  Purpose: Data Structure 004 shall indicate the date at the end of which the item expires. This is intended to be used for
 *  medical devices with a human tissue component or for supplies such as filters or solutions. While in the past this data
 *  structure has been used for blood, tissue, or cellular therapy products, it is now recommended that Data Structure 005 be
 *  used for these products.
 *
 *  Structure: =>cyyjjj
 *
 *  Element Length  Type
 *  =       1       data identifier, first character
 *  >       1       data identifier, second character
 *  c       1       numeric {0–9}
 *  yy      2       numeric {0–9}
 *  jjj     3       numeric {0–9}
 *
 *  The six (6)-character data content string, cyyjjj, is encoded and interpreted as follows:
 *  c shall specify the century of the year in which the item expires
 *  yy shall specify the year within the century in which the item expires
 *  jjj shall specify the ordinal (Julian) date on which the item expires
 *
 *
 *
 *
 * **************************************************************************
 * HISTORY RECORDS
 * DATE        WHO  ACTION
 * --------------------------------------------------------------------------
 *
 *
 */

class ExpiryDateValidation {
    private boolean isExpiryDateValid = false;
    private DateCalculation dateCalculation = new DateCalculation();

    private static ExpiryDateValidation TheInstance = new ExpiryDateValidation();

    public static ExpiryDateValidation GetInstance(){
        return ExpiryDateValidation.TheInstance;
    }

    ExpiryDateValidation() {
    }


    String validateExpiryDateAndTime(String strExpiryDateTimeBarcode) {
        String result = "";
        String strDatePart = strExpiryDateTimeBarcode.substring(0, 8);   // e.g. &>019023
        String strTimePart = strExpiryDateTimeBarcode.substring(8, 11);  // i.e. hhmm

        strDatePart = validateExpiryDate(strDatePart);
        if (strDatePart.isEmpty()){

            setExpiryDateValid(false);

            result = "";
        } else {

            if (strTimePart.isEmpty()){

                setExpiryDateValid(true);
                result = strDatePart + "0000";
            } else {
                setExpiryDateValid(true);
                result = strDatePart + strTimePart;
            }
        }

        return result;
    }



    String validateExpiryDate(String strExpiryDateBarcode) {
        String result = "";
        boolean bValidBarcode;

        if ((strExpiryDateBarcode.length() == 8) || (strExpiryDateBarcode.length() == 10)) { // Barcode e.g. &>019023 or A20190234A
            bValidBarcode = true;

            String strCentury = strExpiryDateBarcode.substring(2, 3);
            String strYear = strExpiryDateBarcode.substring(3, 5);
            String strDayno = strExpiryDateBarcode.substring(5, 8);

            int iCentury = 20 + Integer.valueOf(strCentury);
            strCentury = Integer.toString(iCentury);

            strYear = strCentury + strYear;

            int iDayno = Integer.valueOf(strDayno);

            String newDate = dateCalculation.addDaysToDate(strYear+"0101", iDayno-1);

            if (!newDate.isEmpty()) {

                setExpiryDateValid(true);

                return newDate;
            } else {
                result = "";
            }
        }

        setExpiryDateValid(false);

        return result;
    }

    public boolean isExpiryDateValid() {
        return isExpiryDateValid;
    }

    private void setExpiryDateValid(boolean expiryDateValid) {
        isExpiryDateValid = expiryDateValid;
    }
}
