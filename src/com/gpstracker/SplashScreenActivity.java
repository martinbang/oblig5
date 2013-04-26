package com.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Klassen brukes for � vise animasjoner og kj�rer animasjonen i en egen tr�d.
 *Klassen kan kj�re flere animasjoner om �nsket.
 */
public class SplashScreenActivity extends Activity {
	
	//tag for LOG, brukes for � sjekke at ting kj�rer
	private static String TAG = SplashScreenActivity.class.getName();
	//sleep timer er  tiden tr�den "sover", f�r den kj�rer en intent � skifter aktivitet
	private static long SLEEP = 3;
	
	/** (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//fjerner Tittelen(titlebar)
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//fjerner Notification baren
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		//setter view til aktivitetn
		setContentView(R.layout.splash_layout);
		
		IntentLauncher il = new IntentLauncher();
		il.start();
		
	}//end
	
	/**
	 * Inner klass for kj�ring av splash i egen tr�d
	 * @author Martin
	 *
	 */
	private class IntentLauncher extends Thread{
		@Override
		public void run(){
			try{
				
				Thread.sleep(SLEEP);
				
			}catch(Exception e){
				Log.v(TAG,e.getMessage());
			}
			//lager nytt intent object, som avslutter aktivitetn og startet MainActivity.class
			Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
			SplashScreenActivity.this.startActivity(intent);
			SplashScreenActivity.this.finish();
		}//end run
	}//inner class
}//end class
