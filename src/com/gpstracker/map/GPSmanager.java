package com.gpstracker.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Toast;

/**
 * Brukes til og lage alert dialoger for sjekking av GPs forbindelse
 * @author 
 *
 */
public class GPSmanager {
	
	//TrackerMapActivity context;
	
	View v;
	
	/**
	 * Constructor
	 * @param title
	 * @param msg
	 * @param posMSG
	 * @param negMSg
	 * @param context
	 */
	public GPSmanager(String title,String msg, String posMSG, String negMSg, final TrackerMapActivity context){
			
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		//setter tittel
		alertDialogBuilder.setTitle(title);
		
		//setter dialog beskjeden
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton(posMSG, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent callGPSSettingIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(callGPSSettingIntent);

				}
			})
			.setNegativeButton(negMSg, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dialog.cancel();
					
				}
			});
		
		AlertDialog ad = alertDialogBuilder.create();
		
		ad.show();
			
		
	}//end constructor
	
	
	
}//end class
