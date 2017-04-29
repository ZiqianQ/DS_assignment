package Server;



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

	public class queryRelay {
		

		public static void execute (String ip, int port, JSONObject received, JSONArray display) throws ParseException{
			try(Socket socket = new Socket(ip,port)){
				//output stream
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				//input stream
				DataInputStream input = new DataInputStream(socket.getInputStream());
				
				/*//print JSONObject
				Client.logger.info("SENT:");
				System.out.println(newCommand.toJSONString());*/
				
				//send command to server
				output.writeUTF(received.toJSONString());
				output.flush();
				//read from server and print out
				JSONParser parser = new JSONParser();
				JSONObject result;
				System.out.println("relaying to ip:"+ip+"port:"+port);
				while(true){
					if (input.available()>0) {
						 
						result = (JSONObject) parser.parse(input.readUTF());
//						Client.logger.info("REVEIVED:");
						System.out.println(result);
						//only store the resource from other server
						if (result.get("uri")!=null) {
							
							display.add(result);
						}
						if (result.containsKey("resultSize")) {
							break;
						}
					}

				}
				
			}catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
			

