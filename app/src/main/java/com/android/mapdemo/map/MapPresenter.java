package com.android.mapdemo.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hcltmac029 on 31/05/18.
 * 7:44 PM
 * Interface to implement the load the data and popup message
 */
interface MapPresenter {
    void loadLocationsData();
    void showNewLocationPopup(LatLng latLng);
}
