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
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

public class Server {
	private static int port = 3000;
	private static Logger logger = Logger.getLogger(Server.class);
	private static JSONArray Store = new JSONArray();
	// private static String secret = "seed"
	public static String secret = RandomStringUtils.randomAlphanumeric(20);
	public static JSONArray serverList = new JSONArray();
	private static int getinterval = 600000;
	private static String hostname = "Aswecan server";
	private static int connectinterval = 500000;
	private static int setport = 3000;

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

			CommandLineParser commandparser = new DefaultParser();
			CommandLine commandLine = commandparser.parse(options, args);
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
				setport = Integer.parseInt(commandLine.getOptionValue("port"));
			}
			if (commandLine.hasOption("secret")) {
				secret = commandLine.getOptionValue("secret");
			}
			//??????
			if (commandLine.hasOption("debug")) {

			}
			logger.info("Starting the EZShare Server" + "\n");
			logger.info("Using secret: " + secret + "\n");
			logger.info("Using advertised hostname: " + hostname + " \n");
			logger.info("Bound to port " + port + "\n");
			// wait for connection
			while (true) {

				Socket client = server.accept();

				Thread t = new Thread(() -> serverClient(client));
				t.start();
			}

		} catch (Exception e) {
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
				/*int interval = getinterval;
				int delay = 1000;
				Timer t = new Timer();
				t.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						if (serverList != null) {
							Random random = new Random();
							int index = random.nextInt(serverList.size());
							JSONObject server = (JSONObject) serverList.get(index);
							JSONArray serverlist = new JSONArray();
							serverlist.add(server);
							Exchange exchange = new Exchange();
							exchange.exe(clientServer, serverlist);
							
						}
					}
				}, delay, interval);*/

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
