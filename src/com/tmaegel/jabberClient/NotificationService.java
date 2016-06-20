package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;
import com.tmaegel.jabberClient.XMPP;

import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.UUID;

import android.util.Log;
import android.util.Base64;

import android.os.Binder;
import android.os.IBinder;

import android.app.Service;

import android.content.Intent;

import java.math.BigInteger;

import java.security.MessageDigest;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.net.Socket;
import java.net.InetAddress;

import android.support.v4.content.LocalBroadcastManager;

public class NotificationService extends Service  {

    private Socket socket;
	private InputStream input;
	private BufferedWriter output;
	private Thread thread;
	private Parser parser;
    private final IBinder localBinder = new LocalBinder();

	// objects
	public Stanza stanza;
	public Contact contact;
	public Message message;

	private String serverIpAddr = "192.168.178.103";	// alternativ "www.maegel-online.de" or "37.187.216.212"
	private int serverPort = 5222; 						// only for client to server communication, 5269 for server to server communication

	// Account information
    public String fullJid;
    private String jid;

    private boolean connected = false;
	private boolean initialized = false;

    public class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(Constants.LOG_TAG, "> Start service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.LOG_TAG, "Received start id " + startId + ": " + intent);

        parser = new Parser();

        Runnable r = new Runnable() {
            public void run() {

                try {

        			// InetAddress serverAddr = InetAddress.getByName(serverIpAddr);
        			Log.d(Constants.LOG_TAG, "> Open socket to " + serverIpAddr + ":" + serverPort);
        			socket = new Socket(serverIpAddr, serverPort);

        			input = socket.getInputStream();
        			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

                    Log.d(Constants.LOG_TAG, "> Connected");
        			connected = true;

        			try {

                        while(!MainActivity.instance.isBound) {
                            Log.d(Constants.LOG_TAG, "> Service isn't bound yet");
                            Log.d(Constants.LOG_TAG, "> Waiting...");
                            Thread.sleep(1000);
                        }

						initialized = XMPP.initialize(
							MainActivity.instance.session.user, 
							MainActivity.instance.session.domain, 
							MainActivity.instance.session.password, 
							MainActivity.instance.session.resource
						);


        				while(connected && initialized) {
        					// Receive response in the loop
        					
        					recvResponse();

                            // Thread.sleep(5000);
        				}

        			} catch(Exception e) {
        				Log.e(Constants.LOG_TAG, "Error: IO", e);
        				socket.close();
        				Log.d(Constants.LOG_TAG, "> Socket closed");
        			}

        		} catch(Exception e) {
        			Log.e(Constants.LOG_TAG, "Error: Socket", e);
        			connected = false;
        		}

            }
        };

        thread = new Thread(r);
        thread.start();

        // after process, stop service
        // stopSelf();

        // restart after destroy
        return START_STICKY;
    }

    private void publishProgress(int type) {
        switch(type) {
            /**
			 * Message
			 */
			case Constants.RECV_MESSAGE:
                Intent msgIntent = new Intent("service-broadcast");
                msgIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(msgIntent);
 				break;

			/**
			 * Roster
			 */
			case Constants.S_ROSTER_RESPONSE:
                for(int i = 0; i < stanza.items.size(); i++) {
                	MainActivity.instance.dbCon.insertContact(stanza.items.get(i));
                }
                Intent rosterIntent = new Intent("service-broadcast");
            	rosterIntent.putExtra("update-contact", 1);
            	LocalBroadcastManager.getInstance(this).sendBroadcast(rosterIntent);
				break;
			case Constants.S_ROSTER_PUSH:
				/*stanza.items.addAll(main.contacts);
				main.listAdapter.notifyDataSetChanged();*/
				break;
			case Constants.S_ROSTER_ERROR:

				break;
		}
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "> Destroy service");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return localBinder;
    }

	/**
	 * @brief receive response
	 * @todo: problems with to many messages
	 * @todo: In welchen Schritt befinden wir uns? Übergabe eines TAGS um zu detektieren. Weil es mehrere IQ tags gibt, die unterschiedliches bewirken
	 */
	public void recvResponse() {
		String stream = readStream();

		if(stream != null && !stream.isEmpty()) {
			stanza = parser.parseXML(stream);

			if(stanza != null && stanza.stanzaType > 0) {
				switch(stanza.stanzaType) {
					/**
					 * IQ
					 */
					case Constants.IQ:
						if(stanza.type.equals("result")) {
							Log.d(Constants.LOG_TAG, "> Received roster result");
                            publishProgress(Constants.S_ROSTER_RESPONSE);
						} else if(stanza.type.equals("set")) {
							Log.d(Constants.LOG_TAG, "> Received roster push");
						    publishProgress(Constants.S_ROSTER_PUSH);
						} else if(stanza.type.equals("error")) {
							Log.d(Constants.LOG_TAG, "> Received roster error");
						    publishProgress(Constants.S_ROSTER_ERROR);
						}
						break;

					/**
					 * PRESENCE
					 */
					case Constants.PRESENCE:

						break;

					/**
					 * MESSAGE
					 */
					case Constants.MESSAGE:
						Log.d(Constants.LOG_TAG, "> Receive message object");
						message = stanza.message;
						String type = message.getType();
						Log.d(Constants.LOG_TAG, "> Message type: " + type);
						if(type.equals("chat")) {
						    publishProgress(Constants.RECV_MESSAGE);
						} else if(type.equals("groupchat")) {

						} else if(type.equals("headline")) {

						} else if(type.equals("normal")) {

						} else if(type.equals("error")) {

						}
						break;
				}
			} else {
				Log.d(Constants.LOG_TAG, "No stanza object received");
			}
		}
	}

	/**
	 * @brief get connection status
	 * return true or false
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @brief get connection status
	 * return true or false
	 */
	public boolean isInitialized() {
		return initialized;
	}

    /**
	 * @todo: Prüfen ob letztes Zeichen angekommen ist
	 *	      Aktuelle Lösung nicht sauber
	 */
	public String readStream() {
		int character = 0;
		boolean endOfStream = false;
		String tmp = "";

		try {
			while(!endOfStream && (character = input.read()) != -1) {
				tmp = tmp + "" + (char)character;
				if(input.available() <= 0) {
					endOfStream = true;
				}
			}
		} catch(Exception e) {
			Log.e(Constants.LOG_TAG, "Read failed: ", e);
			return null;
		}

		return tmp;
	}

	public int writeStream(String stream) {
		try {
			output.write(stream);
			output.flush();
		} catch(IOException e) {
			Log.e(Constants.LOG_TAG, "Write failed: ", e);
			return 0;
		}

		return 1;
	}
}
