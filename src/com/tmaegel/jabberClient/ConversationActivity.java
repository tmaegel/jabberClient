package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Session;

import android.os.Bundle;

import android.util.Log;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.content.LocalBroadcastManager;

public class ConversationActivity extends Activity {

	// public references
	public ConversationAdapter convAdapter;

	// private references
	private ListView listView;
	private ImageButton btnSend;
	private EditText textMsg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);

		// msgBroadcast = new MessageBroadcast(this);

		listView = (ListView)findViewById(R.id.conv_history);
		btnSend = (ImageButton)findViewById(R.id.conv_send);
		textMsg = (EditText)findViewById(R.id.conv_input_message);

		Bundle localBundle = getIntent().getExtras();
		convAdapter = new ConversationAdapter(this, R.layout.single_message, localBundle.getString("jid"));

		listView.setAdapter(convAdapter);

		btnSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Message msg = new Message();
				msg.setFrom(MainActivity.instance.session.getFullJid());
				msg.setBody(textMsg.getText().toString());
				msg.setLocal(true);
				convAdapter.addMessage(msg);
				
				textMsg.setText("", TextView.BufferType.EDITABLE);
			}
		});
	}

	// handler for received Intents for the event
	private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(Constants.LOG_TAG, "" + (Message)intent.getSerializableExtra("message"));
			convAdapter.addMessage((Message)intent.getSerializableExtra("message"));
		}
	};

	/** Called when rebuild activity, switch in landscape mode */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	@Override
    protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("receive-message"));
    }

    @Override
    protected void onPause() {
		super.onPause();
		// LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}
