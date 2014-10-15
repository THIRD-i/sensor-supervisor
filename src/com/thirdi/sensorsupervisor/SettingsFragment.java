package com.thirdi.sensorsupervisor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
	/**
	 * Shows settings of your app like showing camera, where to save data etc.
	 * 
	 * mDataSpinner : Used to control where to save sensor data. mAudioSpinner :
	 * Used to control audio recorder's sample rate. mDataSwitch : Used to
	 * control whether we update UI or not when we receive sensor data.
	 * mCameraSwitch : Used to control whether we show camera data or not.
	 */
	private Spinner mDataSpinner;
	private Spinner mAudioSpinner;
	private Switch mDataSwitch;
	private Switch mCameraSwitch;
	private SeekBar mFPSSeekbar;
	private TextView mFPSTextView;

	public SettingsFragment() {
		// Required empty constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// This method is being invoked to create the view. (What this method
		// returns, is set as the view.)

		// Inflate the layout and store it in a view variable.(Which we will
		// return after we do necessary stuff.)
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		// Initialize member variables.(Which are view elements.)
		mDataSpinner = (Spinner) view.findViewById(R.id.data_spinner);
		mAudioSpinner = (Spinner) view.findViewById(R.id.audio_spinner);
		mDataSwitch = (Switch) view.findViewById(R.id.data_switch);
		mCameraSwitch = (Switch) view.findViewById(R.id.camera_switch);
		mFPSSeekbar = (SeekBar) view.findViewById(R.id.videoFPSBar);
		mFPSTextView = (TextView) view.findViewById(R.id.textViewFPS);
		// Set the default state of switches based on the previous choices.
		mCameraSwitch.setChecked(CameraPreview.SHOW_CAMERA);
		mDataSwitch.setChecked(SensorFragment.SHOW_DATA);
		// Create an array adapter, take available save location options with it
		// and adapt it to the spinner.
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter
				.createFromResource(view.getContext(), R.array.data_array,
						android.R.layout.simple_spinner_dropdown_item);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDataSpinner.setAdapter(dataAdapter);
		// Create an array adapter for available audio sample rates.
		ArrayAdapter<CharSequence> audioAdapter = ArrayAdapter
				.createFromResource(view.getContext(), R.array.audio_array,
						android.R.layout.simple_spinner_dropdown_item);
		audioAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAudioSpinner.setAdapter(audioAdapter);
		// Set an item selection listener for data spinner.
		mDataSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						//TODO: This has some sort of a problem, check it and fix it.
						// This method is being invoked whenever we select an
						// item from the spinner.

						// Depending on the item, set the save location
						// accordingly.
	
						switch (position) {
						case 0:
							SensorFragment.SAVE_LOCATION = "Database";
							break;
						case 1:
							SensorFragment.SAVE_LOCATION = "File";
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// This method is being invoked when we select nothing
						// from the spinner.
					}
				});
		// Set an item selection listener for audio spinner.
		mAudioSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// This method is being invoked whenever we select an
						// item from the spinner.

						// Depending on the item, set audio recording sample
						// rate accordingly.
						switch (position) {
						case 0:
							AudioFragment.RECORDER_SAMPLERATE = 8000;
							break;
						case 1:
							AudioFragment.RECORDER_SAMPLERATE = 11025;
							break;
						case 2:
							AudioFragment.RECORDER_SAMPLERATE = 16000;
							break;
						case 3:
							AudioFragment.RECORDER_SAMPLERATE = 22050;
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// This method is being invoked when we select nothing
						// from the spinner.
					}
				});

		mFPSSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				mFPSTextView.setText("FPS: " + arg1);
				CameraFragment.RECORDER_FPS = arg1;
			}
		});
		
		// Set a listener for our data switch to control whether we show data or
		// not.
		mDataSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// This method is being invoked whenever checked status of
				// switch changes.(i.e. you touch it)

				// Set the SHOW_DATA to checked status of our switch.
				SensorFragment.SHOW_DATA = isChecked;
			}
		});

		// Set a listener for our camera switch to control whether we show
		// camera preview or not.
		mCameraSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// This method is being invoked whenever checked status of
				// switch changes.(i.e. you touch it)

				// Set the SHOW_CAMERA to checked status of our switch.
				CameraPreview.SHOW_CAMERA = isChecked;
			}
		});

		// We have set everything needed on our view, now return it.
		return view;
	}
}