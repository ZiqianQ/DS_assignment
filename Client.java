package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Client {

	private static String ip = "sunrise.cis.unimelb.edu.au";//sunrise.cis.unimelb.edu.au
	private static int port = 3780;//3780
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			ClientCommands aClientCommand = new ClientCommands();
			JSONObject newCommand = new JSONObject();
			
			
				case "-query":
					newCommand = aClientCommand.query();
					break;
				
				case "-fetch":
					newCommand = aClientCommand.fetch();
					break;
					
				
			}
			
			
			System.out.println(newCommand.toJSONString());
			
//		    receive server message		
			//String message = input.readUTF();
			//System.out.println(message);
			
//			Send RMI to Server	
			output.writeUTF(newCommand.toJSONString());
			output.flush();
			
//			Print out results received from server..
			String result = input.readUTF();
			while(result!=null){
				result = input.readUTF();
			    System.out.println("Received from server: "+result);
			}
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
		
		
		
	}
	
		


}
