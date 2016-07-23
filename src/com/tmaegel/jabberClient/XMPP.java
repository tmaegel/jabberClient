package com.tmaegel.jabberClient;

import android.util.Log;
import android.util.Base64;

final public class XMPP {

    /**
     * @brief Initialization
     * @param user user
     * @param host host
     * @param password password
     * @param resource resource
     */
    public static final boolean initialize(String user, String host, String password, String resource) {
        Log.d(Constants.LOG_TAG, "> XMPP: Initialization ...");

        String jid = user + "@" + host;

        XMPP.openStream(jid, host, false);
        MainActivity.instance.notificationService.recvResponse();

        // authentifaction
        XMPP.authenticate(user, password);
        MainActivity.instance.notificationService.recvResponse();

        // reopen stream
        XMPP.openStream(jid, host, true);
        MainActivity.instance.notificationService.recvResponse();

        // resource binding
        XMPP.bindResource(resource);
        MainActivity.instance.notificationService.recvResponse();
        // fullJid = stanza.jid;

        // initial roster request
        XMPP.requestRoster();
        MainActivity.instance.notificationService.recvResponse();

        Log.d(Constants.LOG_TAG, "> XMPP: Initialization success");

        return true;
    }

    /**
     * @brief Open stream
     * @param from jid
     * @param to host
     */
    public static final void openStream(String from, String to, boolean reopen) {
        Log.d(Constants.LOG_TAG, "> XMPP: Open stream");

        if(to.isEmpty() || to == null) {
            Log.d(Constants.LOG_TAG, ">> No host");
            return;
        }

        String streamStr = "";

        if(!reopen) {
            streamStr = streamStr + "<?xml version='1.0'?>";
        }
        streamStr = streamStr + "<stream:stream to='" + to + "'";

        if(!from.isEmpty() || from != null) {
            Log.d(Constants.LOG_TAG, ">> Use normal authentifaction");
            streamStr = streamStr + " from='" + from + "' version='1.0' xml:lang='de' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>";
        } else {
            Log.d(Constants.LOG_TAG, ">> Use anonymous authentifaction");
            streamStr = streamStr + " version='1.0' xml:lang='de' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>";
        }

        MainActivity.instance.notificationService.writeStream(streamStr);

    }

    /**
     * @brief authenticate
     * @param user user
     * @param password passwod
     */
    public static final void authenticate(String user, String password) {
        Log.d(Constants.LOG_TAG, "> XMPP: Authentifaction");

        try {
            String authStr = "\0" + user + "\0" + password;
            MainActivity.instance.notificationService.writeStream("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + Base64.encodeToString(authStr.getBytes("UTF-8"), 0) + "</auth>");
        } catch(Exception e) {
            Log.e(Constants.LOG_TAG, "Error: authenticate", e);
        }
    }


    /**
     * @brief Bind resource, if resource null or empty esource generate by server
     * @todo id generation
     * @todo return value resource or error status
     */
    public static final void bindResource(String resource) {
        Log.d(Constants.LOG_TAG, "> XMPP: Bind resource");


        String id = "xyz123";
        String resourceStr = "<iq id='" + id + "' type='set'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'";
        if(resource == null || resource.isEmpty()) {
            Log.d(Constants.LOG_TAG, ">> Use server side resource binding");
            resourceStr = resourceStr + "/>";
        } else {
            Log.d(Constants.LOG_TAG, ">> Use client side resource binding");
            resourceStr = resourceStr + "><resource>" + resource + "</resource></bind>";
        }

        resourceStr = resourceStr + "</iq>";
        MainActivity.instance.notificationService.writeStream(resourceStr);
    }

    /**
     * @brief Send message
     * @todo id generation
     * @todo xml:lang
     */
    public static final void sendMessage(Message message) {
        Log.d(Constants.LOG_TAG, "> XMPP: Send message");

		boolean receipt = false;
        String msgStr = "";
        String id = "xyz123";

		if(message.getTo() != null) {
			msgStr = "<message from='" + message.getFrom() + "' to='" + message.getTo() + "' id='" + id + "' type='chat' xml:lang='de'>";
		} else {
			Log.d(Constants.LOG_TAG, ">> No receiver detected.");
			return;
		}

		if(message.getSubject() != null) {
			msgStr = msgStr + "<subject>" + message.getSubject() + "</subject>";
		}
		if(message.getBody() != null) {
			msgStr = msgStr + "<body>" + message.getBody() + "</body>";
		}
		if(message.getThread() != null) {
			msgStr = msgStr + "<thread>" + message.getThread() + "</thread>";
		}
		if(receipt) {
			msgStr = msgStr + "<request xmlns='urn:xmpp:receipts'/>";
		}
		msgStr = msgStr + "</message>";

		Log.d(Constants.LOG_TAG, "" +  msgStr);

		MainActivity.instance.notificationService.writeStream(msgStr);
    }

    /**
     * @brief Request roster
     * @todo id generation
     */
    public static final void requestRoster() {
        Log.d(Constants.LOG_TAG, "> XMPP: Roster request");

        String id = "xyz123";
        MainActivity.instance.notificationService.writeStream("<iq from='" + MainActivity.instance.session.getFullJid() + "' type='get' id='" + id + "'><query xmlns='jabber:iq:roster'/></iq>");
    }

    /**
     * @brief Set roster
     * @todo id generation
     */
    public static final void setRoster(Contact contact) {
        Log.d(Constants.LOG_TAG, "> XMPP: Set roster");

		if(contact != null) {
		    String id = "xyz123";
		    if(contact.jid != null) {
		        String rosterStr = "<iq from='" + MainActivity.instance.session.getFullJid() + "' type='set' id='" + id + "'><query xmlns='jabber:iq:roster'><item jid='" + contact.jid + "'";
		        if(contact.name != null) {
		            rosterStr = rosterStr + " name='" + contact.name + "'";
		        }
		        if(contact.group == null) {
		            rosterStr = rosterStr + "/>";
		        } else {
		            rosterStr = rosterStr + "><group>" + contact.group + "</group></item>";
		        }
		        rosterStr = rosterStr + "</query></iq>";

		        Log.d(Constants.LOG_TAG, ">> Add roster item");
		        MainActivity.instance.notificationService.writeStream(rosterStr);
		    } else {
		        Log.d(Constants.LOG_TAG, ">> Jid is empty. No Roster set.");
		    }
		} else {
			Log.d(Constants.LOG_TAG, ">> No roster item found.");
		}
    }
    
    /**
     * @brief update roster
     * @todo id generation
     */
    public static final void updateRoster(Contact contact) {
        Log.d(Constants.LOG_TAG, "> XMPP: Update roster");

		if(contact != null) {
		    String id = "xyz123";
		    if(contact.jid != null) {
		        String rosterStr = "<iq from='" + MainActivity.instance.session.getFullJid() + "' type='set' id='" + id + "'><query xmlns='jabber:iq:roster'><item jid='" + contact.jid + "'";
		        if(contact.name != null) {
		            rosterStr = rosterStr + " name='" + contact.name + "'";
		        } else {
		        	rosterStr = rosterStr + " name=''";
		        }
		        
		        if(contact.group == null) {
		            rosterStr = rosterStr + "/>";
		        } else {
		            rosterStr = rosterStr + "><group>" + contact.group + "</group></item>";
		        }
		        rosterStr = rosterStr + "</query></iq>";

		        Log.d(Constants.LOG_TAG, ">> Update roster item");
		        MainActivity.instance.notificationService.writeStream(rosterStr);
		    } else {
		        Log.d(Constants.LOG_TAG, ">> Jid is empty. No Roster update.");
		    }
		} else {
			Log.d(Constants.LOG_TAG, ">> No roster item found.");
		}
    }
    
    /**
     * @brief Remove roster
     * @todo id generation
     */
    public static final void delRoster(Contact contact) {
        Log.d(Constants.LOG_TAG, "> XMPP: Remove roster");

		if(contact != null) {
		    String id = "xyz123";
		    if(contact.jid != null) {
		        String rosterStr = "<iq from='" + MainActivity.instance.session.getFullJid() + "' type='set' id='" + id + "'><query xmlns='jabber:iq:roster'><item jid='" + contact.jid + "' subscription='remove'/></query></iq>";

		        Log.d(Constants.LOG_TAG, ">> Remove roster item");
		        MainActivity.instance.notificationService.writeStream(rosterStr);
		    } else {
		        Log.d(Constants.LOG_TAG, ">> Jid is empty. No Roster remove.");
		    }
		} else {
			Log.d(Constants.LOG_TAG, ">> No roster item found.");
		}
    }

    /**
	 * @brief Close stream
	 */
    public static final void closeStream() {
        Log.d(Constants.LOG_TAG, "> XMPP: Close stream");

        MainActivity.instance.notificationService.writeStream("</stream:stream>");
    }

}
