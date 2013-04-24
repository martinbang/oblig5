package com.gpstracker.log;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gpstracker.R;

public class LogFragment extends Fragment
{
	private static final String PREF_NAME = "preferes";
	private static final String TYPES = "type";
	private static final String SENDERS = "sender";
	private static final String MESSAGES = "msg";
	private static final String COLOR = "color";
	private static final String COUNT = "count";

	public static Handler handler = new Handler();
	public static LogArrayAdapter logArrayAdapter;
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_log, container, false);
		
		Editor editor = getActivity().getSharedPreferences(PREF_NAME, 0).edit();
		editor.clear();
		editor.commit();
		
		LogItem[] items = getLogItems();
		
		logArrayAdapter = new LogArrayAdapter(getActivity(), R.layout.log_fragment_row, items);
		ListView list = (ListView)view.findViewById(R.id.listView_actions);
		list.setAdapter(logArrayAdapter);
		
		return view;
	}
	
	private LogItem[] getLogItems()
	{

		SharedPreferences sp = getActivity().getSharedPreferences(PREF_NAME, 0);
		int count = sp.getInt(COUNT, 0);
		LogItem[] items = new LogItem[count];
		
		if(count == 0)
			return new LogItem[]{new LogItem(	getResources().getString(R.string.log_default_action), 
												getResources().getString(R.string.log_default_sender), 
												getResources().getString(R.string.log_default_message))};
		else
			for(int i = 0; i < items.length; i++)
				items[i] = new LogItem
						(
						sp.getString(TYPES + i, ""), 
						sp.getString(SENDERS + i, ""), 
						sp.getString(MESSAGES + i, ""),
						sp.getInt(COLOR + i, -16776961)
						);
		return items;
	}
	
//	public static void addLogItem(LogArrayAdapter adapter, Context context, LogItem item)
//	{
////		try
////		{
////			adapter = fragment.logArrayAdapter;
////		}catch(NullPointerException e)
////		{
////			Log.d("NULLPOINT", "Adapter ble ikke satt, view vil ikke oppdateres");
////		}
////		
//		addLogItem(context, item);
//		
//		adapter.notifyDataSetChanged();
//	}
	

	
	public static void addLogItem(Context context, LogItem item)
	{
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, 0);
		Editor edit = sp.edit();
		int count = sp.getInt(COUNT, 0);
		
		edit.putString(TYPES + count, item.action);
		edit.putString(SENDERS + count, item.sender);
		edit.putString(MESSAGES + count, item.message);
		edit.putInt(COLOR + count, item.color);
		edit.putInt(COUNT, count + 1);
		edit.commit();
	}
}
