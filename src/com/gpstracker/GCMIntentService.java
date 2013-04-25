package com.gpstracker;

import java.text.NumberFormat;

import junit.framework.Test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.maps.MyLocationOverlay;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceTestClass;
import com.gpstracker.log.LogFragment;
import com.gpstracker.log.LogItem;
import com.gpstracker.map.MyItemizedOverlay;
import com.gpstracker.map.TrackerMapActivity;

public class GCMIntentService extends GCMBaseIntentService
{
	private final String TAG_ID = "id";
	private final String TAG_MESSAGE = "msg";
	private final String TAG_SENDER = "snd";
	private final String TAG_DESCRIPTION = "des";
	private final String TAG_COLOR = "clr";
	
	private final String TAG_ADMIN_COMMAND = "cmd";
	private final String TAG_LATITUDE = "lat";
	private final String TAG_LOGNTITUDE = "lng";
	
	private final String ADMIN_COMMAND_DROP = "drop";
	
	@Override
	protected void onError(Context arg0, String arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void onMessage(Context context, Intent intent) 
	{
		Bundle extras = intent.getExtras();//henter extras fra intent.
		
		handleAdminCommand(context, extras);//Behandler eventuelle kommandoer fra admin
		handleId(context, extras);//Om en id kommer alene, behandles den her
		handleMessage(context, extras);//behandler beskjeder
		handlePos(context, extras);//Oppdaterer posisjon
	}
	
	/**
	 * Behandler inkommende posisjon
	 * @param context
	 * @param extras
	 */
	private void handlePos(Context context, Bundle extras)
	{
		try
		{
			int id = Integer.parseInt(extras.getString(TAG_ID));
			double latitude = Double.parseDouble(extras.getString(TAG_LATITUDE));
			double longtitude = Double.parseDouble(extras.getString(TAG_LOGNTITUDE));
			String colorStr = extras.getString(TAG_COLOR);//inkommende farge er på formatet "12,234,45". 
			int color = LogItem.parseColorString(colorStr); //Dette blir gjort om til int-verdi som android forstår
			
			ServiceTestClass.positionUpdate(latitude, longtitude, id);
			
			Log.d("POS", "Ny posisjon id:" + id + " lat: " + latitude + "lng: " + longtitude + " color: " + colorStr);
			
			SharedPreferences prefs = getApplicationContext()
					.getSharedPreferences("TEST",
							Context.MODE_APPEND);
			Editor editor = prefs.edit();
			editor.putLong("latitude", Double.doubleToLongBits(latitude));
			editor.putLong("longtitude", Double.doubleToLongBits(longtitude));
			editor.commit();
			
		} 
		catch(NumberFormatException e){Log.d("POS", e.getMessage());}
		catch(NullPointerException ne){Log.d("POS", "Nullpointer");}
	}

	
	/**
	 * Skjekker om inkommende intent har en id. Hvis den har det og det ikke er 
	 * en kommando for å kaste noen ut, skal brukers id settes lik denne
	 * @param context
	 * @param extras
	 */
	private void handleId(Context context, Bundle extras)
	{
		try
		{
			int id = Integer.parseInt(extras.getString(TAG_ID));
			String cmd = "";
			cmd += extras.getString(TAG_ADMIN_COMMAND);
			
			if(!cmd.equals(ADMIN_COMMAND_DROP)) //hvis id kom med admin cmd, skal ikke id behandles her
			{
				Log.d("HandleID", "Ny id: " + id);
				//Lagrer id i shared preferences
				Configuration conf = Configuration.getCurrentConfiguration(context);
				conf.setId(id);
				conf.commit(context);
			}
		} catch(NumberFormatException e){}
	}
	
	/**
	 * Skjekker om intent inneholder en melding som skal logges. Logger hvis den gjør det
	 * @param context
	 * @param extras
	 */
	private void handleMessage(final Context context, Bundle extras)
	{
		try
		{
			String msg = extras.getString(TAG_MESSAGE);//Meldingens innhold
			String action = extras.getString(TAG_DESCRIPTION); //om det er en privat melding eller en offentlig melding
			String sender = extras.getString(TAG_SENDER);//hvem som sendte meldingen
			String color = extras.getString(TAG_COLOR);//Fargekoden til avsender

			final LogItem item = new LogItem(action, sender, msg, color);//Oppretter ett loggbart item
			if(!action.equals("null"))
				{
				Log.d("MESSAGE", "melding: " + msg + " type: " + action + " sender: " + sender + " farge: " + color);
				
				/*
				 * Legger til ved å bruke Logfragmentets handler.
				 */
				LogFragment.handler.post(new Runnable()
				{
					@Override
					public void run() 
					{
						Log.d("HANDLER", "run metoden kjører ");
						LogFragment.addLogItem(LogFragment.context, item);
					}
				});
			}
			
		} catch(NullPointerException e){}//Om det skjer en 
	}
	
	/**
	 * Behandler inkommende kommandoer fra admin, hvis det er noen
	 * @param context
	 * @param extras
	 */
	private void handleAdminCommand(Context context, Bundle extras)
	{
		String cmd = "";
		cmd += extras.getString(TAG_ADMIN_COMMAND);
		Log.d("COMMAND", "CMD: " + cmd);
		
		/**
		 * Hvis kommandoen er at noen ble kastet ut
		 */
		if(cmd.equals(ADMIN_COMMAND_DROP))
		{
			int dropId = Integer.parseInt(extras.getString(TAG_ID));//Id på den som ble kastet ut
			Configuration conf = Configuration.getCurrentConfiguration(context); //Finner konfigurasjon
			if(conf.getId() == dropId) //Hvis det var du som ble kastet ut
			{
				conf.setRegistered(false); //setter registrert til false
				conf.commit(context);//Lagrer
				
				//Bruker Mainactivitys handler til å oppdatere tab. Kartet skal ikke vises lengre, men registrerings fragmentet skal vises
				MainActivity.handler.post(new Runnable()
				{
					@Override
					public void run() 
					{
						GTTabListener.initTabs(MainActivity.activity);
					}
					
				});
			}
		}
	}

	
	@Override
	protected void onRegistered(Context arg0, String regId) 
	{
		Log.d("REGISTER", "called onRegister");
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) 
	{
		// TODO Auto-generated method stub
		
	}
}
