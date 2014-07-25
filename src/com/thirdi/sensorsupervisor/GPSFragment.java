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
	 * mLatitudeView, mLongitudeView, mAccuracyView : Shows location data as text.
	 * mLocationManager : Location manager to retrieve location.
	 * mProvider : String that holds the current provider.
	 * mLocationButton : Button that registers GPS.
	 */
	private TextView mLatitudeView, mLongitudeView, mAccuracyView;
	private LocationManager mLocationManager;
	private String mProvider;
	private Button mLocationButton;
	
	public GPSFragment() {
		//Required empty public constructor.
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Inflate the layout for this fragment.
		return inflater.inflate(R.layout.fragment_gps,  container, false);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mLatitudeView = (TextView) getView().findViewById(R.id.latitude);
		mLongitudeView = (TextView) getView().findViewById(R.id.longitude);
		mAccuracyView = (TextView) getView().findViewById(R.id.accuracy);
		mLocationButton = (Button) getView().findViewById(R.id.locationButton);
		mLocationManager = (LocationManager) getActivity().getBaseContext()
				.getSystemService(Context.LOCATION_SERVICE);
		
		mLocationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				registerGPS();
				
			}
		});
	}
	
	private boolean registerGPS() {
		//Registers GPS if there is a viable GPS provider. Otherwise shows no data.
		//True for success, false for failure.
		Location location;
		
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			//Provider is present, continue registering. Use default criteria to select provider.
			mProvider = mLocationManager.getBestProvider(new Criteria(), false);
			location = mLocationManager.getLastKnownLocation(mProvider);
			
			if (location != null) {
				Toast.makeText(getActivity().getBaseContext() ,
						"Provider " + mProvider + " has been selected.", Toast.LENGTH_SHORT);
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
						"Oh snap! Can't open location settings.", Toast.LENGTH_SHORT);
			}
		}
		
		return false;
	}
	
	private boolean openLocationSettings() {
		//Checks whether a settings program for location settings is present, if there is attempts to open it.
		final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		
		if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		//Location changed. Show it.
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
		Toast.makeText(getActivity().getBaseContext(), "Disabled provider: " + 
				provider, Toast.LENGTH_SHORT);
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getActivity().getBaseContext(), "Enabled new provider: " + 
				provider, Toast.LENGTH_SHORT);
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}
