package com.tmaegel.jabberClient;

public class Contact {

	public String jid;
	public String name;
	public String group;
	public String subscription;
	// public String ver;
	// public String ask;

	public Contact() {

	}

	public Contact(String jid, String name, String group) {
		this.jid = jid;
		this.name = name;
		this.group = group;
	}
}
