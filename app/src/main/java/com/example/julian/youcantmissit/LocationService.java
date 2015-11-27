/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */

package com.example.julian.youcantmissit;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

import java.util.ArrayList;

class customLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        LocationService.myLocation=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

public class LocationService extends IntentService {
    private static LocationData topPriority=null;
    DBManager db;
    static Location myLocation;
    LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    int lat=0,lng=0;

    LocationListener locationListener = new customLocationListener();

    public LocationService() {
        super(".LocationService");
    }

    public static LocationData getTop() {
        return topPriority;
    }

    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        db=DBManager.getInstance(getApplicationContext());
        updateTargetLocation();
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,1000,1,locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //shall be implemented

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateTargetLocation() {
        ArrayList<LocationData> locationList = db.getActiveLocation();

    }

}
