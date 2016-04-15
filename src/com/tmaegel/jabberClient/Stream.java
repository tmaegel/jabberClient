package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import java.util.HashMap;

public class Stream {
	
	// xmmp stream
	private String id;
	private String xmlns;
	private String version;
	
	// mostly used by message
	private String to;
	private String from;
	private String body;
	
	// mostly used by roster
	private String jid;
	private String name;
	private String type;
	private String subscription;
	private String ver;
	
	
	/**
	 * CONSTANTS
	 */
	private int objectType = Constants.NONE;
	
	// Assoziatives Array
	private HashMap tags;				/**< available tags */
	private HashMap attributes;			/**< attributes in tags e.g. <tag attribute="attribute"> */
	private HashMap values;				/**< values in tags e.g. <tag>value</tag> */
	
	// m.put("3X", foo);

	public Stream(int objectType) {
		super();
		this.objectType = objectType;
		
		tags = new HashMap();
		attributes = new HashMap();
		values = new HashMap();
	}
	
	/**
	 * SETTER
	 */
	
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * GETTER
	 */
	
	public int getObjectType() {
		return objectType;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTo() {
		return to;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getType() {
		return type;
	}
}
