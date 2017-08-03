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

public class Prodtype {
    private static ArrayList<ProdtypeRecord> alProdtype = new ArrayList<>();




    static class ProdtypeRecord extends Prodtype {
        private String prdty;
        private String ptypdesc;

        ProdtypeRecord(String prdty, String ptypdesc) {
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

    public static String getDescriptionFromCode(String strProductType) {
        for (int i = 0; i < alProdtype.size(); i++) {
            ProdtypeRecord prodtypeRecord = alProdtype.get(i);

            if (prodtypeRecord.getPrdty().equals(strProductType)){
                return prodtypeRecord.getPtypdesc();
            }

        }
        return "";
    }

    static ArrayList<ProdtypeRecord> getAlProdtype() {
        return alProdtype;
    }
}
