package com.android.mapdemo.roomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by hcltmac029 on 01/06/18.
 * 4:15 PM
 * Interface to implement the query to RoomDB
 */
@Dao
public interface DaoAccess {

    //Insert the single location into the database
    @Insert
    void insertOnlySingleLocation (DBLocations locations);

    //Insert the multiple location into the DB
    @Insert
    void insertMultipleLocations (List<DBLocations> locationsList);

    //Get the record with name
    @Query("SELECT * FROM DBLocations WHERE name = :name")
    DBLocations fetchOneLocatinbyName (String name);

    //Read all the records from the DB
    @Query("SELECT * FROM DBLocations")
    List<DBLocations> getAllLocations();

    //Update the location
    @Update
    void updateLocation (DBLocations locations);

    //Delete the record from the DB
    @Query("Delete FROM DBLocations WHERE name LIKE  :name")
    void deleteLocation (String name);
}
