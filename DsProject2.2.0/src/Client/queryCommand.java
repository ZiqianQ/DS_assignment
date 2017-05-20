package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class queryCommand {

	public static void queryCmd(String ip, int port, Resource aResource, boolean debugMode, boolean relay) throws ParseException {

		JSONObject newCommand = new JSONObject();
		JSONObject resource = aResource.render();

		newCommand.put("command", "QUERY");
		newCommand.put("relay", relay);
		newCommand.put("resourceTemplate", resource);

		// print JSONObject
		if (debugMode) {
			Client.logger.info("querying to " + ip + ":" + port + "\n");
			Client.logger.info("SENT:");
			System.out.println(newCommand.toJSONString());
		}

		sendMessage.sender(ip, port, newCommand, debugMode);
	}
}
