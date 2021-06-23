import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;


/*
 * - Listen for client requests on the specified network address and port
 * 
 */
public class Driver {

	public static void main(String[] args) throws IOException {
		
		int portNum = 0;
		int backlog = 50;
		InetAddress ip;
		Scanner keyboard = new Scanner(System.in);
		//Ask for portNum specify here
		
		ServerSocket house = new ServerSocket(portNum);	//binds the server to a specified port
		
		Socket mailBox = house.accept();				//starts listening for incomming client requests
		
		InputStream letter = mailBox.getInputStream();	//Recieve byte array from client
		InputStreamReader readingGlasses = new InputStreamReader(letter); //turn byte array into characters
		
		//Process input
		
		mailBox.close();
		
	}

}
