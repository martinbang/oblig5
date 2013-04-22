package com.gpstracker.gcm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class ServiceTestClass 
{
	public static final long BACKOFF_MILLI_SECOUNDS = 2000;
	public static final int MAX_ATTEMPTS = 5;
	
	public static String SERVER_URL = "http://kenh.dyndns.org/android5/tomcat/register";
	
	public static void register(final Context context, String name, 
			String email, final String regId)
	{
		final String serverUrl = SERVER_URL;
		final Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("name", name);
		params.put("email", email);
		
		AsyncTask.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				try 
				{
					post(serverUrl, params);
				} catch (IOException e) 
				{
					Log.d("ERROR", e.getMessage());
				}
			}
		});
			
		GCMRegistrar.setRegisteredOnServer(context,  true);		
	}
	
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
		
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
			if(iterator.hasNext())
			{
				bodyBuilder.append('&');
			}
		}
		
		String body = bodyBuilder.toString();
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		//Log.d("POST", "PRØVER Å SENDE POST");
		try
		{	
			conn = (HttpURLConnection) url.openConnection();
			//Log.d("POST", "openConnection");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			//Log.d("POST", "Conn parametre satt");
			
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			//Log.d("POST", "connection gjort");
			
			int status = conn.getResponseCode();
			if(status != 200)
			{
				throw new IOException("Post failed with error code " + status);
			}
		} 
		catch(Exception e)
		{
			if(e.getMessage() != null)
				Log.d("POST", e.getMessage());
			else Log.d("POST", e.toString());
		}
		finally
		{
			if(conn != null)
				conn.disconnect();
		}
	}
}
