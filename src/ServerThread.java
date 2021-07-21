import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;


public class ServerThread extends Thread {
	
	private Socket POBox = new Socket();
	int ID = -30;
	
	ServerThread(Socket mailbox){
		POBox = mailbox;
	}
	
	@Override
	public void run() {
		try {
		InputStream letter = POBox.getInputStream();					//Recieve byte array from client
		//System.out.println("InputStream Successful");
		
		InputStreamReader bifocals = new InputStreamReader(letter); 	//turn byte array into characters
		//System.out.println("InputStreamReader Successful");
		
		BufferedReader readingGlasses = new BufferedReader(bifocals); 	//make characters easier to process
		//System.out.println("BufferedReader Successful");
		
		ID = Integer.parseInt(readingGlasses.readLine());				//Use the Reading Glasses to read the letter
		System.out.println("Input Read from Client: " + ID);
		
		OutputStream output = POBox.getOutputStream();				//get the return address
		//System.out.println("Recieved OutputStream");
		
		PrintWriter returnToSender = new PrintWriter(output, true);		//have the mail office on spd dial
		//System.out.println("PrintWriter Established");
		
		System.out.printf("\nInput from Client: %d", ID);

							
		processCommand(returnToSender, ID);	//Handle client request					

		POBox.close();  					//close client connection
		
		System.out.println("Request Completed");
		System.out.println("Client Disconnected");
		}catch (IOException ex){
			System.out.println("Server Exception" + ex.getMessage());
			ex.printStackTrace();
		}
	}
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
			//System.out.println(new Date().toString());			//Make sure data is correct
			outBox.println("Date: " + new Date().toString()); 		//Send Date to client
			break;
		case 1:
			//Uptime
			System.out.printf(", UpTime");
			RuntimeMXBean RTB = ManagementFactory.getRuntimeMXBean();
			long upTime = TimeUnit.MILLISECONDS.toSeconds(RTB.getUptime()); 
			//System.out.println("Server Up Time: " + upTime + "S");	//Data verification
			outBox.println("Server Up Time: " + upTime + " S");			//send Uptime to client
			break;
		case 2:
			//Memory_usage
			System.out.printf(", Memory Usage");
			Runtime gas = Runtime.getRuntime();
			long memUse = gas.totalMemory() - gas.freeMemory();
			//System.out.println("Server Memory Usage: " + memUse); //Data verification
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
