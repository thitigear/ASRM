package com.example.gear.asrm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gear.asrm.MainActivity;
import com.example.gear.asrm.R;



public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        prepareBeacon();
        stopScan();

        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(getApplicationContext() ,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

    private void prepareBeacon() {
        /**
         bluetoothLeScanner.startScan(new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            op_find.setText("" + deviceList.size());
            scanResult = result;

            if (scanResult.getDevice().getName() != null) {
                int[] capsuleRssiTxPower = {scanResult.getRssi(), scanResult.getScanRecord().getTxPowerLevel()};

                Log.e(TAG, "******************* Device Key : " +deviceList.keySet());

                if (deviceList.containsKey(scanResult.getDevice().getAddress())) {
                    /* Update Device Rssi & TxPowerLevel
                    deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                } else {
                    /* Add Device to DeviceList */
                    /**  deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);}

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

             }
         }
         }); */
    }
    private void stopScan(){

    }
}