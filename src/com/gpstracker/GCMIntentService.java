package com.gpstracker;

import com.google.android.gcm.GCMBaseIntentService;
import com.gpstracker.gcm.ServiceTestClass;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GCMIntentService extends GCMBaseIntentService
{

	@Override
	protected void onError(Context arg0, String arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) 
	{
		Log.d("MESSAGE", "Mottok beskjed");
		Log.d("MESSAGE", arg1.getExtras().getString("msg"));
	}

	@Override
	protected void onRegistered(Context arg0, String regId) 
	{
		Log.d("REGISTER", "called onRegister");
		ServiceTestClass.register(this, "name", "email", regId);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) 
	{
		// TODO Auto-generated method stub
		
	}
	


}
