package uk.co.savant.barry;

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


class ScannedProducts {

    private static ArrayList<ProductRecord> alScannedProducts = new ArrayList<>();


    private static class ProductRecord extends ScannedProducts {

        private String donationNumber;
        private String productCode;
        private String productDescription;
        private String productType;
        private String expiryDate;
        private String drawdate;
        private String productTypeDescription;
        private boolean isCommited;



        ProductRecord(String donationNumber, String productCode, String productDescription, String productType, String expiryDate, String drawdate, String productTypeDescription) {
            this.donationNumber = donationNumber;
            this.productCode = productCode;
            this.productDescription = productDescription;
            this.productType = productType;
            this.expiryDate = expiryDate;
            this.drawdate = drawdate;
            this.productTypeDescription = productTypeDescription;
            this.isCommited = false;
        }

        String getDonationNumber() {
            return donationNumber;
        }

        String getProductCode() {
            return productCode;
        }

        String getProductDescription() {
            return productDescription;
        }

        String getProductType() {
            return productType;
        }

        String getExpiryDate() {
            return expiryDate;
        }

        String getDrawdate() {
            return drawdate;
        }

        String getProductTypeDescription() {
            return productTypeDescription;
        }

        public boolean isCommited() {
            return isCommited;
        }

        public void setCommited(boolean commited) {
            isCommited = commited;
        }
    }

    static ArrayList<ProductRecord> getAlScannedProducts() {
        return alScannedProducts;
    }


}
