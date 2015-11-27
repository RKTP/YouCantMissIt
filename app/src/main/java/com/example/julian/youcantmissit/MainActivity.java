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

        try {
            Thread.sleep(2000);
        } catch(InterruptedException e) {
            //No way...
        }

        settingIntent = new Intent(this,SettingActivity.class);
        serviceIntent = new Intent(this,LocationService.class);
        startService(serviceIntent);
        startActivity(settingIntent);
    }
}
