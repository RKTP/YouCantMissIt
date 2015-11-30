/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */

package com.example.julian.youcantmissit;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

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

public class LocationService extends Service {
    private static LocationData topPriority=null;
    DBManager db;
    static Location myLocation=null;
    LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    int lat=0,lng=0;
    static int  permission;

    LocationListener locationListener = new customLocationListener();

    public LocationService() {
        super();
    }

    public static LocationData getTop() {
        return topPriority;
    }

    @Override
    public void onCreate() {
        db=DBManager.getInstance(getApplicationContext());
        updateTargetLocation();
        myLocation = new Location("");
        myLocation.setLatitude(0);
        myLocation.setLongitude(0);
        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,1000,1,locationListener);
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,1000,1,locationListener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateTargetLocation();
        try {
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            //This shan't happen
        }
        //widget update
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateTargetLocation() {
        ArrayList<LocationData> locationList = db.getActiveLocation();
        float minimum=Float.MAX_VALUE;
        for(LocationData e : locationList) {
            Location element = new Location("");
            element.setLatitude(e.getLat());
            element.setLongitude(e.getLng());
            float distance = myLocation.distanceTo(element);
            if(distance>500) {
                continue;
            } else if(distance<minimum) {
                minimum=distance;
                topPriority=e;
            }
            if(minimum==Float.MAX_VALUE) topPriority=null;
        }
    }
}
