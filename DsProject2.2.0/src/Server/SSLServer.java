package Server;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLServer {
	
public static void SSLsocket(int sport){
	
		//specify the keystore details
		//the keystore file contains an application's own certificate and private key
	    System.out.println("hey ");
		System.setProperty("javax.net.ssl.keyStore", "server_keystore/qiaokey");
		//password to access the private key from keystore file
		System.setProperty("javax.net.ssl.keyStorePassword", "comp90015");
		//if the debug is on , set it to all mode
		//System.setProperty("javax.net.debug", Server_ssl.debugmode);
		System.setProperty("javax.net.debug","all");
		System.out.println("hey 2");
	try {
		//create SSL server socket
		System.out.println("hey 3");
		SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		System.out.println("hey 555");
		SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(sport);
		System.out.println("hey 4");
		//wait for connection
		while (true) {
			System.out.println("here secure ");
			SSLSocket sslclient = (SSLSocket) sslServerSocket.accept();
			Thread t = new Thread(() -> SSLserverClient(sslclient));
			t.start();
			
			
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}

private static void SSLserverClient(SSLSocket sslclient) {
	try(SSLSocket sslclientServer = sslclient) {
		
		//Input stream
		InputStream inputStream =sslclientServer.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		System.out.println("this is secure mode");
		String string = null;
		while((string = bufferedReader.readLine())!=null){
			System.out.println(string);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}
}