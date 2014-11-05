//
package us.kristjansson.rocket;
//
import java.io.*;
import java.net.*;





class Server
{
	// Version info
	private static final String VER = "0.0.0.1";
	private static final int PORT_NUMBER = 7007; 
	
	// First authorise 
	private static Authoration_State AuthState = Authoration_State.START;
	// then tranactions
	private static Transaction_State TransState = Transaction_State.START;
	
	// Replies
	private static String OK = "Server: +OK";
	private static String ERR = "Server: -ERR";
	private static final String EOL = "\n";
	
	// Hardcoded User
	private static final String WHOAMI = "Batman";
	private static final String PASSWORD = "Robin";

	// Client commands
	private static final String USER = "USER";
	private static final String PASS = "PASS";
	private static final String RSET = "RSET";
	private static final String STAT = "STAT";
	private static final String NOOP = "NOOP";
	private static final String QUIT = "QUIT";
	// ARM and FIRE the rocket - Caution HOT HOT !!!
	private static final String ARM = "ARM";
	private static final String FIRE = "FIRE";
		
	
	
	// Rocket server, POP3 style
	public static void main(String argv[]) throws Exception
	{
	     String clientRequest ="";
		 String serverResponse = "";
		 // Setup
		 System.out.println("Rocket server started..."  );
		 ServerSocket welcomeSocket = new ServerSocket(  PORT_NUMBER );
		 Socket connectionSocket = welcomeSocket.accept();
	 	 // Read from client 
         BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
         DataOutputStream toClient = new DataOutputStream(connectionSocket.getOutputStream());
         
		 // Infinite
		 while(true)
		 {
			 try
			 {
		            // Startup, send greetings to the client 
	            	serverResponse = Server.setState( clientRequest );
	            	toClient.writeBytes( serverResponse );
	            	System.out.println("---------------------------------, 1"  );

		            // FIRE !
		            if( TransState == Transaction_State.FIRE )
		            {
		            	// Blink warning led rocket
		            	Board.Blink();
		            	// FIRE rocket !
		            	Board.Fire();
		            	// Remove state
		            	TransState = Transaction_State.START;
		            }

	            	// Get client input
	            	clientRequest = fromClient.readLine();
	            	System.out.println("Client: " + clientRequest );
		
		            // Null if client has disconnected 
		            if( clientRequest == null )
		            	throw new Exception("Client disconnected");
		            
			 }
			 catch( Exception Ex )
			 {
				 System.out.println("---------------------------------, 4"  );
				 System.out.println("Error thrown=" + Ex );
				 
				 // Close out the client socket
		         connectionSocket.close();
		         // Accept further client connection
		         connectionSocket = welcomeSocket.accept();
                 fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                 toClient = new DataOutputStream(connectionSocket.getOutputStream());
		    	 clientRequest = "";
		    	 AuthState = Authoration_State.START;
		    	 TransState = Transaction_State.START;
			 }
		    	
		 } // while
		 
   }
	   
	// Handle client requests 
	private static String setState( String psRequest )
	{
		// Defaults
		String sRet = ERR + " unknown Rocket command" + EOL;;

		// Always check first if quitting
		if( psRequest.toUpperCase().equals( QUIT ) )
		{
			AuthState = Authoration_State.START;
			sRet = OK + " Rocket server saying Good-Bye" + EOL;
		}
		// Not logged in yet 
		else if( AuthState != Authoration_State.LOGGED_IN )
		{
			// Just starting up
			if( AuthState == Authoration_State.START )
			{
				sRet = OK + " Rocket server " + VER + " ready " + EOL;
				AuthState = Authoration_State.USER;
			}
			else if( psRequest.startsWith( USER ) )
			{
				// User
				if( psRequest.toUpperCase().equals( USER + " " + WHOAMI.toUpperCase() ))
				{
					sRet = OK + " send your password" + EOL;
					AuthState = Authoration_State.PASS;
				}
				else
				{
					sRet = ERR + " sorry, no rockets for you here" + EOL;
					AuthState = Authoration_State.USER;
				}
			}
			else if( psRequest.startsWith( PASS ) )
			{
				// User is correct, now try password
				if( AuthState == Authoration_State.PASS && 
					psRequest.toUpperCase().equals( PASS + " " + PASSWORD.toUpperCase() ))
				{
					AuthState = Authoration_State.LOGGED_IN;
					sRet = OK + " " + WHOAMI + " is a real hoopy frood" + EOL;
				}
				else
				{
					AuthState = Authoration_State.USER;
					sRet = ERR + " invalid password" + EOL;
				}
			}
			
		}
		// Logged in, now in TRANSACTION state, now for some commands 
		else 
		{
			if( psRequest.equals( STAT ) )
			{
				if( TransState == Transaction_State.READY_FIRE )
					sRet = OK + " rocket is ARMED, ready to lunch" + EOL;
				else
					sRet = OK + " rocket is NOT ARMED" + EOL;
			}		
			if( psRequest.equals( ARM ) )
			{
				TransState = Transaction_State.READY_FIRE;
				sRet = OK + " rocket is ARMED, ready to lunch" + EOL;
			}
			if( psRequest.equals( RSET ))
			{
				TransState = Transaction_State.START;
				sRet = OK + " rocket is NOT ARMED" + EOL;
			}
			if( psRequest.equals( FIRE ) )
			{
				if( TransState == Transaction_State.READY_FIRE )
				{
					TransState = Transaction_State.FIRE;
					sRet = OK + " lunching rocket, WATCH OUT !" + EOL;
				}
				else
					sRet = ERR + " rocket is NOT ARMED" + EOL;
			}
			if( psRequest.equals( NOOP ) )
			{
				sRet = OK + " hi n00b" + EOL;
			}
		}
		
		// Debug
		System.out.print( sRet );
		return sRet;
	}


	
}