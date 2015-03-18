package us.kristjansson.rocket.rocketluncher;

import us.kristjansson.rocket.rocketluncher.R.drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FxLauncher extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.luncher_main);
		
		
		// Wire up button Launch listener 
		Button btLaunch = (Button) findViewById(R.id.btLaunch);
		btLaunch.setOnClickListener(mLaunchListener);
		// Wire up button Abort listener 
		Button btAbort = (Button) findViewById(R.id.btAbort);
		btAbort.setOnClickListener(mAbortListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.luncher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Create the listener
	private OnClickListener mLaunchListener = new OnClickListener() {
		public void onClick(View v) {
			clickLaunch();
		}
	};

	// Create the listener
	private OnClickListener mAbortListener = new OnClickListener() {
		public void onClick(View v) {
			clickAbort();
		}
	};

	
	// Launch the rocket
	public void clickLaunch() 
	{
		// Wire up button Launch listener 
		ImageView imRocket = (ImageView) findViewById(R.id.imRocket);
		imRocket.setImageResource( R.drawable.rocket_launch  );		
	}
		
	// Abort the Launch
	public void clickAbort() 
	{
		this.finish();
	}
}
