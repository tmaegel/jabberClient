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
	private List<Message> history = new ArrayList();

	public ConversationAdapter(Context context, int resourceId) {
		super(context, resourceId);
		this.context = context;
		this.resourceId = resourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.single_message, parent, false);
		}

		singleMessageContainer = (LinearLayout)row.findViewById(R.id.conv_single_message_container);
		Message msg = getItem(position);
		singleMessage = (TextView)row.findViewById(R.id.conv_single_message);
		singleMessage.setText(msg.getMessage());
		// chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_a : R.drawable.bubble_b);
		singleMessageContainer.setGravity(msg.left ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}
	
	/** add message to container and to list */
	public void addMessageToHistory(Message msg, boolean local) {
		/** is local true, message is sending */
		if(local == true) {
			history.add(msg);
			super.add(msg);
			MainActivity.net.sendMessage("user2@localhost", msg.getMessage());
		} else {
			history.add(msg);
			super.add(msg);
		}
	}

	/** return message object */
	public Message getItem(int index) {
		return history.get(index);
	}
}
