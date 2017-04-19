package Client;

import org.apache.commons.cli.*;

public class client {
	
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
	private static Resource aResource = new Resource();//新建了一个Resource class 管理resource  initialize后是template格式
	
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
		options.addOption("h","help", false, "information about how to use");
		
		
	   
	    try {
	    	// create the parser
	    	CommandLineParser parser = new DefaultParser();
			CommandLine commandline = parser.parse(options, args);
			
		
            //首先先把resource设置好 看command line 有没有对 channel name owner description tags uri的设置
			//notice:::: 还没有整理 host port servers secret 
			if (commandline.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				
				formatter.printHelp("client command line argument", options);
			}
			if (commandline.hasOption("channel")) {
				getchannel = commandline.getOptionValue("channel");
				aResource.setChannel(getchannel);
			}
			if (commandline.hasOption("description")) {
				getdescription = commandline.getOptionValue("decription");
				aResource.setDescription(getdescription);
			}
			if (commandline.hasOption("host")) {
				gethost = commandline.getOptionValue("host");
			}
			if (commandline.hasOption("name")) {
				getname = commandline.getOptionValue("name");
				aResource.setName(getname);
			}
			if (commandline.hasOption("owner")) {
				getowner = commandline.getOptionValue("owner");
				aResource.setOwner(getowner);
			}
			if (commandline.hasOption("port")) {
				getport = commandline.getOptionValue("port");
			}
			if (commandline.hasOption("secert")) {
				getsecret = commandline.getOptionValue("secret");
			}
			if (commandline.hasOption("servers")) {
				getservers = commandline.getOptionValue("servers");
			}
			if (commandline.hasOption("tags")) {
				gettags = commandline.getOptionValue("tags");
				aResource.setTags(gettags);
			}
			if (commandline.hasOption("uri")) {
				geturi = commandline.getOptionValue("uri");
				aResource.setUri(geturi);
			}
			
			//整理好之后就可以向server端传command了，把整理好的resource跟着command传过去
			//此处单独建立了一个ClientCommand 文件 管理执行Cmd
			//因为query publish remove share exchange 很相似 就合并到一个method里面了，详见 ClientCommand.java 里面 executeCmd（）
			//因为fetch跟其他命令相差较大 单独拿出来了 详见ClientCommand.java 里面 Fetch（）和 setChunkSize

			//query resource from server
			if (commandline.hasOption("query")) {
				ClientCommand clientQuery = new ClientCommand();
				clientQuery.executeCmd("query", aResource);
			}
			//publish resource to server
			if (commandline.hasOption("publish")) {
				ClientCommand clientPublish = new ClientCommand();
				clientPublish.executeCmd("publish", aResource);
			}
			//remove resource from server
			if (commandline.hasOption("remove")) {
				ClientCommand clientRemove = new ClientCommand();
				clientRemove.executeCmd("remove", aResource);
			}
			//share file to server
			if (commandline.hasOption("share")) {
				ClientCommand clientShare = new ClientCommand();
				clientShare.executeCmd("share", aResource);
			}
			//fetch files from server
			if (commandline.hasOption("fetch")) {
				ClientCommand clientFetch = new ClientCommand();
				clientFetch.Fetch(aResource);
			}
			//exchange server address and port
			if (commandline.hasOption("exchange")) {
				ClientCommand clientExchange = new ClientCommand();
				clientExchange.executeCmd("exchange", aResource); //实际上exchange用不到这个aResource
			} 
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	

}
