package com.gpstracker.log;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gpstracker.R;
import com.gpstracker.conf.Configuration;
import com.gpstracker.gcm.ServiceTestClass;
import com.gpstracker.tab.GTTabListener_v4;

public class LogFragment_v4 extends Fragment implements OnClickListener
{
	
	private static final String PREF_NAME = "preferes";
	private static final String TYPES = "type";
	private static final String SENDERS = "sender";
	private static final String MESSAGES = "msg";
	private static final String COLOR = "color";
	private static final String COUNT = "count";

	private static LogArrayAdapter logArrayAdapter;
	private static View view;
	public static Context context;
	public static Handler handler = new Handler();
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_log, container, false);
		updateList(getActivity());
		context = getActivity();
		
		Button btn = (Button)view.findViewById(R.id.button_actions_send);
		Configuration conf = Configuration.getCurrentConfiguration(getActivity());
		if(!conf.getRegistered())
			btn.setEnabled(false);
		
		btn.setOnClickListener(this);
		
		Button unregister = (Button)view.findViewById(R.id.v4button_unregister);
		unregister.setVisibility(View.VISIBLE);
		unregister.setOnClickListener(this);
		
		return view;
	}
	
	private static LogItem[] getLogItems(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, 0);
		int count = sp.getInt(COUNT, 0);
		LogItem[] items = new LogItem[count];
		
		if(count == 0)
			return new LogItem[]{new LogItem(	context.getResources().getString(R.string.log_default_action), 
												context.getResources().getString(R.string.log_default_sender), 
												context.getResources().getString(R.string.log_default_message))};
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
	
	public static void updateList(Context context)
	{
		LogItem[] items = getLogItems(context);
		
		logArrayAdapter = new LogArrayAdapter(context, R.layout.log_fragment_row, items);
		ListView list = (ListView)view.findViewById(R.id.listView_actions);
		list.setAdapter(logArrayAdapter);
		list.setSelection(logArrayAdapter.getCount() - 1);
	}
	
	public static void addLogItem(Context context, LogItem item)
	{
		try
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
			updateList(context);
		} catch (NullPointerException e)
		{
			Log.e("LogFragment.addLogItem", "Nullref");
		}
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId() == R.id.button_actions_send)
		{
			EditText receiver = (EditText)(view.findViewById(R.id.editText_actions_send));
			EditText message = (EditText)(view.findViewById(R.id.editText_actions_message));
			String receiverText = receiver.getText().toString();
			String messageText = message.getText().toString();
				
			if(messageText.equals(""))
				Toast.makeText(getActivity(), getResources().getString(R.string.log_fragment_message_not_set), Toast.LENGTH_SHORT).show();
			else
			{
				ServiceTestClass.sendMessage(
						Configuration.getCurrentConfiguration(getActivity()).getId(), 
						messageText, receiverText);
				
				message.setText("");
				receiver.setText("");
			}
		} else if(v.getId() == R.id.v4button_unregister)
		{
			ServiceTestClass.unRegister(getActivity());
			GTTabListener_v4.initTabs(getActivity());
		}
	}
}
