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

public class exchangeCommand {
	
	public static void execute (String ip, int port){
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			JSONObject newCommand = new JSONObject();

			newCommand.put("command", "EXCHANGE");
			JSONObject host1 = new JSONObject();
			JSONObject host2 = new JSONObject();
			//host name one
			host1.put("hostname", "192.168.1.1");
			host1.put("port", 3000);
			//host name two
			host2.put("hostname", "192.168.0.1");
			host2.put("port", 3000);

			JSONArray list = new JSONArray();
			list.add(host1);
			list.add(host2);

			newCommand.put("serverList", list);
			
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
			
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}