package com.gpstracker.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

/**
 * Brukes til og lage alert dialoger for sjekking av GPs forbindelse
 * @author 
 *
 */
public class GPSmanager {
	
	
	View v;
	
	/**
	 * Constructor
	 * @param title
	 * @param msg
	 * @param posMSG
	 * @param negMSg
	 * @param context
	 */
	public GPSmanager(String title,String msg, String posMSG, String negMSg, final Context context){
			
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		//setter tittel
		alertDialogBuilder.setTitle(title);
		
		//setter dialog beskjeden
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton(posMSG, new DialogInterface.OnClickListener() {
				//setter en onclick for "YES" button
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(callGPSSettingIntent);

				}
			})
			//setter en Onclick for "NO" button
			.setNegativeButton(negMSg, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dialog.cancel();
				}
			});
		//lager dialogen
		AlertDialog ad = alertDialogBuilder.create();
		
		//viser dialogen
		ad.show();
			
		
	}//end constructor
	
}//end class
