package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class sendMessage {

	public static void sender(String ip, int port, JSONObject newCommand, boolean debugMode) throws ParseException {
		try (Socket socket = new Socket(ip, port)) {
			// output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			// input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());

			// send command to server
			output.writeUTF(newCommand.toJSONString());
			output.flush();

			JSONParser parser = new JSONParser();

			// read from server and print out
			while (true) {
				if (input.available() > 0) {
					String result = input.readUTF();
					Client.logger.info("RECEIVED:");
					System.out.println(result);
			
			
				
				    //String result = input.readUTF();
					// ONLY USEFUL WHEN ITS FETCH COMMAND
					JSONObject command = (JSONObject) parser.parse(result);
					if (command.containsKey("resourceSize")) {
						String fileName = "client_files/" + (String) command.get("name");

						// The file location
						// Create a RandomAccessFile to read and write the
						// output file.
						RandomAccessFile downloadingFile = new RandomAccessFile(fileName, "rw");
						// Find out how much size is remaining to get from the
						// server.
						long fileSizeRemaining = (Long) command.get("resourceSize");
						int chunkSize = setChunkSize(fileSizeRemaining);

						// Represents the receiving buffer
						byte[] receiveBuffer = new byte[chunkSize];
						// Variable used to read if there are remaining size
						// left to read.
						int num;
						while ((num = input.read(receiveBuffer)) > 0) {
							// Write the received bytes into the
							// RandomAccessFile
							downloadingFile.write(Arrays.copyOf(receiveBuffer, num));

							// Reduce the file size left to read..
							fileSizeRemaining -= num;

							// Set the chunkSize again
							chunkSize = setChunkSize(fileSizeRemaining);
							receiveBuffer = new byte[chunkSize];

							// If you're done then break
							if (fileSizeRemaining == 0) {
								break;		
							}
						}
						downloadingFile.close();
					}
					
				}
				
			}
		

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// ONLY USEFUL WHEN ITS FETCH COMMAND
	private static int setChunkSize(long fileSizeRemaining) {
		// determine the chunksize
		int chunkSize = 1024 * 1024;
		// if the file size remaining is less than chunksize reset it
		if (fileSizeRemaining < chunkSize) {
			chunkSize = (int) fileSizeRemaining;
		}
		return chunkSize;
	}

}
