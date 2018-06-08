package com.android.mapdemo.roomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by hcltmac029 on 01/06/18.
 * 4:11 PM
 */
@Entity
public class DBLocations {

    @NonNull
    public int getId() {
        return id;
    }


    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Double latitude;
    private Double longitude;

    public DBLocations() { }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

