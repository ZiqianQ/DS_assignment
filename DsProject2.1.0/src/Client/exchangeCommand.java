package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class exchangeCommand {
	
	public static void exchangeCmd (String ip, int port, String getservers, boolean debugMode) throws ParseException{
		
			JSONObject newCommand = new JSONObject();

			newCommand.put("command", "EXCHANGE");
			
			if (getservers!=null) {
				String[] serverList = getservers.split(",");
				for (int i = 0; i < serverList.length; i++) {
					String host = serverList[i].split(":")[0];
					String ports = serverList[i].split(":")[1];
					JSONObject server = new JSONObject();
					server.put("hostname", host);
					server.put("port", ports);
					JSONArray serve = new JSONArray();
					serve.add(server);
					newCommand.put("serverList", serve);
				}
				
				
				//print JSONObject
				//print JSONObject
				if(debugMode){

					Client.logger.info("SENT:");
					System.out.println(newCommand.toJSONString());
					} 
				
				sendMessage send = new sendMessage();
				send.sender(ip, port, newCommand, debugMode);
		
				
			}
			else { 
				JSONObject errm = new JSONObject();
					errm.put("response", "error");
					errm.put("errorMessage", "missing or invalid serverlists");
				Client.logger.info("RECEIVED:");
				System.out.println(errm);
			}

			
		
	}
}