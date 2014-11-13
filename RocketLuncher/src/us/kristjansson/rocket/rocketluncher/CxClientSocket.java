//
package us.kristjansson.rocket.rocketluncher;
//
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
//
import android.os.*;


public class CxClientSocket extends AsyncTask<String, Void, String> 
{
    //private Exception exception;
	 //
	 private static final String HOST = "192.168.1.5";
	 private static final int PORT_NUMBER = 7007;


    protected String doInBackground(String... urls)
    {
		 //
		 String command = "";
		 String response = "";
		 
		try
		{
			
			 Socket clientSocket = new Socket( HOST, PORT_NUMBER );
			 BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			 
			 // Connect and interact with the server
			 BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 // Read the greeting first
			 response = inFromServer.readLine();
			 CxLogger.i( response );
			 
			 
			 /*
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
			 */
			 
			//  Bye 
			if( clientSocket != null && clientSocket.isConnected() )
				clientSocket.close();
			//
			return response;
		}
		catch( Exception Ex )
		{
			CxLogger.e("Connection error", Ex);
			return "Error=" + Ex;
		}

		
    }

    protected void onPostExecute(String feed) 
    {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
}