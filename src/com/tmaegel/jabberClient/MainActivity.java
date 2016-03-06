package com.tmaegel.jabberClient;

import android.os.Bundle;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.app.ActionBar.Tab;
import android.app.Activity;
// import android.app.TabActivity;
import android.app.ListActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

// import android.widget.TabHost;
// import android.widget.TabHost.TabSpec;

import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;

public class MainActivity /*extends TabActivity*/ extends ListActivity {

	// public references
	public static MainActivity main;
	public static ConversationActivity convAct;
	public static Network net;
	public SQLController dbCon;

	private TextView content;

	// intents
	public static Intent convInt;
	public static Intent prefInt;

	private static final String TAG = "jabberClient";
	
	public static List<Contact> contactList = new ArrayList();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		main = this;

		// database
		dbCon = new SQLController(main);
		// dbCon.insert("test1@localhost", "TEST1", "GROUP A");
		// dbCon.insert("test2@localhost", "TEST2", "GROUP B");
		// dbCon.insert("test3@localhost", "TEST3", "GROUP C");
		contactList = dbCon.fetch();

		content = (TextView)findViewById(R.id.contact);

		String[] elements = new String[MainActivity.contactList.size()];
		for(int i = 0; i < MainActivity.contactList.size(); i++) {
			elements[i] = MainActivity.contactList.get(i).getName() + " (" + MainActivity.contactList.get(i).getGroup() + ")";
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, elements);
		// Assign adapter to list
		setListAdapter(adapter);
		
		convInt = new Intent(this, ConversationActivity.class);
		prefInt = new Intent(this, PreferencesActivity.class);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Intent convActivity = new Intent(this, convActivity.class);
				startActivity(convInt);
			}
		});

		// Check network status
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()) {
			Log.i(TAG, "Network is ready");
			net = new Network();
    		net.execute(/*sleepTime*/);		
		} else {
			Log.i(TAG, "No network available");
		}
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
				startActivity(convInt);
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
				startActivity(prefInt);
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

	/*public static getInstance() {
		return instance;
	}*/
}
