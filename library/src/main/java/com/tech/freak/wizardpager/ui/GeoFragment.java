package com.tech.freak.wizardpager.ui;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tech.freak.wizardpager.R;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.SimpleLocationListener;

import java.io.IOException;
import java.util.List;

public class GeoFragment extends Fragment implements SimpleLocationListener {

    protected static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private Page mPage;

    private LocationUpdatesHandler mLocationHandler;

    private TextView textViewLocationStatus;
    private TextView textViewLocation;
    private ProgressBar progressBar;

    private Geocoder mGeocoder;

    public static GeoFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        GeoFragment f = new GeoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

        mGeocoder = new Geocoder(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_geo, container,
                false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage
                .getTitle());

        textViewLocationStatus = (TextView) rootView
                .findViewById(R.id.textViewLocationStatus);
        textViewLocation = (TextView) rootView
                .findViewById(R.id.textViewLocation);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        String currentData = mPage.getData().getString(Page.SIMPLE_DATA_KEY);

        if (!TextUtils.isEmpty(currentData)) {
            textViewLocationStatus.setText(R.string.geo_status_found);
            textViewLocation.setText(currentData);
            progressBar.setVisibility(View.GONE);
        } else {
            textViewLocationStatus.setText(R.string.geo_status_searching);
            textViewLocation.setText("");
            progressBar.setVisibility(View.VISIBLE);
        }

        // Start location updates
        if (mLocationHandler != null) {
            mLocationHandler.setLocationListener(this);
            mLocationHandler.startLocationUpdates();
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException(
                    "Activity must implement PageFragmentCallbacks");
        }
        mCallbacks = (PageFragmentCallbacks) activity;

        if (!(activity instanceof LocationUpdatesHandler)) {
            throw new ClassCastException(
                    "Activity must implement LocationUpdatesHandler");
        }
        mLocationHandler = (LocationUpdatesHandler) activity;
    }

    @Override
    public void onDetach() {
        if (mLocationHandler != null) {
            mLocationHandler.stopLocationUpdates();
            mLocationHandler.setLocationListener(null);
        }
        mCallbacks = null;
        mLocationHandler = null;
        super.onDetach();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            String locationString = location.getLatitude() + ","
                    + location.getLongitude();
            mPage.getData().putString(Page.SIMPLE_DATA_KEY, locationString);
            mPage.notifyDataChanged();
            stopLocationUpdates(location);

        }
    }

    private void stopLocationUpdates(final Location location) {
        if (mLocationHandler != null) {
            mLocationHandler.stopLocationUpdates();
            mLocationHandler.setLocationListener(null);
        }
        textViewLocationStatus.setText(R.string.geo_status_found);
        updateLocationLabel(location.getLatitude() + ","
                + location.getLongitude());

    }

    private void updateLocationLabel(final String locationString) {
        String[] coordinateStrings = locationString.split(",");
        final double latitude = Double.parseDouble(coordinateStrings[0]);
        final double longitude = Double.parseDouble(coordinateStrings[1]);

        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            ;

            @Override
            protected String doInBackground(Void... params) {
                try {
                    List<Address> locationList = mGeocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (locationList != null && locationList.size() > 0) {
                        Address address = locationList.get(0);

                        return address.getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (!TextUtils.isEmpty(result)) {
                    textViewLocation.setText(getString(
                            R.string.geo_status_location, result,
                            locationString));
                } else {
                    textViewLocation.setText(latitude + "," + longitude);
                }

                progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }

    public interface LocationUpdatesHandler {
        public void setLocationListener(SimpleLocationListener locationListener);

        public void startLocationUpdates();

        public void stopLocationUpdates();
    }

}
