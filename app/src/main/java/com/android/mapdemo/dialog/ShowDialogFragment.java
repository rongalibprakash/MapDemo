package com.android.mapdemo.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mapdemo.App;
import com.android.mapdemo.R;
import com.android.mapdemo.adaptor.LocationListAdapter;
import com.android.mapdemo.map.MapPresenterImpl;
import com.android.mapdemo.model.service.LocationApiImpl;
import com.android.mapdemo.roomDB.DBLocations;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hcltmac029 on 04/06/18.
 * 6:47 PM
 * ShowDialogFragment is to show the dialog when the user touch and hold on the map
 * It will show the location name with country and location
 * User can save this location into the database
 * User can edit the default name with custom name
 * The location name should not be the empty
 */
public class ShowDialogFragment extends DialogFragment implements ShowDialogView {

    private LatLng mLatLng;
    private ShowDialogPresenterImpl mPresenter;
    private MapPresenterImpl mMaPresenter;
    private String mLocationName;
    private TextView mEtName;
    private TextView mTvError;
    public InterfaceCommunicator interfaceCommunicator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interfaceCommunicator = (InterfaceCommunicator)getActivity();
        if (getArguments() != null) {
            mLatLng = getArguments().getParcelable("longLat");

        }
        mPresenter = new ShowDialogPresenterImpl(getActivity(),this, mLatLng, new LocationApiImpl(App.getLocationDB()));
        mPresenter.loadLocationData();

    }

    /**
     * inflate the dialog layout
     * @param inflater layout inflater
     * @param container viee grou container
     * @param savedInstanceState saved instances state
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container);
    }

    /**
     *
     * Display the popup message with location
     * user can save oe cancel the data
     *
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtName = view.findViewById(R.id.et_name);
        mEtName.setText(mLocationName);
        mTvError = view.findViewById(R.id.tv_error_msg);
        Button mBtSave = view.findViewById(R.id.bt_save);
        Button mBtCancel = view.findViewById(R.id.bt_cancel);
        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEtName.getText().toString() != null && !mEtName.getText().toString().equals("")) {
                    mPresenter.addLocation(mEtName.getText().toString(), mLatLng);
                    dismissDialog();
                } else {
                    mTvError.setVisibility(View.VISIBLE);
                }

            }


        });
        // Buttion click listener to cancel the popup
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

    }

    /**
     * Dismiss the dialog when the cancel is pressed from the popup
     */
    private void dismissDialog() {
        getDialog().dismiss();
    }

    /**
     * Call when the location is saved successfully into the DB
     * @param location saved location
     */
    @Override
    public void showAddLocationSuccess(DBLocations location) {
        interfaceCommunicator.sendRequestCode(location);

    }

    /**
     * Show the error message when the connection error occurred
     */
    @Override
    public void showConnectionError() {

    }

    @Override
    public void showLocationName(String locationName) {
        mLocationName = locationName;
    }

    /**
     * Set the layout size when the activity is resume
     */
    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    /**
     * Interface to send the request code to the activity to update the map location
     */
    public interface InterfaceCommunicator {
        void sendRequestCode(DBLocations location);
    }

}
