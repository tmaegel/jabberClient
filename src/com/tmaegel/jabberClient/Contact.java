package com.tmaegel.jabberClient;

public class Contact {

	public String jid 			= "undefined";
	public String name 			= "undefined";
	public String group 		= "undefined";
	public String subscription 	= "undefined"; /**< int of subscriptionValues */
	public String ask 			= "";
	public String approved 		= ""; /**< boolean */
	
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
		this.jid = jid;
		this.name = name;
		this.group = group;
	}
}
