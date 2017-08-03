package uk.co.savant.barry;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentResult;

import uk.co.savant.barry.database.BarryDatabase;
import uk.co.savant.barry.database.Product;


/**
 * barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  Neilb
 * <p>
 * <p>
 * Details:      :-  Created by Neilb on 25/01/2017.
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

class ScanningScreen {
    private View view;
    private int scanCount = 0;
    private int itemCount = 0;
    private int iProductLife = 0;
    private String message = "";
    private BarryDatabase barryDatabase = null;

    ScanningScreen(View view) {
        this.setView(view);
    }


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }


    private void storeData() {

        // Store the data here

        TextView textView = (TextView) view.findViewById(R.id.textViewDntno);
        String strDntno = textView.getText().toString().replaceAll("\\s",""); // remove embedded spaces from the displayed string

        textView = (TextView) view.findViewById(R.id.textViewProductCode);
        String strProductCode = textView.getText().toString();

        textView = (TextView) view.findViewById(R.id.textViewProductName);
        String strProductDescription = textView.getText().toString();

        textView = (TextView) view.findViewById(R.id.textViewProductType);
        String strProductType = textView.getText().toString();

        textView = (TextView) view.findViewById(R.id.textViewExpiryDate);
        String strExpiryDate = textView.getText().toString();

        textView = (TextView) view.findViewById(R.id.textViewDrawDate);
        String strDrawdate = textView.getText().toString();

        String strProducttypeDescription = "";


        barryDatabase.addScannedProduct(strDntno, strProductCode, strProductDescription,
                strProductType, strExpiryDate, strDrawdate, strProducttypeDescription);

    }


    void confirmData() {

        storeData();

        setItemCount(getItemCount() + 1);

        // now reset all the buttons
        resetScreen();

    }

    void resetScreen() {
        Button btn = (Button) view.findViewById(R.id.btnScan);
        // now reset all the buttons
        btn.setText("Scan");
        btn.setBackgroundResource(R.drawable.button_shape_rounded_grey);
//        btn.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_grey));

        TextView textView = (TextView) view.findViewById(R.id.textViewScanDntno);
        textView.setBackgroundResource(R.drawable.button_shape_rounded_red);
//        textView.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_red_dark));
        textView.setText("Scan");

        textView = (TextView) view.findViewById(R.id.textViewScanProduct);
        textView.setBackgroundResource(R.drawable.button_shape_rounded_red);
//        textView.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_red_dark));
        textView.setText("Scan");

        textView = (TextView) view.findViewById(R.id.textViewScanDate);
        textView.setBackgroundResource(R.drawable.button_shape_rounded_red);

//        textView.setBackground(view.getResources().  getBackground(R.drawable.button_shape_rounded_red));

                //        );Color(view.getResources().getColor(R.color.colorHolo_red_dark));
        textView.setText("Scan");

        TextView txt = (TextView) view.findViewById(R.id.textViewDntno);
        txt.setText("");
        txt = (TextView) view.findViewById(R.id.textViewProductName);
        txt.setText("");
        txt = (TextView) view.findViewById(R.id.textViewProductCode);
        txt.setText("");
        txt = (TextView) view.findViewById(R.id.textViewProductType);
        txt.setText("");
        txt = (TextView) view.findViewById(R.id.textViewExpiryDate);
        txt.setText("");
        txt = (TextView) view.findViewById(R.id.textViewDrawDate);
        txt.setText("");

        int scanCount = barryDatabase.countScannedProducts();
        txt = (TextView) view.findViewById(R.id.textViewScanCount);
        txt.setText(Integer.toString(scanCount));


        setScanCount(0);
    }


    boolean ProcessScan(IntentResult scannedData) {
        boolean result = false;

        // What are we Scanning?
        switch (getScanCount()) {

            case 0:

                TextView textViewDntno = (TextView) view.findViewById(R.id.textViewDntno);
                String strDonationNumber;
                try {
                    strDonationNumber = scannedData.getContents();
                    if (strDonationNumber.length() == 16) {
                        strDonationNumber = ValidateDonationNumber(strDonationNumber);

                        if (!strDonationNumber.isEmpty()) {

                            if (!isAlreadyStored(strDonationNumber)) {

                                TextView textView = (TextView) view.findViewById(R.id.textViewScanDntno);
                                textView.setBackgroundResource(R.drawable.button_shape_rounded_green);
//                                textView.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_green_dark));
                                textView.setText("OK");

                                setScanCount(getScanCount() + 1);
                                setMessage("");
                                result = true;

                            } else {

                                setMessage("Donation number already scanned");
                                result = false;
                            }
                        } else {

                            setMessage("Invalid donation number ");
                            result = false;
                        }
                    } else {

                        setMessage("Invalid donation number ");
                        result = false;
                    }

                    if (!strDonationNumber.isEmpty()) {
                        String strPaddedDntno = DonationNumberValidation.padDonationNumber(strDonationNumber);

                        textViewDntno.setText(strPaddedDntno);

                    } else {
                        textViewDntno.setText("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case 1:

                TextView textViewProductCode = (TextView) view.findViewById(R.id.textViewProductCode);
                TextView textViewProductName = (TextView) view.findViewById(R.id.textViewProductName);
                TextView textViewProductType = (TextView) view.findViewById(R.id.textViewProductType);

                String strProductBarCode = scannedData.getContents();
                String strProductCode = "";
                String strProductName = "";
                String strProductType = "";

                if (strProductBarCode.length() == 7) {

                    Product.ProductRecord prodctRecord = validateProductCode(strProductBarCode);

                    if (null != prodctRecord) {

                        strProductCode = prodctRecord.getPrdcd();
                        strProductName = prodctRecord.getPrdsl();
                        strProductType = prodctRecord.getPrdty();

                        setProductLife(prodctRecord.getLife());

                        TextView textView = (TextView) view.findViewById(R.id.textViewScanProduct);
                        textView.setBackgroundResource(R.drawable.button_shape_rounded_green);

                        textView.setText("OK");

                        setScanCount(getScanCount() + 1);

                        setMessage("");
                        result = true;
                    } else {

                        setMessage("Invalid product code");
                        result = false;
                    }

                } else {

                    setMessage("Invalid product code");
                    result = false;
                }

                textViewProductName.setText(strProductName);
                textViewProductType.setText(strProductType);
                textViewProductCode.setText(strProductCode);

                break;

            case 2:

                TextView textViewExpiryDate = (TextView) view.findViewById(R.id.textViewExpiryDate);
                TextView textViewDrawDate = (TextView) view.findViewById(R.id.textViewDrawDate);
                String strExpiryDate = scannedData.getContents();
                String strDrawDate;
                String strFormattedExpiryDate = "";
                String strFormattedDrawDate = "";

                // &>019023
                if ((strExpiryDate.length() == 8) || (strExpiryDate.length() == 10)) {
                    strExpiryDate = validateExpiryDate(strExpiryDate);

                    if (!strExpiryDate.isEmpty()) {

                        DateCalculation dateCalculation = new DateCalculation();
                        strDrawDate = dateCalculation.subtractDaysFromDate(strExpiryDate, getiProductLife());

                        strFormattedDrawDate = formatDateMMDDYYWithSlashes(strDrawDate);
                        strFormattedExpiryDate = formatDateMMDDYYWithSlashes(strExpiryDate);

                        TextView textView = (TextView) view.findViewById(R.id.textViewScanDate);
                        textView.setBackgroundResource(R.drawable.button_shape_rounded_green);
//                        textView.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_green_dark));
                        textView.setText("OK");

                        Button button = (Button) view.findViewById(R.id.btnScan);
                        button.setText("Confirm");
                        button.setBackgroundResource(R.drawable.button_shape_rounded_green);
//                        button.setBackgroundColor(view.getResources().getColor(R.color.colorHolo_green_dark));

                        setScanCount(0);

                        setMessage("");
                        result = true;
                    } else {

                        setMessage("Invalid date");
                        result = false;
                    }

                } else {

                    setMessage("Invalid date");
                    result = false;
                }

                textViewDrawDate.setText(strFormattedDrawDate);
                textViewExpiryDate.setText(strFormattedExpiryDate);

                break;

            case 3:
                result = false;
                break;
        }


        return result;
    }

    private boolean isAlreadyStored(String strDonationNumber) {

        return (barryDatabase.isAlreadyStored(strDonationNumber));
    }

    private String validateExpiryDate(String strExpiryDate) {
        ExpiryDateValidation expiryDateValidation = new ExpiryDateValidation();

        return expiryDateValidation.validateExpiryDate(strExpiryDate);

    }

    private Product.ProductRecord validateProductCode(String strProductCode) {
        ProductCodeValidation productCodeValidation = new ProductCodeValidation();

        productCodeValidation.setBarryDatabase(barryDatabase);

        return productCodeValidation.validateProductCode(strProductCode);

    }


    private String ValidateDonationNumber(String contents) throws Exception {
        DonationNumberValidation donationNumberValidation = new DonationNumberValidation();

        return donationNumberValidation.validateDonationNumber(contents);

    }

    private int getScanCount() {
        return scanCount;
    }

    private void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    private void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    private int getItemCount() {
        return itemCount;
    }


    private String formatDateMMDDYYWithSlashes(String strDate) {

        return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(0, 4);
    }

    private int getiProductLife() {
        return iProductLife;
    }

    private void setProductLife(int iProductLife) {
        this.iProductLife = iProductLife;
    }

    String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    void setBarryDatabase(BarryDatabase barryDatabase) {
        this.barryDatabase = barryDatabase;
    }

}
