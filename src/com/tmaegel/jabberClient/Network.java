package com.tmaegel.jabberClient;

import com.tmaegel.jabberClient.Constants;

import android.os.Bundle;
import android.os.AsyncTask;

import android.util.Log;
import android.util.Base64;

import java.util.UUID;

import java.math.BigInteger;

import java.security.MessageDigest;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.net.Socket;
import java.net.InetAddress;

public class Network extends AsyncTask<String, Stream, String> {

	// private references
	private Socket socket;
	private XMLParser xmlParser;
	private InputStream input;
	private BufferedWriter output;

	private Thread sendThread;
	private Thread recvThread;

	// private String serverIpAddr = "www.maegel-online.de";
	// private String serverIpAddr = "37.187.216.212";
	private String serverIpAddr = "192.168.178.148";
	private int serverPort = 5222; // only for client to server communication
	// private int serverPort = 5269; // only for server to server communication

	static private String uft8null = "\\x00"; // Use for PLAIN authentifaction

	// Account information
	private String fullJid;

	// Authentifaction (TEST)
	private String senderUser = "user1";
	private String receiverUser = "user2";
	private String password = "123456";
	private String resource = "ressource";
	private String host = "localhost";
	private String lang = "de";
	private String xmlns = "jabber:client";
	private String xmlns_stream = "http://etherx.jabber.org/streams";

	private boolean connected = false;
	private boolean initialized = false;
	
	@Override
	protected String doInBackground(String... params) {
		Log.e("jabberClient", "exec doInBackground()");
		Log.d("jabberClient", "Network is starting");
		
		xmlParser = new XMLParser(this);
		
		/**
		 * Sender loop
		 */
		sendThread = new Thread(new Runnable() {
		    public void run() {
		    	try {
		    		int waitingPeriod = 100;
				    while(connected) {
				    
				    	// Get roster
				    	// @todo bad  solution
				    	while(waitingPeriod >= 100 && isInitialized() == true) {
							Stream obj = new Stream(Constants.ROSTER_REQUEST);
							sendRequest(obj);
							
							waitingPeriod = 0;
						}
						waitingPeriod++;
				    	
				    	Thread.sleep(3000);
				    }
				} catch(Exception e) {
				    Log.e("jabberClient", "Error: Sender loop", e);
				}
		    }
    	});
    	
    	/**
    	 * Receiver loop
    	 */
		recvThread = new Thread(new Runnable() {
		    public void run() {
		    	try {
				    while(connected) {
				    
				        // Receive response in the loop
						recvResponse();

				        Thread.sleep(3000);
				    }
				} catch(Exception e) {
				    Log.e("jabberClient", "Error: Receiver loop", e);
				}
		    }
    	});
		
		/**
		 * Initialization
		 */
    	try {
			// InetAddress serverAddr = InetAddress.getByName(serverIpAddr);
			Log.d("jabberClient", "Open socket to " + serverIpAddr + ":" + serverPort);
			socket = new Socket(serverIpAddr, serverPort);

			Log.d("jabberClient", "Connected");
			connected = true;

			input = socket.getInputStream();
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			
			sendThread.start();
			
			try {
				// first authenticate
				if(initStream() == true) {
					recvThread.start();
				} else {
					// close socket
					connected = false;
				}
			} catch(Exception e) {
				Log.e("jabberClient", "Error: IO", e);
				socket.close();
				Log.d("jabberClient", "Socket closed");
			}
			
		} catch(Exception e) {
			Log.e("jabberClient", "Error: Socket", e);
			connected = false;
		}
    	
    	// Starting threads
		// sendThread.start();
		// recvThread.start();
		
		return null; // returns what you want to pass to the onPostExecute()
	}

	/**
	 * execution of result of Long time consuming operation
	 */
	@Override
	protected void onPostExecute(String result) {
		Log.e("jabberClient", "exec onPostExecute()");
	}

	/**
	 * Things to be done before execution of long running operation
	 */
	@Override
	protected void onPreExecute() {
		Log.e("jabberClient", "exe onPreExecute()");
	}
	
	protected void onProgressUpdate(Stream... obj) {
		Log.e("jabberClient", "exe onProgressUpdate() ");
		MainActivity.convAct.convAdapter.addMessageToHistory(obj[0], false);
	}

	
	/**
	 * @brief init stream, first authenticate
	 * @param succes authentication true or false
	 */
	public boolean initStream() {
		try {
			Log.d("jabberClient", "Initialization ...");
			// For Authentifaction
			writeStream("<stream:stream from='" + senderUser + "@" + host + "' to='" + host + "' version='1.0' xml:lang='" + lang + "' xmlns='" + xmlns + "' xmlns:stream='" + xmlns_stream + "'>");
			// For Anonymous Authentifaction
			// writeStream("<?xml version='1.0'?><stream:stream to='localhost' version='1.0' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>");
			recvResponse();
			
			// Log.d("jabberClient", "" + readStream()); /** todo parsing STREAM:STREAM tag failed */

			 // SASL Authentifaction: PLAIN
			 String authStream = "\0" + senderUser + "\0" + password;
			 Log.d("jabberClient", "authStream >> " + authStream);
			 writeStream("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + Base64.encodeToString(authStream.getBytes("UTF-8"), 0) + "</auth>");
			 //String encodeHandshake = xmlParser.parseStream(readStream());
			 // readStream();
			recvResponse();


			writeStream("<stream:stream from='" + senderUser + "@" + host + "' to='" + host + "' version='1.0' xml:lang='" + lang + "' xmlns='" + xmlns + "' xmlns:stream='" + xmlns_stream + "'>");
			// Log.d("jabberClient", "" + readStream()); /** todo parsing STREAM:STREAM tag failed */
			recvResponse();

			// Resource binding, Server generate resource
			writeStream("<iq id='yhc13a95' type='set'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/></iq>");
			recvResponse();
			
			Log.d("jabberClient", "Initialization success");
			initialized = true;
		} catch(Exception e) {
			Log.e("jabberClient", "Error: Init stream", e);
		}

		return true;
	}
	
	/**
	 * @brief receive response
	 * @todo: problems with to many messages
	 * @todo: In welchen Schritt befinden wir uns? Übergabe eines TAGS um zu detektieren. Weil es mehrere IQ tags gibt, die unterschiedliches bewirken
	 */
	public void recvResponse() {
		String stream = readStream();
		// Log.d("jabberClient", "" + stream);
		Stream obj = xmlParser.parseResponse(stream);
		
		if(obj != null) {
			switch(obj.getObjectType()) {
				case Constants.MESSAGE:
					Log.d(Constants.LOG_TAG, "FROM: " + obj.getFrom() + ", TO: " + obj.getTo() + ", BODY: " + obj.getBody());
					publishProgress(obj);
					break;
				case Constants.CHALLENGE:
					Log.d(Constants.LOG_TAG, "CHALLENGE: " + obj.getBody());
					break;
				case Constants.IQ:
					Log.d(Constants.LOG_TAG, "JID: " + obj.getBody());
					fullJid = obj.getBody();
					break;
			}
		} else {
			Log.d(Constants.LOG_TAG, "Null object");
		}
		
	}
	
	/**
	 * @brief send request
	 * @todo: problems with to many messages
	 */
	public void sendRequest(Stream obj) {
		/**< initial roster request **/
		// <iq from='juliet@example.com/balcony' type='get' id='roster_1'><query xmlns='jabber:iq:roster'/></iq>
		/**< add a roster item **/
		// <iq from='juliet@example.com/balcony' type='set' id='roster_2'><query xmlns='jabber:iq:roster'><item jid='nurse@example.com' name='Nurse'><group>Servants</group></item></query></iq>
		/**< update a roster item */
		// <iq from='juliet@example.com/chamber' type='set' id='roster_3'><query xmlns='jabber:iq:roster'><item jid='romeo@example.net' name='Romeo' subscription='both'><group>Friends</group><group>Lovers</group></item></query></iq>
		/**< delete a roster item */
		// <iq from='juliet@example.com/balcony' type='set' id='roster_4'><query xmlns='jabber:iq:roster'><item jid='nurse@example.com' subscription='remove'/></query></iq>
	
		switch(obj.getObjectType()) {
			/**
			 * ROSTER
			 */
			case Constants.ROSTER_REQUEST:
				/**
				 * @todo generate rendom id
				 */
				Log.d(Constants.LOG_TAG, "Roster request");
				writeStream("<iq from='" + fullJid + "' type='get' id='roster_1'><query xmlns='jabber:iq:roster'/></iq>");
				break;
			
			/**
			 * MESSAGE
			 */
			case Constants.MESSAGE:
				Log.d(Constants.LOG_TAG, "Send message");
				Log.d(Constants.LOG_TAG, "TO: " + obj.getTo() + ", BODY: " + obj.getBody());
				writeStream("<message to='" + obj.getTo() + "'><body>" + obj.getBody() + "</body></message>");
				break;
		}
	}
	
	/**
	 * @brief a client's request for the server to modify (i.e., create, update, or delete) a roster item
	 */
	public void setRoster(/*String from, String jid*/) {
		Log.d("jabberClient", "Set roster ...");
		
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
	 * @brief Close stream
	 */
	public void closeStream() {
		writeStream("</stream:stream>");
		readStream();
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

				if(input.available() == 0) {
					endOfStream = true;
				}
			}
		} catch(Exception e) {
			Log.e("jabberClient", "Read failed: ", e);
			return null;
		}

		return tmp;
	}

	public int writeStream(String stream) {
		try {
			output.write(stream);
			output.flush();
		} catch(IOException e) {
			Log.e("jabberClient", "Write failed: ", e);
			return 0;
		}

		return 1;
	}

	/*public void run() {
		try {
			InetAddress serverAddr = InetAddress.getByName(serverIpAddr);
			Log.d("jabberClient", "Open socket");
			socket = new Socket(serverAddr, serverPort);

	/*		Log.d("jabberClient", "Connected");
			connected = true;

	/*		input = socket.getInputStream();
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

	/*		try {

				// first authenticate
				if(initStream() == true) {
					// Starting Receiver Thread
					Thread thread = new Thread(recv);
					thread.start();
				} else {
					// close socket
					connected = false;
				}

				/**
				 * Receiver loop
				 */
	/*			while(connected) {
					// Write some text in the loop
					// sendMessage(receiverUser + "@" + host, "loop message");

	/*				Thread.sleep(5000);
				}

	//			closeStream();

				 /**
				  * SCRAM-SHA-1 Authentifaction
				  * @todo normalize the password (using SASLprep)
				  */

				 /*String cnonce = UUID.randomUUID().toString();
				 String authStream = "n,,n=" + user + ",r=" + cnonce;
				 Log.d("jabberClient", "authStream >> " + authStream);
				 writeStream("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='SCRAM-SHA-1'>" + Base64.encodeToString(authStream.getBytes("UTF-8"), 0) + "</auth>");*/

				/**
				 * challenge from server
				 * realm = Authentifizierungsbereiche
				 * nonce = Zeichenkette, zurückschicken
				 * qop - Wenn qop=auth nicht vorhanden -> Abbruch
				 * charset - if not present, encoded with ISO 8859-1, more than one -> abor
				 * algorithm
				 */
				/*String encodeHandshake = xmlParser.parseStream(readStream());
				Log.d("jabberClient", "Encode HANDSHAKE >> " + encodeHandshake);
				byte[] decodeHandshake = Base64.decode(encodeHandshake, 0);
				String decodeHandshakeStr = new String(decodeHandshake);
				Log.d("jabberClient", "Decode HANDSHAKE >> " + decodeHandshakeStr);

				String response = "";
				String snonce = "";		// This is the serverNonce. The client MUST ensure that it starts with the clientNonce it sent in its initial message.
				String salt = "";		// This is the salt, base64 encoded (yes, this is base64-encoded twice!)
				String iterations = "";	// Number of iterations
				String[] handshakeSplit = decodeHandshakeStr.split(",");
				// int nc = 1;
				for(int i = 0; i < handshakeSplit.length; i++) {
					if(handshakeSplit[i].indexOf("r=") != -1) {
						snonce = handshakeSplit[i].substring(handshakeSplit[i].indexOf("=") + 1, handshakeSplit[i].length());
						Log.d("jabberClient", "s >> " + snonce);
					} else if(handshakeSplit[i].indexOf("s=") != -1) {
						salt = handshakeSplit[i].substring(handshakeSplit[i].indexOf("=") + 1, handshakeSplit[i].length());
						Log.d("jabberClient", "i >> " + salt);
					} else if(handshakeSplit[i].indexOf("i=") != -1) {
						iterations = handshakeSplit[i].substring(handshakeSplit[i].indexOf("=") + 1, handshakeSplit[i].length());
						Log.d("jabberClient", "i >> " + iterations);
					}
				}

				String clientFinalMessageBare = "c=biws,r=" + snonce;
				saltedPassword = PBKDF2-SHA-1(normalizedPassword, salt, i)
				clientKey = HMAC-SHA-1(saltedPassword, "Client Key")
				storedKey = SHA-1(clientKey)
				authMessage = initialMessage .. "," .. serverFirstMessage .. "," .. clientFinalMessageBare
				clientSignature = HMAC-SHA-1(storedKey, authMessage)
				clientProof = clientKey XOR clientSignature
				serverKey = HMAC-SHA-1(saltedPassword, "Server Key")
				serverSignature = HMAC-SHA-1(serverKey, authMessage)
				clientFinalMessage = clientFinalMessageBare .. ",p=" .. base64(clientProof)*/

				/**
				 * response from client
				 * username The user's name in the specified realm.
				 * realm The authentication realm that this user's account is in. If this is missing, it will be set to the empty string.
				 * nonce: Server generated string. If missing or specified more than once, authentication fails.
				 * cnonce Client generated string. Server will send this back to the client. If missing or specified more than once, authentication fails.
				 * nc Hexadecimal count of the number of responses (including this one) that the client has sent with the nonce value in this request.
				 * serv-type This should be set to "xmpp".
				 * host The DNS hostname or IP address for the service requested.
				 * digest-uri The full name of the service, formed from the serv-type and host options.
				 * response A string of 32 hex digits (with the alphabetic characters lower-cased) that proves the user knows the password. If missing or specified more than once, authentication fails.
				 * charset If present, specifies that the client has used UTF-8 encoding for the username and password. If not present, the username and password must be encoded in ISO 8859-1.
				 * authzid This should be the full JID (including resource). E.g. jid@host/ressorce
				 */
				/*String response = "";
				String servType = "xmpp";
				String realm = "", charset = "", nonce = "", qop =  "", algorithm = "";
				String[] handshakeSplit = decodeHandshakeStr.split(",");
				int nc = 1;
				for(int i = 0; i < handshakeSplit.length; i++) {
					Log.d("jabberClient", i + " >> " + handshakeSplit[i]);
					if(handshakeSplit[i].indexOf("realm") != -1) {
						realm = handshakeSplit[i].substring(handshakeSplit[i].indexOf("\"") + 1, handshakeSplit[i].lastIndexOf("\""));
						Log.d("jabberClient", "realm >> " + realm);
					} else if(handshakeSplit[i].indexOf("nonce") != -1) {
						nonce = handshakeSplit[i].substring(handshakeSplit[i].indexOf("\"") + 1, handshakeSplit[i].lastIndexOf("\""));
						Log.d("jabberClient", "nonce >> " + nonce);
					} else if(handshakeSplit[i].indexOf("qop") != -1) {
						qop = handshakeSplit[i].substring(handshakeSplit[i].indexOf("\"") + 1, handshakeSplit[i].lastIndexOf("\""));
						Log.d("jabberClient", "qop >> " + qop);
					} else if(handshakeSplit[i].indexOf("charset") != -1) {
						charset = handshakeSplit[i].substring(handshakeSplit[i].indexOf("=") + 1, handshakeSplit[i].length());
						Log.d("jabberClient", "charset >> " + charset);
					} else if(handshakeSplit[i].indexOf("algorithm") != -1) {
   						algorithm = handshakeSplit[i].substring(handshakeSplit[i].indexOf("=") + 1, handshakeSplit[i].length());
						Log.d("jabberClient", "algorithm >> " + algorithm);
   					}
				}*/


				/**
				 * DIGEST-MD5 Authentifaction
				 */

				/*String cnonce = UUID.randomUUID().toString();

				// Generate Binary num
				String format = "%0" + 8 + "d";
				String ncStr = Integer.toBinaryString(nc);
				ncStr = String.format(format, new Integer(ncStr));

				// Generate response string
				// 1. Create a string of the form "username:realm:password". Call this string X.
				String x = user + ":" + host + ":" + password;
				Log.d("jabberClient", "X >> " + x);
				// 2. Compute the 16 octet MD5 hash of X. Call the result Y.
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(x.getBytes(), 0, x.length());
				BigInteger y = new BigInteger(1, md.digest());
				//byte[] hashRow = hash.toByteArray();
				//String y = hash.toString(16);
				Log.d("jabberClient", "Y >> " + y);
				// 3. Create a string of the form "Y:nonce:cnonce:authzid". Call this string A1.
				String a1 = y + ":" + nonce + ":" + cnonce;
				// + ":" + user + "@" + host + "/" + resource;
				Log.d("jabberClient", "A1 >> " + a1);
				// 4. Create a string of the form "AUTHENTICATE:digest-uri". Call this string A2.
				String a2 = "AUTHENTICATE:" + servType + "/" + host;
				Log.d("jabberClient", "A2 >> " + a2);
				// 5. Compute the 32 hex digit MD5 hash of A1. Call the result HA1.
				md.update(a1.getBytes(), 0, a1.length());
				BigInteger ha1 = new BigInteger(1, md.digest());
				Log.d("jabberClient", "HA1 >> " + ha1);
				// 6. Compute the 32 hex digit MD5 hash of A2. Call the result HA2.
				md.update(a2.getBytes(), 0, a2.length());
				BigInteger ha2 = new BigInteger(1, md.digest());
				Log.d("jabberClient", "HA2 >> " + ha2);
				// 7. Create a string of the form "HA1:nonce:nc:cnonce:qop:HA2". Call this string KD.
				String kd = ha1.toString() + ":" + nonce + ":" + ncStr + ":" + cnonce + ":" + qop + ":" + ha2.toString();
				Log.d("jabberClient", "KD >> " + kd);
				// 8. Compute the 32 hex digit MD5 hash of KD. Call the result Z.
				md.update(kd.getBytes(), 0, kd.length());
				BigInteger z= new BigInteger(1, md.digest());
				Log.d("jabberClient", "Z >> " + z);*/


				// response
				// username="user",realm="host",nonce"X",cnone="Y",nc=00000001,qop=auth,digesturi="serv-type/host",response=Z,charset=utf-8,authzid"user@host/ressource"
				// response = "username=\""+user+"\",realm=\""+realm+"\",nonce=\""+nonce+"\",cnonce=\""+cnonce+"\",nc="+ncStr+",qop="+qop+",digest-uri=\""+servType+"/"+host+"\",response="+z+",charset="+charset+",authzid=\""+user+"@"+host+"/"+resource+"\"";
				// Log.d("jabberClient", "Response >> " + response);

				// response
				// writeStream("<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" + Base64.encodeToString(response.getBytes("UTF-8"), 0) + "</response>");
				// readStream();
	/*		} catch(Exception e) {
				Log.e("jabberClient", "Error: IO", e);
			}

	/*		socket.close();
			Log.d("jabberClient", "Socket closed");

	/*	} catch(Exception e) {
			Log.e("jabberClient", "Error: Socket", e);
			connected = false;
		}


		/*try {


			// while(connected) {
				try {

					/*public String decode(String s) {
					    return StringUtils.newStringUtf8(Base64.decodeBase64(s));
					}
					public String encode(String s) {
					    return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
					}*/


					//String encoded = Base64.getEncoder().encodeToString(bytes);
					//byte[] decoded = Base64.getDecoder().decode(encoded);

					/**
					 * Optional ressource binding
					 */

					// writeStream("<stream:stream from='" + user + "@" + host + "' to='" + host + "' version='1.0' xml:lang='" + lang + "' xmlns='" + xmlns + "' xmlns:stream='" + xmlns_stream + "'>");
					// readStream();

					// \x00 + utf8(saslprep(username)) + \x00 + utf8(saslprep(password)))
					/*String authStream =  user + uft8null + host + uft8null + password;
					String base64Stream = Base64.encodeToString(authStream.getBytes("UTF-8"), 0);
					Log.e("jabberClient", "STRING >> " + authStream);
					Log.e("jabberClient", "UNICODE >> " + authStream.getBytes("UTF-8"));
					Log.e("jabberClient", "BASE64 >> " + base64Stream);
					base64Stream = base64Stream.replaceAll("\\n", "");
					writeStream("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + base64Stream + "</auth>");*/

					// For Anonymous Authentifaction
					// writeStream("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='ANONYMOUS'/>");

					// writeStream("<message from='test@localhost' to='test@localhost' xml:lang='de'><body>Art thou not Romeo, and a Montague?</body></message>");


					// writeStream("<?xml version='1.0'?><stream:stream to='localhost' version='1.0' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>");
					// readStream();

					// Base64
					/*String string1 = "Hallo";
					Log.e("jabberClient", "String >> " + string1);
					Log.e("jabberClient", "Byte >> " + string1.getBytes());
					String test = Base64.encodeToString(string1.getBytes(), 0);
					Log.e("jabberClient", "Base64 >> " + test);*/

					//String test = Integer.toHexString(0);
					//Log.e("jabberClient", "NULL >> " + test);

		/*		} catch(Exception e) {
					Log.e("jabberClient", "Error: IO", e);
				}
			//}

			socket.close();
			Log.d("jabberClient", "Socket closed");

		} catch(Exception e) {
			Log.e("jabberClient", "Error: Socket", e);
			connected = false;
		}*/
	/*}*/
}
