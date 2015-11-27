/**
 * Initially Created by Julian on 2015-11-22.
 * Lastly modified by Julian on 2015-11-25.
 */

package com.example.julian.youcantmissit;

public class LocationData {
    private String name;
    private int key;
    private int lat,lng;
    private boolean activate;

    public LocationData(int key, String name, int lat, int lng, int activeFlag) {
        this.name = name;
        this.key = key;
        this.lat = lat;
        this.lng = lng;
        if(activeFlag==1) {
            this.activate=true;
        } else this.activate=false;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public int getLat() {
        return lat;
    }

    public int getLng() {
        return lng;
    }

    public boolean isActivated() {
        return activate;
    }

    public boolean activationSwap() {
        if (this.activate) {
            this.activate = false;
        } else {
            this.activate = true;
        }
        return this.activate;
    }
}
