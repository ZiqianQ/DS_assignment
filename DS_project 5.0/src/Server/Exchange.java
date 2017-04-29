package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Exchange {

	public void exe(Socket clientServer, JSONArray serverList) {

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

			for (int i = 0; i < serverList.size(); i++) {
				JSONObject servers = (JSONObject) serverList.get(i);
				String host = (String) servers.get("hostname");
				int port = Integer.parseInt((String) servers.get("port"));
				Socket socket = new Socket(host, port);
				if (socket.isConnected()) {
					JSONObject server = new JSONObject();
					server.put("host", host);
					server.put("port", port);
					Server.serverList.add(server);
					socket.close();
				} 
				
			}
			output.writeUTF(Server.serverList.toJSONString());
			output.flush();
			
		} catch (IOException e) {
			// TODO: handle exception
		}

	}

}