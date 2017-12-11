package com.example.gear.asrm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gear.asrm.MainActivity;
import com.example.gear.asrm.R;

public class HistoryResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_result);

        TextView textView_showStatus = findViewById(R.id.historyResult_textView_showStatus);
        textView_showStatus.setText(calculateMotionGrade());

        Button historyResult_button_OK = findViewById(R.id.historyResult_button_OK);

        historyResult_button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void genImage(){
        //create Angle Image
    }

    private void setTextView(String data){
        //set Result Activity TextViews
    }

    private String getResultData(){
        //get Data from Database and return TYPE
        return "DATA";
    }

    private String calculateMotionGrade(){
        return "Good!";
    }

}

