package Server;

//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Remove {
	
	public void exe(Socket client, JSONArray Store, JSONObject resource) throws IOException{
		try {
			//Input stream
			//DataInputStream input = new DataInputStream(client.getInputStream());
			
			//Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
		
			//primary key
			String uri = (String)resource.get("uri");
			String channel = (String)resource.get("channel");
			String owner = (String)resource.get("owner");
			
			//check if the resource exists in the Store
			boolean exist = false;
			for (int i = 0; i < Store.size(); i++) {
				JSONObject storeResource = (JSONObject)Store.get(i);
				
				//compare primary key, don't need to check other keys
				String storeUri = (String)storeResource.get("uri");
				String storeChannel = (String)storeResource.get("channel");
				String storeOwner = (String)storeResource.get("owner");
			
				if (uri.equals(storeUri) && channel.equals(storeChannel) && owner.equals(storeOwner)){
					exist = true;
					Store.remove(storeResource);
					break;
				}
			}

			//send respond to client
			JSONObject message = new JSONObject();
			if (exist == true){
				message.put("response", "sucess");
				output.writeUTF(message.toJSONString());
				output.flush();	
			}else {
				message.put("response", "error");
				message.put("errorMessage", "cannot remove resource");
				output.writeUTF(message.toJSONString());
				output.flush();
			}
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}
