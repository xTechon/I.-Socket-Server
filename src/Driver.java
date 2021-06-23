import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;


/*
 * - Listen for client requests on the specified network address and port
 * 
 */
public class Driver {

	public static void main(String[] args) throws IOException{
		
		
		int backlog = 50;
		InetAddress loopback = InetAddress.getByName("127.0.0.1");		//For testing purposes
		Scanner input = new Scanner(System.in);
		System.out.println("Enter port number:");
		int portNum = input.nextInt();
		
		System.out.println("Starting Server");
		try (ServerSocket house = new ServerSocket(portNum, backlog, loopback)){ //binds the server to a specified port
			while(true) {
				System.out.println("Listening for Clients");
				Socket mailBox = house.accept();				//starts listening for incomming client requests
				
				InputStream letter = mailBox.getInputStream();	//Recieve byte array from client
				InputStreamReader readingGlasses = new InputStreamReader(letter); //turn byte array into characters
				int ID = readingGlasses.read();	//Read from the byte array
				System.out.println("Input from Client: " + ID);
				proccessCommand(mailBox, ID);	//Handle client request
				//Process input
				/* Commands:
				 * QUIT(-1, null, "Quit"),
					DATE_AND_TIME(0, "free", "Date and Time"), 
					UPTIME(1, "uptime", "Uptime"), 
					MEMORY_USAGE(2, "free", "Memory Usage"), 
					NETSTAT(3, "netstat", "Netstat"), 
					CURRENT_USERS(4, "w", "Current Users"), 
					RUNNING_PROCESSES(5, "ps", "Running Processes");
				 */
				
				mailBox.close();//close client connection
			}//End while loop	
		} catch (IOException ex) {
			System.out.println("Server Exception" + ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}
	
	static public void proccessCommand(Socket outBox, int ID) throws IOException {
		OutputStream output = outBox.getOutputStream();	//get the return address
		
		switch(ID) {
		case -1:
			return;
		case 0:
			//Date and Time
			break;
		case 1:
			//Updtime
			break;
		case 2:
			//Memory_usage
			break;
		case 3:
			//Netstat
			break;
		case 4:
			//Current_Users
			break;
		case 5:
			//Running Processes
			break;
		}
	}

}
