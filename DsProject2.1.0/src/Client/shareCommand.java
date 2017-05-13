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

public class shareCommand {

	public static void shareCmd(String ip, int port, Resource aResource, boolean debugMode) throws ParseException {

		JSONObject newCommand = new JSONObject();
		JSONObject resource = aResource.render();

		newCommand.put("command", "SHARE");
		newCommand.put("secret", aResource.getSecret());
		newCommand.put("resource", resource);
		if (debugMode) {
			Client.logger.info("Sharing to " + ip + ":" + port + "\n");
			Client.logger.info("SENT:");
			System.out.println(newCommand.toJSONString());
		}

		sendMessage send = new sendMessage();
		send.sender(ip, port, newCommand, debugMode);

	}
}
