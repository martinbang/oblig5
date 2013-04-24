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
	
	
	private final String ADMIN_COMMAND_KICK = "kick";
	
	@Override
	protected void onError(Context arg0, String arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) 
	{
		String message = "";
		String adminCommand = "";
		int id = -1;
		
		Bundle extras = intent.getExtras();//.getString(TAG_MESSAGE);
		
		message += extras.getString(TAG_MESSAGE);
		adminCommand += extras.getString(TAG_ADMIN_COMMAND);
		try
		{
			id = Integer.parseInt(extras.getString(TAG_ID));
		}catch(NumberFormatException e)
		{
			Log.e("MELDING", e.getMessage());
		}
		
		Log.d("COMMAND", "CMD: " + adminCommand);
		Log.d("MESSAGE", "MSG: " + message);
		
		
		if(id != -1)
			handleId(context, id);
		if(!message.equals(""))
			handleMessage(context, extras);
		if(!adminCommand.equals(""))
			handleAdminCommand(context, adminCommand);
	}
	
	private void handleId(Context context, int id)
	{
		Log.d("HandleID", "Ny id: " + id);
		Configuration conf = Configuration.getCurrentConfiguration(context);
		conf.setId(id);
		conf.commit(context);
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
			//LogFragment.addLogItem(context, item);
			LogFragment.handler.post(new Runnable()
			{
				@Override
				public void run() 
				{
					LogFragment.addLogItem(context, item);
					LogFragment.logArrayAdapter.notifyDataSetChanged();
				}
				
			});
		} catch(NullPointerException e)
		{
			Log.e("MESSAGE", "nullreferanse, melding gikk ikke gjennom");
		}
	}
	
	private void handleAdminCommand(Context context, String adminCmd)
	{
		if(adminCmd.equals(ADMIN_COMMAND_KICK))
		{
			Configuration conf = Configuration.getCurrentConfiguration(context);
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
