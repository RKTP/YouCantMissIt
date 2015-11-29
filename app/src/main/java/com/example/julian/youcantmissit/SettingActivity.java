/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */

package com.example.julian.youcantmissit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    DBManager dbManager;
    ArrayList<LocationData> locationList;
    ListView listView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbManager=DBManager.getInstance(getApplicationContext());
        locationList = dbManager.getAllLocation();

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ItemAdapter(this,this.locationList);
        listView.setAdapter(adapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),LocationDetailActivity.class);
                intent.putExtra("lat",locationList.get(position).getLat());
                intent.putExtra("lng",locationList.get(position).getLng());
                intent.putExtra("name",locationList.get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
