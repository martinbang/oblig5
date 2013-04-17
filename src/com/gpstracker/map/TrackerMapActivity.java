package com.gpstracker.map;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.gpstracker.R;

public class TrackerMapActivity extends MapActivity implements LocationListener {
	
	
	private MapView mapView;
	private MyItemizedOverlay itemizedOverlay;
	private LocationManager locManger;
	private GeoPoint point;
	private MapController controller;
	
	private boolean setZoomeEnable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackermapactivity_layout);
		
		MapInit();
		
	}//end onCreate

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* Når aktiviten startes op, sendes det en request for å få posisjon*/
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
	}
	
	/** (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locManger.removeUpdates(this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Map
	 */
	public void MapInit(){
		
		//henter kartet fra layout
		mapView = (MapView) findViewById(R.id.map_view);
		//Gjør zoom controllerne tilgjengelig
		mapView.setBuiltInZoomControls(setZoomeEnable);
		
		//lengdegrad og breddegrad for Narvik(HIN)
		double latitude = 0;
		double longtitude = 0;
		
		 point = new GeoPoint((int)(latitude * 1E6), (int)(longtitude *1E6));
		
		//henter mapController objekt
		 controller = mapView.getController();
		
		controller.animateTo(point);
		
		//setter zoom for 
		controller.setZoom(13);
		
		mapView.invalidate();
		
		//bruker location manger til GPS
		
		locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
		
		//henter siste kjente lokasjon
		Location location = locManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(location != null){
			
			Toast.makeText(getApplicationContext(), "Location: " + location.getLongitude() + " , " + location.getLatitude(), Toast.LENGTH_SHORT).show();
			
			point = new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude() *1E6));
			controller.animateTo(point);
			
		}else{
			Toast.makeText(getApplicationContext(), "Finner ikke din posisjon", Toast.LENGTH_SHORT).show();
		}
		
		//henter drawable objekt å tegner en marker på kartet
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker_red);
		
		//lager et nytt overlayItem og legger den i MyItemzedoverlay listen
		OverlayItem overlayItem = new OverlayItem(point, "", "");

        itemizedOverlay = new MyItemizedOverlay(drawable,this);
        itemizedOverlay.addOverlay(overlayItem);

        // legger tul overlayet til kartet
        mapView.getOverlays().add(itemizedOverlay);
        mapView.invalidate();
		
		//når lokasjonen er funnet stopper locationlisterner å lytte etter ny posisjoner
		//locManger.removeUpdates(this);
		
		
		
		
	}//end method

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double l = location.getLatitude();
		double ll = location.getLongitude();
		String s = "Din posisjon nå: " + l + "," + ll;
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
		
		GeoPoint newPoint = new GeoPoint((int)(l*1e6),(int)(ll*1e6));
        controller.animateTo(newPoint);    
        
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker_red);
		
		//lager et nytt overlayItem og legger den i MyItemzedoverlay listen
		OverlayItem overlayItem = new OverlayItem(newPoint, "", "");

        itemizedOverlay = new MyItemizedOverlay(drawable,this);
        itemizedOverlay.addOverlay(overlayItem);

        // legger tul overlayet til kartet
        mapView.getOverlays().add(itemizedOverlay);
        mapView.invalidate();
		
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
	
	
	
}//end Activity
