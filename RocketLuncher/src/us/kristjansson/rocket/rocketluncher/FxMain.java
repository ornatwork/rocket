//
package us.kristjansson.rocket.rocketluncher;
//
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class FxMain extends ActionBarActivity 
{

	// privates
	//private boolean mBound = true;
	private CxClientSocket mConnection = null;
	private String msVersion = "0.0.0.1";
	private ConnectTask mConnect = new ConnectTask();
	 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Wire up button listener
	    Button button = (Button)findViewById(R.id.btConnect);
	    button.setOnClickListener(mListener);
		// Wire up button listener
	    Button button2= (Button)findViewById(R.id.btDisconnect);
	    button2.setOnClickListener(mListener2);
	    
	    // Advertise version
		//EditText txVer = (EditText)findViewById(R.id.lbVersion);
		//txVer.setText( "Version: " + msVersion );
	}

	// Create the listener 
	private OnClickListener mListener2 = new OnClickListener() 
	{
	    public void onClick(View v) 
	    {
	    	// Do something fun !
	    	myClick2();
	    }
	};
	
	// Create the listener 
	private OnClickListener mListener = new OnClickListener() 
	{
	    public void onClick(View v) 
	    {
	    	// Do something fun !
	    	myClick();
	    }
	};

	// Implement the OnClickListener callback
    public void myClick2()
    {
		// Collect the command 
		EditText txTerm = (EditText)findViewById(R.id.txCommand);
		String sendCommand = txTerm.getText().toString();
		// Wipe out the last command
		txTerm.setText("");
		//
		if( mConnection != null )
			mConnection.sendCommand( sendCommand );
    }

    
	// Implement the OnClickListener callback
    public void myClick()
    {
		//
		EditText txTerm = (EditText)findViewById(R.id.txTerminal);
		txTerm.setText( "Connecting to server " + '\n'  );

		String sRet = ""; 
		// Call background thread
		//if( mBound )
		//{
			if( this.mConnect != null )
			{
				mConnect.execute( "start" );
			}
			else
			{
				//sRet = mService.sendCommand( "QUIT" );
			}
		//}
    }
    
   
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	protected void onPause() 
	{
	    super.onPause();
	    if( mConnect != null) 
	    {
	    	mConnect.cancel(true);
	    }
	    
	    if( this.mConnection != null) 
	    {
	        CxLogger.i("stopClient");
	        mConnection.Close();
	        mConnection = null;
	    }
	}

	
	public class ConnectTask extends AsyncTask<String, String, CxClientSocket> 
	{

	    @Override
	    protected CxClientSocket doInBackground(String... message) 
	    {
	    	CxLogger.i("doInBackground");

	        // we create a TCPClient object and
	        mConnection = new CxClientSocket( new CxClientSocket.OnMessageReceived() 
	        {
	            @Override
	            // here the messageReceived method is implemented
	            public void messageReceived(String message) 
	            {
	                // this method calls the onProgressUpdate
	                publishProgress(message);
	            }
	        });
	        //
	        mConnection.run();

	        return null;
	    }

	    @Override
	    protected void onProgressUpdate(String... values) 
	    {
	        super.onProgressUpdate(values);
	        CxLogger.i("onProgressUpdate");
	        // Responses from server 
	        EditText txTerm = (EditText)findViewById(R.id.txTerminal);
	        txTerm.setText( txTerm.getText() + values[0].toString() + '\n' );
	    }
	}

}




