//
package us.kristjansson.rocket;
//
import java.io.*;
import java.net.*;


public class Test 
{
	
	public static void main(String argv[]) throws Exception
	{
		//
		String clientSentence = "Fire at will, rocket is loaded and ready to go.";
		
		//
		for( int i=0; i<clientSentence.length(); i++ )
		{
		   Thread.sleep( 50 );
		   System.out.print( "" + clientSentence.charAt(i) );
		}
	}

}
