package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Remove {
	
	public void exe(Socket client, JSONArray Store, JSONObject received) throws IOException{
		try {
			//Input stream
			DataInputStream input = new DataInputStream(client.getInputStream());
			
			//Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
		
			//store the json received from client to jsonArray
			JSONObject resource = (JSONObject)received.get("resource");
			
			//primary key
			String uri = (String)resource.get("uri");
			String channel = (String)resource.get("channel");
			String owner = (String)resource.get("owner");
			
			for (Iterator iterator = Store.iterator();iterator.hasNext();) {
				//compare primary key
				JSONObject storeResource = (JSONObject)iterator.next();
				String storeUri = (String)storeResource.get("uri");
				String storeChannel = (String)storeResource.get("channel");
				String storeOwner = (String)storeResource.get("owner");
			
				if (uri.equals(storeUri) && channel.equals(storeChannel) && owner.equals(storeOwner)){
					Store.remove(storeResource);
				}
			}

			//send respond to client
			JSONObject message = new JSONObject();
			message.put("response", "sucess");
			output.writeUTF(message.toJSONString());
			output.flush();
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}
