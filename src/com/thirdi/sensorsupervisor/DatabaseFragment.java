package com.thirdi.sensorsupervisor;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DatabaseFragment extends Fragment {

    SQLiteDatabase mDatabase;
    DBHelper mDbHelper;
	
    public DatabaseFragment() {
        // Required empty public constructor
    	mDbHelper = new DBHelper(getActivity().getBaseContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_database, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        mDatabase = mDbHelper.getReadableDatabase();
        String[] projection = {
        	DBContract.DBEntry.COLUMN_NAME_SENSOR_ID,
        	DBContract.DBEntry.COLUMN_NAME_SENSOR_NAME,
        	DBContract.DBEntry.COLUMN_NAME_VALUE_X,
        	DBContract.DBEntry.COLUMN_NAME_VALUE_Y,
        	DBContract.DBEntry.COLUMN_NAME_VALUE_Z,
        };
        String sortOrder = DBContract.DBEntry.COLUMN_NAME_SENSOR_NAME;
        Cursor cursor = mDatabase.query(
        		DBContract.DBEntry.TABLE_NAME,
        		projection,
        		null,
        		null,
        		null,
        		null,
        		sortOrder);
        int[] views = {
        		R.id.bssidView,
        };
        ListView mListView = (ListView) getView().findViewById(R.id.listview);
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
        		R.layout.wifi_list_item, cursor, projection, views, 0);
        mListView.setAdapter(mAdapter);
    }
}
