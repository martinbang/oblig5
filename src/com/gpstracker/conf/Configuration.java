package com.gpstracker.conf;

import com.gpstracker.map.TrackerMapActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Configuration 
{
	private static final String DEFAULT_USER_NAME = "";
	private static final boolean DEFAULT_SEND_ON_TIME_PASSED = true;
	private static final boolean DEFAULT_SEND_ON_USER_MOVED = true;
	private static final int DEFAULT_MINUTES_BETWEEN_SEND = 5;
	private static final int DEFAULT_METERS_BETWEEN_SEND = 50;
	private static final int DEFAULT_ZOOM_LEVEL = 15;
	private static final boolean DEFAULT_IS_REGISTERED = false;
	private static final int DEFAULT_ID = -1;

	private final static int INDEX_USERNAME = 0;
	private final static int INDEX_SEND_ON_TIME_PASSED = 1;
	private final static int INDEX_SEND_ON_USER_MOVED = 2;
	private final static int INDEX_MINUTES_BETWEEN_SEND = 3;
	private final static int INDEX_METERS_BETWEEN_SEND = 4;
	private final static int INDEX_ZOOM_LEVEL = 5;
	private final static int INDEX_IS_REGISTERED = 6;
	private final static int INDEX_ID = 7;
	
	private final static String PARAMETER_SEPARATOR = "-P";
	private final int AVAILABLE_CONFIGURATIONS = 8;
	private final static String PREF_NAME = "pref";
	private final static String CONF_NAME = "conf";
	
	private String username;
	private boolean sendLocationOnTimePassed;
	private boolean sendLocationOnUserMoved;
	private int rangeMinutes;
	private int rangeMeters;
	private int zoomLevel;
	private boolean isRegistered;
	private int id;
	
	/**
	 * Setter alle parametre til defaultverdiene
	 */
	private Configuration()
	{
		this.username = DEFAULT_USER_NAME;
		this.sendLocationOnTimePassed = DEFAULT_SEND_ON_TIME_PASSED;
		this.sendLocationOnUserMoved = DEFAULT_SEND_ON_USER_MOVED;
		this.rangeMinutes = DEFAULT_MINUTES_BETWEEN_SEND;
		this.rangeMeters = DEFAULT_METERS_BETWEEN_SEND;
		this.zoomLevel = DEFAULT_ZOOM_LEVEL;
		this.isRegistered = DEFAULT_IS_REGISTERED;
		this.id = DEFAULT_ID;
	}
	
	/**
	 * Bruker verdiene som er satt til nå til å lage en string som representerer Configurasjons-objektet
	 * @return
	 */
	private String getConfigurationString()
	{
		String[] confParameters = new String[AVAILABLE_CONFIGURATIONS];
		confParameters[INDEX_USERNAME] = username;
		confParameters[INDEX_SEND_ON_TIME_PASSED] = sendLocationOnTimePassed + "";
		confParameters[INDEX_SEND_ON_USER_MOVED] = sendLocationOnUserMoved + "";
		confParameters[INDEX_MINUTES_BETWEEN_SEND] = rangeMinutes + "";
		confParameters[INDEX_METERS_BETWEEN_SEND] = rangeMeters + "";
		confParameters[INDEX_ZOOM_LEVEL] = zoomLevel + "";
		confParameters[INDEX_IS_REGISTERED] = isRegistered + "";
		confParameters[INDEX_ID] = id + "";
		
		String confString = "";
		for(String s : confParameters)
			confString += s + PARAMETER_SEPARATOR;
		
		return confString;
	}
	
	/**
	 * Gjør om en konfigurasjonstring til et konfigurasjons objekt
	 * @param confString getConfigurationString()
	 * @return
	 */
	private static Configuration getConfigurationFromString(String confString)
	{
		String[] confParameters = confString.split(PARAMETER_SEPARATOR);
		Configuration conf = new Configuration();
		
		conf.setUserName(confParameters[INDEX_USERNAME]);
		conf.setSendOnTimePassed(confParameters[INDEX_SEND_ON_TIME_PASSED].equals("true"));
		conf.setSendOnUserMoved(confParameters[INDEX_SEND_ON_USER_MOVED].equals("true"));
		conf.setMinutesBetweenSend(Integer.parseInt(confParameters[INDEX_MINUTES_BETWEEN_SEND]));
		conf.setMetersBetweenSend(Integer.parseInt(confParameters[INDEX_METERS_BETWEEN_SEND]));
		conf.setZoomLevel(Integer.parseInt(confParameters[INDEX_ZOOM_LEVEL]));
		conf.setRegistered(confParameters[INDEX_IS_REGISTERED].equals("true"));
		conf.setId(Integer.parseInt(confParameters[INDEX_ID]));
		
		return conf;
	}
	
	/**
	 * Returnerer et konfigurasjons objekt med parametrene som er lagret i SharedPreferences
	 * Hvis det ikke er lagret noe der, blir det returnert et konfigurasjons objekt
	 * med default verdiene
	 * @param context
	 * @return
	 */
	public static Configuration getCurrentConfiguration(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, 0);
		String confString = sp.getString(CONF_NAME, "");
		if(confString.equals(""))
			return new Configuration();
		
		else return getConfigurationFromString(confString);
	}
	
	/**
	 * lagrer verdiene som er satt i shared preferences
	 * @param context
	 */
	public void commit(Context context)
	{
		Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
		String confString = getConfigurationString();//Henter konfigurasjonstring med verdiene som er satt til nå
		editor.putString(CONF_NAME, confString);
		editor.commit();//Lagrer
	}
	
	/*
	 * accsessors & mutators
	 */
	TrackerMapActivity a;
	public void setUserName(String userName){this.username = userName;}
	public void setSendOnTimePassed(boolean sendOnTimePassed){this.sendLocationOnTimePassed = sendOnTimePassed;}
	public void setSendOnUserMoved(boolean sendOnUserMoved){this.sendLocationOnUserMoved = sendOnUserMoved;}
	public void setMinutesBetweenSend(int min){this.rangeMinutes = min;}
	public void setMetersBetweenSend(int meters){this.rangeMeters = meters;}
	public void setZoomLevel(int zoomLevel){this.zoomLevel = zoomLevel;}
	public void setRegistered(Boolean isRegistered){this.isRegistered = isRegistered;}
	public void setId(int id){this.id = id;}
	
	public String getUserName(){return username;}
	public boolean getSendOnTimePassed(){return sendLocationOnTimePassed;}
	public boolean getSendOnUserMoved(){return sendLocationOnUserMoved;}
	public int getMinutesBetweenSend(){return rangeMinutes;}
	public int getMetersBetweenSend(){return rangeMeters;}
	public int getZoomLevel(){return zoomLevel;}
	public boolean getRegistered(){return isRegistered;}
	public int getId(){return id;}
}
