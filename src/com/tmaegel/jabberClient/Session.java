package com.tmaegel.jabberClient;

import android.util.Log;

final public class Session {

    public String user;
    public String password;
    public String resource;
    public String domain;
    public String ip;
    public int port;
    
    public Session(String user, String password, String resource, String domain, String ip, int port) {
		this.user = user;
		this.password = password;
		this.resource = resource;
		this.domain = domain;
		this.ip = ip;
		this.port = port; 
    }

    public String getJid() {
        String jid = user + "@" + domain;
        return jid;
    }

    public String getFullJid() {
        String jid = user + "@" + domain + "/" + resource;
        return jid;
    }
}
