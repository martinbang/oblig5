package com.gpstracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

import com.google.android.gcm.GCMBaseIntentService;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceClass;
import com.gpstracker.log.LogFragment;
import com.gpstracker.log.LogItem;
import com.gpstracker.tab.GTTabListener;

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
	private final String TAG_NAME = "name";
	
	private final String ADMIN_COMMAND_DROP = "drop";
	private final String ADMIN_COMMAND_MESSAGE = "message";
	private final String ADMIN_COMMAND_POS = "pos";
	private final String ADMIN_COMMAND_ID = "id";
	private final String ADMIN_COMMAND_NEW_USER = "new";
	
	@Override
	protected void onError(Context arg0, String arg1) 
	{
		
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) 
	{
		Bundle extras = intent.getExtras();//henter extras fra intent.
		String cmd = extras.getString(TAG_ADMIN_COMMAND);
		
		if(cmd.equals(ADMIN_COMMAND_DROP))
			handleAdminCommand(context, extras);//Behandler eventuelle kommandoer fra admin
		else if(cmd.equals(ADMIN_COMMAND_ID))
			handleId(context, extras);//Om en id kommer alene, behandles den her
		else if(cmd.equals(ADMIN_COMMAND_MESSAGE))
			handleMessage(extras);//behandler beskjeder
		else if(cmd.equals(ADMIN_COMMAND_POS))
			handlePos(context, extras);//Oppdaterer posisjon
		else if(cmd.equals(ADMIN_COMMAND_NEW_USER))
			handleNewUser(extras);//Registrerer at en ny bruker ble lagt til
			
	}
	
	private void handleNewUser(Bundle extras)
	{
		String name = extras.getString(TAG_NAME);
		if(!Configuration.getCurrentConfiguration(this).getUserName().equals(name))
		{
			LogItem item = new LogItem(getResources().getString(R.string.incomming_message_type_3), getResources().getString(R.string.app_name), 
					name + " " + getResources().getString(R.string.log_logged_out));
			LogFragment.addLogItem(item);
		}
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
			String colorStr = extras.getString(TAG_COLOR);//inkommende farge er p� formatet "12,234,45". 
			int color = LogItem.parseColorString(colorStr);
			
			ServiceClass.positionUpdate(latitude, longtitude, id, color);
			
			Log.d("POS", "Ny posisjon id:" + id + " lat: " + latitude + "lng: " + longtitude + " color: " + colorStr);
			
		} 
		catch(NumberFormatException e){Log.d("POS", e.getMessage());}
		catch(NullPointerException ne){Log.d("POS", "Nullpointer");}
	}

	/**
	 * Skjekker om inkommende intent har en id. Hvis den har det og det ikke er 
	 * en kommando for � kaste noen ut, skal brukers id settes lik denne
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
			
			LogItem item = new LogItem(getResources().getString(R.string.incomming_message_type_3), getResources().getString(R.string.app_name), "du logget inn");
			LogFragment.addLogItem(item);
			
		} catch(NumberFormatException e){}
	}
	
	/**
	 * Skjekker om intent inneholder en melding som skal logges. Logger hvis den gj�r det
	 * @param context
	 * @param extras
	 */
	private void handleMessage(Bundle extras)
	{
		try
		{
			String msg = extras.getString(TAG_MESSAGE);//Meldingens innhold
			String action = extras.getString(TAG_DESCRIPTION); //om det er en privat melding eller en offentlig melding
			String sender = extras.getString(TAG_SENDER);//hvem som sendte meldingen
			String color = extras.getString(TAG_COLOR);//Fargekoden til avsender
			String type;
			
			if(action.equals("1"))//Finner ut om det er en privat eller offentlig melding
				type = this.getResources().getString(R.string.incomming_message_type_1);
			else type = this.getResources().getString(R.string.incomming_message_type_2);
			
			final LogItem item = new LogItem(type, sender, msg, color);//Oppretter ett loggbart item
			if(!action.equals("null"))
				LogFragment.addLogItem(item);
				
		} catch(NullPointerException e){}//Om det skjer en 
	}
	
	/**
	 * Behandler inkommende kommandoer fra admin, hvis det er noen
	 * @param context
	 * @param extras
	 */
	private void handleAdminCommand(final Context context, Bundle extras)
	{
		String cmd = "";
		cmd += extras.getString(TAG_ADMIN_COMMAND);
		Log.d("COMMAND", "CMD: " + cmd);
		
		/**
		 * Hvis kommandoen er at noen ble kastet ut
		 */
		if(cmd.equals(ADMIN_COMMAND_DROP))
		{
			int dropId = Integer.parseInt(extras.getString(TAG_ID));//Id p� den som ble kastet ut
			Configuration conf = Configuration.getCurrentConfiguration(context); //Finner konfigurasjon
			if(conf.getId() == dropId) //Hvis det var du som ble kastet ut
			{
				//TODO
				conf.setRegistered(false); //setter registrert til false
				conf.commit(context);//Lagrer
				
				LogItem item = new LogItem(this.getResources().getString(R.string.incomming_message_type_4), 
						this.getResources().getString(R.string.app_name), getResources().getString(R.string.log_you_logged_out));
				LogFragment.addLogItem(item);
				
				//Bruker Mainactivitys handler til � oppdatere tab. Kartet skal ikke vises lengre, men registrerings fragmentet skal vises
				MainActivity_v11.handler.post(new Runnable()
				{
					@Override
					public void run() 
					{
						//hvis du blir kastet ut fra serveren, logges man ut og blir sendt til MainActivity
						Intent i  = new Intent(context, MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);
					}
				});
			} else
			{
				LogItem item = new LogItem(getResources().getString(R.string.incomming_message_type_4), getResources().getString(R.string.app_name), 
						getResources().getString(R.string.log_user) + extras.getString(TAG_NAME) + getResources().getString(R.string.log_logged_out));
				LogFragment.addLogItem(item);
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
