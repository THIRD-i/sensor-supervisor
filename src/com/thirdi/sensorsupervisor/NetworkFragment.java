package com.thirdi.sensorsupervisor;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class NetworkFragment extends Fragment {
	private WifiManager mWifiManager;
    private Button mScanButton;
    private ToggleButton mWifiToggleButton;
    public NetworkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this sensorFragment
        return inflater.inflate(R.layout.fragment_network, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mScanButton = (Button) getView().findViewById(R.id.scanButton);
        mWifiToggleButton = (ToggleButton) getView().findViewById(R.id.wifiToggleButton);
        mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        //Set button states
        if (mWifiManager.isWifiEnabled()) {
            mScanButton.setEnabled(true);
            mWifiToggleButton.setChecked(true);
        } else {
            mScanButton.setEnabled(false);
            mWifiToggleButton.setChecked(false);
        }

        //Set wifi scan button
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!scanWifi()) {
                    //Couldn't scan. Show error.
                    Toast.makeText(getActivity().getBaseContext(), "Oh snap! Can't scan for wifi.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set wifi toggle
        mWifiToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    if (!toggleWifi()) {
                        //Couldn't toggle, reset the button and show error text.
                        Toast.makeText(getActivity().getBaseContext(), "Oh snap! Can't turn wifi on.",
                                Toast.LENGTH_SHORT).show();
                        mWifiToggleButton.setChecked(false);
                    }
                } else {
                    if (!toggleWifi()) {
                        //Couldn't toggle, reset the button and show error text.
                        Toast.makeText(getActivity().getBaseContext(), "Oh snap! Can't turn wifi off.",
                                Toast.LENGTH_SHORT).show();
                        mWifiToggleButton.setChecked(true);
                    }
                }
            }
        });
    }

    private boolean toggleWifi() {
        //Toggles wifi, returns true for success, false for error.
        if (mWifiManager.isWifiEnabled()) {
            //Wifi is enabled, disable it.
            if (mWifiManager.setWifiEnabled(false)) {
                mWifiToggleButton.setChecked(false);
                mScanButton.setEnabled(false);
            } else {
                return false;
            }
        } else {
            //Wifi is disabled, enable it.
            if (mWifiManager.setWifiEnabled(true)) {
                mWifiToggleButton.setChecked(true);
                mScanButton.setEnabled(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean scanWifi() {
        //Scans for wifi networks and shows scan data as a list.
        //Returns true for success, false for error.
        final List<ScanResult> scanResultsList;
        WifiAdapter wifiAdapter;
        ListView listView = (ListView) getView().findViewById(R.id.list);

        if (mWifiManager.startScan()) {
            //Scan successful, show data.
            scanResultsList = mWifiManager.getScanResults();
            wifiAdapter = new WifiAdapter(getActivity(), scanResultsList);
            listView.setAdapter(wifiAdapter);
            return true;
        } else {
            //Scan failed, return false.
            return false;
        }
    }
}
