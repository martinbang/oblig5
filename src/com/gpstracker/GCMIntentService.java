package com.gpstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceTestClass;
import com.gpstracker.log.LogFragment;
import com.gpstracker.log.LogItem;

public class GCMIntentService extends GCMBaseIntentService
{
	private final String TAG_ID = "id";
	
	private final String TAG_MESSAGE = "msg";
	private final String TAG_SENDER = "snd";
	private final String TAG_DESCRIPTION = "des";
	private final String TAG_COLOR = "clr";
	
	private final String TAG_ADMIN_COMMAND = "cmd";
	private final String TAG_POS = "pos";
	
	private final String ADMIN_COMMAND_DROP = "drop";
	
	@Override
	protected void onError(Context arg0, String arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) 
	{
		String message = "";
	
		int id = -1;
		
		Bundle extras = intent.getExtras();//.getString(TAG_MESSAGE);
	
		handleAdminCommand(context, extras);
		
		handleId(context, extras);

		handleMessage(context, extras);
		
	}
	
	private void handleId(Context context, Bundle extras)
	{
		try
		{
			int id = Integer.parseInt(extras.getString(TAG_ID));
			Log.d("HandleID", "Ny id: " + id);
			Configuration conf = Configuration.getCurrentConfiguration(context);
			conf.setId(id);
			conf.commit(context);
		} catch(NumberFormatException e)
		{
			
		}
	}
	
	private void handleMessage(final Context context, Bundle extras)
	{
		try
		{

			String msg = extras.getString(TAG_MESSAGE);
			String action = extras.getString(TAG_DESCRIPTION);
			String sender = extras.getString(TAG_SENDER);
			String color = extras.getString(TAG_COLOR);
			Log.d("MESSAGE", "melding: " + msg + " type: " + action + " sender: " + sender + " farge: " + color);
			final LogItem item = new LogItem(action, sender, msg, color);
			
			LogFragment.handler.post(new Runnable()
			{
				@Override
				public void run() 
				{
					Log.d("HANDLER", "run metoden kjører ");
					LogFragment.addLogItem(LogFragment.context, item);
				}
			});
		} catch(NullPointerException e)
		{
			Log.e("MESSAGE", "nullreferanse, melding gikk ikke gjennom");
		}
	}
	
	private void handleAdminCommand(Context context, Bundle extras)
	{
		String cmd = "";
		cmd += extras.getString(TAG_ADMIN_COMMAND);
		Log.d("COMMAND", "CMD: " + cmd);
		
		if(cmd.equals(ADMIN_COMMAND_DROP))
		{
			int dropId = Integer.parseInt(extras.getString(TAG_ID));
			Configuration conf = Configuration.getCurrentConfiguration(context);
			if(conf.getId() == dropId)
			{
				conf.setRegistered(false);
				conf.commit(context);
				
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
