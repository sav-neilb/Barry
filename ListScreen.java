package uk.co.savant.barry;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.savant.barry.database.BarryDatabase;
import uk.co.savant.barry.database.ScannedProducts;


/**
 * Barry –
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

class ListScreen {

    private View view;
    private boolean bShowArchived = false;
    private BarryDatabase barryDatabase = null;

    ListScreen(View view) {
        setView(view);
    }


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    void populateListView(ArrayAdapter<String> adapter) {
        ArrayList<ScannedProducts.ScannedProductRecord> alScannedProducts = new ArrayList<>();
        String strScancount =  Integer.toString(barryDatabase.countScannedProducts());

        adapter.clear();

        TextView textView = (TextView) view.findViewById(R.id.textViewScanCountListPage);
        textView.setText(strScancount);

        ListView listView = (ListView) view.findViewById(R.id.ListViewProducts);
        listView.setAdapter(adapter);

        String listElement = " " + "Donation ID" + "\t\t\t\t\t\t\t\t" + "Type" + "\t\t" + "Product" + "\t" + "Draw Date";
        adapter.add(listElement);

        alScannedProducts.clear();
        alScannedProducts = barryDatabase.readScannedProducts();


        if ((null != alScannedProducts) && (!alScannedProducts.isEmpty())) {

            for (int i = 0; i < alScannedProducts.size(); i++) {

                ScannedProducts.ScannedProductRecord scannedProductRecord = alScannedProducts.get(i);

                if (!scannedProductRecord.isCommited() || isbShowArchived()) {

                    String donationID = scannedProductRecord.getDonationNumber();
                    String strPaddedDntno = DonationNumberValidation.padDonationNumber(donationID);

                    String productType = scannedProductRecord.getProductType();
                    String productCode = scannedProductRecord.getProductCode();
                    String drawDate = scannedProductRecord.getDrawdate();

                    listElement = " " + strPaddedDntno + "\t\t" + productType + "\t\t\t" + productCode + "\t\t\t\t" + drawDate;

                    adapter.add(listElement);
                }

            }
        }
    }

    boolean isbShowArchived() {
        return bShowArchived;
    }

    void setbShowArchived(boolean bShowArchived) {
        this.bShowArchived = bShowArchived;
    }

    void setBarryDatabase(BarryDatabase barryDatabase) {
        this.barryDatabase = barryDatabase;
    }
}
