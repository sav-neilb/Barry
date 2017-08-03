package uk.co.savant.barry;

import uk.co.savant.barry.database.BarryDatabase;
import uk.co.savant.barry.database.ProductBarcode;
import uk.co.savant.barry.database.Product;

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

class ProductCodeValidation {
    BarryDatabase barryDatabase = null;
    private boolean isProductCodeValid = false;


    ProductCodeValidation() {

    }

    public void setBarryDatabase(BarryDatabase barryDatabase) {
        this.barryDatabase = barryDatabase;
    }

    Product.ProductRecord validateProductCode(String strProductBarCode) {
        Product.ProductRecord result = null;

        setProductCodeValid(false);

        if (strProductBarCode.length() == 7) { // Barcode (PRBCD)
            strProductBarCode = strProductBarCode.substring(1, 6);
        }

        if (strProductBarCode.length() == 5) {
            // Look up in PRDBCD table
            for (int i = 0; i < ProductBarcode.getAlProductBarcode().size(); i++) {

                ProductBarcode.ProductBarcodeRecord prdbcdRecord = ProductBarcode.getAlProductBarcode().get(i);

                if (strProductBarCode.equals(prdbcdRecord.getPrbcd())) {

                    String strPrdcd = prdbcdRecord.getPrdcd();
                    result = barryDatabase.readProduct((strPrdcd));

                    break;

                }
            }

        } else if (strProductBarCode.length() == 4) { // Product code (PRDCD)

            result = barryDatabase.readProduct((strProductBarCode));

        }

        return result;
    }


    public boolean isProductCodeValid() {
        return isProductCodeValid;
    }

    private void setProductCodeValid(boolean productCodeValid) {
        isProductCodeValid = productCodeValid;
    }
}
