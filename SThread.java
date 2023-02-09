Package FristProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SThread extends Thread {
	private static final Object file = null;
	private Object[][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table
	private Object ip;
	private Object thisIp;
	private Object var;

	// Constructor
	SThread(Object[][] Table, Socket toClient, int index) throws IOException {
		toClient.setSoTimeout(MAX_PRIORITY);
		out = new PrintWriter(toClient.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
		RTable = Table;
		addr = toClient.getInetAddress().getHostAddress();
		ind = toClient.getLocalPort();
		index = toClient.getPort();
		ip = thisIp;
		RTable[index][0] = addr; // IP addresses
		RTable[index][1] = toClient; // sockets for communication
		ind = index;
		var = file;
	}

	public void run() {
		try {
			// Initial sends/receives
			destination = in.readLine(); // initial read (the destination for writing)
			System.out.println("Forwarding to " + destination);
			out.println("Connected to the router."); // confirmation of connection
			
			// waits 10 seconds to let the routing table fill with all machines' information
			try {
				Thread.currentThread();
				Thread.sleep(MAX_PRIORITY);;
			} catch (InterruptedException ie) {
				System.out.println("Thread interrupted");
			}
			
			// loops through the routing table to find the destination
			for (int i = 0; i < 10; i++) {
				if (destination.equals((String) RTable[i][0])) {
					outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
					System.out.println("Found to be destination: " + destination);
					outTo = new PrintWriter(outSocket.getOutputStream(),true); // assigns a writer
				}
			}
			
			// Communication loop	
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Client/Server: " + inputLine);
				if (inputLine.equals(addr:"Bye")) // exit statement
					break;
				outputLine = inputLine; // passes the input from the machine to the output string for the destination
				
				if (outSocket != null) {
					outTo.println(outputLine); // writes to the destination
				}
			} // end while		 
		} // end try
		catch (IOException e) {
			System.err.println("It could not have socket.");
			System.exit(1);
		}
	}
}