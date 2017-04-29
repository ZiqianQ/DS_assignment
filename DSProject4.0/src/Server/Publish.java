package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Publish {

	public void exe(Socket clientServer, JSONArray Store, JSONObject resource) throws IOException {
		try {
			// Input stream
			DataInputStream input = new DataInputStream(clientServer.getInputStream());

			// Output steam
			DataOutputStream output = new DataOutputStream(clientServer.getOutputStream());

			// send respond to client
			JSONObject message = new JSONObject();

			// check if the resource from client is valid or not
			boolean valid = true;

			// rules: not file scheme
			String invalidUri = "file";

			// primary keys
			String uri = (String) resource.get("uri");
			String channel = (String) resource.get("channel");
			String owner = (String) resource.get("owner");

			// other keys
			// String name = (String)resource.get("name");
			// String description = (String)resource.get("description");
			// String tag = (String)resource.get("tags");
			// split tags
			// List<String> tags = Arrays.asList(tag.split(","));

			if (uri.startsWith(invalidUri)) {
				valid = false;
				message.put("response", "error");
				message.put("errorMessage", "cannot publish resource");

			} else {
				for (int i = 0; i < Store.size(); i++) {
					// compare primary key
					JSONObject storeResource = (JSONObject) Store.get(i);
					String storeUri = (String) storeResource.get("uri");
					String storeChannel = (String) storeResource.get("channel");
					String storeOwner = (String) storeResource.get("owner");

					if (uri.equals(storeUri) && channel.equals(storeChannel)) {
						if (owner.equals(storeOwner)) {
							Store.remove(storeResource); // if same primary key,
															// overwrites
							break;
						} else {
							valid = false;
							message.put("response", "error");
							message.put("errorMessage", "cannot publish resource");
							break;
						}
					}
				}
			}

			if (valid == true) {
				Store.add(resource);
				message.put("response", "success");
			}

			output.writeUTF(message.toJSONString());
			output.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
