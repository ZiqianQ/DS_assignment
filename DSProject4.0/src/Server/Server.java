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
import javax.print.attribute.standard.OutputDeviceAssigned;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.PUBLIC_MEMBER;



public class Server {
	private static int port = 3000;
	private final static Logger logger = Logger.getLogger(Server.class);
	private static JSONArray Store;

	public static void main(String[] args) {

		Options options = new Options();

		// command line argument
		options.addOption("advertisedhostname", true, "advertised hostname");
		options.addOption("connectionintervallimit", true, "connection interval limit in seconds");
		options.addOption("exchangeinterval", true, "exchange interval in seconds");
		;
		options.addOption("port", true, "server port, an integer");
		options.addOption("secret", true, "secret");
		options.addOption("debug", false, "print debug information");

		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		try (ServerSocket server = factory.createServerSocket(port)) {
			logger.info("Starting the EZShare Server"+"\n");
			logger.info("Using secret: aswecan"+"\n");
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
			DataInputStream input = new DataInputStream(client.getInputStream());

			// Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());

			// json parser
			JSONParser parser = new JSONParser();

			JSONObject received;
			 
			while (true) {
				while (input.available() > 0) {
					received = (JSONObject) parser.parse(input.readUTF());
					logger.info("RECEIVED:");
					System.out.println(received.toJSONString()); 
					switch (received.get("command").toString().toLowerCase()) {
					case "query":
						Query query = new Query();
						query.exe(clientServer, Store, received);
						break;
						
					case "publish":
						Publish publish = new Publish();
						publish.exe(client, Store, received);
						break;
						
					case "remove":
						Remove remove = new Remove();
						remove.exe(clientServer, Store, received);
						break;
						
					case "share":
						ServerCommand.Share(client, received);
						break;
					case "fetch":
						ServerCommand.Fetch(client, received);
						break;
					default:
						JSONObject error = new JSONObject();
						error.put("response", "error");
						error.put("errorMessage", "invalid resourceTemplate");
						output.writeUTF(error.toJSONString());
						output.flush();
						// output.close();
						break;
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}