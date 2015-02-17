//
package us.kristjansson.rocket.rocketluncher;
//
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import us.kristjansson.rocket.rocketluncher.CxClientSocket.OnMessageReceived;
//
import android.app.IntentService;
import android.content.Intent;

// Sample service 
// http://developer.android.com/guide/components/bound-services.html#Binder

public class CxClientSocket 
{
	// IP and port 
	private static final String HOST = "192.168.1.10";
	//private static final String HOST = "localhost";
	//private static final String HOST = "127.0.0.1";
	private static final int PORT_NUMBER = 7007;
	//
	private static Socket clientSocket = null;
	private static BufferedReader inFromUser = null;
	private static DataOutputStream outToServer = null;
	private static BufferedReader inFromServer = null;
	//
	private static String msResponse = "";
	private static String msCommand = "";
	// Notifier
	private OnMessageReceived mMessageListener = null;
	private boolean mbRun = true;
	

	
	public CxClientSocket(OnMessageReceived listener) 
    {
        mMessageListener = listener;
    }

     public void run() 
	 {
    	 
    	 CxLogger.i("run...");
    	 msResponse = this.connect();
    	 if( msResponse  != null && mMessageListener != null) 
    	 {
             // call the method messageReceived from MyActivity class
             mMessageListener.messageReceived( msResponse );
         }
    	 
    	 
    	 // Keep it going until run is not true anymore  
    	 while( mbRun )
    	 {
    		 try
    		 {
	    		 String sTemp = msCommand;
	    		 msCommand = "";
	    		 if( msResponse != null && mMessageListener != null) 
	        	 {
	    			if( sTemp.length() > 0 )
	    			{
	    				outToServer.writeBytes( sTemp + '\n');
	    				// 	Read server response
	    				msResponse = inFromServer.readLine();
	    				CxLogger.e("from server=" + msResponse );
	    				mMessageListener.messageReceived( msResponse );
	    			}
	        	 }
	    		 
	    		 if( sTemp.equals("QUIT"))
	    		 {
	    			 mbRun = false;
	    			 this.Close();
	    		 }
    		 }
    		 catch( Exception ex )
    		 {
    			 CxLogger.e("in run", ex );
    		 }
    			 
    	 }

		 CxLogger.i("run is done ");
	 }
 
     
	// setup 
    private String connect()
    {
		 //
		 String response = "";
		 //
		 CxLogger.i( "Connecting to Rocket server" );
		 
		try
		{
			 clientSocket = new Socket( HOST, PORT_NUMBER );
			 inFromUser = new BufferedReader( new InputStreamReader(System.in));
			 outToServer = new DataOutputStream(clientSocket.getOutputStream());
			 
			 // Connect and interact with the server
			 inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 // Read the greeting first
			 response = inFromServer.readLine();
			 CxLogger.i( response );
			 
			//
			return response;
		}
		catch( Exception Ex )
		{
			CxLogger.e("Connection error", Ex);
			return "Error=" + Ex;
		}
    }
    
    // Send command to the server 
    public void sendCommand( String psCommand )
    {
		//
		CxLogger.i( "send command to Rocket server=" + psCommand );
		this.msCommand = psCommand;
    }

   
    public void Close() 
    {
		try 
		{
			//  Bye 
			if( clientSocket != null && clientSocket.isConnected() )
				clientSocket.close();
		} 
		catch (IOException Ex ) 
		{
			CxLogger.e("Connection error", Ex );
		}
    }

    // Send message back
    public interface OnMessageReceived 
    {
        public void messageReceived(String message);
    }

}  // EOC