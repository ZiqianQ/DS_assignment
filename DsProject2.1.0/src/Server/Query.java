package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Query {
	public void exe(Socket clientServer, JSONArray Store, JSONObject received) throws IOException, ParseException {
		try {
			// Input stream
			DataInputStream input = new DataInputStream(clientServer.getInputStream());

			// Output steam
			DataOutputStream output = new DataOutputStream(clientServer.getOutputStream());

			// tell client: received
			JSONObject message = new JSONObject();
			message.put("response", "sucess");
			output.writeUTF(message.toJSONString());
			output.flush();
			JSONObject resourceTemplate = (JSONObject) received.get("resourceTemplate");

			// primary key && other rules
			String uri = (String) resourceTemplate.get("uri");
			String channel = (String) resourceTemplate.get("channel");
			String owner = (String) resourceTemplate.get("owner");
			JSONArray tags = (JSONArray) resourceTemplate.get("tags");
			CharSequence name = (CharSequence) resourceTemplate.get("name");
			CharSequence description = (CharSequence) resourceTemplate.get("description");

                        //this array will store the data returned to client
			JSONArray display = new JSONArray();

			for (int i = 0; i < Store.size(); i++) {

				// compare primary key
				JSONObject storeResource = (JSONObject) Store.get(i);
				String storeUri = (String) storeResource.get("uri");
				String storeChannel = (String) storeResource.get("channel");
				// System.out.println(storeChannel);
				String storeOwner = (String) storeResource.get("owner");
				JSONArray storeTags = (JSONArray) storeResource.get("tags");
				String storeName = (String) storeResource.get("name");
				String storeDesc = (String) storeResource.get("description");

				// the template channel must equal the resource channel
				if (!channel.equals(storeChannel)) {
					// do nothing
				}

				// if the template contains an owner that is not"", must equal
				// the resource owner
				else if (!owner.equals("") && !owner.equals(storeOwner)) {
					// do nothing
				}

				// if any tags, should equal
				else if (!tags.isEmpty() && !tags.equals(storeTags)) {
					// do nothing
				}

				// if uri, must equal
				else if (!uri.equals("") && !uri.equals(storeUri)) {
					// do nothing
				}

				// if name, must contain
				else if (!name.equals("") && !storeName.contains(name)) {
					// do nothing
				}

				// if any description, must contain
				else if (!description.equals("") && storeDesc.contains(description)) {
					// do nothing
				}

				else {
					display.add(storeResource);
				}
			}

			// if realy is true, query from serverList
			if ((boolean) received.get("relay")) {
				for (int i = 0; i < Server.serverList.size(); i++) {
					String ip = (String) ((JSONObject) (Server.serverList.get(i))).get("host");
					int port = (int) ((JSONObject) Server.serverList.get(i)).get("port");
					received.put("relay", false);
					queryRelay.execute(ip, port, received, display);
				}
			}

			int count = 0;
			for (int i = 0; i < display.size(); i++) {
				JSONObject displayResource = (JSONObject) display.get(i);

				// the ezserver field be filled with hostname and port
				displayResource.put("ezserver", Server.hostname + " " + Server.setport);

				// the server will never reveal the owner of a resource in a
				// response
				String displayOwner = (String) displayResource.get("owner");
				if (!displayOwner.equals("")) {
					displayResource.put("owner", "*");
				}
				output.writeUTF(displayResource.toJSONString());
				output.flush();
				count++;
			}

			// print {"resultSize" : int }
			JSONObject resultSize = new JSONObject();
			resultSize.put("resultSize", count);
			output.writeUTF(resultSize.toJSONString());
			output.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
