package com.android.mapdemo.dialog;

import com.android.mapdemo.roomDB.DBLocations;

/**
 * Created by hcltmac029 on 04/06/18.
 * 7:41 PM
 * Interface implement to show location details
 */
public interface ShowDialogView {
    void showAddLocationSuccess(DBLocations location);
    void showConnectionError();
    void showLocationName(String locationName);
}
