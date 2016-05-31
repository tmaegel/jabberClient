package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;

import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Service;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.ServiceConnection;
import android.content.BroadcastReceiver;

import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;

import android.support.v4.content.LocalBroadcastManager;

public class MainActivity extends Activity {

	public static MainActivity instance = null;

	public NotificationService notificationService;

	// public references
	public ConversationActivity convAct;
	// public Network net;
	public Session session;
	// public SQLController dbCon;

	// intents
	public Intent convInt;
	public Intent setStatInt;
	public Intent prefInt;

	public ListAdapter listAdapter;
	public ArrayList<Contact> contacts;

	boolean isBound = false;

	/**
	 * REQUEST CODE OF CHILD ACTIVITIES
	 */
	static final int ADD_ROSTER_ITEM 		= 1;	/**< Add roster item */
	static final int DEL_ROSTER_ITEM 		= 2;	/**< Delete roster item */
	static final int UPD_ROSTER_ITEM 		= 3;	/**< Update roster item */
	static final int START_CONVERSATION 	= 4;	/**< Update roster item */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		instance = this;

		session = new Session("user1", "localhost", "my-resource", "123456");

		// database
		// dbCon = new SQLController(main);
		// dbCon.insert("user1@localhost", "TEST1", "GROUP A");
		// dbCon.insert("user2@localhost", "TEST2", "GROUP B")
		// contacts = dbCon.fetch();

		setStatInt = new Intent(this, SetStatusActivity.class);
		prefInt = new Intent(this, PreferencesActivity.class);

		// Contact list
		contacts = new ArrayList<Contact>();
        ListView list = (ListView)findViewById(R.id.list);
		listAdapter = new ListAdapter(this, contacts);
        list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String jid = contacts.get(position).getJid();
				convInt = new Intent(view.getContext(), ConversationActivity.class);
				convInt.putExtra("jid", jid);
				startActivityForResult(convInt, START_CONVERSATION);
			}
		});

		// Network
		/*ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()) {
			Log.i(Constants.LOG_TAG, "> Network is ready");
			net = new Network(this);
    		net.execute(/*sleepTime*///);
		/*} else {
			Log.i(Constants.LOG_TAG, "> No network available");
		}*/
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection myConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
			notificationService = binder.getService();
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}
	};

	// handler for received Intents for the event
	private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(Constants.LOG_TAG, "> Receive broadcast");

			Message message = (Message)intent.getSerializableExtra("message");
			if(message != null) {
				Log.d(Constants.LOG_TAG, "> Push message to history");
				Log.d(Constants.LOG_TAG, "" +  message);
				//intent.putExtra("message", net.message);
				//LocalBroadcastManager.getInstance(MainActivity.instance).sendBroadcast(intent);
				pushMessageToHistory(message);
				return;
			}

			Contact contact = (Contact)intent.getSerializableExtra("roster");
			if(contact != null) {

				Log.d(Constants.LOG_TAG, "> Push contact to list");
				contacts.add((Contact)intent.getSerializableExtra("roster"));
				listAdapter.notifyDataSetChanged();
				return;
			}


		}
	};

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
			/** Add contact */
			case R.id.opt_add_contact:
				Intent addContInt = new Intent(this, AddContactActivity.class);
				startActivityForResult(addContInt, ADD_ROSTER_ITEM);
				return true;
			/** Add conference */
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
	 * Push message to conversation activity
	 */
	public void pushMessageToHistory(Message message) {
		Intent intent = new Intent("receive-message");
		intent.putExtra("message", message);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	/**
	 * Get results of child activities
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case ADD_ROSTER_ITEM:
				if (resultCode == RESULT_OK) {
					XMPP.setRoster(new Contact(data.getStringExtra("jid"), data.getStringExtra("name"), data.getStringExtra("group")));
				}
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		instance = this;

		LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, new IntentFilter("service-broadcast"));

		Intent intent = new Intent(this, NotificationService.class);
		bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
		startService(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		// instance = null;

		LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceReceiver);

		if (isBound) {
            unbindService(myConnection);
            isBound = false;
        }
	}
}
