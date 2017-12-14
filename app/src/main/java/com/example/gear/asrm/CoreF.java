package com.example.gear.asrm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Gear on 12/10/2560.
 */

class CoreF extends AppCompatActivity{
    // Double[] bodyHalf = {armL, kneeL, ankleL, shinL constant}

    public CoreF() {
    }

    //ขาตรง 180 องศา            วัดได้ 180
    Double[] getBodyHalf(double arm, double knee, double ankle, double shin){
        return new Double[]{arm, knee, ankle, shin};
    }

/**
    //งอขา ประมาณ 120 องศา     วัดได้ 140
    Double[] getBody1(){
        body = new Double[]{0.3, 0.3, 0.45, 0.45, 0.8, 0.8, 0.4, 0.4};
        return body;
    }

    //งอขา ประมาณ 90 องศา      วัดได้ 106
    Double[] getBody2(){
        body = new Double[]{0.3, 0.3, 0.35, 0.35, 0.6, 0.6, 0.4, 0.4};
        return body;
    }

    //งอขา น้อยกว่า 90 องศา      วัดได้ 92
    Double[] getBody3(){
        body = new Double[]{0.3, 0.3, 0.43, 0.43, 0.6, 0.6, 0.4, 0.4};
        return body;
    }

*/

    double findAngle(Double fKnee, Double fAnkle, Double fShin){
        //power(3, 2) == 9
        double s = (Math.pow(fKnee,2)+Math.pow(fShin,2)-Math.pow(fAnkle,2))/(2*fKnee*fShin);
        //radian to Degree
        double r = Math.toDegrees(Math.acos(s));

        //Degree Condition
        if (r == 180){return r;}
        else {return r - 20;}
    }

    String getTextDouble(Double d){return String.format("%.2f", d);}

    double calculateDistance(int rssi, int txPowerLevel){
        /* Distance Formula is
          * d = 10 ^ ((P-Rssi) / 10n) (n ranges from 2 to 4);
        */
        double dRssi = rssi*1.0;
        double dTxPowerLevel = txPowerLevel*1.0;
        double n = 3.0;
        double powValue = (dTxPowerLevel-dRssi)/(10*n);

        return Math.pow(10.0, powValue); // Distance
    }

}
