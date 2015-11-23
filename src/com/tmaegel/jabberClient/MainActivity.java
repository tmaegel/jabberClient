package com.tmaegel.jabberClient;

import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.TabActivity;

import android.content.Intent;
import android.content.res.Resources;

import android.view.Menu;

import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	private static final String TAG = "jabberClient"; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Log.i(TAG, "starting jabberClient");
		// Log.d(TAG, "starting jabberClient...");
		
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
		TabSpec contactsTab = tabHost.newTabSpec(getResources().getString(R.string.tab_contacts));
		TabSpec conversationsTab = tabHost.newTabSpec(getResources().getString(R.string.tab_conversations));
        
		// contacts tab
		contactsTab.setIndicator(getResources().getString(R.string.tab_contacts));
		contactsTab.setContent(new Intent(this, ListContactsActivity.class));
        
        // conversations tab
		conversationsTab.setIndicator(getResources().getString(R.string.tab_conversations));
		conversationsTab.setContent(new Intent(this, ListConversationsActivity.class));

		// add tabs 
        tabHost.addTab(contactsTab);
        tabHost.addTab(conversationsTab);
	}

	/** Called when create menu  */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);

		return true;
	}

	/** Listening to click on menu items */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			/** Start conversation */
			case R.id.opt_start_conversation:
				Intent convActivity = new Intent(this, ConversationActivity.class);
				startActivity(convActivity);
				return true;
			/** Add contact */
			case R.id.opt_add_contact:
				
				return true;
			/** Add conversation */
			case R.id.opt_add_conference:
				
				return true;
			/** Set status */
			case R.id.opt_set_status:
				
				return true;
			/** Preferences */
			case R.id.opt_settings:
				Intent prefActivity = new Intent(this, PreferencesActivity.class);
				startActivity(prefActivity);
				return true;
			/** Default */
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/** Called when rebuild activity, switch in landscape mode */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	
	}
}
