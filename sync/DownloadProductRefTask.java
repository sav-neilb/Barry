package uk.co.savant.barry.sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import jcifs.smb.SmbFile;
import uk.co.savant.barry.LogHelper;
import uk.co.savant.barry.PrefUtils;
import uk.co.savant.barry.database.BarryDatabase;
import uk.co.savant.barry.database.Product;


/**
 * Barry –
 * *************************************************
 * <p>
 * Copyright     :-  National Blood Authority, 2017
 * <p>
 * Author        :-  georget
 * <p>
 * <p>
 * Details:      :-  Created by georget on 26/01/2017.
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

public class DownloadProductRefTask extends AsyncTask<String, Integer, String> {
    private Context mContext;

    private boolean fDownloadingFile;
    private boolean fDBUpdate;

    private static final String TAG = LogHelper.makeLogTag("DownloadProductRefTask");

    public DownloadProductRefTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
//        Toast.makeText(Main.getApp(),"Invoke on PreExecute()", Toast.LENGTH_SHORT).show();
//        progress_status = 0 ;
//        MainActivity.getApp().txt_percentage.setText("downloading 0%");

        broadcastIntent("onPreExecute", 0);
    }

    @Override
    protected String doInBackground(String... urls) {
        int count;
        fDownloadingFile = false;
        fDBUpdate = false;

        try {
            LogHelper.LOGI(TAG, "Downloading - " + PrefUtils.getServerFilename(mContext));

            fDownloadingFile = true;

            InputStream fileInputStream;
            int len;
            long lengthOfFile;
            FileOutputStream fileOutputStream;
            String localFileName;

            if (!PrefUtils.getServerAddress(mContext).isEmpty()) {

                jcifs.Config.setProperty("resolveOrder", "DNS");
                jcifs.Config.setProperty("jcifs.smb.client.disablePlainTextPasswords", "false");

                // smb://ntsavant;georget:password@192.168.0.10/network/share/folder/path/filename.xls

                String remoteFilePath = buildFilenameWithAuth(
                        PrefUtils.getServerAddress(mContext),
                        PrefUtils.getServerDomain(mContext),
                        PrefUtils.getServerUsername(mContext),
                        PrefUtils.getServerPassword(mContext),
                        PrefUtils.getServerFolderpath(mContext),
                        PrefUtils.getServerFilename(mContext));

                localFileName = getLocalFileName(remoteFilePath);

                SmbFile remoteFile = new SmbFile(remoteFilePath);


                fileOutputStream = new FileOutputStream(localFileName);
                fileInputStream = remoteFile.getInputStream();
                // getting file length
                lengthOfFile = remoteFile.length();

            } else {


                String remoteFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                localFileName = getLocalFileName(remoteFilePath + "/" + PrefUtils.getServerFilename(mContext));
                File remoteFile = new File(remoteFilePath + "/" + PrefUtils.getServerFilename(mContext));
                fileOutputStream = new FileOutputStream(localFileName);
                fileInputStream = new FileInputStream(remoteFile);
                lengthOfFile = remoteFile.length();
            }


            try {
                //byte[] buf = new byte[16 * 1024 * 1024];
                byte[] buf = new byte[1024];

                long total = 0;

                while ((len = fileInputStream.read(buf)) > 0) {
                    fileOutputStream.write(buf, 0, len);
                    total += len;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress((int) ((total * 100) / lengthOfFile));


                }

            } finally {
                fileInputStream.close();
                fileOutputStream.close();
            }

            // excel file has been copied locally.
            // Read contents and insert into database.

            populateLocalDbFromSpreadsheet(localFileName);

            return fileInputStream.toString();

        } catch (Exception e) {
            e.printStackTrace();

            return e.getMessage(); // ???
        }

    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        String message = "";
        if (fDownloadingFile) {
            message = "onFileDownload";
        } else if (fDBUpdate) {
            message = "onDatabaseUpdate";
        }

        broadcastIntent(message, values[0]);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Toast.makeText(MainActivity.getApp(), "Invoke onPostExecute()", Toast.LENGTH_SHORT).show();
        //MainActivity.getApp().txt_percentage.setText("download complete");
        broadcastIntent("onPostExecute", 0);
    }

    private String buildFilenameWithAuth(String host, String domain, String username, String password, String serverPath, String rFilename) {

        // Example format of the SMB path.
        // Note : If the path requires a domain then this must prefix the username and be terminated with a semi colon.
        //        For example "ntsavant;username"
        //
        // smb://ntsavant;martins:password@192.168.0.10/network/share/folder/path/filename.xls

        String fullFilePath = "smb://";
        boolean up = false;

        if ((!(username == null)) && (!username.isEmpty())) {

            if ((!(domain == null)) && (!domain.isEmpty())) {
                fullFilePath = fullFilePath + domain + ";";
            }

            fullFilePath = fullFilePath + username;
            up = true;
        }

        if ((!(password == null)) && (!password.isEmpty())) {
            fullFilePath = fullFilePath + ":" + password;
        }

        if (up) {
            fullFilePath = fullFilePath + "@";
        }

        if (!serverPath.substring(serverPath.length() - 1).equals("/") && !serverPath.substring(serverPath.length() - 1).equals("\\")) {
            serverPath = serverPath.concat("/");
        }
        fullFilePath = fullFilePath + host + serverPath + rFilename;

        return fullFilePath;
    }

    @NonNull
    private String getLocalFileName(String remoteFilePath) {
        File f = new File(remoteFilePath);
        String lfilename = f.getName();

        return mContext.getFilesDir() + "/" + lfilename;
    }

    // broadcast a custom intent.
    public void broadcastIntent(String message, Integer progress) {
        Intent intent = new Intent();
        intent.setAction("com.example.georget.quotient.CUSTOM_MESSAGE");
        Bundle aSyncBundle = new Bundle();
        aSyncBundle.putString("message_to_broadcast", message);
        aSyncBundle.putInt("progress_to_broadcast", progress);
        intent.putExtras(aSyncBundle);
        mContext.sendBroadcast(intent);
    }

    private void populateLocalDbFromSpreadsheet(String filename) {
        Log.d(TAG, "Beginning Database update");
        ArrayList<Product.ProductRecord> productRefs = new ArrayList<>();

        try {
            BarryDatabase barryDatabase = new BarryDatabase(mContext);

            fDownloadingFile = false;
            fDBUpdate = true;
            // the progress of the updtaing database cannot include the actual update as it is done in 1 block
            // that cannot be interigated so we just need to call publishProgress once to reset the progress bar to indeterminate
            // set indeterminate value to arbitary 95% complete
            publishProgress(95);


            File file = new File(filename);
            //FileInputStream myInput = new FileInputStream(file);
            FileInputStream myInput = new FileInputStream(file);

            // Create a workbook using the File System
            // HSSF for prior 2007 edition of Excel workbooks
            // XSSF for 2007 + editions jar files are not easy to setup and are no longer supported so have not used them....

            HSSFSheet mySheet;
            Workbook myWorkBook;
            Iterator rowIter;

            if (FilenameUtils.getExtension(filename).equalsIgnoreCase("xls")) {

                // Create a POIFSFileSystem object
                POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

                myWorkBook = new HSSFWorkbook(myFileSystem); // ??? could the stream myInput
                // Get the first sheet from workbook
                mySheet = (HSSFSheet) myWorkBook.getSheetAt(Integer.parseInt(PrefUtils.getWorkbookIndex(mContext)));
                // We now need something to iterate through the cells.
                rowIter = mySheet.rowIterator();

            } else if (FilenameUtils.getExtension(filename).equalsIgnoreCase("xlsx")) {

                throw new Exception("Excel 2007+ is not supported. Please save data file as Excel .xls (97-2003) format and try again");


            } else {
                throw new IllegalArgumentException("Received file does not have a standard excel extension.");
            }

            int counter = 0;
            double MaxRows = mySheet.getPhysicalNumberOfRows();

            ArrayList<Product.ProductRecord> alProducts = new ArrayList<>();

            while (rowIter.hasNext()) {
                //HSSFRow myRow = (HSSFRow) rowIter.next();
                Row myRow = (Row) rowIter.next();
                Iterator cellIter = myRow.cellIterator();

                String code = null;             // Column A
                String lDesc = null;            // Column B
                String codeVer = null;          // Column C
                String type = null;             // Column D
                String inuse = null;            // Column E
                String sDesc = null;            // Column F
                String lastAmended = null;

                int iLife = 0;

                while (cellIter.hasNext()) {
                    //HSSFCell myCell = (HSSFCell) cellIter.next();
                    Cell myCell = (Cell) cellIter.next();

                    if (myCell.getColumnIndex() == getColumnIndexFromCode("A")) {
                        code = myCell.toString();
                    } else if (myCell.getColumnIndex() == getColumnIndexFromCode("B")) {
                        lDesc = myCell.toString();
                    } else if (myCell.getColumnIndex() == getColumnIndexFromCode("C")) {
                        codeVer = myCell.toString();
                    } else if (myCell.getColumnIndex() == getColumnIndexFromCode("D")) {
                        type = myCell.toString();
                    } else if (myCell.getColumnIndex() == getColumnIndexFromCode("E")) {
                        inuse = myCell.toString();
                    } else if (myCell.getColumnIndex() == getColumnIndexFromCode("F")) {
                        sDesc = myCell.toString();
                    } else if (myCell.getColumnIndex() == getColumnIndexFromCode("G")) {
                        iLife = Integer.parseInt(myCell.toString());
                    }
                }

                // Ensure the primary key....
                if ((code != null) && (code.length() == 5) &&
                        (lDesc != null) && (codeVer != null) &&
                        (type != null)) {

                    alProducts.add(new Product.ProductRecord(code, inuse, lDesc, type, iLife));

                }
                // publishing the progress....
                // After this onProgressUpdate will be called

                // the progress of the updtaing database cannot include the actual update as it is done in 1 block
                // that cannot be interigated so we just need to call publishProgress once to reset the progress bar to indeterminate
                /*counter= counter +1;
                if (counter % 100 > 0) {
                    double percentage = ((counter / MaxRows) * 100);

                    publishProgress((int) percentage);
                }*/


            }

            if (alProducts.size() > 0) {

                // Delete anything already in the table
                barryDatabase.deleteAllProducts();

                // Now insert the rows.
                barryDatabase.bulkInsertProducts(alProducts);

                LogHelper.LOGD(TAG, "Finished");

            }

        } catch (Exception e) {
            e.printStackTrace();
            //mContentResolver.notifyChange(ArchieContract.DocumentArchiveContract.CONTENT_URI, null, false); ???
            broadcastIntent(e.getMessage(), 0);
        }

    }

    public int getColumnIndexFromCode(String code) {
        String input = code.toLowerCase();
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < input.length(); i++) {
            return alphabet.indexOf(input.charAt(i));
        }
        return 0;
    }
}
