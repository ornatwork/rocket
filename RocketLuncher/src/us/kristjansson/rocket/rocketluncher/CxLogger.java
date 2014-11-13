//
package us.kristjansson.rocket.rocketluncher;
//
import android.util.Log;


public class CxLogger
{
	public static final int NONE = 0;
	public static final int ERRORS_ONLY = 1;
	public static final int ERRORS_WARNINGS = 2;
	public static final int ERRORS_WARNINGS_INFO = 3;
	public static final int ERRORS_WARNINGS_INFO_DEBUG = 4;

	// logging tag
	public static final String TOKEN = "RocketLuncher";
	// Only Errors when deployed
	//private static final int LOGGING_LEVEL = ERRORS_ONLY ;
	// Errors + warnings + info + debug (default)
	private static final int LOGGING_LEVEL = ERRORS_WARNINGS_INFO_DEBUG;
	
	public static void e(String msg)
	{
		if ( LOGGING_LEVEL >=1) Log.e(TOKEN,msg);
	}
	
	public static void e(String msg, Exception e)
	{
		if ( LOGGING_LEVEL >=1) Log.e(TOKEN,msg,e);
	}

	public static void w(String msg)
	{
		if ( LOGGING_LEVEL >=2) Log.w(TOKEN, msg);
	}
	
	public static void i(String msg)
	{
		if ( LOGGING_LEVEL >=3) Log.i(TOKEN,msg);
	}
	
	public static void d(String msg)
	{
		if ( LOGGING_LEVEL >=4) Log.d(TOKEN, msg);
	}
}