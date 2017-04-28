package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Query {
	public void exe(Socket client, JSONArray Store, JSONObject received) throws IOException{
		try {
			//Input stream
			DataInputStream input = new DataInputStream(client.getInputStream());
			
			//Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
			
			//tell client: received 
			JSONObject message = new JSONObject();
			message.put("response", "sucess");
			output.writeUTF(message.toJSONString());
			output.flush();
			
			JSONObject resourceTemplate = (JSONObject)received.get("resourceTemplate");
			//primary key && other rules
			String uri = (String)resourceTemplate.get("uri");
			String channel = (String)resourceTemplate.get("channel");
			String owner = (String)resourceTemplate.get("owner");
			JSONArray tags = (JSONArray)resourceTemplate.get("tags");
			CharSequence name = (CharSequence)resourceTemplate.get("name");
			
			JSONArray display = new JSONArray();
			for (Iterator iterator = Store.iterator();iterator.hasNext();){
				display.add(iterator.next());
			}
			
			for (Iterator iterator = display.iterator();iterator.hasNext();) {
				//compare primary key
				JSONObject storeResource = (JSONObject)iterator.next();
				String storeUri = (String)storeResource.get("uri");
				String storeChannel = (String)storeResource.get("channel");
				String storeOwner = (String)storeResource.get("owner");
				JSONArray storeTags = (JSONArray)storeResource.get("tags");
				String storeName = (String)storeResource.get("name");
			
				if (!channel.equals(storeChannel)){
					display.remove(storeResource);
				}
				
				else if (!owner.equals("") && !owner.equals(storeOwner)){
					display.remove(storeResource);
				}
				
				else if (!tags.isEmpty() && !tags.equals(storeTags)){
					display.remove(storeResource);
				}
				
				else if (!uri.equals("") && !uri.equals(storeUri)){
					display.remove(storeResource);
				}
				
				else if (!name.equals("") && !storeName.contains(name)){
					display.remove(storeResource);
				}
			}
			
			for (Iterator iterator = display.iterator();iterator.hasNext();){
				output.writeUTF(((JSONObject)iterator.next()).toJSONString());
				output.flush();
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
