package com.tmaegel.jabberClient;

import android.os.Bundle;

import android.app.Activity;
import android.app.ListActivity;

import android.widget.TextView;
import android.widget.ArrayAdapter;

public class ListConversationsActivity extends ListActivity {

	TextView content;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_conversations);

		content = (TextView)findViewById(R.id.conversation);

		String[] values = new String[] {
			"Conversations 01",
			"Conversations 02",
			"Conversations 03",
			"Conversations 04",
			"Conversations 05"
		};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        
		// Assign adapter to list
		setListAdapter(adapter); 
	}

	/** Called when rebuild activity, switch in landscape mode */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	
	}
}
