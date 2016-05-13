package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;

import android.util.Log;
import java.util.List;
import java.util.ArrayList;

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

public class MainActivity /*extends TabActivity*/ extends Activity {

	// public references
	public MainActivity main;
	public ConversationActivity convAct;
	public Network net;
	// public SQLController dbCon;

	// intents
	public Intent convInt;
	public Intent setStatInt;
	public Intent prefInt;

	public ListAdapter listAdapter;
	public ArrayList<Contact> contacts;

	/**
	 * REQUEST CODE OF CHILD ACTIVITIES
	 */
	static final int ADD_ROSTER_ITEM = 1;	/**< Add roster item */
	static final int DEL_ROSTER_ITEM = 2;	/**< Delete roster item */
	static final int UPD_ROSTER_ITEM = 3;	/**< Update roster item */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// database
		// dbCon = new SQLController(main);
		// dbCon.insert("user1@localhost", "TEST1", "GROUP A");
		// dbCon.insert("user2@localhost", "TEST2", "GROUP B")
		// contacts = dbCon.fetch();

		// content = (TextView)findViewById(R.id.contact);

		/**
		 * Contact list
		 */
		// listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactList);
		// setListAdapter(listAdapter);
		// Get ListView from main.xml

		convInt = new Intent(this, ConversationActivity.class);
		setStatInt = new Intent(this, SetStatusActivity.class);
		prefInt = new Intent(this, PreferencesActivity.class);

		// Contact list
		contacts = new ArrayList<Contact>();
        ListView list = (ListView)findViewById(R.id.list);
		listAdapter = new ListAdapter(this, contacts);
        list.setAdapter(listAdapter);

		/*getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Intent convActivity = new Intent(this, convActivity.class);
				String jid = ((TextView)view).getText().toString();
				convInt.putExtra("jid", jid);
				startActivity(convInt);
			}
		});*/

		// Network
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()) {
			Log.i(Constants.LOG_TAG, "> Network is ready");
			net = new Network(this);
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
				Intent addContInt = new Intent(this, AddContactActivity.class);
				startActivityForResult(addContInt, ADD_ROSTER_ITEM);
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

	/**
	 * Refresh contact list
	 */
	public void listUpdate(ArrayList<Contact> items) {
		contacts.clear();
		contacts.addAll(items);
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * Get results of child activities
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case ADD_ROSTER_ITEM:
				if (resultCode == RESULT_OK) {
					net.contact = new Contact(data.getStringExtra("jid"), data.getStringExtra("name"), data.getStringExtra("group"));
					net.sendRequest(Constants.C_ROSTER_SET);
				}
				break;
		}
	}
}
