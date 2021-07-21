import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;

public class I_Server {
	
	public static void main(String[] args) throws IOException{
		if (args.length < 1) {System.err.println("\n Usage: java I_Server <listening port>\n"); return;} //process args string
		
		int backlog = 100;														//Set the max number of queued connections
		//InetAddress loopback = InetAddress.getByName("127.0.0.1");				//For use loopback address for testing purposes
		//Scanner input = new Scanner(System.in);
		//System.out.println("Enter port number:");
		int portNum = Integer.parseInt(args[0]);
		
		
		//try (ServerSocket house = new ServerSocket(portNum, backlog, loopback)){ 	//binds the server to loopback port for testing locally
		try (ServerSocket house = new ServerSocket(portNum, backlog)){ 				//binds the server to a specified port
			System.out.println("Starting Server on 139.62.210.153");				//easier to find IP
			System.out.println("Listening for Clients on port " + portNum);
			
			while(true) {
				//System.out.println("looping...");
				Socket mailBox = house.accept();								//starts listening for incoming client requests
				
				System.out.println("New Client connected");
				
				new ServerThread(mailBox).start();
				//mailBox.close();
				
			}//End while loop
		} catch (IOException ex) {
			System.out.println("Server Exception" + ex.getMessage());
			ex.printStackTrace();
		}//End Catch
		
	}//End main
	
	

}
