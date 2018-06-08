package com.android.mapdemo.map;

import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by hcltmac029 on 31/05/18.
 * 7:24 PM
 *
 */
public interface MapView {
    void showProgress();
    void hideProgress();
    void removeLocations();
    void showLocations(List<DBLocations> locations);
    void showConnectionError();
    void showLocationPopup(LatLng latLng);


}

