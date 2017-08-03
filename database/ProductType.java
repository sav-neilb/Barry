package uk.co.savant.barry.database;

import java.util.ArrayList;

/**
 * Barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  Neilb
 * <p>
 * <p>
 * Details:      :-  Created by Neilb on 27/01/2017.
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

public class ProductType {
    private static ArrayList<ProductTypeRecord> alProductType = new ArrayList<>();




    static class ProductTypeRecord extends ProductType {
        private String prdty;
        private String ptypdesc;

        ProductTypeRecord(String prdty, String ptypdesc) {
            this.prdty = prdty;
            this.ptypdesc = ptypdesc;
        }

        String getPrdty() {
            return prdty;
        }

        String getPtypdesc() {
            return ptypdesc;
        }

    }

//    public static String getDescriptionFromCode(String strProductType) {
//        for (int i = 0; i < alProductType.size(); i++) {
//            ProductTypeRecord prodtypeRecord = alProductType.get(i);
//
//            if (prodtypeRecord.getPrdty().equals(strProductType)){
//                return prodtypeRecord.getPtypdesc();
//            }
//
//        }
//        return "";
//    }

    static ArrayList<ProductTypeRecord> getAlProductType() {
        return alProductType;
    }
}
