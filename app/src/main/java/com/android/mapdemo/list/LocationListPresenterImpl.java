package com.android.mapdemo.list;

import android.content.Context;
import android.location.Location;

import com.android.mapdemo.dialog.ShowDialogView;
import com.android.mapdemo.model.service.LocationApi;
import com.android.mapdemo.model.service.LocationApiImpl;
import com.android.mapdemo.roomDB.DBLocations;

import java.util.List;

/**
 * Created by hcltmac029 on 05/06/18.
 * 7:45 PM
 * LocationListPresenterImpl to read the location data from the DM
 * Delete the location from the list
 * Can get the call when the success or failure callback when the operation is done
 */
public class LocationListPresenterImpl implements LocationListPresenter{
    private LocationListView mView;
    private final LocationApiImpl mLocationsApi;

    public LocationListPresenterImpl(LocationListView view, LocationApiImpl locationApi) {
        mView = view;
        mLocationsApi = locationApi;
    }

    /**
     * Load the location data from the DB
     */
    @Override
    public void loadLocationsData() {
        mLocationsApi.getAllLocations(new LocationApi.LocationServiceCallback<List<DBLocations>>() {

            @Override
            public void onSuccess(List<DBLocations> locations) {
                mView.showLocations(locations);
            }

            @Override
            public void onFailure() {
                mView.showConnectionError();
            }
        });

    }

    /**
     * Delete the location from the list
     * @param location location
     * @param position index of the list position
     */

    @Override
    public void deleteLocationData(DBLocations location, final int position) {
        mLocationsApi.deleteLocation(location, new LocationApi.delLocationCallback<DBLocations>() {
            @Override
            public void onSuccess(DBLocations loc) {
                mView.locationRemoveSuccess(loc,position);
            }

            @Override
            public void onFailure() {
                mView.locationRemoveFailure();
            }
        });
    }
}
