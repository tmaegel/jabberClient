package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

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
	public static Intent addContInt;
	public static Intent setStatInt;
	public static Intent prefInt;

	public static ArrayAdapter<String> listAdapter;
	public static List<String> contactList = new ArrayList<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		main = this;

		// database
		// dbCon = new SQLController(main);
		// dbCon.insert("user1@localhost", "TEST1", "GROUP A");
		// dbCon.insert("user2@localhost", "TEST2", "GROUP B")
		// contacts = dbCon.fetch();

		content = (TextView)findViewById(R.id.contact);

		/**
		 * Contact list
		 */
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactList);
		setListAdapter(listAdapter);

		convInt = new Intent(this, ConversationActivity.class);
		addContInt = new Intent(this, AddContactActivity.class);
		setStatInt = new Intent(this, SetStatusActivity.class);
		prefInt = new Intent(this, PreferencesActivity.class);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Intent convActivity = new Intent(this, convActivity.class);
				String jid = ((TextView)view).getText().toString();
				convInt.putExtra("jid", jid);
				startActivity(convInt);
			}
		});

		// Check network status
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()) {
			Log.i(Constants.LOG_TAG, "> Network is ready");
			net = new Network();
    		net.execute(/*sleepTime*/);
		} else {
			Log.i(Constants.LOG_TAG, "> No network available");
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
				convInt.putExtra("jid", "0");
				startActivity(convInt);
				return true;
			/** Add contact */
			case R.id.opt_add_contact:
				startActivity(addContInt);
				return true;
			/** Add conversation */
			/*case R.id.opt_add_conference:

				return true;*/
			/** Set status */
			case R.id.opt_set_status:
				startActivity(setStatInt);
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

	public static void updateContactList() {
		Log.d(Constants.LOG_TAG, "Update " + net.stanza.items.size() + " contacts");

		contactList.clear();
		for (int i = 0; i < net.stanza.items.size(); ++i) {
			contactList.add(net.stanza.items.get(i).name + "," + net.stanza.items.get(i).jid + ","+net.stanza.items.get(i).group + ","+net.stanza.items.get(i).subscription);

		}
		listAdapter.notifyDataSetChanged();
	}
}
