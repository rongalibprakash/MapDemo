package com.android.mapdemo.list;

import android.location.Location;

import com.android.mapdemo.roomDB.DBLocations;

import java.util.List;

/**
 * Created by hcltmac029 on 05/06/18.
 * 7:44 PM
 * interface to implement the show and delete the location calls
 */
public interface LocationListView {
    void showLocations(List<DBLocations> locations);
    void showConnectionError();
    void locationRemoveSuccess(DBLocations location, int position);
    void locationRemoveFailure();

}
