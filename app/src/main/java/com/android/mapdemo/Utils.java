package com.android.mapdemo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by hcltmac029 on 05/06/18.
 * 3:19 PM
 */
public class Utils {
    private static Geocoder mGeocoder;
    private static List<Address> mAddress;
    private static String mCity;
    private static String mCountry;

    /**
     * Get the location by given latlng point
     * @param context context
     * @param mLatLng LatLng point
     * @return  location name
     * @throws IOException null pointer exception
     */
    public static String getLocationName(Context context, LatLng mLatLng) throws IOException {
        mGeocoder = new Geocoder(context, Locale.getDefault());

        mAddress = mGeocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude , 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        mCity = null; mCountry = null;
        if(mAddress != null && mAddress.size() > 0) {
            mCity = mAddress.get(0).getLocality();
            mCountry = mAddress.get(0).getCountryName();

        }
        return ((mCountry != null)? ((mCity != null)? mCountry+"-"+mCity : mCountry) : "");

    }

    /**
     * Get the distance to given two locations
     * @param mContext context
     * @param fromLocation location Latitude, longitude point
     * @param toLocation location Latitude, longitude point
     * @return distance
     */
    public static String getLocationDistance(Context mContext, DBLocations fromLocation, DBLocations toLocation) {
        LatLng latLngFrom = new LatLng(fromLocation.getLatitude(),fromLocation.getLongitude());
        LatLng latLngTo = new LatLng(toLocation.getLatitude(),toLocation.getLongitude());
        DecimalFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(SphericalUtil.computeDistanceBetween(latLngFrom, latLngTo)* 0.001);
    }
}
