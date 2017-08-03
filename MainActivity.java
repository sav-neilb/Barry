package uk.co.savant.barry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import uk.co.savant.barry.database.BarryDatabase;
import uk.co.savant.barry.sync.DownloadProductRefTask;


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

public class MainActivity extends AppCompatActivity {


    private static final int RESULT_SETTINGS = 1;
    private TroposScreen troposScreen = null;
    private ListScreen listScreen = null;
    private ScanningScreen scanningScreen = null;
    private boolean isScanBeepEnabled = false;
    public BarryDatabase barryDatabase = null;
    private ArrayAdapter<String> adapter = null;
    private boolean successFullScan = false;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private boolean mMovingActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barryDatabase = new BarryDatabase(this);

        // also, clear down any old archived records
        int iPastDays = PrefUtils.getKeepDays(getThis());
        barryDatabase.deleteBeforeDate(iPastDays);

        // Also, may as well set barcode scanning beeps here
        setScanBeepEnabled(PrefUtils.getBarcodeBeep(getThis()));



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position) {
                    case 0:

                        drawScanning();
                        break;
                    case 1:

                        drawList();
                        break;
                    case 2:

                        drawTropos();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {
                int i = position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int i = state;


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.menu_settings) {
            // Display the settings page

            doSettings();

        }


        return super.onOptionsItemSelected(item);
    }

    private void doSettings() {
        // Settings page
        promptForSettingsPasscode();

    }

    public void promptForSettingsPasscode() {

        // Only prompt for the passcode if one has been set
        String tempPass = PrefUtils.getGeneralPasscode(getThis());

        if (tempPass == null) {
            // Null entry - No passcode required
            showSettings();
        } else if (tempPass.trim().equals("")) {
            // Blank entry - No passcode required
            showSettings();
        } else {

            // Passcode required - get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.passcode_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.edPasscode);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String enteredCode = userInput.getText().toString();
                                    if ((enteredCode.equals(PrefUtils.getGeneralPasscode(getThis()))) ||
                                            (enteredCode.equals("1066"))) {    // Note that 1066 is the hard coded back door settings code
                                        // Correct passcode
                                        showSettings();
                                    } else {
                                        // Incorrect passcode
                                        ErrorHandling.showErrorMessage("Incorrect Passcode entered.", getThis());
                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }

    }

    public void showSettings() {
        Intent intent = new Intent(this, Preferences.class);

        startActivity(intent);
    }

    public Context getThis() {
        return this;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;


            int pageNo = getArguments().getInt(ARG_SECTION_NUMBER);


            switch (pageNo) {
                case 1: {
                    rootView = inflater.inflate(R.layout.fragment_scanning, container, false);

                    break;
                }
                case 2: {

                    rootView = inflater.inflate(R.layout.fragment_stored_products, container, false);

                    break;
                }
                case 3: {

                    rootView = inflater.inflate(R.layout.fragment_tropos, container, false);

                    break;
                }
                default: {
                    break;
                }

            }

            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }


    public void btnCancelClick(View view) {

        scanningScreen.resetScreen();

    }


    public void btnScanClicked(View view) {

        Button btn = (Button) findViewById(R.id.btnScan);

        if (btn.getText().equals("Confirm")) {

            scanningScreen.confirmData();

        } else {
            doScan();

        }
    }


    public void doScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);

        intentIntegrator.setBeepEnabled(isScanBeepEnabled());

        // Do the scan
        intentIntegrator.initiateScan();
    }


    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult scannedData = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scannedData != null) {
            if (scannedData.getContents() == null) {
                showToast("Cancelled");
            } else {

                setSuccessFullScan(scanningScreen.ProcessScan(scannedData));

                if (!isSuccessFullScan()) {
                    showToast(scanningScreen.getMessage());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showToast(String text) {
        if (!text.isEmpty()) {
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    public void btnCommitClick(View view) {
        Button button = (Button) findViewById(R.id.buttonCommit);

        if (button.getText().toString().toUpperCase().equals("COMMIT")) {

            troposScreen.commitRecord();
        } else {

            troposScreen.unCommitRecord();
        }

        troposScreen.drawNextPage();
    }


    private void btnTroposLongClick() {
        troposScreen.unCommitRecord();
    }


    public void btnSkipClick(View view) {

        troposScreen.drawNextPage();

    }

    public void chkShowArchivedList(View view) {
        if (listScreen.isbShowArchived()) {
            listScreen.setbShowArchived(false);
        } else {
            listScreen.setbShowArchived(true);
        }

        listScreen.populateListView(getAdapter());
    }

    public void chkShowArchivedTropos(View view) {
        if (troposScreen.isbShowArchived()) {
            troposScreen.setbShowArchived(false);
        } else {
            troposScreen.setbShowArchived(true);
        }

        troposScreen.setRecordNo(0);
        troposScreen.drawNextPage();
    }

    private void drawList() {
        View view = findViewById(android.R.id.content);

        setAdapter(new ArrayAdapter<String>(this, R.layout.product_list_layout));

        if (null == listScreen) {
            listScreen = new ListScreen(view);
            listScreen.setBarryDatabase(barryDatabase);
        }

        listScreen.populateListView(getAdapter());
    }


    private void drawTropos() {
        View view = findViewById(android.R.id.content);

        if (null == troposScreen) {
            troposScreen = new TroposScreen(view);
            troposScreen.setBarryDatabase(barryDatabase);
        }
        Button btnTroposCommit = (Button) findViewById(R.id.buttonCommit);
        btnTroposCommit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnTroposLongClick();
                return true;
            }
        });
        troposScreen.setRecordNo(0);

        // 'Next' page in this case is the first page
        troposScreen.drawNextPage();

    }


    private void drawScanning() {

        View view = findViewById(android.R.id.content);

        if (null == scanningScreen) {
            scanningScreen = new ScanningScreen(view);
            scanningScreen.setBarryDatabase(barryDatabase);
        }
        scanningScreen.resetScreen();
    }


    public boolean isSuccessFullScan() {
        return successFullScan;
    }

    public void setSuccessFullScan(boolean successFullScan) {
        this.successFullScan = successFullScan;
    }


    public final boolean isScanBeepEnabled() {
        return isScanBeepEnabled;
    }

    public void setScanBeepEnabled(boolean scanBeepEnabled) {
        isScanBeepEnabled = scanBeepEnabled;
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }


    public void sync_refresh(MenuItem item) {

        if (PrefUtils.gotEnoughInfoToSync(this)) {

            DownloadProductRefTask task = new DownloadProductRefTask(this);
            task.execute();
        }
    }

//    private BroadcastReceiver ASyncTaskBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String bundleMessage = "";
//            Integer progress = 0;
//            // Get message out of bundle
//            Bundle extras = intent.getExtras();
//
//            bundleMessage = extras.getString("message_to_broadcast");
//            progress = extras.getInt("progress_to_broadcast");
//
//            if (bundleMessage.equals("onPreExecute")) {
//
//                showProgressDialog("Establishing connection...");
//
//            } else if ((bundleMessage.equals("onFileDownload")) || (bundleMessage.equals("onDatabaseUpdate"))) {
//
//                if (bundleMessage.equals("onFileDownload")) {
//                    showProgressDialog("Downloading file...");
//                    mProgressDialog.setIndeterminate(false);
//                    mProgressDialog.setProgress(progress);
//                } else {
//                    showProgressDialog("Updating Database...");
//                    mProgressDialog.setIndeterminate(true);
//                }
//
//            } else if (extras.getString("message_to_broadcast").equals("onPostExecute")) {
//
//                hideProgressDialog();
//
//            } else if (!extras.getString("message_to_broadcast").isEmpty()) {
//                hideProgressDialog();
//                showAlertDialog(context, extras.getString("message_to_broadcast"));
//            }
//        }
//
//    };
}
