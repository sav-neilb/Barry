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
 *    Details:      :-  Created by Neilb on 24/01/2017.
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

public class Product {
    private static ArrayList<ProductRecord> alProduct = new ArrayList<>();


    public static class ProductRecord extends Product {
        private String prdcd;
        private String inuse;
        private String prdsl;
        private String prdty;
        private int life;

        public ProductRecord(String prdcd, String inuse, String prdsl, String prdty, int life) {
            this.prdcd = prdcd;
            this.inuse = inuse;
            this.prdsl = prdsl;
            this.prdty = prdty;
            this.life = life;
        }

        public String getPrdcd() {
            return prdcd;
        }

        String getInuse() {
            return inuse;
        }

        public String getPrdsl() {
            return prdsl;
        }

        public String getPrdty() {
            return prdty;
        }

        public int getLife() {
            return life;
        }
    }

    public static ArrayList<ProductRecord> getAlProduct() {
        return alProduct;
    }
}
