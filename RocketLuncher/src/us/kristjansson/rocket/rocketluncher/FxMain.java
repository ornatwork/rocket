//
package us.kristjansson.rocket.rocketluncher;
//
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class FxMain extends ActionBarActivity 
{

	// privates
	//private boolean mBound = true;
	private CxClientSocket mConnection = null;
	private String msVersion = "0.0.0.2";
	private ConnectTask mConnect = new ConnectTask();
	 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Wire up button listener
	    Button button = (Button)findViewById(R.id.btConnect);
	    button.setOnClickListener(mConnectListener);
		// Wire up button listener
	    Button button2= (Button)findViewById(R.id.btDisconnect);
	    button2.setOnClickListener(mSendListener);
	    // Wire up Enter key listener for commands
	    EditText txCommand= (EditText)findViewById(R.id.txCommand);
	    //txCommand.setOnEditorActionListener(CommandListener);
	    txCommand.setOnEditorActionListener(new DoneOnEditorActionListener());
	    
	    // Advertise version
	    TextView lbVer = (TextView)findViewById(R.id.lbVersion);
	    lbVer.setText( "Version: " + msVersion );
	}

	// Create the listener 
	private OnClickListener mSendListener= new OnClickListener() 
	{
	    public void onClick(View v) 
	    	{
	    	CxLogger.i("***************************** onCLick @@@ ");
	    	clickSend(); 
	    	}
	};
	
	// Create the listener 
	private OnClickListener mConnectListener = new OnClickListener() 
	{
	    public void onClick(View v) 
	    	{
	    		
	    		clickConnect(); 
	    	}
	};

	// Implement the OnClickListener callback
    public void clickSend()
    {
		// Collect the command 
		EditText txCommand = (EditText)findViewById(R.id.txCommand);
		String sendCommand = txCommand.getText().toString();
		// Wipe out the last command
		//txCommand.setText("");
		//
		CxLogger.i("***************************** clickSend=" + sendCommand );
		if( mConnection != null && sendCommand != null && sendCommand.length() > 0 )
		{
	        // Responses from server 
	        EditText txTerm = (EditText)findViewById(R.id.txTerminal);
	        txTerm.setText( txTerm.getText() + sendCommand + '\n' );
	        //
			mConnection.sendCommand( sendCommand );
		}
    }

    
	// Implement the OnClickListener callback
    public void clickConnect()
    {
		//
		EditText txTerm = (EditText)findViewById(R.id.txTerminal);
		txTerm.setText( "Connecting to server... " + '\n'  );

		String sRet = ""; 
		if( this.mConnect != null )
			mConnect.execute( "start" );

    }
    
  
    // Listen for the ENTER key and send command over the wire
    class DoneOnEditorActionListener implements OnEditorActionListener 
    {
	    @Override
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
	    {
	    	CxLogger.i("***************************** actionId=" + actionId );
	    	CxLogger.i("***************************** keyCode=" + event.getKeyCode()   );
	    	CxLogger.i("***************************** getAction=" + event.getAction()   );
	    	
	    	// Enter pressed, do the Dew
	    	if( event.getAction()  == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER )
	    	{	
	    		CxLogger.i("***************************** keyCode = ENTER" );
	    		// Does the same as hitting the send button
	    		clickSend();
	    		return true;
	    	}
	        return false;
	    }
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
	        // kick off
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
	        //txTerm.setText( txTerm.getText() + values[0].toString() + '\n' );
	        txTerm.append( values[0].toString() + '\n' );
	        txTerm.setMovementMethod(new ScrollingMovementMethod());
	    }
	}

}




