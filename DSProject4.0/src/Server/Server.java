package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;


import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.lang3.RandomStringUtils;


public class Server {
	private static int port = 3000;
	private final static Logger logger = Logger.getLogger(Server.class);
	private static JSONArray Store = new JSONArray();
	private static String secret = "seed";
//	public static final String secret = RandomStringUtils.randomAlphanumeric(20);
	public static JSONArray serverList = new JSONArray();
	public static void main(String[] args) {

		Options options = new Options();

		// command line argument
		options.addOption("advertisedhostname", true, "advertised hostname");
		options.addOption("connectionintervallimit", true, "connection interval limit in seconds");
		options.addOption("exchangeinterval", true, "exchange interval in seconds");
		
		options.addOption("port", true, "server port, an integer");
		options.addOption("secret", true, "secret");
		options.addOption("debug", false, "print debug information");

		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		try (ServerSocket server = factory.createServerSocket(port)) {
			logger.info("Starting the EZShare Server"+"\n");
			logger.info("Using secret: "+ secret + "\n");
			// wait for connection
			while (true) {

				Socket client = server.accept();

				Thread t = new Thread(() -> serverClient(client));
				t.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
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
					
					resource = (JSONObject)received.get("resource");
					
					//check if there is a resource field
					if (received.containsKey("resource") || received.containsKey("resourceTemplate")){
						if (received.containsKey("resource")){
							resource = (JSONObject)received.get("resource");
						}else {
							resource = (JSONObject)received.get("resourceTemplate");
						}
						owner = (String)resource.get("owner");
					}else {
						if (received.containsKey("resource")){
							output.writeUTF(error.missingRes().toJSONString());
						}else if(received.containsKey("resourceTemplate")){
							output.writeUTF(error.missingResT().toJSONString());
						}
						output.close();
						break;//exit while loop
					}
					
			        //check if the resource is valid
					if(!resource.containsKey("uri") || !resource.containsKey("channel") || !resource.containsKey("owner") 
							|| !resource.containsKey("name") || !resource.containsKey("description") ||!resource.containsKey("ezserver")
							|| !resource.containsKey("tags") || owner.equals("*") || resource.size() != 7){
						if (received.containsKey("resource")){
							output.writeUTF(error.invalidRes().toJSONString());
						}else if(received.containsKey("resourceTemplate")){
							output.writeUTF(error.invalidResT().toJSONString());
						}
						output.close();
						break;//exit while loop
					}
					
					//check if there is a command field
					else if (!received.containsKey("command")){
						output.writeUTF(error.missingCmd().toJSONString());
						output.close();
						break;//exit while loop
					}

					String receivedCmd = received.get("command").toString();
					
					switch (receivedCmd.toLowerCase()) {
					case "query":
						Query query = new Query();
						query.exe(clientServer, Store, resource);
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
						if (received.containsKey("secret")){
							String receivedSecret = (String) received.get("secret");
							if (receivedSecret.equals(secret)){
								Share share = new Share();
						        share.exe(clientServer, Store, resource);
							}else {
								output.writeUTF(error.incorrectSecret().toJSONString());
								output.close();
							}
						}else {
							output.writeUTF(error.noSecret().toJSONString());
							output.close();
						}					
						break;
						
					case "fetch":
						Fetch fetch = new Fetch();
						fetch.exe(clientServer, Store, resource);
						break;
					
					default:
						output.writeUTF(error.unknownCmd().toJSONString());
						output.close();
						break;
					}
					
				}
			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
	
	
