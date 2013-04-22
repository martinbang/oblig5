package com.gpstracker.conf;

public class Configuration 
{
	private String username;
	private boolean sendLocationOnTimePassed;
	private boolean sendLocationOnUserMoved;
	private int rangeMinutes;
	private int rangeMeters;
	private int zoomLevel;
	
	public void setUserName(String userName){this.username = userName;}
	public void setSendOnTimePassed(boolean sendOnTimePassed){this.sendLocationOnTimePassed = sendOnTimePassed;}
	public void setSendOnUserMoved(boolean sendOnUserMoved){this.sendLocationOnUserMoved = sendOnUserMoved;}
	public void setMinutesBetweenSend(int min){this.rangeMinutes = min;}
	public void setMetersBetweenSend(int meters){this.rangeMeters = meters;}
	public void setZoomLevel(int zoomLevel){this.zoomLevel = zoomLevel;}
	
	public String getUserName(){return username;}
	
	

}
