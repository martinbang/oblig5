package com.gpstracker.log;

import android.graphics.Color;
import android.util.Log;

public class LogItem 
{
	public String action;
	public String sender;
	public String message;
	public int color;
	
	public LogItem(String action, String sender , String message, int color)
	{
		this.action = action;
		this.sender = sender;
		this.message = message;
		this.color = color;
	}
	
	public LogItem(String action, String sender, String message, String color)
	{
		this.action = action;
		this.sender = sender;
		this.message = message;

		
		this.color = parseColorString(color);
	}
	
	public static int parseColorString(String color)
	{
		String[] rgb = color.split(",");
		int[] rgbIntValues = new int[3];
		try
		{
			rgbIntValues[0] = Integer.parseInt(rgb[0]);
			rgbIntValues[1] = Integer.parseInt(rgb[1]);
			rgbIntValues[2] = Integer.parseInt(rgb[2]);
		} catch(NumberFormatException e)
		{
			Log.e("NUMBERFORAT", e.getMessage());
		}
		
		return Color.argb(255, rgbIntValues[0], rgbIntValues[1], rgbIntValues[2]);
	}
	
	public LogItem(String action, String sender, String message)
	{
		this.action = action;
		this.sender = sender;
		this.message = message;
		this.color = Color.argb(255, 0, 0, 0);
	}
}
