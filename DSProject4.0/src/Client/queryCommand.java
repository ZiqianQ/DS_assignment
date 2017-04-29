package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class queryCommand {
	

	public static void execute (String ip, int port, Resource aResource, boolean debugMode){
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			JSONObject newCommand = new JSONObject();
			JSONObject resource = aResource.render();

			newCommand.put("command", "QUERY");
			newCommand.put("relay", false);
			newCommand.put("resourceTemplate", resource);
				

			//print JSONObject
			if(debugMode){
				Client.logger.info("querying to localhost:3000"+"\n");	
				Client.logger.info("SENT:");
				System.out.println(newCommand.toJSONString());
				} 
			//send command to server
			output.writeUTF(newCommand.toJSONString());
			output.flush();
			//read from server and print out
			while(true){
				if (input.available()>0) {
					String result = input.readUTF();
					Client.logger.info("RECEIVED:");
					System.out.println(result);
				}
			}
			
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
		
