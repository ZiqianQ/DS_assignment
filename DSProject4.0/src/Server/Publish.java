package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Publish {
	
	public void exe(Socket client, JSONArray Store, JSONObject received) throws IOException{
		try {
		    //Input stream
			DataInputStream input = new DataInputStream(client.getInputStream());
		
			//Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
		
			//send respond to client
			JSONObject message = new JSONObject();
		
			//store the json received from client to jsonArray
			JSONObject resource = (JSONObject)received.get("resource");
		
			//check if the resource from client is valid or not
			boolean valid = true;
				
			//primary key
			String uri = (String)resource.get("uri");
			String channel = (String)resource.get("channel");
			String owner = (String)resource.get("owner");
		
			//rules: not file scheme, only http and ftp
			CharSequence validUri1 = "http";
			CharSequence validUri2 = "ftp";
		
			if (!uri.contains(validUri1) && !uri.contains(validUri2)){
				valid = false; 
				message.put("response", "error");
		    	message.put("errorMessage", "missing resource"); 
			}
		
			else if (owner.equals("*")){
				valid = false; 
				message.put("response", "error");
		    	message.put("errorMessage", "invalid resource"); 
			}
		
			for (Iterator iterator = Store.iterator();iterator.hasNext();) {
				//compare primary key
				JSONObject storeResource = (JSONObject)iterator.next();
				String storeUri = (String)storeResource.get("uri");
				String storeChannel = (String)storeResource.get("channel");
				String storeOwner = (String)storeResource.get("owner");
			
				if (uri.equals(storeUri) && channel.equals(storeChannel)){
					if (owner.equals(storeOwner)){
						Store.remove(storeResource);
					}else {
						valid = false;
						message.put("response", "error");
						message.put("errorMessage", "invalid resource");
						
					}
				}
			}

			if (valid == true) {
				Store.add(resource);
				message.put("response", "success");	
			}
		
			output.writeUTF(message.toJSONString());
	    	output.flush();	
	    	
	    }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
