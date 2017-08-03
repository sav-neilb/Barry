package uk.co.savant.barry.database;

import java.util.ArrayList;

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
 *    Details:      :-  Created by Neilb on 23/01/2017.
 *
 *
 *
 *
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


public class ScannedProducts {

    private static ArrayList<ScannedProductRecord> alScannedProducts = new ArrayList<>();


    public static class ScannedProductRecord extends ScannedProducts {

        private String donationNumber;
        private String productCode;
        private String productDescription;
        private String productType;
        private String expiryDate;
        private String drawdate;
        private String productTypeDescription;
        private boolean isCommited;



        public ScannedProductRecord(String donationNumber, String productCode, String productDescription, String productType, String expiryDate, String drawdate, String productTypeDescription, boolean isCommited) {
            this.donationNumber = donationNumber;
            this.productCode = productCode;
            this.productDescription = productDescription;
            this.productType = productType;
            this.expiryDate = expiryDate;
            this.drawdate = drawdate;
            this.productTypeDescription = productTypeDescription;
            this.isCommited = isCommited;
        }

        public String getDonationNumber() {
            return donationNumber;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public String getProductType() {
            return productType;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public String getDrawdate() {
            return drawdate;
        }

        public String getProductTypeDescription() {
            return productTypeDescription;
        }

        public boolean isCommited() {
            return isCommited;
        }

        public void setCommited(boolean commited) {
            isCommited = commited;
        }
    }

    public static ArrayList<ScannedProductRecord> getAlScannedProducts() {
        return alScannedProducts;
    }


}
