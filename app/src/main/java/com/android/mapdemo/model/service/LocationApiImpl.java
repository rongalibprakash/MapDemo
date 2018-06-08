package com.android.mapdemo.model.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.mapdemo.App;
import com.android.mapdemo.roomDB.DBLocations;
import com.android.mapdemo.roomDB.LocationDatabase;
import com.google.android.gms.maps.model.LatLng;

import android.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcltmac029 on 31/05/18.
 * 8:00 PM
 * Location implementation to read adn delete the location from the DB
 */
public class LocationApiImpl implements LocationApi, LocationListener {

    protected final static String DEFAULT_LOCATIONS_URL = "https://s3-ap-southeast-2.amazonaws.com/com-cochlear-sabretooth-takehometest/locations.json";
    private LocationDatabase mLocationDatabase;
    private DBLocations dbLocations;
    protected LocationManager locationManager;
    private  readLocationCallback mReadCallback;
    private String mProvider;

    private List<DBLocations> mLocations = new ArrayList<>();

    public LocationApiImpl(LocationDatabase locationDatabase) {
        mLocationDatabase = locationDatabase;

    }


    @Override
    public List<DBLocations> getAllLocations(LocationServiceCallback<List<DBLocations>> callback) {
        //

        new LoadLocationAsyncTask(callback).execute();

        return null;
    }

    /**
     * Add the new location
     * @param name name of the location
     * @param latLng latlang of the location
     * @param callback  callback of the addlocation
     */
    @Override
    public void addNewLocation(String name, LatLng latLng, addLocationCallback callback) {
        new addLocationAsyncTask(name, latLng, callback).execute();
    }

    /**
     * Delete the location from the DB
     * @param dbLocations index of the loation list
     * @param delCallback delete callback
     */
    @Override
    public void deleteLocation(DBLocations dbLocations, delLocationCallback delCallback) {
        new delLocationAsyncTask(dbLocations, delCallback).execute();
    }

    /**
     * Read the current location
     * @param context context
     * @param readCallback call back
     */
    @Override
    public void readCurrentLocation(Context context, readLocationCallback readCallback) {
        mReadCallback = readCallback;
        readLocation(context);
    }

    /**
     * Read the current location
     * @param context context
     * @return location
     */
    private Location readLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = locationManager.getBestProvider(criteria, false);


        if (mProvider != null && !mProvider.equals("")) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
           locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
        /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }*/
        return null;
    }

    /**
     * Call when the location changed
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        if(location != null) {
            mReadCallback.onSuccess(location);
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     * AsyncTask to read the data
     * Is the datais available in the local it will read from the database
     * if the data is not available it will read the default locations from the given url
     * All The data will store in the locally with RoomDB
     */
    private class LoadLocationAsyncTask extends AsyncTask<Void, Void, List<DBLocations>> {
        private final LocationServiceCallback mCallback;

        public LoadLocationAsyncTask(LocationServiceCallback callback) {
            mCallback = callback;

        }

        @Override
        protected List<DBLocations> doInBackground(Void... params) {
            try {
                List<DBLocations> locations = App.getInstance().getLocationDB().daoAccess().getAllLocations();
                if(locations != null && locations.size() > 0 ){
                    Log.d("Impl","Read from DB");
                    return locations;
                } else {
                    Log.d("Impl","Read from url");
                    URL url = new URL(DEFAULT_LOCATIONS_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    StringBuilder content = new StringBuilder();
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;

                    String line;
                    while ((line = in.readLine()) != null) {
                        content.append(line);
                    }

                    jsonObject = new JSONObject(content.toString());
                    jsonArray = jsonObject.getJSONArray("locations");

                    JSONObject object;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        dbLocations = new DBLocations();
                        object = jsonArray.getJSONObject(i);
                        dbLocations.setName(object.getString("name"));
                        dbLocations.setLatitude(Double.parseDouble(object.getString("lat")));
                        dbLocations.setLongitude(Double.parseDouble(object.getString("lng")));
                        mLocationDatabase.daoAccess().insertOnlySingleLocation(dbLocations);
                        //locations.add(new Location(object.getString("name"), object.getString("lat"), object.getString("lng")));
                        mLocations.add(dbLocations);
                    }

                    return mLocations;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DBLocations> locations) {

            if (locations != null) {
                mCallback.onSuccess(locations);
            } else {
                mCallback.onFailure();
            }
        }
    }

    /**
     * Ann the new location into the database
     */
    private class addLocationAsyncTask extends AsyncTask<Void, Void, DBLocations> {
        private final addLocationCallback mCallback;
        private LatLng mLatLng;
        private String mName;

        public addLocationAsyncTask(String name, LatLng latLng, addLocationCallback callback) {
            mCallback = callback;
            mLatLng = latLng;
            mName = name;
        }

        @Override
        protected DBLocations doInBackground(Void... params) {
            dbLocations = new DBLocations();

            dbLocations.setName(mName);
            dbLocations.setLatitude(mLatLng.latitude);
            dbLocations.setLongitude(mLatLng.longitude);
            mLocationDatabase.daoAccess().insertOnlySingleLocation(dbLocations);

            return dbLocations;
        }

        @Override
        protected void onPostExecute(DBLocations locations) {

            if (locations != null) {
                mCallback.onSuccess(locations);
            } else {
                mCallback.onFailure();
            }
        }
    }

    /**
     * Delete the location from the DB
     */
    private class delLocationAsyncTask extends AsyncTask<Void, Void, DBLocations> {
        private final delLocationCallback mCallback;
        private  DBLocations mDbLocations;


        public delLocationAsyncTask(DBLocations dbLocations, delLocationCallback callback) {
            mCallback = callback;
            mDbLocations = dbLocations;
        }

        @Override
        protected DBLocations doInBackground(Void... params) {

            mLocationDatabase.daoAccess().deleteLocation(mDbLocations.getName());

            return mDbLocations;
        }

        @Override
        protected void onPostExecute(DBLocations locations) {

            if (locations != null) {
                mCallback.onSuccess(locations);
            } else {
                mCallback.onFailure();
            }
        }
    }
}
