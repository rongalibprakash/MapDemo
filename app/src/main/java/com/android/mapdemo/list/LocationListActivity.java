package com.android.mapdemo.list;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.mapdemo.App;
import com.android.mapdemo.R;
import com.android.mapdemo.adaptor.LocationListAdapter;
import com.android.mapdemo.model.service.LocationApiImpl;
import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

/**
 * Created by hcltmac029 on 05/06/18.
 * 7:44 PM
 * LocationListActivity to list out all the location from the DB
 * User can delete the location from the list
 * In the list it is showing the name and distance from the default location
 */
public class LocationListActivity extends AppCompatActivity implements LocationListView ,LocationListAdapter.LocationItemListener{

    private LocationListPresenterImpl mPresenter;
    private List<DBLocations> mLocations;
    private LocationListAdapter mLocationListAdapter;
    private RecyclerView rvLocationList;
    private FusedLocationProviderClient mFusedLocationClient;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_location_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the presenter
        mPresenter = new LocationListPresenterImpl(this,new LocationApiImpl(App.getLocationDB()));
        initListView();
        initSwipeRefreshLayout();
    }

    /**
     * initlizing the list view and the adaptor
     */
    private void initListView() {
        mLocationListAdapter = new LocationListAdapter(this,this);

        rvLocationList = findViewById(R.id.rv_main);
        rvLocationList.setLayoutManager(new LinearLayoutManager(this));
        rvLocationList.setAdapter(mLocationListAdapter);
    }

    /**
     * initlizing the swipe view to refresh the list view when the user swipe down
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO As of now no nee to update so make it false directly
                mSwipeRefreshLayout.setRefreshing(false);;
            }
        });
    }

    /**
     * Back key navigation when the user press the back on the tool bar
     */
    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    /**
     * Load the location data when the application is resume
     */
    @Override
    public void onResume() {
        super.onResume();
       mPresenter.loadLocationsData();


    }


    /**
     * Show the location
     * @param locations DB locations
     */
    @Override
    public void showLocations(List<DBLocations> locations) {
        mLocations = locations;
        mLocationListAdapter.replaceData(locations);

    }

    /**
     * Show the error when the connection error
     */
    @Override
    public void showConnectionError() {
        Toast.makeText(this,R.string.main_error_connection, Toast.LENGTH_SHORT).show();
    }

    /**
     * Call when the location is removed from the DB
     * @param location
     * @param position
     */
    @Override
    public void locationRemoveSuccess(DBLocations location, int position) {
        mLocationListAdapter.notifyItemRemoved(position);
        mLocationListAdapter.removeItem(position);

        Toast.makeText(this, "Location Deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Call when the location delete error occur
     */

    @Override
    public void locationRemoveFailure() {
        Toast.makeText(this, "Location Delete Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationEditClick(DBLocations item) {

    }

    /**
     * call when the delete button clicked on the list
     * @param item
     * @param position
     */
    @Override
    public void onLocationDelClick(DBLocations item, int position) {
        mPresenter.deleteLocationData(item, position);
    }
}
