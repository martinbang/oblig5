package com.gpstracker.log;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gpstracker.R;

public class LogFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_log, container, false);
		
		LogItem[] testItems = new LogItem[]
		{
			new LogItem("privat-melding", "Bjarne", "Hei"),
			new LogItem("kringkasting", "admin", "ingen prating takk!"),
			new LogItem("kringkasting", "Bjarne", "hvorfor det?"),
			new LogItem("utkastelse", "admin", "Du har blitt kastet ut")
		};
		
		LogArrayAdapter adapter = new LogArrayAdapter(getActivity(), R.layout.log_fragment_row, testItems);
		ListView list = (ListView)view.findViewById(R.id.listView_actions);
		list.setAdapter(adapter);
		
		return view;
	}

}
