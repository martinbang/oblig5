package com.gpstracker.log;

import com.gpstracker.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LogArrayAdapter extends ArrayAdapter<LogItem>
{
	private Context context;
	private int layoutResourceId;
	private LogItem[] items;
	
	public LogArrayAdapter(Context context, int layoutResourceId, LogItem[] items)
	{
		super(context, layoutResourceId, items);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		ItemHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ItemHolder();
			holder.action = (TextView)row.findViewById(R.id.textViewActionContent);
			holder.sender = (TextView)row.findViewById(R.id.textViewSenderContent);
			holder.message = (TextView)row.findViewById(R.id.textViewMessageContent);
			
			
			row.setTag(holder);
		} else
		{
			holder = (ItemHolder)row.getTag();
		}
		
		LogItem item = items[position];
		holder.action.setText(item.action);
		holder.sender.setText(item.sender);
		holder.message.setText(item.message);
		
		return row;
	}
	
	private static class ItemHolder
	{
		TextView action;
		TextView sender;
		TextView message;
	}
}
