package Client;

import java.rmi.server.SocketSecurityException;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

 

public class Client {
	

	private static String ip;
    private static int port;

	private static String getchannel = null;
	private static String getdescription = null;
	private static String gethost = null;
	private static String getname = null;
	private static String getowner = null;
	private static String getport = null;
	private static String getsecret = null;
	private static String getservers = null;
	private static String gettags = null;
	private static String geturi = null;
	private static int getsport ;
	public static boolean getsecure = false;
	
	private static boolean relay = false;

	public static boolean debugMode = false;
	public static String debugmode = "off";
	
	private static Resource aResource = new Resource();

	public final static Logger logger = Logger.getLogger(Client.class);

	public static boolean subscribing = false;
	
	public static void main(String[] args) {
		
		// create Options object
		Options options = new Options();
		
		options.addOption("channel", true, "channel");
		options.addOption("debug", false, "print debug information");
		options.addOption("description", true, "resource description");
		options.addOption("exchange", false, "exchange server list with server");
		options.addOption("fetch", false, "fetch resources from server");
		options.addOption("host", true, "server host, a domain name or IP address");
		options.addOption("name", true, "resource name");
		options.addOption("owner", true, "owner");
		options.addOption("port", true, "server port, an integer");
		options.addOption("publish", false, "publish resource on server");
		options.addOption("query", false, "query for resources from server");
		options.addOption("remove", false, "remove resource from server");
		options.addOption("secret", true, "secret");
		options.addOption("servers", true, "server list, host1:port1,host2:port2,...");
		options.addOption("share", false, "share resource on server");
		options.addOption("tags", true, "resource tags, tag1,tag2,tag3,...");
		options.addOption("uri", true, "resource URI");
		options.addOption("relay", true, "relay status");
		options.addOption("h","help", false, "information about how to use"); 
		options.addOption("secure", false, "secure connection");
		options.addOption("subscribe", false, "subscribe channel");
	    try {
	    	
	    	// create the parser
	    	CommandLineParser parser = new DefaultParser();
			CommandLine commandline = parser.parse(options, args);
			
			
	
			if (commandline.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("client command line argument", options);
			}


            //get ip and port first
			if (commandline.hasOption("host")) {
				gethost = commandline.getOptionValue("host");
				ip = gethost;
			}

			if (commandline.hasOption("port")) {
				getport = commandline.getOptionValue("port");
				port = Integer.parseInt(getport);
				
			}

            //set a resource then
			if (commandline.hasOption("channel")) {
				getchannel = commandline.getOptionValue("channel");
				aResource.setChannel(getchannel);
			}
			if (commandline.hasOption("description")) {
				getdescription = commandline.getOptionValue("decription");
				aResource.setDescription(getdescription);
			}
			
			if (commandline.hasOption("name")) {
				getname = commandline.getOptionValue("name");
				aResource.setName(getname);
			}
			if (commandline.hasOption("owner")) {
				getowner = commandline.getOptionValue("owner");
				aResource.setOwner(getowner);
			}

			if (commandline.hasOption("tags")) {
				gettags = commandline.getOptionValue("tags");
				aResource.setTags(gettags);
			}
			if (commandline.hasOption("uri")) {
				geturi = commandline.getOptionValue("uri");
				aResource.setUri(geturi);
			}

			//this one is for shareCommand
			if (commandline.hasOption("secret")) {
				getsecret = commandline.getOptionValue("secret"); 
				aResource.setSecret(getsecret);
			}

            //this one is for exhangeCommand
			if (commandline.hasOption("servers")) {
				getservers = commandline.getOptionValue("servers");
			}
			
			//set relay status
			if (commandline.hasOption("relay")) {
				 relay = new Boolean(commandline.getOptionValue("relay"));
	
			}

			//secure query
			if (commandline.hasOption("secure")) {
				getsecure = true;
				/*SSLClient sslClient = new SSLClient(); 
				sslClient.SSLClient(ip, port, aResource, debugMode,relay);*/
			}
			//set debug status
			if (commandline.hasOption("debug")) {
				 debugMode = true;
				 debugmode = "all";
				 logger.info("setting debug on");	
	
			}
			//sercure port
//			if (commandline.hasOption("sport")) {
//				port = Integer.parseInt(commandline.getOptionValue("sport"));
//			}
			 
			//query resource from server
			if (commandline.hasOption("query")) {
				if (getsecure) {
					SSLClient sslClient = new SSLClient();
					sslClient.SSLClient(ip, port, aResource, debugMode, relay);
				}
				queryCommand query = new queryCommand(); 
				query.queryCmd(ip, port, aResource, debugMode,relay);
			}
			
	
			
			//publish resource to server
			if (commandline.hasOption("publish")) {
				publishCommand publish = new publishCommand();
				publish.publishCmd(ip, port, aResource,debugMode);
			}
			//remove resource from server
			if (commandline.hasOption("remove")) {
				removeCommand remove = new removeCommand();
				remove.removeCmd(ip, port, aResource,debugMode);
			}
			//share file to server
			if (commandline.hasOption("share")) {
				shareCommand share = new shareCommand();
				share.shareCmd(ip, port, aResource,debugMode);
			}
			//fetch files from server
			if (commandline.hasOption("fetch")) {
				fetchCommand fetch = new fetchCommand();
				fetch.fetchCmd(ip, port, aResource,debugMode);
			}
			//exchange server address and port
			if (commandline.hasOption("exchange")) {
				exchangeCommand exchange = new exchangeCommand();
				exchange.exchangeCmd(ip, port, getservers,debugMode); //实际上exchange用不到这个aResource,应该有一个servers list
			} 
			//subscribe from server
			if (commandline.hasOption("subscribe")) {
				subscribeCommand subscribe = new subscribeCommand();
				subscribing = true;
				subscribeCommand.subscibeCmd(ip, port, aResource, debugMode,relay); 
			} 
			//testing the sercure socket
			/*if (commandline.hasOption("secure")) {
				SSLClient sslClient = new SSLClient();
				JSONObject newCommand = new JSONObject();

				newCommand.put("command", "secure");
				newCommand.put("sport", getsport);
				sendMessage send = new sendMessage();
				send.sender(ip, port, newCommand, debugMode);
				sslClient.SSLClient(ip, getsport);
			}*/
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	

}
