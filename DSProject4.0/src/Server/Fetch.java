package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Fetch {

	public static void exe(Socket client, JSONArray store, JSONObject received) {
		try {
			// Input stream
			DataInputStream input = new DataInputStream(client.getInputStream());

			// Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
			JSONObject message = new JSONObject();
			 //check if there is an record in server
			System.out.println(datainclude( store,received));
			if(datainclude( store,received)){
			
			// Send this back to client so that they know what the file is.
			JSONObject resourcesTemplate = (JSONObject) received.get("resourceTemplate");
			String fileName = (String) resourcesTemplate.get("name");
			File f = new File("server_files/" + fileName);

			// prepare sending files to client

			// Check if file exists in server
			if (f.exists()) {

				message.put("response", "sucess");
				output.writeUTF(message.toJSONString());
				output.flush();

				resourcesTemplate.put("resourceSize", f.length());
				try {
					// Send trigger to client
					output.writeUTF(received.toJSONString());
					output.flush();
					// Start sending file
					RandomAccessFile byteFile = new RandomAccessFile(f, "r");
					byte[] sendingBuffer = new byte[1024 * 1024];
					int num;
					// While there are still bytes to send..
					while ((num = byteFile.read(sendingBuffer)) > 0) {
						// System.out.println(num);
						output.write(Arrays.copyOf(sendingBuffer, num));
					}
					byteFile.close();
					JSONObject finish = new JSONObject();
					finish.put("resultSize", "1");
					output.writeUTF(finish.toJSONString());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				 
				message.put("response", "error");
		    	message.put("errorMessage", "invalid resource");
		    	output.writeUTF(message.toJSONString());
				output.flush();
			}
			}
			else{ 
				message.put("response", "error");
		    	message.put("errorMessage", "missing resource");
		    	output.writeUTF(message.toJSONString());
				output.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static boolean datainclude(JSONArray store, JSONObject received) {
		Boolean exist = false;
		JSONArray display = new JSONArray();
		for (int i =0 ;i<store.size();i++){
			display.add(store.get(i));
		}
		JSONObject resourceTemplate = (JSONObject)received.get("resourceTemplate");
		//primary key && other rules
		String uri = (String)resourceTemplate.get("uri");
		String channel = (String)resourceTemplate.get("channel");
		
		for (int i =0 ;i<display.size();i++) {
		
			//compare primary key
			JSONObject storeResource = (JSONObject)display.get(i);
			String storeUri = (String)storeResource.get("uri");
			String storeChannel = (String)storeResource.get("channel");  
			if (!channel.equals(storeChannel)){

				display.remove(storeResource);
			}
			if (!uri.equals("") && !uri.equals(storeUri)){
				display.remove(i); 
			}
			
		}
		
		if(display.size()>0){
			exist= true; 
		}
		return exist;
	}

	private static int setChunkSize(long fileSizeRemaining) {
		// determine the chunksize
		int chunkSize = 1024 * 1024;
		// if the file size remaining is less than chunksize reset it
		if (fileSizeRemaining < chunkSize) {
			chunkSize = (int) fileSizeRemaining;
		}
		return chunkSize;
	}

}
