package com.example.gear.asrm.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gear.asrm.MainActivity;
import com.example.gear.asrm.R;
import com.example.gear.asrm.fragment.allBeaconFragment;
import com.example.gear.asrm.fragment.historyFragment;
import com.example.gear.asrm.fragment.newRoundFragment;

public class allBeaconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_beacon);
    }

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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.action_bar_new_round){
            Intent intent = new Intent(getApplicationContext(), NewRoundActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.action_bar_all_beacon){
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.action_bar_history){
            Intent intent = new Intent(getApplicationContext(), historyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
