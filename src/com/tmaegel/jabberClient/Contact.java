package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.util.Log;

public class Contact {

	public String jid;
	public String name;
	public String group;
	public String subscription; 		/**< int of subscriptionValues */
	public String ask;
	public String approved; 			/**< boolean */

	public String[] subscriptionValues = new String[]
	{
		"none",
		"to",
		"from",
		"both"
	};

	public Contact() {

	}

	public Contact(String jid, String name, String group) {
		setJid(jid);
		setName(name);
		setGroup(group);
	}

	public void setJid(String val) {
		if(val.isEmpty()) {
			Log.d(Constants.LOG_TAG, "Empty jid string. Doesn't set jid.");
			jid = null;
		} else {
			this.jid = val;
		}
	}

	public void setName(String val) {
		if(val.isEmpty()) {
			Log.d(Constants.LOG_TAG, "Empty name string. Doesn't set name.");
			name = null;
		} else {
			this.name = val;
		}
	}

	public void setGroup(String val) {
		if(val.isEmpty()) {
			Log.d(Constants.LOG_TAG, "Empty group string. Doesn't set group.");
			group = null;
		} else {
			this.group = val;
		}
	}

	public String getJid() {
		if(jid.isEmpty() || jid == null) {
			Log.d(Constants.LOG_TAG, "Empty jid string. No return value.");
			return null;
		}

		return jid;
	}

	public String getName() {
		if(name.isEmpty() || name == null) {
			Log.d(Constants.LOG_TAG, "Empty name string. No return value.");
			return null;
		}

		return name;
	}

	public String getGroup() {
		if(group.isEmpty() || group == null) {
			Log.d(Constants.LOG_TAG, "Empty group string. No return value.");
			return null;
		}

		return group;
	}
}
