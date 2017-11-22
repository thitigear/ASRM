package com.example.gear.asrm;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconData;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.service.BeaconService;
import org.altbeacon.beacon.service.Callback;
import org.altbeacon.beacon.service.ScanJob;
import org.altbeacon.beacon.simulator.BeaconSimulator;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.startup.StartupBroadcastReceiver;
import org.altbeacon.bluetooth.BleAdvertisement;
import org.altbeacon.bluetooth.Pdu;


public class MainActivity extends AppCompatActivity implements BootstrapNotifier, MonitorNotifier, RangeNotifier, BeaconConsumer {

    private BeaconManager beaconManager;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private Region region = new Region("All Beacon Region", null, null, null);
    private MonitoringActivity monitoringActivity;
    Identifier identifier;

    Pdu pdu;

    ScanResult scanResult;
    ScanRecord scanRecord;
    ScanJob scanJob;

    StartupBroadcastReceiver startupBroadcastReceiver;
    BeaconConsumer beaconConsumer;
    BeaconTransmitter beaconTransmitter;
    Collection<Beacon> beaconsCol;
    BeaconService beaconService;

    Callback callback;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    BluetoothLeAdvertiser bluetoothLeAdvertiser;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "MainActivity";

    CoreF core = new CoreF();
    protected Double[] body = new Double[]{0.0,0.0,0.0,0.0, 0.0,0.0,0.0,0.0};
    protected double angleL;
    protected double angleR;
    int count = 0;
    protected List beaconList;
    List<byte[]> manufacturerDataList;

    /**myUUID : 74278bda-b644-4520-8f0c-720eaf059935 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "APP STARTED UP NOW!!!!!!!!!!!!!!!!!!!!!!!!!");

        final TextView op_find = (TextView) findViewById(R.id.output_found);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        //// Detect the main Beacon frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));

        beaconManager.bind(this);
/**
        if (checkPrerequisites()) {
            beaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));
            beaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
            beaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
            beaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
            beaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));}
            // Transmit a beacon with Identifiers 74278bda-b644-4520-8f0c-720eaf059935 66504 66505
*/
            //beacon.setExtraDataFields(new Beacon.Builder().set);

        //Define Button
        Button button_getData = (Button) findViewById(R.id.button_getData);
        Button button_findAngel = (Button) findViewById(R.id.button_findAngle);

        //Get Default Value
        body = core.getDefaultBody();
        setTextTextView(body);

        Beacon beacon = new Beacon.Builder()
                .setId1("74278bda-b644-4520-8f0c-720eaf059935")
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0000)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[] {0l}))
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        byte[] data = beaconParser.getBeaconAdvertisementData(beacon);

        Log.e(TAG, "*******************BEACON DATA :"+data);


        //Button Get and Show Data
        button_getData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "Click get data button");
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
                    //Find All Device near us

                    onResume();
                    ScanCallback scanCallback = new ScanCallback() {
                        @Override
                        public void onScanResult(int callbackType, ScanResult result) {
                            ScanRecord mScanRecord = result.getScanRecord();
                            scanResult = result;
                            scanRecord = mScanRecord;
                            //int[] manufacturerData = mScanRecord.getManufacturerSpecificData(224);
                            byte[] manufacturerData = mScanRecord.getManufacturerSpecificData(224);
                            int mRssi = result.getRssi();
                            System.out.print(manufacturerData + "!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    };
                }
        });
            //Show/Find Beacon
            button_findAngel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "Click Find Button");
                    Log.d(TAG, "isAnyConsumerBound:" + beaconManager.isAnyConsumerBound());
                    Log.d(TAG, "isRegionStatePersistenceEnabled:" + beaconManager.isRegionStatePersistenceEnabled());
                    Log.d(TAG, "beaconManager.isRegionStatePersistenceEnabled: " + beaconManager.isRegionStatePersistenceEnabled());
                    //regionBootstrap.disable();
//              startupBroadcastReceiver.onReceive(MainActivity.this, monitoringActivity.getIntent());

                    //Log.e(TAG,"mScanCallback.toString: "+ scanResult.getScanRecord().getDeviceName());
                    onPause();
                    op_find.setText("");
                }
            });
    }



    @TargetApi(21)
    private boolean checkPrerequisites(){
        if (android.os.Build.VERSION.SDK_INT < 18) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not supported by this device's operating system");
            builder.setMessage("You will not be able to transmit as a Beacon");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;
        }
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not supported by this device");
            builder.setMessage("You will not be able to transmit as a Beacon");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;
        }
        if (!((BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth not enabled");
            builder.setMessage("Please enable Bluetooth and restart this app.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;

        }

        try {
            // Check to see if the getBluetoothLeAdvertiser is available.  If not, this will throw an exception indicating we are not running Android L
            ((BluetoothManager) this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().getBluetoothLeAdvertiser();
        }
        catch (Exception e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE advertising unavailable");
            builder.setMessage("Sorry, the operating system on this device does not support Bluetooth LE advertising.  As of July 2014, only the Android L preview OS supports this feature in user-installed apps.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;

        }

        return true;

    }

    @NonNull
    public static byte[] uuidToByte(UUID uuid){
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    private void setTextTextView(Double[] body) {
        final TextView op_armL = (TextView) findViewById(R.id.output_armL);
        final TextView op_kneeL = (TextView) findViewById(R.id.output_kneeL);
        final TextView op_ankleL = (TextView) findViewById(R.id.output_ankleL);
        final TextView op_shinL = (TextView) findViewById(R.id.output_shinL);
        final TextView op_armR = (TextView) findViewById(R.id.output_armR);
        final TextView op_kneeR = (TextView) findViewById(R.id.output_kneeR);
        final TextView op_ankleR = (TextView) findViewById(R.id.output_ankleR);
        final TextView op_shinR = (TextView) findViewById(R.id.output_shinR);

        final TextView op_angleL = (TextView) findViewById(R.id.output_angleL);
        final TextView op_angleR = (TextView) findViewById(R.id.output_angleR);

        op_armL.setText(core.getTextDouble(body[0]));
        op_armR.setText(core.getTextDouble(body[1]));
        op_kneeL.setText(core.getTextDouble(body[2]));
        op_kneeR.setText(core.getTextDouble(body[3]));
        op_ankleL.setText(core.getTextDouble(body[4]));
        op_ankleR.setText(core.getTextDouble(body[5]));
        op_shinL.setText(core.getTextDouble(body[6]));
        op_shinR.setText(core.getTextDouble(body[7]));
        angleL = core.findAngle(body[4], body[2], body[6]);
        angleR = core.findAngle(body[5], body[3], body[7]);
        op_angleL.setText(core.getTextDouble(angleL));
        op_angleR.setText(core.getTextDouble(angleR));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void setScanFilter(){
        Log.e(TAG, "SetScanFilter !!!!!!!!!!!");
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();
        ByteBuffer mManufacturerData = ByteBuffer.allocate(23);
        ByteBuffer mManufacturerDataMask = ByteBuffer.allocate(24);


        /**
        UUID uuid = UUID.fromString(bluetoothAdapter.getAddress());
        byte[] uuidByte = uuidToByte(uuid);

        mManufacturerData.put(0, (byte)0xBE);
        mManufacturerData.put (1, (byte)0xAC);
        for (int i=2; i<=17; i++) {
            mManufacturerData.put(i, uuidByte[i-2]);
        }
        for (int i=0; i<=17; i++) {
            mManufacturerDataMask.put((byte)0x01);
        }
        mBuilder.setManufacturerData(224, mManufacturerData.array(), mManufacturerDataMask.array());
        ScanFilter mScanFilter = mBuilder.build();*/
    }

    private void setScanSettings() {
        Log.e(TAG, "SetScanSetting !!!!!!");
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();
        mBuilder.setReportDelay(0);
        mBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        ScanSettings mScanSettings = mBuilder.build();
    }

    protected ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord mScanRecord = result.getScanRecord();
            scanResult = result;
            scanRecord = mScanRecord;
            //int[] manufacturerData = mScanRecord.getManufacturerSpecificData(224);
            byte[] manufacturerData = mScanRecord.getManufacturerSpecificData(224);
            int mRssi = result.getRssi();
            System.out.print(result.getDevice().getAddress());
        }
    };
/**
    private String getScanResult(){

        ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
            }
        };

        return scanCallback;
    }
*/



//Beacon Service Connect
    @Override
    public void onBeaconServiceConnect() {
        Log.e(TAG+"!!!!", "Start Service!!!!!!!!!!!!!!!!!!!");

        Identifier myBeaconNamespaceId = Identifier.parse("0x2f234454f4911ba9ffa6");
        Identifier myBeaconInstanceId = Identifier.parse("0x000000000001");

        Region region = new Region("my-beacon-region", myBeaconNamespaceId, myBeaconInstanceId, null);

        beaconManager.addMonitorNotifier(this);
        try {
            //beaconManager.startMonitoringBeaconsInRegion(region);
            beaconManager.startRangingBeaconsInRegion(region);
            //beaconManager.addRangeNotifier(this);
            //Log.e(TAG,"beaconManager: "+scanResult.getDevice().getName());
            //beaconManager.

        } catch (RemoteException e) {e.printStackTrace();}



    }

//RegionNotifier
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {


        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a  frame
                beaconsCol.add(beacon); Log.e(TAG, "Add Beacon to Beacon Collection");
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();
                Log.d(TAG, "I see a beacon transmitting namespace id: " + namespaceId +
                        " and instance id: " + instanceId +
                        " approximately " + beacon.getDistance() + " meters away.");
                if (beacon.getExtraDataFields().size() > 0) {
                    long telemetryVersion = beacon.getExtraDataFields().get(0);
                    long batteryMilliVolts = beacon.getExtraDataFields().get(1);
                    long pduCount = beacon.getExtraDataFields().get(3);
                    long uptime = beacon.getExtraDataFields().get(4);

                    Log.d(TAG, "The above beacon is sending telemetry version "+telemetryVersion+
                            ", has been up for : "+uptime+" seconds"+
                            ", has a battery level of "+batteryMilliVolts+" mV"+
                            ", and has transmitted "+pduCount+" advertisements.");

                }
            }
        }
    }

//Creat OPTION MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beacon, menu);
        return true;
    }
//Select MENU ITEM
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

//Beacon REGION
    @Override
    public void didEnterRegion(Region region) {
        Log.e(TAG, "Got a didEnterRegion call");
        // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
        // if you want the Activity to launch every single time beacons come into view, remove this call.
        //regionBootstrap.disable();
        Log.d(TAG, "I detected a beacon in the region with namespace id " + region.getId1() +
                " and instance id: " + region.getId2());
    }
    @Override
    public void didExitRegion(Region region) {
        //Don't Care
    }
    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        //Don't Care
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

}
