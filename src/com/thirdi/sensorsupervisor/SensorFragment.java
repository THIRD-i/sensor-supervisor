package com.thirdi.sensorsupervisor;

import java.util.List;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SensorFragment extends Fragment {
	
	/**
	 * Shows a list of available sensors and data retrieved from them. Also allows you to control them.
	 * 
	 * SAVE_LOCATION: Where to save sensor data. Valid values are "Database" and "File".
	 * SHOW_DATA: Whether or not the sensor data will be shown on UI.
	 * 
	 * mInflater: LayoutInflater to inflate views.
	 * mSensorManager: Sensor manager, to register/unregister listeners and retrieve sensor list.
	 * mSensorContainer: LinearLayout that will contain every single sensor row in it.
	 */

	protected static String SAVE_LOCATION = "Database";
	protected static boolean SHOW_DATA = true;
	
	private LayoutInflater mInflater;
	private SensorManager mSensorManager;
	private LinearLayout mSensorContainer;
	private DBHelper mDBHelper;
	
	/**
	 * A holder class that holds view elements, sensor and listener.
	 * Will be set as the tag of the view, so that it can be retrieved when needed.
	 * 
	 * progressbar_x, progressbar_y, progressbar_z : View elements that will show the current sensor's absolute value in a bar.
	 * textview_x, textview_y, textview_z : View elements that will show the current sensor's value as text.
	 * textview_accuracy : View element that will show current sensor's accuracy.
	 * textview_rate : View element that will show the current rate. Needed in order to be able to use seekbar better as well.
	 * switch_sensor : Switch that starts/stops sensorListener.
	 * seekbar_sensor : SeekBar that sets the rate for sensorListener.
	 * sensorListener : SensorEventListener that listens to sensor.
	 * sensorRate : Current sensor's rate.
	 * sensor : Sensor to be displayed, listened to etc.
	 */
	
	private static class Holder {
		// A holder class. Holds items of the row it belongs to.
		// Stored as the tag of that same view. Provides easy access.
		// If used with a listview, also makes UI more responsive.
		private ProgressBar progressbar_x, progressbar_y, progressbar_z;
		private TextView textview_x, textview_y, textview_z, textview_accuracy,
			textview_rate;
		private Switch switch_sensor;
		private SeekBar seekbar_sensor;
		private SensorEventListener sensorListener;
		private int sensorRate = SensorManager.SENSOR_DELAY_FASTEST;
		private Sensor sensor;
	}
	
	public SensorFragment() {
		//Required empty constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Initialize member variables
		mInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSensorManager = (SensorManager) getActivity()
				.getSystemService(Context.SENSOR_SERVICE);
		mDBHelper = new DBHelper(getActivity().getBaseContext());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mInflater.inflate(R.layout.fragment_sensor, container, false);
	}
	
	/**
	 * onStart method that is being called after onResume or onCrate.
	 * 
	 * sensorsList : List of available sensors.
	 * holder : Holder to hold currentRow's elements.
	 * currentRow : Row to be populated. (We are populating our view row-by-row.)
	 */
	
	@Override
	public void onStart() {
		super.onStart();

		//Get database in write mode
		final SQLiteDatabase database = mDBHelper.getWritableDatabase();
		
		//Retrieve the list of available sensors
		List<Sensor> sensorsList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		//Initialize our container(a linear layout which we will add sensors row-by-row)
		mSensorContainer = (LinearLayout) getView().findViewById(R.id.sensor_container);
		
		//Check whether or not we have populated this list before
		if (mSensorContainer.getChildCount() == 0) {
						
			for (Sensor sensor : sensorsList) {
				//If sensor type is one of the default ones, add it. Otherwise ignore.(They usually don't abide by the standards.)
				switch (sensor.getType()) {
		            case Sensor.TYPE_ACCELEROMETER:
		            case Sensor.TYPE_AMBIENT_TEMPERATURE:
		            case Sensor.TYPE_GRAVITY:
		            case Sensor.TYPE_GYROSCOPE:
		            case Sensor.TYPE_LIGHT:
		            case Sensor.TYPE_LINEAR_ACCELERATION:
		            case Sensor.TYPE_MAGNETIC_FIELD:
		            case Sensor.TYPE_PRESSURE:
		            case Sensor.TYPE_PROXIMITY:
		            case Sensor.TYPE_RELATIVE_HUMIDITY:
		            case Sensor.TYPE_ROTATION_VECTOR:
		            case Sensor.TYPE_ORIENTATION:
						final Holder holder;
						View currentRow;
						
						currentRow = mInflater.inflate(R.layout.sensor_list_item, null);
						holder = new Holder();
						
						//Initialize elements of this row's holder(Which includes our SensorEventListener as well.)
						holder.progressbar_x = (ProgressBar) currentRow.findViewById(R.id.progressbar_x);
						holder.progressbar_y = (ProgressBar) currentRow.findViewById(R.id.progressbar_y);
						holder.progressbar_z = (ProgressBar) currentRow.findViewById(R.id.progressbar_z);
						holder.textview_x = (TextView) currentRow.findViewById(R.id.textview_x);
						holder.textview_y = (TextView) currentRow.findViewById(R.id.textview_y);
						holder.textview_z = (TextView) currentRow.findViewById(R.id.textview_z);
						holder.textview_accuracy = (TextView) currentRow.findViewById(R.id.textview_accuracy);
						holder.textview_rate = (TextView) currentRow.findViewById(R.id.textview_rate);
						holder.switch_sensor = (Switch) currentRow.findViewById(R.id.switch_sensor);
						holder.seekbar_sensor = (SeekBar) currentRow.findViewById(R.id.seekbar_sensor);
						holder.sensor = sensor;
						holder.sensorListener = new SensorEventListener() {
							
							@Override
							public void onSensorChanged(SensorEvent sensorEvent) {
								//This method is being invoked when sensor produces a new output.
								
								//If SHOW_DATA option is selected, update UI.
								if (SHOW_DATA) {
									holder.progressbar_x.setProgress((int) Math.abs(sensorEvent.values[0] * 10));
									holder.progressbar_y.setProgress((int) Math.abs(sensorEvent.values[1] * 10));
									holder.progressbar_z.setProgress((int) Math.abs(sensorEvent.values[2] * 10));
									
									holder.textview_x.setText("X: " + String.valueOf(sensorEvent.values[0]));
									holder.textview_y.setText("Y: " + String.valueOf(sensorEvent.values[1]));
									holder.textview_z.setText("Z: " + String.valueOf(sensorEvent.values[2]));
								}
								//Save data according to the value of SAVE_LOCATION.
								if (SAVE_LOCATION == "Database") {
									new Thread(new Runnable() {
									//Create a new map of values where column names are keys
									ContentValues values = new ContentValues();
									values.put(DBContract.DBEntry.COLUMN_NAME_SENSOR_ID, holder.sensor.getType());
									values.put(DBContract.DBEntry.COLUMN_NAME_SENSOR_NAME, holder.sensor.getName());
									values.put(DBContract.DBEntry.COLUMN_NAME_VALUE_X, sensorEvent.values[0]);
									values.put(DBContract.DBEntry.COLUMN_NAME_VALUE_Y, sensorEvent.values[1]);
									values.put(DBContract.DBEntry.COLUMN_NAME_VALUE_Z, sensorEvent.values[2]);
									//Insert the new row, returning the primary key value of the new row
									long newRowId;
									newRowId = database.insert(DBContract.DBEntry.TABLE_NAME, null, values);
									Log.d(getTag(), String.valueOf(newRowId));
									}).start();
								} else if (SAVE_LOCATION == "File") {
									//TODO: ADD FILE SAVE OPTION.
								}
							}
							
							@Override
							public void onAccuracyChanged(Sensor sensor, int accuracy) {
								//This method is being invoked whenever your sensor's accuracy changes.

								//Show accuracy change if SHOW_DATA option is selected.
								if (SHOW_DATA) {
									switch (accuracy) {
									case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
										holder.textview_accuracy.setText("Accuracy: LOW");
									case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
										holder.textview_accuracy.setText("Accuracy: MEDIUM");
									case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
										holder.textview_accuracy.setText("Accuracy: HIGH");
										break;
									default:
										holder.textview_accuracy.setText("Can't retrieve accuracy data.");
										break;
								}									
								}
							}
						};
						
						//Set holder as the tag of the view for future access.
						currentRow.setTag(holder);
						
						//Create a listener for our seekbar which we use to set sensor's rate.
						holder.seekbar_sensor.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
							
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								//This method is being invoked whenever you stop touching the seekbar.

								//If sensor was enabled, re-register sensor.
								if (holder.switch_sensor.isChecked()) {
									sensorRegister(holder.sensor, holder.sensorRate, holder.sensorListener);
								}
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								//This method is being invoked whenever you start touching the seekbar.

								//Unregister sensor while setting the rate.
								mSensorManager.unregisterListener(holder.sensorListener);
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								//This method is being invoked whenever the progress of the seekBar changes.

								//Show rate and store it. Will be used to register sensor accordingly.
								//If the progress is 0, use the fastest delay rate.
								//Formula is used to convert hertz into microseconds. (Android uses microseconds as it's rate unit.)
								holder.sensorRate = progress != 0 ? (int) (1.0/progress * 1000000) : 
									SensorManager.SENSOR_DELAY_FASTEST;
								holder.textview_rate.setText("Rate: " + String.valueOf(progress != 0 ? progress : "FASTEST") + "hertz");
							}
						});
						
						//Set a listener for our switch which we use to start/stop sensorListener.
						holder.switch_sensor.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							//Toggle sensorListener, and if SHOW_DATA is selected, make UI visible.
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								//This method is being invoked whenever checked status of switch changes.(i.e. you touch it)

								if (isChecked) {
									//It's new status is checked, register sensor and if SHOW_DATA is true, make UI visible.
									sensorRegister(holder.sensor, holder.sensorRate, holder.sensorListener);
									if (SHOW_DATA) {
										holder.progressbar_x.setVisibility(View.VISIBLE);
										holder.progressbar_y.setVisibility(View.VISIBLE);
										holder.progressbar_z.setVisibility(View.VISIBLE);
										holder.textview_x.setVisibility(View.VISIBLE);
										holder.textview_y.setVisibility(View.VISIBLE);
										holder.textview_z.setVisibility(View.VISIBLE);
										holder.textview_accuracy.setVisibility(View.VISIBLE);
									}
								} else {
									//It's new status is unchecked, unregister listener and make UI invisible.
									mSensorManager.unregisterListener(holder.sensorListener);
									holder.progressbar_x.setVisibility(View.GONE);
									holder.progressbar_y.setVisibility(View.GONE);
									holder.progressbar_z.setVisibility(View.GONE);
									holder.textview_x.setVisibility(View.GONE);
									holder.textview_y.setVisibility(View.GONE);
									holder.textview_z.setVisibility(View.GONE);
									holder.textview_accuracy.setVisibility(View.GONE);
								}
								
							}
						});
						
						holder.switch_sensor.setText(sensor.getName());
						holder.textview_rate.setText("FASTEST hertz");
						
						//Hide UI elements that are not needed until their sensorListener's get registered.
						holder.progressbar_x.setVisibility(View.GONE);
						holder.progressbar_y.setVisibility(View.GONE);
						holder.progressbar_z.setVisibility(View.GONE);
						holder.textview_x.setVisibility(View.GONE);
						holder.textview_y.setVisibility(View.GONE);
						holder.textview_z.setVisibility(View.GONE);
						holder.textview_accuracy.setVisibility(View.GONE);
						
						//Add finished row to the view.
						mSensorContainer.addView(currentRow);
		            	break;
		            default:
		            	break;
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		//This method is being invoked when this view is being destroyed.

		super.onDestroy();
		//Since listeners are parts of the view elements, unregister listeners to avoid creation of zombie listeners.
		for (int i=0; i < mSensorContainer.getChildCount(); i++) {
			Holder holder = (Holder) mSensorContainer.getChildAt(i).getTag();
			mSensorManager.unregisterListener(holder.sensorListener);
		}
	}
	
	/**
	 * Method that registers a sensor.
	 * 
	 * Returns true for success, false for failure.
	 */
    private boolean sensorRegister(Sensor sensor, int sensorRate, SensorEventListener sensorEventListener) {
    	//Try to register listener, and based on the boolean value we receive from it, return true or false and show errors.
        if (mSensorManager.registerListener(sensorEventListener, sensor, sensorRate)) {
            return true;
        } else {
            Toast.makeText(getActivity().getBaseContext(), "Oh snap! Can't register sensor.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
	
}
