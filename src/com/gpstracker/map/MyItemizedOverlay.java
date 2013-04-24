package com.gpstracker.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private Context context;

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MyItemizedOverlay(Drawable defaultMarker, Context ctx) {
		super(boundCenterBottom(defaultMarker));

	}

	public void addOverlay(OverlayItem overlay) {
		Log.v("addOverlay(OverlayItem overlay)", "Overlay add");
		mOverlays.add(overlay);
		populate();
	}

	public void clear() {

		mOverlays.clear();
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		return true;
	}
	
}//end class
