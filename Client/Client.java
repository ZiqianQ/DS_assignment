package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private static String ip = "sunrise.cis.unimelb.edu.au";
	private static int port = 3780;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		try(Socket socket = new Socket(ip,port)){
			//output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//input stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			output.writeUTF("Hi,i want to connect");
			output.flush();
			
//		    receive server message		
			String message = input.readUTF();
			System.out.println(message);
			
//			Write to Server	
			output.writeUTF(message);
			output.flush();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
