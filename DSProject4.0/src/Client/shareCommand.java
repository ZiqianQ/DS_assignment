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

public class shareCommand {
	
	public static void execute (String ip, int port, Resource aResource){
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			JSONObject newCommand = new JSONObject();
			JSONObject resource = aResource.render();

			 newCommand.put("command", "SHARE");		
		 		newCommand.put("secret", aResource.getSecret());	
		 		newCommand.put("resource",resource); 
		 		Client.logger.info("SENT:");
		 		System.out.println(newCommand.toJSONString());
		 		
		 		// Send command to Server	
		 		output.writeUTF(newCommand.toJSONString());
		 		output.flush();
		 		//allow server to download 
				// Check if file exists
				String uri= (String) aResource.getUri();  
				File f = new File(uri.substring(8)); 
		 		if(f.exists()){
		 			resource.put("resourceSize", f.length());
		 			
		 			//is this a trigger??
		 			output.writeUTF(resource.toJSONString());
		 			output.flush();
		 			
		 			try {
		 			// Start sending file
						RandomAccessFile byteFile = new RandomAccessFile(f,"r");
						byte[] sendingBuffer = new byte[1024*1024];
						int num;
						// While there are still bytes to send..
						while((num = byteFile.read(sendingBuffer)) > 0){
							//System.out.println(num);
							output.write(Arrays.copyOf(sendingBuffer, num));
							//output.flush();
						}
						byteFile.close();
		 			}catch (IOException e) {
						e.printStackTrace();
					}
		 			
		 		}
		 		 
		 		
		 }catch (IOException e) {
				e.printStackTrace();
			}			   
	}
}