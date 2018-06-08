package com.android.mapdemo.dialog;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hcltmac029 on 04/06/18.
 * 7:41 PM
 * interface for the presenter implementation
 * add the location into the DB
 * Load the location data from the DB
 */
interface ShowDialogPresenter {
    void addLocation(String text, LatLng latLng);
    void loadLocationData();
}
