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
import com.gpstracker.gcm.ServiceClass;
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
		updateList(getActivity()); //Oppdaterer logitems lista
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
	
	/**
	 * Henter LogItems fra shared preferences. 
	 * legger alle logitems i en array
	 * @param context
	 * @return
	 */
	private static LogItem[] getLogItems(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, 0);
		int count = sp.getInt(COUNT, 0); //Henter logitems fra lista
		LogItem[] items = new LogItem[count];
		
		if(count == 0) //Oppretter en melding slik at det ikke skal v�re tomt
			return new LogItem[]{new LogItem(	context.getResources().getString(R.string.log_default_action), 
												context.getResources().getString(R.string.app_name), 
												context.getResources().getString(R.string.log_default_message))};
		else
			for(int i = 0; i < items.length; i++) //Legger items inn i array
				items[i] = new LogItem
						(
						sp.getString(TYPES + i, ""), 
						sp.getString(SENDERS + i, ""), 
						sp.getString(MESSAGES + i, ""),
						sp.getInt(COLOR + i, -16776961)
						);
		return items;
	}
	
	/**
	 * Henter logitems fra shared preferences, Putter de i lista som vises i fragmentet, 
	 * Setter adapter til � v�re v�r spesialbygde adapter. scroller lista ned
	 * @param context
	 */
	public static void updateList(Context context)
	{
		LogItem[] items = getLogItems(context); //Henter log items
		
		logArrayAdapter = new LogArrayAdapter(context, R.layout.log_fragment_row, items); //Oppretter en logArrayAdapter
		ListView list = (ListView)view.findViewById(R.id.listView_actions); //Henter ut listviewet
		list.setAdapter(logArrayAdapter); //setter adapter til listviewet
		list.setSelection(logArrayAdapter.getCount() - 1); //Scroller ned
	}
	
	/**
	 * Legger til ett logitem i lista over logitems i shared preferences
	 * @param context
	 * @param item
	 */
	public static void addLogItem(Context context, LogItem item)
	{
		try
		{
			SharedPreferences sp = context.getSharedPreferences(PREF_NAME, 0);
			Editor edit = sp.edit();
			int count = sp.getInt(COUNT, 0);
			
			/*fordeler parametrene og setter de i p� hver sin plass*/
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

	/**
	 * Det som skjer n�r bruker trykker p� send
	 */
	@Override
	public void onClick(View v) 
	{
		if(v.getId() == R.id.button_actions_send)
		{
			EditText receiver = (EditText)(view.findViewById(R.id.editText_actions_send));
			EditText message = (EditText)(view.findViewById(R.id.editText_actions_message));
			String receiverText = receiver.getText().toString();
			String messageText = message.getText().toString();
				
			if(messageText.equals(""))//Skal ikke sende om messagefeltet er tomt
				Toast.makeText(getActivity(), getResources().getString(R.string.log_fragment_message_not_set), Toast.LENGTH_SHORT).show();
			else
			{	//Sender melding
				ServiceClass.sendMessage(
						Configuration.getCurrentConfiguration(getActivity()).getId(), 
						messageText, receiverText);
				if(!receiverText.equals(""))//Hvis dette var en privat melding
				{
					LogItem item = new LogItem(getResources().getString(R.string.incomming_message_type_2) + "=> " 
							+ receiverText, Configuration.getCurrentConfiguration(getActivity()).getUserName(), messageText);
					LogFragment.addLogItem(item); //Logges meldingen hos denne enheten
				}
				//Feltene t�mmes
				message.setText("");
				receiver.setText("");
			}
		} else if(v.getId() == R.id.v4button_unregister)//Avregistreringsknapp. brukes kun p� eldre telefoner
		{
			ServiceClass.unRegister(getActivity());
			GTTabListener_v4.initTabs(getActivity());
		}
	}
}
