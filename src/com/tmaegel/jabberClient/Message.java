package com.tmaegel.jabberClient;

import android.util.Log;

public class Message {

	private boolean local = false;

	private String id;
	private String from;
	private String to;
	private String subject;
	private String body;
	private String thread;

	// private String date;

	public Message() {

	}

	public void setTo(String val) {
		if(val.isEmpty()) {
			Log.d(Constants.LOG_TAG, "Empty to string. Doesn't set to.");
			to = null;
		} else {
			this.to = val;
		}
	}

	public void setBody(String val) {
		if(val.isEmpty()) {
			Log.d(Constants.LOG_TAG, "Empty body string. Doesn't set body.");
			body = null;
		} else {
			this.body = val;
		}
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public String getBody() {
		if(body.isEmpty() || body == null) {
			Log.d(Constants.LOG_TAG, "Empty body string. No return value.");
			return null;
		}

		return body;
	}

	public String getTo() {
		if(to.isEmpty() || to == null) {
			Log.d(Constants.LOG_TAG, "Empty to string. No return value.");
			return null;
		}

		return to;
	}

	public boolean isLocal() {
		return this.local;
	}
}
