package com.tmaegel.jabberClient;

import android.util.Log;

final public class Session {

    public String user;
    public String domain;
    public String resource;
    public String password;

    public Session(String user, String domain, String resource, String password) {
        this.user = user;
        this.domain = domain;
        this.resource = resource;
        this.password = password;
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
