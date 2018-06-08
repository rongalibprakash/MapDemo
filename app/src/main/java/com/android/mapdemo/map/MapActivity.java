package com.android.mapdemo.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.mapdemo.App;
import com.android.mapdemo.R;
import com.android.mapdemo.dialog.ShowDialogFragment;
import com.android.mapdemo.list.LocationListActivity;
import com.android.mapdemo.model.service.LocationApiImpl;
import com.android.mapdemo.roomDB.DBLocations;
import com.android.mapdemo.roomDB.LocationDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * MapActivity to display the map with the default and custom location
 * User can add the location by press and hold on the map
 * User can edit the default location
 * The location name should not be the empty
 */

public  class MapActivity extends AppCompatActivity implements MapView, OnMapReadyCallback , GoogleMap.OnMapLongClickListener, ShowDialogFragment.InterfaceCommunicator {

    private GoogleMap mMap;
    private MapPresenterImpl mPresenter;
    private List<DBLocations> mLocations;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ShowDialogFragment dialogFragment;


    /**
     * create the presenter implemantaion
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mPresenter = new MapPresenterImpl(this, new LocationApiImpl(App.getLocationDB()));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Load the location data when the application is resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadLocationsData();

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        showMarkers();

    }

    /**
     * call when the long click on the map
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        mPresenter.showNewLocationPopup(latLng);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                Intent intent = new Intent(this, LocationListActivity.class);
                startActivity(intent);

                break;
        }
        return true;
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void removeLocations() {

    }

    /**
     * call when the location data is received from the DB
     * @param locations
     */
    @Override
    public void showLocations(List<DBLocations> locations) {
        Log.d("MainActivity","Locations list::"+locations.size());
        this.mLocations = locations;
        showMarkers();


    }

    /**
     * Show the markers on the map with given locations
     */
    private void showMarkers() {
        if(mMap != null && mLocations != null ) {
            mMap.clear();
            for (DBLocations location : mLocations) {
                addMarkerToMap(location);

            }
        }
    }

    /**
     * Add the markers into the map and move the camera into that location
     * @param location
     */
    private void addMarkerToMap(DBLocations location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(position).title(location.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    /**
     * Call when the connection error
     */

    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.main_error_connection, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showLocationPopup(LatLng latLng) {
         showDialog(latLng);

    }

    /**
     * Call the dialog when the user long press on the map
     * @param newMarker marker
     */
    private void showDialog(final LatLng newMarker) {

        dialogFragment = new ShowDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("longLat", newMarker);
        // Show DialogFragment
        dialogFragment.setArguments(args);
        dialogFragment.show(fragmentManager, "New Location");
    }

    /**
     * Call when the new location is added
     * @param location location
     */
    @Override
    public void sendRequestCode(DBLocations location) {
        addMarkerToMap(location);
    }
}
