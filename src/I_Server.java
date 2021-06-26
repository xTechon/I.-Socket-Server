import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;

public class I_Server {
	
	public static void main(String[] args) throws IOException{
		if (args.length < 1) {System.err.println("\n Usage: java Driver <listening port>\n"); return;} //process args string
		
		int backlog = 25;														//Set the max number of queued connections
		//InetAddress loopback = InetAddress.getByName("127.0.0.1");				//For use loopback address for testing purposes
		//Scanner input = new Scanner(System.in);
		//System.out.println("Enter port number:");
		int portNum = Integer.parseInt(args[0]);
		
		System.out.println("Starting Server");
		//try (ServerSocket house = new ServerSocket(portNum, backlog, loopback)){ 	//binds the server to loopback port for testing locally
		try (ServerSocket house = new ServerSocket(portNum, backlog)){ 				//binds the server to a specified port
			
			System.out.println("Listening for Clients on port " + portNum);
			
			while(true) {
				Socket mailBox = house.accept();								//starts listening for incomming client requests
				
				System.out.println("New Client connected");
				
				InputStream letter = mailBox.getInputStream();					//Recieve byte array from client
				System.out.println("InputStream Successful");
				InputStreamReader bifocals = new InputStreamReader(letter); 	//turn byte array into characters
				System.out.println("InputStreamReader Successful");
				BufferedReader readingGlasses = new BufferedReader(bifocals); 	//make characters easier to process
				System.out.println("BufferedReader Successful");
				int ID = Integer.parseInt(readingGlasses.readLine());			//Use the Reading Glasses to read the letter
				System.out.println("Input Read from Client: " + ID);
				
				OutputStream output = mailBox.getOutputStream();				//get the return address
				System.out.println("Recieved OutputStream");
				
				PrintWriter returnToSender = new PrintWriter(output, true);		//have the mail office on spd dial
				System.out.println("PrintWriter Established");
				
				System.out.printf("\nInput from Client: %d", ID);
//				while (ID != -1) {
									
					processCommand(returnToSender, ID);	//Handle client request					
//				}
				letter.close();
				mailBox.close();  						//close client connection
				System.out.println("Request Completed");
				System.out.println("Client Disconnected");
			}//End while loop
			
		} catch (IOException ex) {
			System.out.println("Server Exception" + ex.getMessage());
			ex.printStackTrace();
		}//End Catch
	}//End main
	
	/**
	 * Uses a switch/case to process command IDs
	 * @param outBox
	 * @param ID
	 * @throws IOException
	 */
	static public void processCommand(PrintWriter outBox, int ID) throws IOException {
		switch(ID) {
		case -1:
			return;
		case 0:
			//Date and Time
			System.out.printf(", Date and Time");
			outBox.println(new Date().toString()); //Send Date to client
			break;
		case 1:
			//Uptime
			System.out.printf(", UpTime");
			RuntimeMXBean RTB = ManagementFactory.getRuntimeMXBean();
			long upTime = TimeUnit.MILLISECONDS.toSeconds(RTB.getUptime()); 
			outBox.println("Server Up Time: " + upTime + " S");		//send Uptime to client
			break;
		case 2:
			//Memory_usage
			System.out.printf(", Memory Usage");
			Runtime gas = Runtime.getRuntime();
			long memUse = gas.totalMemory() - gas.freeMemory();
			outBox.println("Server Memory Usage: " + memUse);
			break;
		case 3:
			//Netstat "netstat -all"
			System.out.printf(", NetStat");
			Process N = Runtime.getRuntime().exec("netstat -all");
			BufferedReader NOut = new BufferedReader(new InputStreamReader(N.getInputStream()));
			printLinuxCommand(outBox, NOut);
			break;
		case 4:
			//Current_Users "who -H"
			System.out.printf("Current Users");
			Process U = Runtime.getRuntime().exec("who -H");
			BufferedReader UOut = new BufferedReader(new InputStreamReader(U.getInputStream()));
			printLinuxCommand(outBox, UOut);
			break;
		case 5:
			//Running Processes "ps -ef"
			System.out.printf(", Running Processes");
			Process P = Runtime.getRuntime().exec("ps -ef");
			BufferedReader POut = new BufferedReader(new InputStreamReader(P.getInputStream()));
			printLinuxCommand(outBox, POut);
			break;
		}
	}//End proccessCommand
	
	/**
	 * Sends output from a BufferedReader to the Client 
	 * @param mailBoy PrintWriter that sends data to client
	 * @param typewriter Output from linux command
	 */
	static public void printLinuxCommand(PrintWriter mailBoy, BufferedReader typewriter) {
		String list;
		try {
			while((list = typewriter.readLine()) != null) {
				mailBoy.println(list); 		//send linux command output to client
				//System.out.println(list);	//send the results in the console
			}
		} catch (IOException e) {
			System.out.println("Server Exception" + e.getMessage());
			e.printStackTrace();
		}
	}//End printLinuxCommand

}
