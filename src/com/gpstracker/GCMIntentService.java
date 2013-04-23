package com.gpstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceTestClass;

public class GCMIntentService extends GCMBaseIntentService
{
	private final String TAG_MESSAGE = "msg";
	
	private final String TAG_ADMIN_COMMAND = "cmd";
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
		
		Bundle extras = intent.getExtras();//.getString(TAG_MESSAGE);
		
		message += extras.getString(TAG_MESSAGE);
		adminCommand += extras.getString(TAG_ADMIN_COMMAND);
		
		Log.d("COMMAND", "CMD: " + adminCommand);
		Log.d("MESSAGE", "MSG: " + message);
		
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
