package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;


//import com.sun.scenario.animation.shared.InfiniteClipEnvelope;

//import jdk.nashorn.internal.runtime.JSONFunctions;

public class Server {

	private static int port = 3000;
	private static int count = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		try(ServerSocket server = factory.createServerSocket(port)){
			System.out.println("waiting for connection");
			
			//wait for connection
			while(true){
				count++;
				Socket client = server.accept();
				
				System.out.println("client:"+count+" waiting for connection");
				
				Thread t = new Thread(() ->serverClient(client));
				t.start();
			}
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void serverClient(Socket client){
		try(Socket clientServer = client){

			//Input stream
			DataInputStream input = new DataInputStream(client.getInputStream());
			
			//Output steam
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
			
			System.out.println("Client:"+input.readUTF());
			output.writeUTF("Server: Hi, Client" + count);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
