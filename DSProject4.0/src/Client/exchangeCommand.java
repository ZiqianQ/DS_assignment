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
	
	public static void execute (String ip, int port, String getservers){
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
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
				Client.logger.info("SENT:");
				System.out.println(newCommand.toJSONString());
				
				//send command to server
				output.writeUTF(newCommand.toJSONString());
				output.flush();
				//read from server and print out
				while(true){
					if (input.available()>0) {
						String result = input.readUTF();
						Client.logger.info("REVEIVED:");
						System.out.println(result);
					}
				}
				
			}
			else {
				Client.logger.info("ERROR: missing serverlists");
				
			}

			
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
