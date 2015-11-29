/**
 * Initially Created by Julian on 2015-11-25.
 * Lastly modified by Julian on 2015-11-25.
 */

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

public class LocationDetailActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private int lat,lng;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        lat = intent.getExtras().getInt("lat");
        lng = intent.getExtras().getInt("lng");
        name = intent.getExtras().getString("name");

        setContentView(R.layout.activity_location_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng target = new LatLng(this.lat, this.lng);
        mMap.addMarker(new MarkerOptions().position(target).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
    }
}
