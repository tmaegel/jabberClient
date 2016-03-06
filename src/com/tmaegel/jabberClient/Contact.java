package com.tmaegel.jabberClient;

public class Contact {

	private String jid;
	private String name;
	private String group;

	public Contact() {
		
	}
	
	/**
	 * @brief get jid
	 */
	public String getJID() {
		return jid;
	}
	
	/**
	 * @brief get name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @brief get group
	 */
	public String getGroup() {
		return group;
	}
	
	/**
	 * @brief set jid
	 */
	public void setJID(String jid) {
		this.jid = jid;
	}
	
	/**
	 * @brief set name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @brief set group
	 */
	public void setGroup(String group) {
		this.group = group;
	}
}
