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

public class fetchCommand {

	public static void fetchCmd(String ip, int port, Resource aResource, boolean debugMode) throws ParseException {

		JSONObject newCommand = new JSONObject();
		newCommand.put("command", "FETCH");

		JSONObject resource = aResource.render();

		newCommand.put("resourceTemplate", resource);

		if (debugMode) {
			Client.logger.info("Fetching to " + ip + ":" + port + "\n");
			Client.logger.info("SENT:");
			System.out.println(newCommand.toJSONString());
		}

		sendMessage.sender(ip, port, newCommand, debugMode);

	}

}
