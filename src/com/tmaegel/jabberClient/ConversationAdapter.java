package com.tmaegel.jabberClient;

import android.content.Context;
import android.widget.ArrayAdapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConversationAdapter extends ArrayAdapter {

	private final Context context;
	private final int resourceId;

	private TextView singleMessage;
	private LinearLayout singleMessageContainer;
	private List<Stream> history = new ArrayList();
	
	private String jid;
	
	public ConversationAdapter(Context context, int resourceId, String jid) {
		super(context, resourceId);
		this.context = context;
		this.resourceId = resourceId;
		this.jid = jid;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.single_message, parent, false);
		}

		singleMessageContainer = (LinearLayout)row.findViewById(R.id.conv_single_message_container);
		// Stream msg = getItem(position);
		singleMessage = (TextView)row.findViewById(R.id.conv_single_message);
		// singleMessage.setText(msg.getBody());
		// chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
		// singleMessageContainer.setGravity(msg.left ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}
	
	/** add message to container and to list */
	public void addMessageToHistory(Stream msg, boolean local) {
		/** is local true, message is sending */
		if(local == true) {
			// msg.setTo(jid);
			// history.add(msg);
			super.add(msg);
			// MainActivity.net.sendRequest(msg);
		} else {
			// history.add(msg);
			// super.add(msg);
		}
	}

	/** return message object */
	public Stream getItem(int index) {
		return history.get(index);
	}
}
