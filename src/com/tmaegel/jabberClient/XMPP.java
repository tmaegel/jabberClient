package com.tmaegel.jabberClient;

import android.util.Log;

final public class XMPP {

    public static final void openStream(String resource) {
        Log.d(Constants.LOG_TAG, "Open stream");
        /**
         * ...
         */
    }

    /**
     * @brief Bind resource, if resource null or empty esource generate by server
     * @todo id generation
     * @todo return value resource or error status
     */
    public static final void bindResource(String resource) {
        Log.d(Constants.LOG_TAG, "Bind resource");

        String id = "xyz123";
        if(resource == null || resource.isEmpty()) {
            MainActivity.instance.net.writeStream("<iq id='" + id + "' type='set'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/></iq>");
        } else {
            MainActivity.instance.net.writeStream("<iq id='" + id + "' type='set'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><resource>" + resource + "</resource></bind></iq>");
        }
    }

    /**
     * @brief Send message
     * @todo id generation
     * @todo xml:lang
     */
    public static final void sendMessage(Message message) {
        Log.d(Constants.LOG_TAG, "Send message");

        String msgStr = "";
        String id = "xyz123";

		if(message.getTo() != null) {
			msgStr = "<message from='" + message.getFrom() + "' to='" + message.getTo() + "' id='" + id + "' type='chat' xml:lang='de'>";
		} else {
			Log.d(Constants.LOG_TAG, "No receiver detected.");
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
		msgStr = msgStr + "</message>";

		Log.d(Constants.LOG_TAG, "Send message");
		Log.d(Constants.LOG_TAG, "" +  msgStr);

		MainActivity.instance.net.writeStream(msgStr);
    }

    /**
     * @brief Request roster
     * @todo id generation
     */
    public static final void requestRoster() {
        Log.d(Constants.LOG_TAG, "Roster request");

        String id = "xyz123";
        MainActivity.instance.net.writeStream("<iq from='" + MainActivity.instance.session.getFullJid() + "' type='get' id='" + id + "'><query xmlns='jabber:iq:roster'/></iq>");
    }

    /**
     * @brief Set roster
     * @todo id generation
     */
    public static final void setRoster(Contact contact) {
        Log.d(Constants.LOG_TAG, "Set roster");

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

            Log.d(Constants.LOG_TAG, "Add roster item");
            MainActivity.instance.net.writeStream(rosterStr);
        } else {
            Log.d(Constants.LOG_TAG, "Jid is empty. No Rostewr set.");
        }
    }

    /**
	 * @brief Close stream
	 */
    public static final void closeStream() {
        Log.d(Constants.LOG_TAG, "Close stream");

        MainActivity.instance.net.writeStream("</stream:stream>");
    }

}
