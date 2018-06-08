package com.android.mapdemo;

import android.Manifest;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.android.mapdemo.roomDB.LocationDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * Created by hcltmac029 on 01/06/18.
 * 5:11 PM
 * Application class
 * Load the location database
 */
public class App extends Application {
    public static App app;
    private static final String DATABASE_NAME = "locations_db";

    private static LocationDatabase locationDatabase;
    private FusedLocationProviderClient mFusedLocationClient;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        locationDatabase = Room.databaseBuilder(getApplicationContext(),
                LocationDatabase.class, DATABASE_NAME)
                .build();
        app = this;
    }


    // get the location data base
    public static LocationDatabase getLocationDB() {
        return locationDatabase;
    }

}
