/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */

package com.example.julian.youcantmissit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Intent settingIntent;
    Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(android.R.id.content).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
                MainActivity.this.startActivityForResult(settingIntent,0);
                MainActivity.this.startService(serviceIntent);
            }
        }, 2000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0) {
            MainActivity.this.finish();
        }
    }
}
