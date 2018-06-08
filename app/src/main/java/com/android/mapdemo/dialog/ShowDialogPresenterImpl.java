package com.android.mapdemo.dialog;


import android.content.Context;

import com.android.mapdemo.Utils;
import com.android.mapdemo.model.service.LocationApi;
import com.android.mapdemo.model.service.LocationApiImpl;
import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

/**
 * Created by hcltmac029 on 04/06/18.
 * 7:42 PM
 * Dialog implementation to add and load the location name from the given location point
 */
public class ShowDialogPresenterImpl implements ShowDialogPresenter{

    private ShowDialogView mView;
    private final LocationApiImpl mLocationsApi;
    private LatLng mLatLng;
    private Context mContext;

    public ShowDialogPresenterImpl(Context context, ShowDialogView dialogView, LatLng latLng, LocationApiImpl locationApi) {
        this.mView = dialogView;
        this.mLocationsApi = locationApi;
        this.mLatLng = latLng;
        this.mContext = context;
    }

    /**
     * API implementation for add the location in to DB
     * @param name name of the location
     * @param latLng latlang values
     */
    @Override
    public void addLocation(String name, LatLng latLng) {
        mLocationsApi.addNewLocation(name, latLng, new LocationApi.addLocationCallback<DBLocations>() {

            @Override
            public void onSuccess(DBLocations location) {
                mView.showAddLocationSuccess(location);
            }

            @Override
            public void onFailure() {
                mView.showConnectionError();
            }

        });
    }

    /**
     * Load the location name from the given location point
     */
    @Override
    public void loadLocationData() {
        try {
            String locationName = Utils.getLocationName(mContext, mLatLng);
            mView.showLocationName(locationName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
