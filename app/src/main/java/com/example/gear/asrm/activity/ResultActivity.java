package com.example.gear.asrm.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gear.asrm.MainActivity;
import com.example.gear.asrm.R;

public class ResultActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textView_showStatus = (TextView) findViewById(R.id.result_textView_showStatus);
        textView_showStatus.setText(calculateMotionGrade());

        Button button_OK = (Button) findViewById(R.id.result_button_OK);

        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void genImage(){
        //create Angle Image
    }

    private void setTextView(){
        //set Result Activity TextViews
    }

    private void getResultData(){
        //get Data from Database
    }

    private String calculateMotionGrade(){
        return "Good!";
    }
}
