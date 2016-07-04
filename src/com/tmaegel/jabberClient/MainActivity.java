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
import android.view.ContextMenu;  
import android.view.ContextMenu.ContextMenuInfo;  

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
import android.widget.AdapterView.AdapterContextMenuInfo;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;

import android.support.v4.content.LocalBroadcastManager;

public class MainActivity extends Activity {

	public static MainActivity instance = null;

	public NotificationService notificationService;
	public Session session;
	public SQLController dbCon;

	// intents
	public Intent convInt;
	public Intent setStatInt;
	public Intent prefInt;

	public ListAdapter listAdapter;
	public ArrayList<Contact> contacts;

	boolean isBound = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		instance = this;
		
		// Database
		dbCon = new SQLController(this);
		
		// Session, only for client to server communication, 5269 for server to server communication
		// session = new Session("user1", "123456", "my-resource", "localhost", "192.168.178.103", 5222);
		session = dbCon.selectSession();
		
		// Service
		LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, new IntentFilter("service-broadcast"));
		Intent intent = new Intent(this, NotificationService.class);
		bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
		startService(intent);

		setStatInt = new Intent(this, SetStatusActivity.class);
		prefInt = new Intent(this, PreferencesActivity.class);

		// Contact list
		contacts = new ArrayList<Contact>();
		contacts.addAll(dbCon.fetchContacts());
        ListView list = (ListView)findViewById(R.id.list);
		listAdapter = new ListAdapter(this, contacts);
        list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String jid = contacts.get(position).getJid();
				convInt = new Intent(view.getContext(), ConversationActivity.class);
				convInt.putExtra("jid", jid);
				startActivityForResult(convInt, Constants.START_CONVERSATION);
			}
		});
		registerForContextMenu(list);

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
				pushMessageToHistory(message);
				return;
			}
			
			if(intent.getIntExtra("update-contact", -1) != -1) {
				Log.d(Constants.LOG_TAG, "> Update contact list");
				refreshContactList();
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
	
	@Override   
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);  
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmenu, menu);
	}
	
	@Override    
	public boolean onContextItemSelected(MenuItem item) {    
		switch (item.getItemId()) {
			/** Edit contact */
			case R.id.context_edit:
				Log.d(Constants.LOG_TAG, "> Edit roster item");
				return true;
			/** Delete contact */
			case R.id.context_delete:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				int id = contacts.get(info.position).id;
				Log.d(Constants.LOG_TAG, "> Remove roster item with id " + id);
				XMPP.delRoster(dbCon.selectContact(id));
				dbCon.removeContact(id);
				refreshContactList();
				return true;
			/** Default */
			default:
				return super.onOptionsItemSelected(item);
		} 
	}

	/** Listening to click on menu items */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			/** Add contact */
			case R.id.opt_add_contact:
				Intent addContInt = new Intent(this, AddContactActivity.class);
				startActivityForResult(addContInt, Constants.ADD_ROSTER_ITEM);
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

	public void refreshContactList() {
		contacts.clear();
		contacts.addAll(dbCon.fetchContacts());
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * Get results of child activities
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case Constants.ADD_ROSTER_ITEM:
				if (resultCode == RESULT_OK) {
					Contact contact = new Contact(data.getStringExtra("jid"), data.getStringExtra("name"), data.getStringExtra("group"));
					XMPP.setRoster(contact);
					MainActivity.instance.dbCon.insertContact(contact);
					refreshContactList();
				}
				break;
			case Constants.DEL_ROSTER_ITEM:
				if (resultCode == RESULT_OK) {
					
				}
				break;
			case Constants.UPD_ROSTER_ITEM:
				if (resultCode == RESULT_OK) {
					
				}
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		instance = this;

		/* LocalBroadcastManager.getInstance(this).registerReceiver(serviceReceiver, new IntentFilter("service-broadcast"));

		Intent intent = new Intent(this, NotificationService.class);
		bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
		startService(intent);*/
	}

	@Override
	public void onPause() {
		super.onPause();
		// instance = null;

		/* LocalBroadcastManager.getInstance(this).unregisterReceiver(serviceReceiver);

		if (isBound) {
            unbindService(myConnection);
            isBound = false;
        } */
	}
}
