package com.example.casthelloworld;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector; 

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.IVideoCastConsumer;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.google.sample.castcompanionlibrary.widgets.MiniController;
import com.google.android.gms.cast.MediaStatus;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class ConfigGame extends ActionBarActivity  {
	
    private static final String TAG = "ConfigGame";

    private VideoCastManager mCastManager;
    private VideoCastConsumerImpl mCastConsumer;
    
    String SendMsg;
    String playername;
    String usertype;
	private LudoProtocol protocol;
	static Boolean startgame; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configgame);
		Log.d(TAG, "onCreate() was called");
		ActionBar actionBar = getSupportActionBar();
		mCastManager = CastApplication.getCastManager(this);
		setupActionBar(actionBar);
		
		startgame = false;
		
		TextView textview =(TextView) findViewById(R.id.textView4);
		textview.setText(MainActivity.username) ;
		
		RadioButton radio_button01 = (RadioButton)findViewById(R.id.RadioButton01);		
		RadioButton radio_button11 = (RadioButton)findViewById(R.id.RadioButton11);			
		RadioButton radio_button21 = (RadioButton)findViewById(R.id.RadioButton21);			
		RadioButton radio_button31 = (RadioButton)findViewById(R.id.RadioButton31);			

		radio_button01.setText(MainActivity.username);
		radio_button11.setText(MainActivity.username);
		radio_button21.setText(MainActivity.username);
		radio_button31.setText(MainActivity.username);
		
		protocol = new LudoProtocol();
		
        Button getready=(Button)findViewById(R.id.button_getready);  
        getready.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {
            	try {
					SendMsg = protocol.genMessage_getready();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	sendMessage(SendMsg);
                
            }  
        });  		
		
		
		
		
		RadioGroup group00 = (RadioGroup)this.findViewById(R.id.RadioGroup00);
		
		group00.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg00, int arg01) {

			int radioButtonId = arg00.getCheckedRadioButtonId();

			RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
			
            System.out.println("Get group select = "+rb00.getText());
            
			try {
				SendMsg = protocol.genMessage_pickup("red",rb00.getText().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessage(SendMsg);

		}
		});
		

		RadioGroup group01 = (RadioGroup)this.findViewById(R.id.RadioGroup01);
		
		group01.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg10, int arg11) {

			int radioButtonId = arg10.getCheckedRadioButtonId();

			RadioButton rb01 = (RadioButton)findViewById(radioButtonId);
			
            System.out.println("Get group select = "+rb01.getText());
            
			try {
				SendMsg = protocol.genMessage_pickup("blue",rb01.getText().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessage(SendMsg);            

		}
		});	
		
		RadioGroup group02 = (RadioGroup)this.findViewById(R.id.RadioGroup02);
		
		group02.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg20, int arg21) {

			int radioButtonId = arg20.getCheckedRadioButtonId();

			RadioButton rb02 = (RadioButton)findViewById(radioButtonId);
			
            System.out.println("Get group select = "+rb02.getText());

			try {
				SendMsg = protocol.genMessage_pickup("yellow",rb02.getText().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessage(SendMsg);            

		}
		});			
		
		RadioGroup group03 = (RadioGroup)this.findViewById(R.id.RadioGroup03);
		
		group03.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg30, int arg31) {

			int radioButtonId = arg30.getCheckedRadioButtonId();

			RadioButton rb03 = (RadioButton)findViewById(radioButtonId);
			
            System.out.println("Get group select = "+rb03.getText());
          
			try {
				SendMsg = protocol.genMessage_pickup("green",rb03.getText().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendMessage(SendMsg);            

		}
		});				
		
		RadioGroup group04 = (RadioGroup)this.findViewById(R.id.RadioGroup04);
		
		group04.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg40, int arg41) {

			int radioButtonId = arg40.getCheckedRadioButtonId();

			RadioButton rb04 = (RadioButton)findViewById(radioButtonId);
			
            System.out.println("Get group select = "+rb04.getText());

		}
		});
		

		
		
		
		mCastConsumer = new VideoCastConsumerImpl() {
			
			 @Override
			 public void onFailed(int resourceId, int statusCode) {
		
			 }
			 
			 @Override
			 public void onDataMessageReceived(String message) {
				 try {
					protocol.parseMessage(message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 if(startgame == true)
				 {
						Intent it = new Intent(ConfigGame.this, PlayGame.class);

						startActivityForResult(it, 0);
						overridePendingTransition(R.anim.push_left_in,
										R.anim.push_left_out);					
						startgame = false;
				 }
				 
				 System.out.println("ConfigGame receiver message = "+message);
			    }
		
			 @Override
			 public void onConnectionSuspended(int cause) {
				 Log.d(TAG, "onConnectionSuspended() was called with cause: " + cause);
				 com.example.casthelloworld.Utils.
						 showToast(ConfigGame.this, R.string.connection_temp_lost);
			 }
		
			 @Override
			 public void onConnectivityRecovered() {
				 com.example.casthelloworld.Utils.
						 showToast(ConfigGame.this, R.string.connection_recovered);
			 }
		
			 @Override
			 public void onCastDeviceDetected(final RouteInfo info) {
				 if (!CastPreference.isFtuShown(ConfigGame.this)) {
					 CastPreference.setFtuShown(ConfigGame.this);
		
					 Log.d(TAG, "Route is visible: " + info);
					 new Handler().postDelayed(new Runnable() {
		
						 @Override
						 public void run() {
	
						 }
					 }, 1000);
				 }
			 }
		 };		
		
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d("config", "onCreateOptionsMenu() was called");
        getMenuInflater().inflate(R.menu.main, menu);
        mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
        return true;
    }

@Override
	protected void onResume() {
		Log.d(TAG, "onResume() was called");
		mCastManager = CastApplication.getCastManager(this);
		if (null != mCastManager) {
			mCastManager.addVideoCastConsumer(mCastConsumer);
			mCastManager.incrementUiCounter();
		}
	
		super.onResume();
	}

	

private void setupActionBar(ActionBar actionBar) {
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	//getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
	getSupportActionBar().setDisplayShowTitleEnabled(false);
}

private void sendMessage(String message) {
	if (mCastManager!=null){			
		try {
			System.out.println("sendmessage = "+message);
			mCastManager.sendDataMessage(message);
		} catch (TransientNetworkDisconnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}	

	
}
