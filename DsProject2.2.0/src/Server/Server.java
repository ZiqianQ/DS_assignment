package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser; 
import org.apache.commons.cli.Options;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

public class Server {
	public static int port = 3000;
	private static Logger logger = Logger.getLogger(Server.class);
	public static JSONArray Store = new JSONArray();
	// private static String secret = "seed"
	public static String secret = RandomStringUtils.randomAlphanumeric(20);
	public static JSONArray serverList = new JSONArray();
	public static JSONArray sslserverList = new JSONArray();
	private static int getinterval = 600;
	public static String hostname = "Aswecan server";
	private static int connectinterval = 500000;
	//public static int setport = 3000;
	public static int sport =3781;
	private static Date lastTime = new Date();
	private static InetAddress lastip;

	
	 

	public static void main(String[] args) {

//		JSONObject server = new JSONObject();
//		server.put("hostname", "10.12.65.16");
//		server.put("port", 3000);
//		serverList.add(server);
//		
//		JSONObject sserver = new JSONObject();
//		sserver.put("hostname", "10.13.64.37");
//		sserver.put("port", 3781);
//		sslserverList.add(sserver);
		
		Options options = new Options();

		// command line argument
		options.addOption("advertisedhostname", true, "advertised hostname");
		options.addOption("connectionintervallimit", true, "connection interval limit in seconds");
		options.addOption("exchangeinterval", true, "exchange interval in seconds");
		options.addOption("port", true, "server port, an integer");
		options.addOption("secret", true, "secret");
		options.addOption("debug", false, "print debug information");
		options.addOption("sport", true, "Secure server port, an integer");

		CommandLineParser commandparser = new DefaultParser();
		CommandLine commandLine;
		try {
			commandLine = commandparser.parse(options, args);
			if (commandLine.hasOption("exchangeinterval")) {
				getinterval = Integer.parseInt(commandLine.getOptionValue("exchangeinterval")) * 1000;
			}
			if (commandLine.hasOption("advertisedhostname")) {
				hostname = commandLine.getOptionValue("advertisedhostname");
			}
			if (commandLine.hasOption("connectionintervallimit")) {
				connectinterval = Integer.parseInt(commandLine.getOptionValue("connectionintervallimit"));
			}
			if (commandLine.hasOption("port")) {
				port = Integer.parseInt(commandLine.getOptionValue("port"));
			}
			if (commandLine.hasOption("secret")) {
				secret = commandLine.getOptionValue("secret");
			} 
			if (commandLine.hasOption("sport")) {
				sport = Integer.parseInt(commandLine.getOptionValue("sport"));
			}
			int interval = getinterval;
			   int delay = 1000;
			   Timer timer = new Timer();
			   
			   timer.schedule(new TimerTask() {
			    public void run() {
//			     if (serverList.size()>0) {
//			    	 for (int i = 0; i < serverList.size(); i++){
//			    		 System.out.println(serverList.get(i).toString());
//			    	 }
//			      Random random = new Random();
//			      int index = random.nextInt(serverList.size());
//			      System.out.println(index);
//			      JSONObject server = (JSONObject) serverList.get(index);
//			      System.out.println(server.toString());
//			      String host1 = (String) server.get("hostname");
//			      System.out.println(host1);
//			      int port1 = (int)server.get("port");
//			      System.out.println(port1);
//			      
//			      try(Socket socket1 = new Socket(host1, port1);){
//			    	  AutoExchange exchange = new AutoExchange();
//			    	  exchange.exe(host1, port1, serverList);
//			       //System.out.println("connected");
//			       if (socket1.isConnected()) {
//			        socket1.close();
//			       // System.out.println("fine!");
//			       }
//			      } catch (Exception e) {
//			       // TODO: handle exception
//			       serverList.remove(index);
//			       //System.out.println("cannot conncet");
//			      }  
//			     } 
			     
			     if (sslserverList.size()>0) {
			    	  
				      Random random = new Random();
				      int index = random.nextInt(sslserverList.size());
				      //System.out.println(index);
				      JSONObject sserver = (JSONObject) sslserverList.get(index);
				     // System.out.println(sserver.toString());
				      String host1 = (String) sserver.get("hostname");
				      //System.out.println(host1);
				      int port1 = (int)sserver.get("port");
				      //System.out.println(port1);
				      SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				      try(SSLSocket socket1 = (SSLSocket) sslsocketfactory.createSocket(host1, port1);){
				    	  SSLAutoExchange exchange = new SSLAutoExchange();
				    	  exchange.exe(host1, port1, sslserverList); 
				       if (socket1.isConnected()) {
				        socket1.close(); 
				       }
				      } catch (Exception e) {
				       // TODO: handle exception
				       sslserverList.remove(index);
				       System.out.println("cannot conncet");
				      }  
				     } 
			     
			     
			    }
			   }, delay, interval);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
			
		while(true) {
			
		    thread1.start();
			secureServ.run();
			
		}

	}
	
	static Thread thread1 = new Thread(){
		public void run(){
		//servers(port);
			//print out the server information 
			
			logger.info("Starting the EZShare Server");
			logger.info("Bound to port " + port ); 
			logger.info("Starting the EZShare Secure Server" );
			logger.info("Bound to secure port " + sport);
			logger.info("Using secret: " + secret );
			logger.info("Using advertised hostname: " + hostname);
		servers(port);
		
		}
	};
	static Thread secureServ = new Thread(){
		public void run(){
			
		SSLServer.SSLsocket(sport);
		
			
		}
	};
	
	private static void servers(int port){
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		try {
			ServerSocket server = factory.createServerSocket(port); 
			
			// wait for connection
			while (true) {
				Date date = new Date(); 
					Socket client = server.accept();
					if(((date.getTime()-lastTime.getTime())>getinterval&& client.getLocalAddress()!=lastip||(lastip==null))){
					lastip = client.getLocalAddress();
					lastTime = date;
						Thread t = new Thread(() -> serverClient(client));
					t.start();	
					} else{
						DataOutputStream response = new DataOutputStream(client.getOutputStream());
						Error error1 = new Error();
						response.writeUTF(error1.interval().toJSONString());
						response.flush();
						response.close();
						client.close();
					}
			}

		} catch (Exception e) {
		//e.printStackTrace();
		}
	}

	private static void serverClient(Socket client) {
		try (Socket clientServer = client) { 
			// Input stream
			DataInputStream input = new DataInputStream(clientServer.getInputStream());
			// Output steam
			DataOutputStream output = new DataOutputStream(clientServer.getOutputStream());
			// json parser
			JSONParser parser = new JSONParser();
			JSONObject received;
			JSONObject resource;
			String owner;
			Error error = new Error();
			while (true) {
				
				if (input.available() > 0) {
					received = (JSONObject) parser.parse(input.readUTF());
					logger.info("RECEIVED:");
					System.out.println(received.toJSONString());
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
						} else if (received.containsKey("resourceTemplate")) {
							output.writeUTF(error.missingResT().toJSONString());
						} else {
							String command = ((String) received.get("command")).toLowerCase();
							if (command.equals("exchange")) {
								Exchange exchange = new Exchange();
								JSONArray serverList = (JSONArray) received.get("serverList");
								exchange.exe(clientServer, serverList);
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
						} else if (received.containsKey("resourceTemplate")) {
							output.writeUTF(error.invalidResT().toJSONString());
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
						query.exe(clientServer, Store, received);
						break;

					case "publish":
						Publish publish = new Publish();
						publish.exe(clientServer, Store, resource);
						break;

					case "remove":
						Remove remove = new Remove();
						remove.exe(clientServer, Store, resource);
						break;

					case "share":
						if (received.containsKey("secret")) {
							String receivedSecret = (String) received.get("secret");
							if (receivedSecret.equals(secret)) {
								Share share = new Share();
								share.exe(clientServer, Store, resource);
							} else {
								output.writeUTF(error.incorrectSecret().toJSONString());
								output.close();
							}
						} else {
							output.writeUTF(error.noSecret().toJSONString());
							output.close();
						}
						break;

					case "fetch":
						Fetch fetch = new Fetch();
						fetch.exe(clientServer, Store, resource);
						break;
						
					case "subscribe":
						Subscribe subscribe = new Subscribe();
						subscribe.exe(clientServer, received);
						break;

					default:
						output.writeUTF(error.unknownCmd().toJSONString());
						output.close();
						break;
					}

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
