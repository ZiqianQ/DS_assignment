package Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jdk.nashorn.internal.ir.debug.JSONWriter;

public class SSLServer {
	private static Logger logger = Logger.getLogger(SSLServer.class);

public static void SSLsocket(int sport){
	
		//specify the keystore details
		//the keystore file contains an application's own certificate and private key
//	    System.out.printn("hey ");
	    System.setProperty( "javax.net.ssl.trustStore", "server_keystore/root.jks");
	    
		System.setProperty("javax.net.ssl.keyStore", "server_keystore/server.jks");
		//password to access the private key from keystore file
		System.setProperty("javax.net.ssl.keyStorePassword", "comp90015");
		//if the debug is on , set it to all mode
		//System.setProperty("javax.net.debug", Server_ssl.debugmode);
		System.setProperty("javax.net.debug","off");
//		System.out.println("hey 2");
	try {
		//create SSL server socket
//		System.out.println("hey 3");
		SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//		System.out.println("hey 555");
		SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(sport);
//		System.out.println("hey 4");
		//wait for connection
		while (true) {
//			System.out.println("here secure ");
			SSLSocket sslclient = (SSLSocket) sslServerSocket.accept();
			Thread t = new Thread(() -> SSLserverClient(sslclient));
			t.start();
			
			
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}

private static void SSLserverClient(SSLSocket sslclient) {
	try(SSLSocket sslclientServer = sslclient) {
		
				/*	//output stream
					OutputStream outputStream = sslclientServer.getOutputStream();
					OutputStreamWriter outputwriter = new OutputStreamWriter(outputStream);
					BufferedWriter bufferedWriter = new BufferedWriter(outputwriter);
					//input stream
					InputStream inputStream = sslclientServer.getInputStream();
					InputStreamReader inputReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputReader);
					*/
					// Input stream
					DataInputStream input = new DataInputStream(sslclientServer.getInputStream());

					// Output steam
					DataOutputStream output = new DataOutputStream(sslclientServer.getOutputStream());
					
					// json parser
					JSONParser parser = new JSONParser();
					JSONObject received;
					JSONObject resource;
					String owner;
					Error error = new Error();
					while (true) {
						String rece;
						try {
							rece =  input.readUTF();
							
						} catch (IOException e) {
							// TODO: handle exception
							continue;
						}
						if (rece != null) {
							
							received = (JSONObject) parser.parse(rece);
							logger.info("RECEIVED:"+received.toJSONString());
							// check if there is a resource field
							if (received.containsKey("resource") || received.containsKey("resourceTemplate")) {
								if (received.containsKey("resource")) {
									resource = (JSONObject) received.get("resource");
								} else {
									resource = (JSONObject) received.get("resourceTemplate");
								}
								owner = (String) resource.get("owner");
							} else {
								if (received.containsKey("resource")) {
									output.writeUTF(error.missingRes().toJSONString());
									output.flush();
								} else if (received.containsKey("resourceTemplate")) {
									output.writeUTF(error.missingResT().toJSONString());
									output.flush();
								} else {
									String command = ((String) received.get("command")).toLowerCase();
									if (command.equals("exchange")) {
										Exchange exchange = new Exchange();
										JSONArray serverList = (JSONArray) received.get("serverList");
										exchange.exe(sslclientServer, serverList);
									}
								}
								output.close();
								break;// exit while loop
							}

							// check if the resource is valid
							if (!resource.containsKey("uri") || !resource.containsKey("channel")
									|| !resource.containsKey("owner") || !resource.containsKey("name")
									|| !resource.containsKey("description") || !resource.containsKey("ezserver")
									|| !resource.containsKey("tags") || owner.equals("*") || resource.size() != 7) {
								if (received.containsKey("resource")) {
									output.writeUTF(error.invalidRes().toJSONString());
									output.flush();
								} else if (received.containsKey("resourceTemplate")) {
									output.writeUTF(error.invalidResT().toJSONString());
									output.flush();
								}
								output.close();
								break;// exit while loop
							}

							// check if there is a command field
							else if (!received.containsKey("command")) {
								output.writeUTF(error.missingCmd().toJSONString());
								output.close();
								break;// exit while loop
							}

							String receivedCmd = received.get("command").toString();

							switch (receivedCmd.toLowerCase()) {
							case "query":
								Query query = new Query();
								query.exe(sslclientServer, Server.Store, received);
								break;

							default:
								output.writeUTF(error.unknownCmd().toJSONString());
								output.close();
								break;
							}

						}
					}

		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}
}
