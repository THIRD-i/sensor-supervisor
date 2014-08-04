package com.thirdi.sensorsupervisor;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WifiAdapter extends BaseAdapter {
	/**
	 * Adapter for listing available wifi routers.
	 * 
	 * mActivity : Activity that this adapter works on.
	 * mScanResults : List of scan results.
	 * mInflater : LayoutInflater to inflate layouts.
	 */
	
	private final Activity mActivity;
    private final List<ScanResult> mScanResults;
    private final LayoutInflater mInflater;

    public WifiAdapter(Activity activity, List<ScanResult> scanResults) {
        mActivity = activity;
        mScanResults = scanResults;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mScanResults.size();

    }

    @Override
    public Object getItem(int i) {
        return mScanResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        TextView ssid, bssid, capabilities, freq, level, timestamp;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ScanResult scanResult = mScanResults.get(i);
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.wifi_list_item, null);
            holder.ssid = (TextView) view.findViewById(R.id.ssidView);
            holder.bssid = (TextView) view.findViewById(R.id.bssidView);
            holder.capabilities = (TextView) view.findViewById(R.id.capabilitiesView);
            holder.freq = (TextView) view.findViewById(R.id.frequencyView);
            holder.level = (TextView) view.findViewById(R.id.levelView);
            holder.timestamp = (TextView) view.findViewById(R.id.timestampView);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.ssid.setText(scanResult.SSID);
        holder.bssid.setText(scanResult.BSSID);
        holder.capabilities.setText(scanResult.capabilities);
        holder.freq.setText(String.valueOf(scanResult.frequency));
        holder.level.setText(String.valueOf(scanResult.level));
        holder.timestamp.setText(String.valueOf(scanResult.timestamp));
        return view;
    }

}
