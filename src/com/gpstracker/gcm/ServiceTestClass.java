package com.gpstracker.gcm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.gpstracker.conf.Configuration;
import com.gpstracker.map.PositionListener;

public class ServiceTestClass 
{
	public static String SERVER_URL = "http://kenh.dyndns.org/android5/tomcat";
	public static String REGISTER_URN = "/register";
	public static String UNREGISTER_URN = "/unregister";
	public static String UPDATEPOS_URN = "/pos";
	public static String SEND_MESSAGE_URN = "/send";
	private static List<PositionListener> positionListeners = new ArrayList<PositionListener>();
	
	public static void register(final Context context, String name)
	{
		final String serverUrl = SERVER_URL + REGISTER_URN;
		final Map<String, String> params = new HashMap<String, String>();
		params.put("regId", GCMRegistrar.getRegistrationId(context));
		params.put("name", name);
		
		executePost(serverUrl, params);
			
		GCMRegistrar.setRegisteredOnServer(context,  true);
		
		Configuration conf = Configuration.getCurrentConfiguration(context);
		conf.setUserName(name);
		conf.setRegistered(true);
		conf.commit(context);
	}
	
	public static void unRegister(final Context context)
	{
		final String serverUrl = SERVER_URL + UNREGISTER_URN;
		final Map<String, String> params = new HashMap<String, String>();
		//params.put("regId", GCMRegistrar.getRegistrationId(context));
		params.put("id", Configuration.getCurrentConfiguration(context).getId() + "");
		
		executePost(serverUrl, params);
		
		GCMRegistrar.setRegisteredOnServer(context, false);
		Configuration conf = Configuration.getCurrentConfiguration(context);
		conf.setRegistered(false);
		conf.commit(context);
	}
	
	public static void updatePosition(double lat, double lng, Context context) 
	{

		final Map<String, String> params = new HashMap<String, String>();
		params.put("lat", lat + "");
		params.put("lng", lng + "");
		params.put("id", Configuration.getCurrentConfiguration(context).getId() + "");
		executePost(SERVER_URL + UPDATEPOS_URN, params);

	}
	
	public static void sendMessage(int id, String message, String receiver)
	{
		final Map<String, String> params = new HashMap<String, String>();
		params.put("msg", message);
		params.put("id", id + "");
		params.put("rcv", receiver);
		executePost(SERVER_URL + SEND_MESSAGE_URN, params);
	}
	
	public static void addPositionListener(PositionListener listener) {
		positionListeners.add(listener);
	}
	
	public static void positionUpdate(double lat, double lng, int id) {
		for (PositionListener listener : positionListeners) {
			listener.positionUpdate(lat, lng, id);
		}
	}
	
	private static void executePost(final String endpoint, final Map<String, String> params)
	{
		AsyncTask.execute(new Runnable()
		{

			@Override
			public void run() 
			{
				try 
				{
					post(endpoint, params);
				} catch (IOException e) 
				{
					Log.d("ERROR", e.getMessage());
				}
			}
		});
	}
	
	/**
	 * Sender post forespørsel med parametre
	 * @param endpoint URI
	 * @param params
	 * @throws IOException
	 */
	private static void post(String endpoint, Map<String, String> params) throws IOException
	{
		Log.d("POST", "STARTER POST. id: " + params.get("regId"));
		URL url = null;
		try
		{
			url = new URL(endpoint);
		} catch(MalformedURLException e)
		{
			Log.d("POST", "MALFORMED URL");
		}
		
		StringBuilder bodyBuilder = new StringBuilder(); //For å lage parameterstring
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		
		
		//Legger til parameter og parameternavn for hver parameter som ble lagdt til 
		while(iterator.hasNext())
		{
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());//parameternavn og parameter skilles med =
			if(iterator.hasNext())
			{
				bodyBuilder.append('&'); //parametre i uri skilles med &
			}
		}
		
		String body = bodyBuilder.toString();
		byte[] bytes = body.getBytes(); //tillegg blir serializert
		HttpURLConnection conn = null;

		try
		{	
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			
			int status = conn.getResponseCode();
			if(status != 200)
			{
				throw new IOException("Post failed with error code " + status);
			}
		}
		catch(Exception e)
		{
			if(e.getMessage() != null)
				Log.d("POST", "post ble ikke sendt! melding: " + e.getMessage());
			else Log.d("POST", e.toString());
		}
		finally
		{
			if(conn != null)
				conn.disconnect();
		}
	}
}
