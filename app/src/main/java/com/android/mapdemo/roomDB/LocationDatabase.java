package com.android.mapdemo.roomDB;

/**
 * Created by hcltmac029 on 01/06/18.
 * 4:22 PM
 * Room database creation
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
@Database(entities = {DBLocations.class}, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
