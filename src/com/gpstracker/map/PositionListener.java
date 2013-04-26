package com.gpstracker.map;

public interface PositionListener {
	void positionUpdate(double lat, double lng, int id, int color);
}
