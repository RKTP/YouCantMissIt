/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */

package com.example.julian.youcantmissit;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.location.*;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

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

    private static boolean isWidgetActive=false;

    private static WindowManager windowManager;
    private static ImageView chatHead;

    LocationListener locationListener = new customLocationListener();

    public LocationService() {
        super();
    }

    public static LocationData getTop() {
        return topPriority;
    }

    public static float getMyLat() {
        return (float)myLocation.getLatitude();
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, locationListener);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.direction38);
        chatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topPriority!=null) {
                    Intent dIntent = new Intent(context,DirectionActivity.class);
                    dIntent.putExtra("mlat",(float)myLocation.getLatitude());
                    dIntent.putExtra("mlng",(float)myLocation.getLongitude());
                    dIntent.putExtra("tlat",topPriority.getLat());
                    dIntent.putExtra("tlng",topPriority.getLng());
                    dIntent.putExtra("tName",topPriority.getName());
                    dIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dIntent);
                }
            }
        });

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

    public static void updateTargetLocation() {
        ArrayList<LocationData> locationList = db.getActiveLocation();
        float minimum=Float.MAX_VALUE;
        for(LocationData e : locationList) {
            Location element = new Location("");
            element.setLatitude(e.getLat());
            element.setLongitude(e.getLng());
            float distance = myLocation.distanceTo(element);
            if(distance>500) {
                continue;
            }
            if(distance<minimum) {
                minimum=distance;
                topPriority=e;
            }
            if(minimum==Float.MAX_VALUE) topPriority=null;
        }
        if(!isWidgetActive&&topPriority!=null) {
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 1500;
            params.y = 1500;
            windowManager.addView(chatHead, params);
            isWidgetActive=true;
        } else if(topPriority==null&&isWidgetActive) {
            windowManager.removeViewImmediate(chatHead);
            isWidgetActive=false;
        }
    }
}
