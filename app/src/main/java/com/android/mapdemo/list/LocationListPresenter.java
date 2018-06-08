package com.android.mapdemo.list;

import android.content.Context;

import com.android.mapdemo.roomDB.DBLocations;

/**
 * Created by hcltmac029 on 05/06/18.
 * 7:45 PM
 * interface to implement in the implementation
 */
interface LocationListPresenter {
    void loadLocationsData();
    void deleteLocationData(DBLocations location, int position);

}
