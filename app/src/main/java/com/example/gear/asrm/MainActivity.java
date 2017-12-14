package com.example.gear.asrm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.gear.asrm.activity.*;
import com.example.gear.asrm.fragment.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
        //implements NavigationView.OnNavigationItemSelectedListener{

    ScanResult scanResult;
    ScanRecord scanRecord;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

    private static final String TAG = "MainActivity";
    CoreF core = new CoreF();
    protected Map<String, int[]> deviceList = new HashMap<String, int[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "APP STARTED UP NOW!!!!!!!!!!!!!!!!!!!!!!!!!");

/* UI Binding */
        final TextView shin = findViewById(R.id.output_shinL);
        final TextView tShin = findViewById(R.id.textView_shinL);
        tShin.setText("Found : ");

        //Define Button
        Button button_showData = (Button) findViewById(R.id.button_getData);
        Button button_findAngel = (Button) findViewById(R.id.button_findAngle);
        //Toolbar app_bar_Toolbar = (Toolbar) findViewById(R.id.mainToolbar);

        /* DeviceList Structure
           --------------------------------------------------------------------------------
           data = Key: capsuleRssiTxPower
           capsuleRssiTxPower = {Rssi, TxPowerLevel};
           ////////////////////////////////////////////////////////////////////////////////
           Device Address
           --------------------------------------------------------------------------------
           HMSoft = D4:36:39:DE:56:C6       //
           HMSoft = D4:36:39:DE:57:D0       // ankle
           HMSensor = 50:8C:B1:75:1C:3C     // shin
           HMSensor = 50:8C:B1:75:16:D2    // knee
           ////////////////////////////////////////////////////////////////////////////////
           Cal Distance
           core.calculateDistance(scanResult.getRssi(), scanRecord.getTxPowerLevel()))
         */

/* Button Coding */
       //Button Get and Show Data
        button_showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "///////////////////////Show Data/////////////////////////////////");
                /* ////////////////////////////////////////////////////////////////////////////////
                * Find Distance
                * --------------------------------------------------------------------------------
                * core.calculateDistance(scanResult.getRssi(), scanRecord.getTxPowerLevel())
                * ////////////////////////////////////////////////////////////////////////////////
                */

        /* Get Body */
                //double arm = core.calculateDistance(deviceList.get("50:8C:B1:75:16:D2")[0], deviceList.get("50:8C:B1:75:16:D2")[1]);
                //Log.e(TAG, "///////////////////////Arm :" + arm);
                double knee = core.calculateDistance(deviceList.get("50:8C:B1:75:1C:3C")[0], deviceList.get("50:8C:B1:75:1C:3C")[1]);
                Log.e(TAG, "///////////////////////Knee :" + knee);
                double ankle = core.calculateDistance(deviceList.get("D4:36:39:DE:57:D0")[0], (deviceList.get("D4:36:39:DE:57:D0")[1]) - 20);
                Log.e(TAG, "///////////////////////Ankle :" + ankle);
                double shin = 40.0;
                setTextTextViewLeft(core.getBodyHalf(999,knee, ankle, shin));

                //Log.e(TAG, "scanRecord manufact : " + scanRecord.getManufacturerSpecificData(224).toString());
          }
        });


/* Bluetooth Scan to Find Beacon */
        //ScanFilter.Builder setManufacturerData (int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask).
/**
        scanFilterList.add(new ScanFilter.Builder().setDeviceAddress("50:8C:B1:75:16:D2").setDeviceName("HMSensor").build());
        scanFilterList.add(new ScanFilter.Builder().setDeviceAddress("D4:36:39:DE:57:D0").setDeviceName("HMSoft").build());

        scanFilterList.add(new ScanFilter.Builder().setDeviceAddress("50:8C:B1:75:1C:3C").setDeviceName("HMSensor").build());
        scanFilterList.add(new ScanFilter.Builder().setDeviceAddress("D4:36:39:DE:56:C6").setDeviceName("HMSoft").build());

        ScanSettings scanSettingsBuilder = new ScanSettings.Builder().setReportDelay(0).build();
*/

        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                shin.setText("" + deviceList.size());
                scanResult = result;

                if (scanResult.getDevice().getName() != null) {
                    int[] capsuleRssiTxPower = {scanResult.getRssi(), scanResult.getScanRecord().getTxPowerLevel()};

                    Log.e(TAG, "******************* Device Key : " +
                            deviceList.keySet());
                    if (deviceList.containsKey(scanResult.getDevice().getAddress())) {
                        /* Update Device Rssi & TxPowerLevel */
                        deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);

                    } else {
                        /* Add Device to DeviceList */
                        deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                    }
              }
            }
        });


    }

    /* Set TextView */
    private void setTextTextViewLeft(Double[] body) {
        final TextView op_armL = (TextView) findViewById(R.id.output_armL);
        final TextView op_kneeL = (TextView) findViewById(R.id.output_kneeL);
        final TextView op_ankleL = (TextView) findViewById(R.id.output_ankleL);
        //final TextView op_shinL = (TextView) findViewById(R.id.output_shinL);
        final TextView op_angleL = (TextView) findViewById(R.id.output_angleL);

        //final  TextView op_found =

        double angleL = core.findAngle(body[1], body[2], body[3]);

        op_armL.setText(core.getTextDouble(body[0]));
        op_kneeL.setText(core.getTextDouble(body[1]));
        op_ankleL.setText(core.getTextDouble(body[2]));
        //op_shinL.setText(core.getTextDouble(body[3]));
        op_angleL.setText(core.getTextDouble(angleL));
    }
    /**
    private void setTextTextViewRight(Double[] body) {
        final TextView op_armR = (TextView) findViewById(R.id.output_armR);
        final TextView op_kneeR = (TextView) findViewById(R.id.output_kneeR);
        final TextView op_ankleR = (TextView) findViewById(R.id.output_ankleR);
        final TextView op_shinR = (TextView) findViewById(R.id.output_shinR);
        final TextView op_angleR = (TextView) findViewById(R.id.output_angleR);

        double angleR = core.findAngle(body[1], body[2], body[3]);

        op_armR.setText(core.getTextDouble(body[0]));
        op_kneeR.setText(core.getTextDouble(body[1]));
        op_ankleR.setText(core.getTextDouble(body[2]));
        op_shinR.setText(core.getTextDouble(body[3]));
        op_angleR.setText(core.getTextDouble(angleR));
    }*/
/**
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home){
            fragmentManager.beginTransaction().
                    replace(R.id.content_frame, new homeFragment()).
                    commit();
        } else if (id == R.id.nav_newRound){
            fragmentManager.beginTransaction().
                    replace(R.id.content_frame, new newRoundFragment()).
                    commit();
        } else if (id == R.id.nav_allBeacon){
            fragmentManager.beginTransaction().
                    replace(R.id.content_frame, new allBeaconFragment()).
                    commit();

        } else if (id == R.id.nav_history){
            fragmentManager.beginTransaction().
                    replace(R.id.content_frame, new historyFragment()).
                    commit();
        }
        //DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    */

    /* Creat OPTION MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    /* Select MENU ITEM */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bar_home){
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.action_bar_new_round){
            Intent intent = new Intent(getApplicationContext(), NewRoundActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_bar_all_beacon){
            Intent intent = new Intent(getApplicationContext(), allBeaconActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_bar_history){
            Intent intent = new Intent(getApplicationContext(), historyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("Confirm Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                        finish();
                    }
                }).create().show();
    }
}
