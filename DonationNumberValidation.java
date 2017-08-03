package uk.co.savant.barry;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  Neilb
 * <p>
 * <p>
 * Details:      :-  Created by Neilb on 18/01/2017.
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

class DonationNumberValidation {
    private boolean isDonationNumberValid = false;


    // Constructor
    DonationNumberValidation() {

    }

    /**
     * Validate the donation number passed in. The donation number could be in 14, 15 or 16
     * characters
     *
     * @param CurrentDonationNumber String
     * @return String
     * @throws Exception
     */

    String validateDonationNumber(String CurrentDonationNumber) throws
            Exception {

        String TempDonationNumber;
        String DntnoMiddle;
        String CorrectDonationNumber = "";
        char EnteredCheckDigit;

        boolean ValidChars = true;

        int DonationLength;
        int NumericPrefix;

        TempDonationNumber = CurrentDonationNumber.toUpperCase().trim();
        DonationLength = TempDonationNumber.length();

        setDonationNumberValid(false);

        if (TempDonationNumber.length() > 0) {

            // Donotion number format =G0746 96 578070 75
            // AND 16 characters long

            if ((TempDonationNumber.substring(0, 1).equals("=")) &&
                    (DonationLength == 16)) {

                // Evaluate and return a single check digit
                EnteredCheckDigit = Single_Mod372(TempDonationNumber.substring(14, 16));
                if (EnteredCheckDigit == '+') EnteredCheckDigit = '*';

                if (EnteredCheckDigit == ' ') {

                    CorrectDonationNumber = " ";

                } else {

                    // Donation should be in the format - G074696578070F
                    TempDonationNumber = (TempDonationNumber.substring(1, 14) +
                            EnteredCheckDigit);
                    DonationLength = 14;

                }
            } else if (DonationLength == 15) {

                EnteredCheckDigit = Single_Mod372(TempDonationNumber.substring(13, 15));
                if (EnteredCheckDigit == '+') EnteredCheckDigit = '*';

                if (EnteredCheckDigit == ' ') {

                    CorrectDonationNumber = " ";

                } else {

                    // Donation should be in the format - G074696578070F
                    TempDonationNumber = (TempDonationNumber.substring(0, 13) +
                            EnteredCheckDigit);
                    DonationLength = 14;

                }
            }

            if (DonationLength == 14) {

                NumericPrefix = Integer.parseInt(TempDonationNumber.substring(1, 5));

                if ((TempDonationNumber.substring(0, 1).equals("G")) &&
                        (NumericPrefix >= 0) && (NumericPrefix <= 2499)) {

                    // Dntno = G074604123456J
                    // DntnoMiddle = 074604123456
                    DntnoMiddle = TempDonationNumber.substring(1, 13);

                    CharacterIterator CharDntnoMiddle = new StringCharacterIterator(
                            DntnoMiddle);

                    // Check that each character is a digit
                    for (char ch = CharDntnoMiddle.first(); ch != CharacterIterator.DONE;
                         ch = CharDntnoMiddle.next()) {

                        if (!Character.isDigit(ch)) {

                            ValidChars = false;
                            CorrectDonationNumber = " ";
                            break;

                        }
                    } // for loop
                } else {

                    CorrectDonationNumber = " ";

                }

                if (ValidChars) {

                    CorrectDonationNumber = CalculateDonorIdCheckDigit(TempDonationNumber.
                            substring(0, 13));

                    // Not equals, then report an error
                    if (!TempDonationNumber.equals(CorrectDonationNumber)) {

                        CorrectDonationNumber = " ";

                    } else {
                        setDonationNumberValid(true);
                    }
                }
            } else {
                CorrectDonationNumber = " ";
            }
        } else {
            CorrectDonationNumber = " ";
        }

        return CorrectDonationNumber;
    }

    /**
     * Verify each character within the donation number.
     *
     * @param i_Number String
     * @return String
     */
    private String CalculateDonorIdCheckDigit(String i_Number) {

        String CharString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ+";

        int Temp1;
        int Temp2 = 0;

        int FinalCheckDigit;
        String CheckDigit;
        char TmpChar;

        CharacterIterator FullDntno = new StringCharacterIterator(i_Number);

        // Cycle through the string checking each character
        for (char ch = FullDntno.first(); ch != CharacterIterator.DONE;
             ch = FullDntno.next()) {

            Temp1 = CharString.indexOf(ch); // Extract the position within 'CharString' of the character within
            Temp2 = (2 * (Temp2 + (Temp1))) % 37; // modulas 37

        }

        FinalCheckDigit = ((38 - Temp2) % 37);

        if (FinalCheckDigit == 36) {

            CheckDigit = "*";

        } else {

            TmpChar = CharString.charAt(FinalCheckDigit);
            CheckDigit = Character.toString(TmpChar);

        }

        // return the donation number and check digit
        return i_Number + CheckDigit;

    }

    /**
     * Check the check digit of the donation number
     *
     * @param i_check_Digits String
     * @return char
     */
    private char Single_Mod372(String i_check_Digits) throws Exception {

        char Result;
        String CharString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ+";
        int CheckDigitPos;
        int checkDigits;

        // Convert string to integer
        checkDigits = Integer.parseInt(i_check_Digits);

        // Check for numeric value
        if ((checkDigits >= 0) && (checkDigits <= 99)) {

            // Check value is between range
            if ((checkDigits >= 60) && (checkDigits <= 96)) {

                CheckDigitPos = checkDigits - 60;

                Result = CharString.charAt(CheckDigitPos);
            } else {
                Result = ' ';
            }
        } else {
            Result = ' ';
        }

        return Result;
    }


    public boolean isDonationNumberValid() {
        return isDonationNumberValid;
    }

    private void setDonationNumberValid(boolean donationNumberValid) {
        isDonationNumberValid = donationNumberValid;
    }

    static String padDonationNumber(String donationID) {
        try {
            return donationID.substring(0, 4) + " " + donationID.substring(4, 7) + " " + donationID.substring(7, 10) + " " + donationID.substring(10, 13) + " " + donationID.substring(13, 14);

        } catch (Exception ex) { // just in case something stupid is passed in
            return "";
        }
    }
}
