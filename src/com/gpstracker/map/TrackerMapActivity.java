package com.gpstracker.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.gpstracker.R;
import com.gpstracker.gcm.ServiceTestClass;

/**
 * 
 *classe/aktivitet som brukes for å vise kart. kart i api v1.
 *
 */
public class TrackerMapActivity extends MapActivity implements LocationListener , OnCheckedChangeListener{

	public static MapView mapView;
	private LocationManager locManger;
	private MapController controller;
	private String provider;
	
	private int updateMapMillisec = 1000;
	private int updateMapMeters = 0;
	private static int setZoomLvl;
	
	//Shared pref settings
	public static final String SHARDE_PREFERENCES_NAME = "com.gps.location";
	public static final String LATITUDE = "latitude";
	public static final String LONGTITUDE = "longtitude";
	public static final String COLOR = "color";

	private boolean setZoomeEnable = true;
	
	private Map<Integer, Overlay> overlayMap = new HashMap<Integer, Overlay>();
	
	View view;
	Context c;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trackermapactivity_layout);
	
		//Legger til kart i onCreate:
		initMap();
	}// end onCreate
	
	/**
	 * Map preferences
	 */
	public void initMap() {
		
		 mapView = (MapView) findViewById(R.id.map_view);
		 mapView.setBuiltInZoomControls(setZoomeEnable);
		 mapView.setStreetView(true);
		 controller = mapView.getController();
		 controller.setZoom(getSetZoomLvl());

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
		ServiceTestClass.addPositionListener(new PositionListener() {
			@Override
			public void positionUpdate(double lat, double lng, int id, int color) {
				updateOverlay(lat, lng, id, color);
			}
		});
		initRadioGroupe();
	}//end initMap()

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * Methode fra LocationListener, methoden brukes for å oppdatere lokasjonen/er på kartet
	 * når posisjonene endrere seg.
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.v("OnLocationChanged", "Location changed");
		
		/**Henter lengde og breddegrad*/
		double latitude = location.getLatitude();
		double longtitude = location.getLongitude();
		
		try {			
			try{				
				ServiceTestClass.updatePosition(latitude, longtitude, this);
				Log.v("Updatepositions", "Posisions sendt");
				
			}catch(Exception e){
				
				Log.v("updatepossition", "Cant send pos" + e.getMessage());
			}

		} catch (Exception e) {
			Log.v("Shared_PREFERNCE_ERROR", e.getMessage());
		}
		
		mapView.invalidate();		
		//updateOverlay(latitude, longtitude, -666, Color.BLACK);
		
		Log.v("OMap overlay", "Overlay added");
	}
	
	private Overlay buildOverlay(double lat, double lng, int color) {
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
		drawable = setColor(drawable, Color.WHITE, color);
		
		MyItemizedOverlay miO = new MyItemizedOverlay(drawable, this);
		GeoPoint point = new GeoPoint((int) (lat * 1E6),(int) (lng * 1E6));
		OverlayItem currentlocation = new OverlayItem(point," Current location", "Lat: " + lat + " , " + " Long: " + lng);
		
		miO.addOverlay(currentlocation);
		return miO;
	}
	
	private Drawable setColor(Drawable drawable, int from, int to) {
		Bitmap src = ((BitmapDrawable)drawable).getBitmap();
		Bitmap bmp = src.copy(Bitmap.Config.ARGB_8888, true);
		for (int x = 0; x < bmp.getWidth(); x++) {
			for (int y = 0; y < bmp.getHeight(); y++) {
				if (bmp.getPixel(x, y) == from) {
					bmp.setPixel(x, y, to);
				}
			}
		}		
		return new BitmapDrawable(bmp);
	}

	private void updateOverlay(double lat, double lng, int id, int color) {
		List<Overlay> mapOverlays = mapView.getOverlays();
		if (overlayMap.containsKey(id)) {
			mapOverlays.remove(overlayMap.get(id));
		}
		
		Overlay overlay = buildOverlay(lat, lng, color);
		overlayMap.put(id, overlay);
		mapOverlays.add(overlay);
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
	
	public void initRadioGroupe(){
		    
	((RadioGroup)findViewById(R.id.radioGroupChangeView)).setOnCheckedChangeListener(this);
		    
    } 

	/**
	 * Brukes til å switche mellom mapview(Street og sattelite)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
		switch (checkedId) {
		    case R.id.radioSatteliteView:
		      toast("sattelite view ON");
		      mapView.setStreetView(false);
		      mapView.setSatellite(true);
		      break;
		    case R.id.radioStreetView:
		      toast("Street view ON");
		      mapView.setSatellite(false);
		      mapView.setStreetView(true);
		      break;
		
		   default:
		      break;
		    }//end switch
	}//end listener

	/**
	 * @return the setZoomLvl
	 */
	public int getSetZoomLvl() {
		return setZoomLvl;
	}

	/**
	 * @param setZoomLvl the setZoomLvl to set
	 */
	public  void setSetZoomLvl(int setZoomLvl) {
		this.setZoomLvl = setZoomLvl;
	}
	
	
}// end Activity
