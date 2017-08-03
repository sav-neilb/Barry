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
 *    Details:      :-  Created by Neilb on 26/01/2017.
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

public class ProductBarcode {

    private static ArrayList<ProductBarcodeRecord> alProductBarcode = new ArrayList<>();


    public static class ProductBarcodeRecord extends ProductBarcode {
        private String prdcd;
        private String prbcd;
        private String ntnlbrcd;
        private String c128brcd;
        private String brcdfrmt;

        ProductBarcodeRecord(String prdcd, String prbcd, String ntnlbrcd, String c128brcd, String brcdfrmt) {
            this.prdcd = prdcd;
            this.prbcd = prbcd;
            this.ntnlbrcd = ntnlbrcd;
            this.c128brcd = c128brcd;
            this.brcdfrmt = brcdfrmt;
        }

        public String getPrdcd() {
            return prdcd;
        }

        public String getPrbcd() {
            return prbcd;
        }

        public String getNtnlbrcd() {
            return ntnlbrcd;
        }

        public String getC128brcd() {
            return c128brcd;
        }

        public String getBrcdfrmt() {
            return brcdfrmt;
        }
    }

    public static ArrayList<ProductBarcodeRecord> getAlProductBarcode() {
        return alProductBarcode;
    }
}
