package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//import com.sun.net.ssl.HostnameVerifier;
//import com.sun.org.apache.bcel.internal.util.SecuritySupport;
//import com.sun.webkit.ThemeClient;

public class Share {

	public void exe(Socket client, JSONArray Store, JSONObject resource) throws IOException {
		try {
			// Input stream
			DataInputStream input = new DataInputStream(client.getInputStream());

			// Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());

			// send respond to client
			JSONObject message = new JSONObject();

			// check if the resource from client is valid or not
			boolean valid = true;

			// primary key
			String uri = (String) resource.get("uri");
			String channel = (String) resource.get("channel");
			String owner = (String) resource.get("owner");

			if (!uri.startsWith("file")) {
				valid = false;
				message.put("response", "error");
				message.put("errorMessage", "missing resource");
			}

			else if (owner.equals("*")) {
				valid = false;
				message.put("response", "error");
				message.put("errorMessage", "invalid resource(owner)");
			}

			for (int i = 0; i < Store.size(); i++) {
				// compare primary key
				JSONObject storeResource = (JSONObject) Store.get(i);
				String storeUri = (String) storeResource.get("uri");
				String storeChannel = (String) storeResource.get("channel");
				String storeOwner = (String) storeResource.get("owner");

				if (uri.equals(storeUri) && channel.equals(storeChannel)) {
					if (owner.equals(storeOwner)) {
						Store.remove(storeResource);
					} else {
						valid = false;
						message.put("response", "error");
						message.put("errorMessage", "invalid resource(repeat)");

					}
				}
			}

			if (valid == true) {
				String[] uriPattern = uri.split("/");
				String fileName = uriPattern[uriPattern.length - 1];
				File file = new File("server_files/" + fileName);
				//System.out.println(fileName);
				// check if the file is exist
				if (file.exists()) {
					Store.add(resource);
					message.put("response", "success");
					//message.put("response", "The resource has been shared");
					// System.out.println(Store.toJSONString());
				} else {
					message.put("response", "error");
					message.put("errorMessage", "invalid resource(not exist)");
				}
			}

			output.writeUTF(message.toJSONString());
			output.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
