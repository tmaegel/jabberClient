package com.tmaegel.jabberClient;

import android.util.Log;

import java.io.Serializable;

public class Message implements Serializable {

	private boolean local = false;

	private String id;
	private String type;
	private String lang;
	private String from;
	private String to;
	private String subject;
	private String body;
	private String thread;

	// private String date;

	public Message() {

	}

	@Override
    public String toString() {
		return "Message [id=" + id + ", type=" + type + ", lang=" + lang + ", from=" + from + ", to=" + to + ", subject=" + subject + ", body=" + body + ", thread=" + thread + "]";
    }

	/**
	 * GETTER
	 */
	public boolean isLocal() {
 		return local;
 	}

	public void setId(String val) {
		if(val.isEmpty()) {
			id = null;
		} else {
			id = val;
		}
	}

	public void setFrom(String val) {
		if(val.isEmpty()) {
			from = null;
		} else {
			from = val;
		}
	}

	public void setTo(String val) {
		if(val.isEmpty()) {
			to = null;
		} else {
			to = val;
		}
	}

	public void setSubject(String val) {
		if(val.isEmpty()) {
			subject = null;
		} else {
			subject = val;
		}
	}

	public void setBody(String val) {
		if(val.isEmpty()) {
			body = null;
		} else {
			body = val;
		}
	}

	public void setThread(String val) {
		if(val.isEmpty()) {
			thread = null;
		} else {
			thread = val;
		}
	}

	public void setType(String val) {
		if(val.isEmpty()) {
			type = null;
		} else {
			type = val;
		}
	}

	public void setLang(String val) {
		if(val.isEmpty()) {
			lang = null;
		} else {
			lang = val;
		}
	}

	/**
	  SETTER
	 */

	public void setLocal(boolean val) {
		local = val;
	}

	public String getId() {
		if(id == null || id.isEmpty()) {
			return null;
		}

		return id;
	}

	public String getFrom() {
		if(from == null || from.isEmpty()) {
			return null;
		}

		return from;
	}

	public String getTo() {
		if(to == null || to.isEmpty()) {
			return null;
		}

		return to;
	}

	public String getSubject() {
		if(subject == null || subject.isEmpty()) {
			return null;
		}

		return subject;
	}

	public String getBody() {
		if(body == null || body.isEmpty()) {
			return null;
		}

		return body;
	}

	public String getThread() {
		if(thread == null || thread.isEmpty()) {
			return null;
		}

		return thread;
	}

	public String getType() {
		if(type.isEmpty() || type == null) {
			return null;
		}

		return type;
	}

	public String getLang() {
		if(lang.isEmpty() || lang == null) {
			return null;
		}

		return lang;
	}
}
