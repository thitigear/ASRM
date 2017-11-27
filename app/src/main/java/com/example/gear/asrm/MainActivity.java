package com.example.gear.asrm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;


public class MainActivity extends AppCompatActivity{

    ScanResult scanResult;
    ScanRecord scanRecord;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

    private static final String TAG = "MainActivity";

    CoreF core = new CoreF();
    protected Map<String, int[]> deviceList = new HashMap<String, int[]>();

    /*
       * HMSoft = D4:36:39:DE:56:C6       // knee
       * HMSoft = D4:36:39:DE:57:D0       // ankle
       * HMSensor = 50:8C:B1:75:1C:3C     // shin
       * HMSensor = 50:8C:B1:75:16:D2]    // arm
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "APP STARTED UP NOW!!!!!!!!!!!!!!!!!!!!!!!!!");

        final TextView op_find = (TextView) findViewById(R.id.output_found);
        final TextView op_kneeL = (TextView) findViewById(R.id.output_kneeL);
        final TextView op_ankleL = (TextView) findViewById(R.id.output_ankleL);

/* UI Binding */
        //Define Button
        Button button_showData = (Button) findViewById(R.id.button_getData);
        Button button_findAngel = (Button) findViewById(R.id.button_findAngle);





        /* DeviceList Structure
         * --------------------------------------------------------------------------------
         * data = Key: capsuleRssiTxPower
         * capsuleRssiTxPower = {Rssi, TxPowerLevel};
         * ////////////////////////////////////////////////////////////////////////////////
         * Device Address
         * --------------------------------------------------------------------------------
         * HMSoft = D4:36:39:DE:56:C6       // knee
         * HMSoft = D4:36:39:DE:57:D0       // ankle
         * HMSensor = 50:8C:B1:75:1C:3C     // shin
         * HMSensor = 50:8C:B1:75:16:D2    // arm
         * ////////////////////////////////////////////////////////////////////////////////
         * Cal Distance
         * core.calculateDistance(scanResult.getRssi(), scanRecord.getTxPowerLevel()))
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
/*
                if (count == 0) {
                    body = core.getBody1();
                    setTextTextView(body);
                    count++;
                } else if (count == 1) {
                    body = core.getBody2();
                    setTextTextView(body);
                    count++;
                } else if (count == 2) {
                    body = core.getBody3();
                    setTextTextView(body);
                    count++;
                } else {
                    body = core.getDefaultBody();
                    setTextTextView(body);
                    count = 0;
                }
*/
                //Log.e(TAG, "scanRecord manufact : " + scanRecord.getManufacturerSpecificData(224).toString());
            }
        });
        //Show/Find Beacon
        button_findAngel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


/* Bluetooth Scan to Find Beacon */
        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        scanSettingsBuilder.setReportDelay(0);

        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                op_find.setText("" + deviceList.size());
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
/*
                    if (deviceList.size() >= 3) {
                        long start = System.currentTimeMillis();
                        long elapsedTimeMillis = System.currentTimeMillis()-start;
                        while(elapsedTimeMillis < 2){
                            //op_kneeL.setText("" + core.calculateDistance(deviceList.get("D4:36:39:DE:56:C6")[0], deviceList.get("D4:36:39:DE:56:C6")[1]));
                            op_kneeL.setText("" + core.calculateDistance(deviceList.get("50:8C:B1:75:1C:3C")[0], deviceList.get("50:8C:B1:75:1C:3C")[1]));
                            op_ankleL.setText("" + core.calculateDistance(deviceList.get("D4:36:39:DE:57:D0")[0], deviceList.get("D4:36:39:DE:57:D0")[1]-20));
                            start = System.currentTimeMillis();
                        }

                    }
*/
                }
            }
        });

        /*
        * Intent intent = new Intent(this, MainActivity);
        * // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
        * // created when a user launches the activity manually and it gets launched from here.
        * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        * this.startActivity(intent);
        */

    }


    /* Set TextView */
    private void setTextTextViewLeft(Double[] body) {
        final TextView op_armL = (TextView) findViewById(R.id.output_armL);
        final TextView op_kneeL = (TextView) findViewById(R.id.output_kneeL);
        final TextView op_ankleL = (TextView) findViewById(R.id.output_ankleL);
        final TextView op_shinL = (TextView) findViewById(R.id.output_shinL);
        final TextView op_angleL = (TextView) findViewById(R.id.output_angleL);

        //final  TextView op_found =

        double angleL = core.findAngle(body[1], body[2], body[3]);

        op_armL.setText(core.getTextDouble(body[0]));
        op_kneeL.setText(core.getTextDouble(body[1]));
        op_ankleL.setText(core.getTextDouble(body[2]));
        op_shinL.setText(core.getTextDouble(body[3]));
        op_angleL.setText(core.getTextDouble(angleL));
    }
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
    }

    /* Creat OPTION MENU */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beacon, menu);
        return true;
    }

    /* Select MENU ITEM */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }





}
