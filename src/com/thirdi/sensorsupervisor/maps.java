package com.thirdi.sensorsupervisor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class maps extends FragmentActivity{
	private GoogleMap googleHarita;
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maps);
	 
	    if (googleHarita == null) {
	        googleHarita = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.haritafragment))
	                .getMap();
	        if (googleHarita != null) {
	            LatLng istanbulKoordinat = new LatLng(41.021161,29.004065);
	            googleHarita.addMarker(new MarkerOptions().position(istanbulKoordinat).title("Kız Kulesi"));
	            googleHarita.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbulKoordinat, 13));         
	        }
	    }
	 
	}
}
