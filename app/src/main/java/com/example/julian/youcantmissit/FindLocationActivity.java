package com.example.julian.youcantmissit;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int lat,lng;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        lat = intent.getExtras().getInt("lat");
        lng = intent.getExtras().getInt("lng");

        setContentView(R.layout.activity_find_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng defaultLocation = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Recent Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
    }
}
