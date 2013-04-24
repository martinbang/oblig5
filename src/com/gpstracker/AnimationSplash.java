	package com.gpstracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Klassen brukes for å lage en animasjon som vises i et ImageView
 * Imageview brukes i Layoutfilen til {@link splash_layout}
 * Splash_layout brukes av SplascreenActivity.class.activity
 * 
 *
 */
public class AnimationSplash extends ImageView{
	
    private Context mContext;

    int x = -1;
    int y = -1;

    private int xVelocity = 10;
    private int yVelocity = 10;

    private Handler h;
    private final int FRAME_RATE = 30;
    
    /**
     * Constructor
     * @param context
     * @param attrs
     */
	public AnimationSplash(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
        mContext = context;
        h = new Handler();
	}//end constructor
	
	private Runnable r  = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
				invalidate();//caller invalidate for å tegne fra onDraw()
			
		}//end run
	};

	/** (non-Javadoc)
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		/**
		 * loading png er hentet fra www.iconfinder.com, fult lovilg å bruke vis ikke applikasjonen 
		 * skal publiseres på google play, Da må det kjøpes lisens: ref http://creativecommons.org/licenses/by-nc-sa/3.0/
		 */
		BitmapDrawable	bd = (BitmapDrawable)mContext.getResources().getDrawable(R.drawable.loading);
		//bevegelses likning
		if (x < 0 && y < 0) {
			x = this.getWidth() / 2;
			y = this.getHeight() / 2;
		} else {
			x += xVelocity;
			y += yVelocity;

			if ((x > this.getWidth() - bd.getBitmap().getWidth()) || (x < 0)) {
				xVelocity = xVelocity * -1;
			}

			if ((y > this.getHeight() - bd.getBitmap().getHeight()) || (y < 0)) {
				yVelocity = yVelocity * -1;
			}

       }
		//tegner Bitmap
       canvas.drawBitmap(bd.getBitmap(), x, y, null);
       h.postDelayed(r, FRAME_RATE);
	}//end ondraw
	
}//end class
