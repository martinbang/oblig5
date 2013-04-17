package com.gpstracker.map;


import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.gpstracker.R;

public class TrackerMapActivity extends MapActivity {
	
	
	private MapView mapView;
	
	private boolean setZoomeEnable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackermapactivity_layout);
		
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(setZoomeEnable);
		
	}//end onCreate

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}//end Activity
