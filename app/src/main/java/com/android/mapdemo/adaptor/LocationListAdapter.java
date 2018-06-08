package com.android.mapdemo.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.mapdemo.R;

import com.android.mapdemo.Utils;
import com.android.mapdemo.roomDB.DBLocations;

import java.util.ArrayList;
import java.util.List;

/**
 * LocationListAdapter is to list the locations
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder> {

    private final Context mContext;
    private final LocationItemListener mListener;
    private List<DBLocations> mList = new ArrayList<>();

    /**
     * LocationListAdapter costructor
     * @param context application context
     * @param listener location item listener
     */

    public LocationListAdapter(Context context, LocationItemListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        DBLocations location = mList.get(position);

        holder.mTvCity.setText(location.getName());
        String distance = Utils.getLocationDistance(mContext, mList.get(0), location);
        holder.mTvDistance.setText("Distance : "+distance+" Km");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Load the location data into the list
     * @param locations Locations from the database
     */
    public void replaceData(List<DBLocations> locations) {
        mList = locations;
        notifyDataSetChanged();
    }

    /**
     * remove the location from the list
     * @param position
     */
    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * Location vie holder to hold the locations from the list
     */
    protected class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //private final TextView mTvTemp;
        private final TextView mTvCity;
        private final TextView mTvDistance;
       //private final TextView mTvWindSpeed;
        private final ImageView mIvDel;

        public LocationViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mTvCity =  itemView.findViewById(R.id.tv_name);
            mTvCity.setOnClickListener(this);
            mIvDel = itemView.findViewById(R.id.iv_del);
            mIvDel.setOnClickListener(this);
            mTvDistance = itemView.findViewById(R.id.tv_distance);


        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            DBLocations location = mList.get(position);
            switch (v.getId()) {
                case R.id.tv_name : {
                    mListener.onLocationEditClick(location);
                    break;
                }
                case R.id.iv_del : {
                    mListener.onLocationDelClick(location,position);
                    break;
                }
            }

        }
    }

    /**
     * Interface to delete and edit the location
     */
    public interface LocationItemListener {
        void onLocationEditClick(DBLocations item);
        void onLocationDelClick(DBLocations item, int position);
    }
}
