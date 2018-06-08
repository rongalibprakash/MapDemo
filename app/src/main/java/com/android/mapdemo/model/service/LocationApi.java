package com.android.mapdemo.model.service;


import android.content.Context;

import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by hcltmac029 on 31/05/18.
 * 8:02 PM
 * Interface to impleent the get all the locations
 * Add the new location into DB
 * Delete the location from the DB
 */
public interface LocationApi {

    // Interface to load the location data with success and failure callbacks
    interface LocationServiceCallback<T> {
        void onSuccess(T locations);
        void onFailure();
    }
    // Interface to add the location data with success and failure callbacks
    interface addLocationCallback<T>{
        void onSuccess(T location);
        void onFailure();
    }
    // Interface to delete the location data with success and failure callbacks
    interface delLocationCallback<T>{
        void onSuccess(T location);
        void onFailure();
    }
    // Interface to read the current location
    interface readLocationCallback<T>{
        void onSuccess(T location);
        void onFailure();
    }


    List<DBLocations> getAllLocations(LocationServiceCallback<List<DBLocations>> callback);
    void addNewLocation(String name,LatLng latLng, addLocationCallback callback);
    void deleteLocation(DBLocations dbLocations, delLocationCallback delCallback);
    void readCurrentLocation(Context context, readLocationCallback readCallback);
}
