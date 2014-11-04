//
package us.kristjansson.rocket;
//
import java.io.*;
import java.net.*;


class Client
{
	 //
	 private static final String HOST = "localhost";
	 private static final int PORT_NUMBER = 7007;
	
	 public static void main(String argv[]) throws Exception
	 {
		 //
		 String command = "";
		 String response = "";
		 Socket clientSocket = new Socket( HOST, PORT_NUMBER );
		 BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		 
		 // Connect and interact with the server
		 BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 // Read the greeting first
		 response = inFromServer.readLine();
		 System.out.println( response );
		 
		 //
		 do
		 {
			  // Get command from user and send to server 
			  command = inFromUser.readLine();
			  outToServer.writeBytes(command + '\n');
			  // Read server response
			  response = inFromServer.readLine();
			  // debug
			  System.out.println( response);
		 } 
		 while( !command.toUpperCase().equals( "QUIT") );
		 
		 // Bye 
		 clientSocket.close();
	 }
}