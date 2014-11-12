package com.thirdi.sensorsupervisor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GPSFragment extends Fragment implements LocationListener {
	/**
	 * Shows GPS data. And let's you control it.
	 * 
	 * mLatitudeView, mLongitudeView, mAccuracyView : Shows location data as
	 * text. mLocationManager : Location manager to retrieve location. mProvider
	 * : String that holds the current provider. mLocationButton : Button that
	 * registers GPS.
	 */
	private TextView mLatitudeView, mLongitudeView, mAccuracyView;
	private LocationManager mLocationManager;
	private String mProvider;
	private Button mLocationButton, mLocationStopButton;
	private Button mMaps;

	public GPSFragment() {
		// Required empty public constructor.
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment.
		return inflater.inflate(R.layout.fragment_gps, container, false);

	}

	@Override
	public void onStart() {
		super.onStart();
		// Initialize member variables.
		mLatitudeView = (TextView) getView().findViewById(R.id.latitude);
		mLongitudeView = (TextView) getView().findViewById(R.id.longitude);
		mAccuracyView = (TextView) getView().findViewById(R.id.accuracy);
		mLocationButton = (Button) getView().findViewById(R.id.locationButton);
		mMaps = (Button) getView().findViewById(R.id.btnMap);
		mLocationManager = (LocationManager) getActivity().getBaseContext()
				.getSystemService(Context.LOCATION_SERVICE);
		mLocationStopButton = (Button) getView().findViewById(R.id.locationStopButton);
		
		mLocationStopButton.setClickable(false);
		
		// Set a click listener to our location button.
		mMaps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getView().getContext(), maps.class);
				startActivity(myIntent);

			}
		});

		mLocationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// This method is being invoked whenever you click the button.
				registerGPS();
				mLocationStopButton.setClickable(true);
				mLocationButton.setClickable(false);
			}
		});
		
		mLocationStopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Removes status listener.
				mLocationManager.removeUpdates(GPSFragment.this);
				mLocationStopButton.setClickable(false);
				mLocationButton.setClickable(true);
			}
		});
	}

	private boolean registerGPS() {
		// Registers GPS if there is a viable GPS provider. Otherwise shows no
		// data.
		// True for success, false for failure.
		Location location;

		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Provider is present, continue registering. Use default criteria
			// to select provider.
			mProvider = LocationManager.GPS_PROVIDER;
			
			//Never-ending location updates, yay.
			mLocationManager.requestLocationUpdates(mProvider, 0, 0, this);
			
			// Get last known location from said provider.
			location = mLocationManager.getLastKnownLocation(mProvider);

			// If we have a location data
			if (location != null) {
				Toast.makeText(getActivity().getBaseContext(), "Provider "
						+ mProvider + " has been selected.", Toast.LENGTH_SHORT);
				onLocationChanged(location);
				return true;
			} else {
				mLatitudeView.setText("No data.");
				mLongitudeView.setText("No data.");
				mAccuracyView.setText("No data.");
			}
		} else {
			Toast.makeText(getActivity().getBaseContext(),
					"Please enable location services.", Toast.LENGTH_SHORT);
			if (!openLocationSettings()) {
				Toast.makeText(getActivity().getBaseContext(),
						"Oh snap! Can't open location settings.",
						Toast.LENGTH_SHORT);
			}
		}

		return false;
	}

	private boolean openLocationSettings() {
		// Checks whether a settings program for location settings is present,
		// if there is attempts to open it.
		final Intent intent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);

		if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// This method is being invoked whenever your location changes.

		// Show new location.
		double latitude, longitude, accuracy;
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		accuracy = location.getAccuracy();

		mLatitudeView.setText(String.valueOf(latitude));
		mLongitudeView.setText(String.valueOf(longitude));
		mAccuracyView.setTag(String.valueOf(accuracy));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// This method is being invoked whenever your provider becomes disabled.

		Toast.makeText(getActivity().getBaseContext(), "Disabled provider: "
				+ provider, Toast.LENGTH_SHORT);

	}

	@Override
	public void onProviderEnabled(String provider) {
		// This method is being invoked whenever your provider becomes enabled.

		Toast.makeText(getActivity().getBaseContext(), "Enabled new provider: "
				+ provider, Toast.LENGTH_SHORT);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// This method is being invoked whenever the provider status changes.
	}
}
