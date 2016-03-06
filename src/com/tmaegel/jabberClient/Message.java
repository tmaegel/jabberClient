package com.tmaegel.jabberClient;

public class Message {

	public boolean left;
	// private String id;
	// private String type;
	private String to;
	private String from;
	private String message;
	// public String timestamp;

	public Message(String message, boolean left) {
		super();
		this.message = message;
		this.left = left;
	}
	
	public Message(String to, String from, boolean left) {
		super();
		this.to = to;
		this.from = from;
		this.left = left;
	}

	public Message(String to, String from, String message, boolean left) {
		super();
		this.to = to;
		this.from = from;
		this.message = message;
		this.left = left;
	}
	
	/*public void setId(String id) {
		this.id = id;
	}*/
	
	/*public void setType(String type) {
		this.type= type;
	}*/
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isLeft() {
		return left;
	}
	
	public String getTo() {
		return to;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getMessage() {
		return message;
	}
}
