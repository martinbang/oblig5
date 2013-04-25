package com.gpstracker.map;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.test.ServiceTestCase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.gpstracker.GCMIntentService;
import com.gpstracker.R;
import com.gpstracker.gcm.ServiceTestClass;

public class TrackerMapActivity extends MapActivity implements LocationListener {

	public static MapView mapView;
	private View view;
	private MyItemizedOverlay itemizedOverlay;
	private LocationManager locManger;
	private GeoPoint point;
	private MapController controller;
	private MapController cc;
	private String provider;
	private CheckBox checkSatteliteView;
	private CheckBox checkStreetView;
	private OnClickListener checkBoxListener;
	
	private int updateMapMillisec = 5000;
	private int updateMapMeters = 0;
	private int setZoomLvl = 1;
	
	//Shared pref settings
	public static final String SHARDE_PREFERENCES_NAME = "com.gps.location";
	public static final String LATITUDE = "latitude";
	public static final String LONGTITUDE = "longtitude";
	public static final String COLOR = "color";

	private boolean setZoomeEnable = true;
	
	private String FILENAME = "location";
	Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackermapactivity_layout);
	
		//Legger til kart i onCreate:
		initMap();
<<<<<<< HEAD
	
		
		
=======
>>>>>>> 3562b88054db4fbc4cfbb8b438afd2c678726bbc
	}// end onCreate
	
	/**
	 * Map preferences
	 */
	public void initMap() {
		
		 mapView = (MapView) findViewById(R.id.map_view);
		 mapView.setBuiltInZoomControls(setZoomeEnable);

		 locManger = (LocationManager) getSystemService(LOCATION_SERVICE);
		 
		 /**sjekker om pgs er slått på*/
		 if(!locManger.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				GPSmanager gps = new GPSmanager("GPS", "GPS not enabled, want to enable?", "Yes", "No", this);
		 }

		 Criteria criteria = new Criteria();
		 provider = locManger.getBestProvider(criteria, true);
		 Location location = locManger.getLastKnownLocation(provider);

		if (location != null) {
			Log.v("Location", "Location changed");
			onLocationChanged(location);
		}

		locManger.requestLocationUpdates(provider, updateMapMillisec, updateMapMeters, this);

		initCheckBoxes();
		 
	}//end initMap()

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		Log.v("OnLocationChanged", "Location changed");
		
		/**Henter lengde og breddegrad*/
		double latitude = location.getLatitude();
		double longtitude = location.getLongitude();
		
		/**lagrer posisjonene i Shared preferenses(Lagrer siste posisjon)*/
		try {

			/*SharedPreferences prefs = getApplicationContext()
					.getSharedPreferences("TEST",
							Context.MODE_WORLD_READABLE);
			//Editor editor = prefs.edit();
			//editor.putLong(LATITUDE, Double.doubleToLongBits(latitude));
			//editor.putLong(LONGTITUDE, Double.doubleToLongBits(longtitude));
			//editor.commit();
			
			double l = Double.longBitsToDouble(prefs.getLong(LATITUDE, 0));
			double ll = Double.longBitsToDouble(prefs.getLong(LONGTITUDE, 0));
			String s = Double.toString(l);
			String d = Double.toString(ll);
			toast(s + d);*/
			
			
				
			//mapOverlays.clear(); //settes denne fjerner den siste markør og viser kun siste posisjon
			
			try{
				
				ServiceTestClass.updatePosition(longtitude, latitude, this);
				Log.v("Updatepositions", "Posisions sendt");
				
			}catch(Exception e){
				
				Log.v("updatepossition", "Cant send pos" + e.getMessage());
			}

		} catch (Exception e) {
			Log.v("Shared_PREFERNCE_ERROR", e.getMessage());
		}
		
		toast("Lat: " + latitude + " Lon: " + longtitude);
		
		SharedPreferences prefs = getApplicationContext()
				.getSharedPreferences("TEST",
						Context.MODE_APPEND);
		
		double l = Double.longBitsToDouble(prefs.getLong(LATITUDE, 0));
		double ll = Double.longBitsToDouble(prefs.getLong(LONGTITUDE, 0));
		
		String s = Double.toString(l);
		String d = Double.toString(ll);
		toast(s + d);
		
		GeoPoint point = new GeoPoint((int) (longtitude* 1E6),(int) (latitude * 1E6));
		GeoPoint p = new GeoPoint((int) (l* 1E6),(int) (ll * 1E6));
		//saveLocation((int)latitude, (int)longtitude);
		controller = mapView.getController();
		controller.animateTo(point);
		controller.animateTo(p);
		controller.setZoom(setZoomLvl);
		mapView.invalidate();

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker_red);
		
		MyItemizedOverlay miO = new MyItemizedOverlay(drawable, this);
		MyItemizedOverlay iOverlay = new MyItemizedOverlay(drawable, this);
		
		OverlayItem currentlocation = new OverlayItem(point," Current location", "Lat: " + latitude + " , " + " Long: " + longtitude);
		OverlayItem over = new OverlayItem(p, "hei","håhå");
		
		miO.addOverlay(currentlocation);
		miO.addOverlay(over);
		Log.v("OverlayItem", " Current Location added");
			
		mapOverlays.clear(); //settes denne fjerner den siste markør og viser kun siste posisjon
		mapOverlays.add(miO);
		mapOverlays.add(iOverlay);
		
		Log.v("OMap overlay", "Overlay added");
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		toast("Gps disabled");
		Log.v("GPS", "GPS IS DISABLED");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		toast("Gps enabled");
		Log.v("GPS", "GPS IS ENABLED");
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
					Log.v("MapView", "Sattelite View enabled");

				}
				if (checkStreetView.isChecked()) {

					checkSatteliteView.setChecked(false);
					mapView.setSatellite(false);
					mapView.setStreetView(true);
					Log.v("MapView", "Street View enabled");
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
		
		locManger.requestLocationUpdates(provider, updateMapMillisec,updateMapMeters, this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locManger.removeUpdates(this);
	}

	
	
	
}// end Activity
