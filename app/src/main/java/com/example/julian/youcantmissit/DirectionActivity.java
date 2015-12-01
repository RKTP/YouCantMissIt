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

public class DirectionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng mLocation,tLocation,centre;
    String targetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mLocation = new LatLng(intent.getExtras().getFloat("mlat"),intent.getExtras().getFloat("mlng"));
        tLocation = new LatLng(intent.getExtras().getFloat("tlat"),intent.getExtras().getFloat("tlng"));
        float clat,clng;
        clat = (intent.getExtras().getFloat("mlat")+intent.getExtras().getFloat("tlat"))/2;
        clng = (intent.getExtras().getFloat("mlng")+intent.getExtras().getFloat("tlng"))/2;
        centre = new LatLng(clat,clng);
        targetName=intent.getExtras().getString("tName");
        setContentView(R.layout.activity_direction);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(mLocation).title("Now"));
        mMap.addMarker(new MarkerOptions().position(tLocation).title(targetName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centre, 15));
    }
}
