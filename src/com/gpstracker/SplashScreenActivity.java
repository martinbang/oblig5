package com.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Klassen brukes for å vise animasjoner og kjører animasjonen i en egen tråd.
 *Klassen kan kjøre flere animasjoner om ønsket.
 */
public class SplashScreenActivity extends Activity {
	
	//tag for LOG, brukes for å sjekke at ting kjører
	private static String TAG = SplashScreenActivity.class.getName();
	//sleep timer er  tiden tråden "sover", får den kjører en intent å skifter aktivitet
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
	 * Inner klass for kjøring av splash i egen tråd
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
