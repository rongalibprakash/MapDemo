package com.android.mapdemo.map;

import com.android.mapdemo.model.service.LocationApi;
import com.android.mapdemo.model.service.LocationApiImpl;
import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by hcltmac029 on 31/05/18.
 * 7:43 PM
 * MapPresenterImpl to read the location data from the DB
 */
public class MapPresenterImpl implements MapPresenter {

    private final MapView mView;
    private final LocationApiImpl mLocationsApi;

    public MapPresenterImpl(MapView mView, LocationApiImpl mLocationsApi) {
        this.mView = mView;
        this.mLocationsApi = mLocationsApi;
    }

    /**
     * Load the location data
     */
    @Override
    public void loadLocationsData() {

        mView.showProgress();

        mLocationsApi.getAllLocations(new LocationApi.LocationServiceCallback<List<DBLocations>>() {

            @Override
            public void onSuccess(List<DBLocations> locations) {
                mView.hideProgress();
                mView.removeLocations();
                mView.showLocations(locations);
            }

            @Override
            public void onFailure() {
                mView.showConnectionError();
                mView.hideProgress();
            }
        });

    }

    /**
     * Show the popup
     * @param latLng
     */
    @Override
    public void showNewLocationPopup(LatLng latLng) {
        mView.showLocationPopup(latLng);
    }


}
