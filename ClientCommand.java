package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientCommand {
	
	private static String ip = "sunrise.cis.unimelb.edu.au";
	private static int port = 3780;
	
	public static void executeCmd (String command, Resource aResource){
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			JSONObject newCommand = new JSONObject();
			JSONObject resource = aResource.render();
			switch (command){
			case "query":  
				newCommand.put("command", "QUERY"); 
				newCommand.put("relay", false);
				newCommand.put("resourceTemplate", resource);
				break;
				
			case "publish":
				newCommand.put("command", "PUBLISH");
				newCommand.put("resource", resource);
				break;
				
			case "remove": 
				newCommand.put("command", "REMOVE");
			    newCommand.put("resource", resource); 
			    break;
			    
			case "share": 
				newCommand.put("command", "SHARE");
			    newCommand.put("resource", resource);
			    break;
			    
			case "exchange":
				newCommand.put("command", "EXCHANGE");
				JSONObject host1 = new JSONObject();
				JSONObject host2 = new JSONObject();
				//host name one
				host1.put("hostname", "192.168.1.1");
				host1.put("port", 3000);
				//host name two
				host2.put("hostname", "192.168.0.1");
				host2.put("port", 3000);

				JSONArray list = new JSONArray();
				list.add(host1);
				list.add(host2);

				newCommand.put("serverList", list);
				
				break;
				
			}
			
			//print JSONObject
			System.out.println(newCommand.toJSONString());
			
			//send command to server
			output.writeUTF(newCommand.toJSONString());
			output.flush();
			//read from server and print out
			while(true){
				if (input.available()>0) {
					String result = input.readUTF();
					System.out.println("[REVEIVED FROM SERVER]: "+result);
				}
			}
			
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void Fetch(Resource aResource) throws ParseException {
		 try(Socket socket = new Socket(ip, port);){
				// Output and Input Stream
				DataInputStream input = new DataInputStream(socket.getInputStream());
			    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				   
			    
				JSONObject newCommand = new JSONObject();
		 		newCommand.put("command", "FETCH");
		 		
		 		JSONObject resource = aResource.render();
		 		
		 		newCommand.put("resource",resource);  
		
		 		System.out.println(newCommand.toJSONString());
		 		
		 		// Send RMI to Server
		 		output.writeUTF(newCommand.toJSONString());
		 		output.flush();
		 		JSONParser parser = new JSONParser();
		 		// Print out results received from server..
		 		while (true){
		 			if(input.available() > 0){
		 				String result = input.readUTF();
		 				System.out.println("SERVER RESPONSE: "+result);
		 				JSONObject command = (JSONObject) parser.parse(result);
		 				if(command.containsKey("uri")){
		 					System.out.println(command.get("uri"));
		 					// The file location
								String fileName = "client_files/"+command.get("name");
								// Create a RandomAccessFile to read and write the output file.
								RandomAccessFile downloadingFile = new RandomAccessFile(fileName, "rw");
								// Find out how much size is remaining to get from the server.
								long fileSizeRemaining = (Long) command.get("resourceSize");
								int chunkSize = setChunkSize(fileSizeRemaining);
								
								// Represents the receiving buffer
								byte[] receiveBuffer = new byte[chunkSize];
								// Variable used to read if there are remaining size left to read.
								int num;
								System.out.println("Downloading "+fileName+" of size "+fileSizeRemaining);
								while((num=input.read(receiveBuffer))>0){
									// Write the received bytes into the RandomAccessFile
									downloadingFile.write(Arrays.copyOf(receiveBuffer, num));
									
									// Reduce the file size left to read..
									fileSizeRemaining-=num;
									
									// Set the chunkSize again
									chunkSize = setChunkSize(fileSizeRemaining);
									receiveBuffer = new byte[chunkSize];
									
									// If you're done then break
									if(fileSizeRemaining==0){
										break;
									}
								}
								System.out.println("File received!");
								downloadingFile.close();
		 				}
		 			}
		 		}
				
			}catch (IOException e) {		
		} catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
	}
	
	private static int setChunkSize(long fileSizeRemaining) {
		// determine the chunksize
		int chunkSize = 1024*1024;
		//if the file size remaining is less than chunksize reset it
		if (fileSizeRemaining<chunkSize) {
			chunkSize=(int) fileSizeRemaining;
		}
		return chunkSize;
	}

}
