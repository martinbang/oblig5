package com.gpstracker.log;

public class LogItem 
{
	public String action;
	public String sender;
	public String message;
	
	public LogItem(String action, String sender, String message)
	{
		this.action = action;
		this.sender = sender;
		this.message = message;
	}
}
