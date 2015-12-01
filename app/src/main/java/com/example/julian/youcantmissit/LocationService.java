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
import android.util.Log;

import java.util.ArrayList;

class customLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        LocationService.myLocation=location;
        LocationService.updateTargetLocation();
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
    Context context;
    private static LocationData topPriority=null;
    private static DBManager db;
    static Location myLocation=null;
    private LocationManager locationManager;
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
        context = getApplicationContext();
        db=DBManager.getInstance(context);
        myLocation = new Location("");
        myLocation.setLatitude(0);
        myLocation.setLongitude(0);
        locationManager=(LocationManager)getSystemService(context.LOCATION_SERVICE);
        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,locationListener);
        updateTargetLocation();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateTargetLocation();
        Log.d("serviceLog",myLocation.getLatitude()+" || " + myLocation.getLongitude());
        //widget update
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected static void updateTargetLocation() {
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
