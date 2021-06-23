import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;


/*
 * - Listen for client requests on the specified network address and port
 *  Commands:
		QUIT(-1, null, "Quit"),
		DATE_AND_TIME(0, "free", "Date and Time"), 
		UPTIME(1, "uptime", "Uptime"), 
		MEMORY_USAGE(2, "free", "Memory Usage"), 
		NETSTAT(3, "netstat", "Netstat"), 
		CURRENT_USERS(4, "w", "Current Users"), 
		RUNNING_PROCESSES(5, "ps", "Running Processes");
 */
public class Driver {

	//static long initialTime;
	
	public static void main(String[] args) throws IOException{
		
		
		int backlog = 50;
		InetAddress loopback = InetAddress.getByName("127.0.0.1");		//For testing purposes
		Scanner input = new Scanner(System.in);
		System.out.println("Enter port number:");
		int portNum = input.nextInt();
		
		System.out.println("Starting Server");
		try (ServerSocket house = new ServerSocket(portNum, backlog, loopback)){ //binds the server to a specified port
			
			//initialTime = System.currentTimeMillis(); //start counting upTime
			System.out.println("Listening for Clients on port " + portNum);
			
			while(true) {
				Socket mailBox = house.accept();				//starts listening for incomming client requests
				
				System.out.println("New Client connected");
				
				InputStream letter = mailBox.getInputStream();	//Recieve byte array from client
				InputStreamReader readingGlasses = new InputStreamReader(letter); //turn byte array into characters
				//BufferedReader l = new BufferedReader(readingGlasses);
				int ID = readingGlasses.read();	//Read from the byte array
				
				OutputStream output = mailBox.getOutputStream();	//get the return address
				PrintWriter returnToSender = new PrintWriter(output, true);
				System.out.println("Input from Client: " + ID);
				while (ID != -1) {
									
					proccessCommand(returnToSender, ID);	//Handle client request					
				}
				
				mailBox.close();//close client connection
			}//End while loop
		} catch (IOException ex) {
			System.out.println("Server Exception" + ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}
	
	static public void proccessCommand(PrintWriter outBox, int ID) throws IOException {
		switch(ID) {
		case -1:
			return;
		case 0:
			//Date and Time
			outBox.println(new Date().toString()); //Send Date to client
			break;
		case 1:
			//Uptime
			RuntimeMXBean RTB = ManagementFactory.getRuntimeMXBean();
			long upTime = TimeUnit.MILLISECONDS.toSeconds(RTB.getUptime()); 
			outBox.println("Server Up Time: " + upTime + " S");
			break;
		case 2:
			//Memory_usage
			Runtime gas = Runtime.getRuntime();
			long memUse = gas.totalMemory() - gas.freeMemory();
			outBox.println("Server Memory Usage: " + memUse);
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
