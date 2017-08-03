package uk.co.savant.barry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  Neilb
 * <p>
 * <p>
 * Details:      :-  Created by Neilb on 24/01/2017.
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

public class BarryDatabase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "barry.db";
    private static final int DATABASE_VERSION = 5;


    public BarryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // For testing...
        ProductBarcode.getAlProductBarcode().add(new ProductBarcode.ProductBarcodeRecord("LY23", "54262", "", "ERROR001", "A"));
        ProductBarcode.getAlProductBarcode().add(new ProductBarcode.ProductBarcodeRecord("LF15", "18300", "", "ERROR001", "A"));
//        Product.getAlProduct().add(new Product.ProductRecord("LY23", "Y", "FROZEN RED CELLS", "R", 3652));
        addProduct("LY23", "Y", "FROZEN RED CELLS", "R", 3652);
        addProduct("LF15", "Y", "LEUCODEPLETED FFP", "P", 3652);
//        ProductType.getAlProductType().add(new ProductType.ProductTypeRecord("BF", "Red Cells Frozen Products"));


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDatabaseTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {


        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PRODUCT");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SCANNEDPRODUCTS");

        onCreate(sqLiteDatabase);
    }


    private void createDatabaseTables(SQLiteDatabase sqLiteDatabase) {
        CreateTableProduct(sqLiteDatabase);
        CreateTableScannedProducts(sqLiteDatabase);

    }

    private void CreateTableScannedProducts(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SCANNEDPRODUCTS (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " DONATIONNUMBER TEXT, " +
                " PRODUCTCODE TEXT, " +
                " PRODUCTDESCRIPTION TEXT, " +
                " PRODUCTTYPE TEXT, " +
                " EXPIRYDATE TEXT, " +
                " DRAWDATE TEXT, " +
                " PRODUCTTYPEDESCRIPTION TEXT, " +
                " ISCOMMITED TEXT, " +
                " DATERECORDED TEXT ) ");
    }


    private void CreateTableProduct(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " PRDCD TEXT, " +
                " INUSE TEXT, " +
                " PRDSL TEXT, " +
                " PRDTY TEXT, " +
                " LIFE INTEGER) ");

    }

    private void addProduct(String prdcd, String inuse, String prdsl, String prdty, int life) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("PRDCD", prdcd);
        values.put("INUSE", inuse);
        values.put("PRDSL", prdsl);
        values.put("PRDTY", prdty);
        values.put("LIFE", life);
        db.insert("PRODUCT", null, values);

    }

    public void addScannedProduct(String strDntno, String strProductCode, String strProductDescription, String strProductType, String strExpiryDate, String strDrawdate, String strProducttypeDescription) {

        String strToday = new SimpleDateFormat("yyyyMMdd", Locale.UK).format(new Date());


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("DONATIONNUMBER", strDntno);
        values.put("PRODUCTCODE", strProductCode);
        values.put("PRODUCTDESCRIPTION", strProductDescription);
        values.put("PRODUCTTYPE", strProductType);
        values.put("EXPIRYDATE", strExpiryDate);
        values.put("DRAWDATE", strDrawdate);
        values.put("PRODUCTTYPEDESCRIPTION", strProducttypeDescription);
        values.put("ISCOMMITED", "F");

        values.put("DATERECORDED", strToday);

        db.insert("SCANNEDPRODUCTS", null, values);

    }

    public boolean isAlreadyStored(String strDonationNumber) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try (Cursor cursor = db.query("SCANNEDPRODUCTS", new String[]{"DONATIONNUMBER"}, "DONATIONNUMBER=?",
                new String[]{strDonationNumber}, null, null, null, null)) {

            if (cursor.moveToFirst()) {
                result = true;
            }
        }

        return result;
    }

    public ArrayList<ScannedProducts.ScannedProductRecord> readScannedProducts() {
        ArrayList<ScannedProducts.ScannedProductRecord> al = new ArrayList<>();

        String selectQuery = "SELECT * FROM SCANNEDPRODUCTS ";
        SQLiteDatabase db = this.getWritableDatabase();


        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {

                do {
                    String strDntno = cursor.getString(1);
                    String strProductCode = cursor.getString(2);
                    String strProductDescription = cursor.getString(3);
                    String strProductType = cursor.getString(4);
                    String strExpiryDate = cursor.getString(5);
                    String strDrawdate = cursor.getString(6);
                    String strProducttypeDescription = cursor.getString(7);
                    boolean isCommitted = cursor.getString(8).equals("T");

                    al.add(new ScannedProducts.ScannedProductRecord(
                            strDntno, strProductCode, strProductDescription,
                            strProductType, strExpiryDate, strDrawdate, strProducttypeDescription, isCommitted));

                } while (cursor.moveToNext());

            }
        }

        return al;
    }

    public ScannedProducts.ScannedProductRecord readScannedProductrecord(int id) {

        ScannedProducts.ScannedProductRecord scannedProductRecord = null;

        if (id > 0) {

            String selectQuery = "SELECT * FROM SCANNEDPRODUCTS WHERE id = ?";
            SQLiteDatabase db = this.getWritableDatabase();


            try (Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)})) {
                if (cursor.moveToFirst()) {
                    String strDntno = cursor.getString(1);
                    String strProductCode = cursor.getString(2);
                    String strProductDescription = cursor.getString(3);
                    String strProductType = cursor.getString(4);
                    String strExpiryDate = cursor.getString(5);
                    String strDrawdate = cursor.getString(6);
                    String strProducttypeDescription = cursor.getString(7);
                    boolean isCommitted = cursor.getString(8).equals("T");

                    scannedProductRecord = new ScannedProducts.ScannedProductRecord(strDntno, strProductCode, strProductDescription, strProductType, strExpiryDate, strDrawdate, strProducttypeDescription, isCommitted);

                }
            }
        }
        return scannedProductRecord;
    }

    public int countScannedProducts() {

        String selectQuery = "SELECT COUNT(*) FROM SCANNEDPRODUCTS";
        SQLiteDatabase db = this.getWritableDatabase();

        SQLiteStatement sqLiteStatement = db.compileStatement(selectQuery);

        return (int) sqLiteStatement.simpleQueryForLong();
    }

    public void updateRecordToCommitted(int recordNo) {
        String updateSql = "UPDATE SCANNEDPRODUCTS SET ISCOMMITED = 'T' WHERE id = ?";
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {

            db.execSQL(updateSql, new String[]{String.valueOf(recordNo)});
            db.setTransactionSuccessful(); // This is a 'commit', apparently

        } catch (SQLException e) {
            // Not committed
        } finally {
            db.endTransaction();
        }

    }

    public void updateRecordToUnCommitted(int recordNo) {

        String updateSql = "UPDATE SCANNEDPRODUCTS SET ISCOMMITED = 'F' WHERE id = ?";
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {

            db.execSQL(updateSql, new String[]{String.valueOf(recordNo)});
            db.setTransactionSuccessful(); // This is a 'commit', apparently

        } catch (SQLException e) {
            // Not committed
        } finally {
            db.endTransaction();
        }
    }

    public void deleteBeforeDate(int iPastDays) {
//        String strToday = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -iPastDays);
        Date pastDate = cal.getTime();

        String strPastDate = new SimpleDateFormat("yyyyMMdd", Locale.UK).format(pastDate);

        String deleteSql = "DELETE FROM SCANNEDPRODUCTS WHERE DATERECORDED < ?";
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {

            db.execSQL(deleteSql, new String[]{String.valueOf(strPastDate)});
            db.setTransactionSuccessful(); // This is a 'commit', apparently

        } catch (SQLException e) {
            // Not committed
        } finally {
            db.endTransaction();
        }
    }

    public void reset() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PRODUCT");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SCANNEDPRODUCTS");

        onCreate(sqLiteDatabase);

    }

    public Product.ProductRecord readProduct(String strPrdcd) {
        Product.ProductRecord productRecord = null;

        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.query("PRODUCT", new String[]{"PRDCD", "INUSE", "PRDSL", "PRDTY", "LIFE"}, "PRDCD=?",
                new String[]{strPrdcd}, null, null, null, null)) {


            if (cursor.moveToFirst()) {

                String prdcd = cursor.getString(0);
                String strInuse = cursor.getString(1);
                String strPrdsl = cursor.getString(2);
                String strPrdty = cursor.getString(3);
                int life = cursor.getInt(4);

                productRecord = new Product.ProductRecord(strPrdcd, strInuse, strPrdsl, strPrdty, life);
            }
        }

        return productRecord;
    }

    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();

        int deletedCount = db.delete("PRODUCT", "", new String[]{});
    }


    public void bulkInsertProducts(ArrayList<Product.ProductRecord> alProducts) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {

            for (int i = 0; i < alProducts.size() - 1; i++) {

                alProducts.get(i).getPrdcd();

                String prdcd = alProducts.get(i).getPrdcd();
                String inuse = alProducts.get(i).getInuse();
                String prdsl = alProducts.get(i).getPrdsl();
                String prdty = alProducts.get(i).getPrdty();
                int life     = alProducts.get(i).getLife();

                ContentValues values = new ContentValues();

                values.put("PRDCD", prdcd);
                values.put("INUSE", inuse);
                values.put("PRDSL", prdsl);
                values.put("PRDTY", prdty);
                values.put("LIFE",  life);

                db.insert("PRODUCT", null, values);
            }

            db.setTransactionSuccessful(); // This is a 'commit', apparently

        } catch (SQLException e) {
            // Not committed
        } finally {
            db.endTransaction();
        }
    }
}
