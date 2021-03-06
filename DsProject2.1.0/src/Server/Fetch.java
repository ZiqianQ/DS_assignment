package Server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Fetch {

	public static void exe(Socket client, JSONArray Store, JSONObject resource) {
		Error error = new Error();
		try {
			
			// Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
			
			
			JSONObject message = new JSONObject();
			
			// check if there is an record in server
			if (datainclude(Store, resource)!= null) {
				
				JSONObject display = new JSONObject();
				display = datainclude(Store, resource);
		
				// Send this back to client so that they know what the file is.
				String uri = (String) display.get("uri"); 
				String[] uriPattern = uri.split("/");
				String fileName = uriPattern[uriPattern.length - 1]; 
				
				File f = new File("server_files/" + fileName);


				// Check if file exists in server
				if (f.exists()) {

					message.put("response", "sucess");
					output.writeUTF(message.toJSONString());
					output.flush();
					resource.put("name", fileName);
					resource.put("ezserver", Server.hostname + " " + Server.setport);
					resource.put("resourceSize", f.length());
					try {
						// Send trigger to client
						output.writeUTF(resource.toJSONString());
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
					output.writeUTF(error.invalidResT().toJSONString());
					output.flush();
				}
			} else {
				output.writeUTF(error.missingResT().toJSONString());
				output.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static JSONObject datainclude(JSONArray Store, JSONObject resource) {

		JSONObject display = new JSONObject();
		
		// primary key && other rules
		String uri = (String) resource.get("uri");
		String channel = (String) resource.get("channel");

		for (int i = 0; i < Store.size(); i++) {

			// compare primary key
			JSONObject storeResource = (JSONObject) Store.get(i);
			String storeUri = (String) storeResource.get("uri");
			String storeChannel = (String) storeResource.get("channel");
			
			if (channel.equals(storeChannel) && !uri.equals("") && uri.equals(storeUri)) {

				display = storeResource;
				break;
			}

		}
		
		return display;
	
	}
}
