package com.gpstracker.map;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.gpstracker.R;

public class TrackerMapActivity extends MapActivity implements LocationListener {

	public static MapView mapView;
	private View view;
	private MyItemizedOverlay itemizedOverlay;
	private LocationManager locManger;
	private GeoPoint point;
	private GeoPoint newPoint;
	private MapController controller;
	private String provider;
	private CheckBox checkSatteliteView;
	private CheckBox checkStreetView;

	private OnClickListener checkBoxListener;

	private boolean setZoomeEnable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackermapactivity_layout);
		
		initMap();
		
	}// end onCreate

	public void initMap() {
		
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(setZoomeEnable);

		 locManger = (LocationManager) getSystemService(LOCATION_SERVICE);

		 Criteria criteria = new Criteria();

		 provider = locManger.getBestProvider(criteria, true);

		Location location = locManger.getLastKnownLocation(provider);

		if (location != null) {
			Log.v("Location", "Location changed");
			onLocationChanged(location);
		}

		locManger.requestLocationUpdates(provider, 5000, 0, this);

		initCheckBoxes();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		double latitude = location.getLatitude();
		double longtitude = location.getLongitude();

		Toast.makeText(getApplicationContext(),
				"Latitude: " + latitude + "Longtitude: " + longtitude,
				Toast.LENGTH_LONG).show();

		GeoPoint point = new GeoPoint((int) (latitude * 1E6),
				(int) (longtitude * 1E6));

		controller = mapView.getController();

		controller.animateTo(point);

		controller.setZoom(15);

		List<Overlay> mapOverlays = mapView.getOverlays();

		Drawable drawable = this.getResources().getDrawable(
				R.drawable.marker_red);

		MyItemizedOverlay miO = new MyItemizedOverlay(drawable, this);

		OverlayItem currentlocation = new OverlayItem(point,
				"Current location", "Latitude: " + latitude + ", "
						+ "Longtitude: " + longtitude);

		miO.addOverlay(currentlocation);
		// mapOverlays.clear(); //settes denne fjerner den siste markør og
		// tegner kun corretent pos på kartet
		mapOverlays.add(miO);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * Check boks implementasjon for å vise satteliteview eller street view
	 */
	public void initCheckBoxes() {
		checkSatteliteView = (CheckBox) findViewById(R.id.checkBoxSatteliteView);
		checkStreetView = (CheckBox) findViewById(R.id.checkBoxStreetView);

		checkBoxListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (checkSatteliteView.isChecked()) {
					checkStreetView.setChecked(false);
					mapView.setStreetView(false);
					mapView.setSatellite(true);

				}
				if (checkStreetView.isChecked()) {

					checkSatteliteView.setChecked(false);
					mapView.setSatellite(false);
					mapView.setStreetView(true);
				}

			}
		};
		// legger til til i onClick lytteren for checkboxene
		checkSatteliteView.setOnClickListener(checkBoxListener);
		checkStreetView.setOnClickListener(checkBoxListener);

	}

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		locManger.requestLocationUpdates(provider, 5000,0, this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locManger.removeUpdates(this);
	}
	
	

}// end Activity
