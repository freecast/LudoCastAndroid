/*
 * Copyright (C) 2014 Google Inc. All Rights Reserved. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.casthelloworld;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.IVideoCastConsumer;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.google.sample.castcompanionlibrary.widgets.MiniController;
import java.io.IOException;
import java.lang.reflect.Field;

import android.text.Editable;  
import android.text.Selection;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.TextView;  
import android.widget.Toast;  
import android.view.KeyEvent;  
import android.widget.TextView.OnEditorActionListener;


/**
 * Main activity to send messages to the receiver.
 */
public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int REQUEST_CODE = 1;
    private VideoCastManager mCastManager;
    private IVideoCastConsumer mCastConsumer;
    private MiniController mMini;
    private MenuItem mediaRouteMenuItem;

	private MediaRouter mMediaRouter;
	private MediaRouteSelector mMediaRouteSelector;
	private MediaRouter.Callback mMediaRouterCallback;
	private CastDevice mSelectedDevice;
	private GoogleApiClient mApiClient;
	private Cast.Listener mCastListener;
	private boolean mApplicationStarted;
	private boolean mWaitingForReconnect;
	static boolean mAppConnected;
	private String mSessionId;
	private LudoProtocol protocol;
	private Button Createbnt;
	static Boolean ishost;
	static Boolean updatestatus;
	String fileName = "username.txt";
	EditText editText;
	 
    static String username = null;
	private final int FIRST_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main);

		Log.d(TAG, "onCreate");

		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));

		mAppConnected = false;

		ishost = false;

		updatestatus = false;
		
		editText=(EditText)findViewById(R.id.editText1); 		

		editText.setOnEditorActionListener(new OnEditorActionListener() {  
	            @Override  
	            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
	                Toast.makeText(MainActivity.this, String.valueOf(actionId), Toast.LENGTH_SHORT).show();  
	                return false;  
	            }  
	        }); 
		 
		 readFileData(fileName);
		

		mCastManager = CastApplication.getCastManager(this);
		
		mAppConnected = mCastManager.isConnected();

		Log.d(TAG, "onCreate mAppConnected = "+mAppConnected);
		

		setupCastListener();


		protocol = new LudoProtocol();

		getOverflowMenu();

	Createbnt = (Button) findViewById(R.id.Startgame);
	Createbnt.setEnabled(false);
	Createbnt.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

			username = editText.getText().toString();

			Log.d(TAG, "username =  " + username);

			if(username.equals(""))
				{
				
				Toast.makeText(MainActivity.this, "Please enter your name first !!!", Toast.LENGTH_SHORT).show();
				return;	
				}else{

					writeFileData(fileName,username);

					}

			if(mAppConnected){
					try {
						String msg = protocol.genMessage_connect(username);
						Log.d(TAG, "connect message: " + msg);
						sendMessage(msg);
						

						Intent it = new Intent(MainActivity.this, ConfigGame.class);
						it.putExtra("username from MainActivity", username);
						startActivityForResult(it, FIRST_REQUEST_CODE);

						overridePendingTransition(R.anim.push_left_in,
										R.anim.push_left_out);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{

	                Toast.makeText(MainActivity.this, "Connect not ready, fail to start game", Toast.LENGTH_SHORT).show();
		
					}

		}
	});
	
	
	Button Gamerule = (Button) findViewById(R.id.GameRule);
	Gamerule.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "Start Game Rule");
			
			Intent it = new Intent(MainActivity.this, GameRule.class);
			startActivityForResult(it, FIRST_REQUEST_CODE);

			overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);			
						
		}
	});
		
	}


	public void readFileData(String fileName){ 
	
			String res=""; 
	
			try{ 
	
			 FileInputStream fin = openFileInput(fileName); 
	
			 int length = fin.available(); 
	
			 byte [] buffer = new byte[length]; 
	
			 fin.read(buffer);	   
	
			 res = EncodingUtils.getString(buffer, "UTF-8"); 
	
			 fin.close();	  
	
			} 
	
			catch(Exception e){ 
	
			 e.printStackTrace(); 
	
			} 
	
			if(res.equals(""))
			{
			}
			else
				{
					editText.setText(res);
					username = editText.getText().toString();
				}
	
		}	



	public void writeFileData(String fileName,String message){ 
	
		try{ 
	
		 FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);
	
		 byte [] bytes = message.getBytes(); 
	
		 fout.write(bytes); 
	
		  fout.close(); 
	
		 } 
	
		catch(Exception e){ 
	
		 e.printStackTrace(); 
	
		} 
	
	}  


    private void getOverflowMenu() {
         try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	private void setupCastListener(){

	mCastConsumer = new VideoCastConsumerImpl() {
	
	
		@Override
		public void onApplicationConnected(ApplicationMetadata appMetadata,
		String sessionId, boolean wasLaunched) {
	
			System.out.println("onApplicationConnected message = "+sessionId);
	
			Toast.makeText(MainActivity.this, "Device is Ready to Start Game!!!", Toast.LENGTH_SHORT).show();
	
			mAppConnected = true;
			try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
			Createbnt.setEnabled(true);
	
		}
	
	
		@Override
		public void onDisconnected() {
	
			System.out.println("onDisconnected message ");
			
			mAppConnected = false;

			if(mAppConnected)
				Createbnt.setEnabled(true);
			else
				Createbnt.setEnabled(false);
	
		
		}			
	
		 @Override
		 public void onFailed(int resourceId, int statusCode) {
	
			System.out.println("onFailed message = "+statusCode);
			
			mAppConnected = false;

			 if(mAppConnected)
				 Createbnt.setEnabled(true);
			 else
				 Createbnt.setEnabled(false);

			
	
		 }
	
			@Override
			public void onApplicationDisconnected(int errorCode) {
	
			System.out.println("onApplicationDisconnected message = "+errorCode);
	
			mAppConnected = false;

			if(mAppConnected)
				Createbnt.setEnabled(true);
			else
				Createbnt.setEnabled(false);			
	
			
			}
	
		 @Override
		 public boolean onApplicationConnectionFailed(int errorCode) {
		 
			System.out.println("onApplicationConnectionFailed message = "+errorCode);
	
			mAppConnected = false;

			 if(mAppConnected)
				 Createbnt.setEnabled(true);
			 else
				 Createbnt.setEnabled(false);

			
			 return true;
		 }
	
			
		 
		 @Override
		 public void onDataMessageReceived(String message) {
			 try {
				protocol.parseMessage(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				 
			 System.out.println("MainActivity receiver message = "+message);
			}
	
		 @Override
		 public void onConnectionSuspended(int cause) {
			 Log.d(TAG, "onConnectionSuspended() was called with cause: " + cause);
			 com.example.casthelloworld.Utils.
					 showToast(MainActivity.this, R.string.connection_temp_lost);
		 }
	
		 @Override
		 public void onConnectivityRecovered() {
			 com.example.casthelloworld.Utils.
					 showToast(MainActivity.this, R.string.connection_recovered);
		 }
	
		 @Override
		 public void onCastDeviceDetected(final RouteInfo info) {
			 if (!CastPreference.isFtuShown(MainActivity.this)) {
				 CastPreference.setFtuShown(MainActivity.this);
	
				 Log.d(TAG, "Route is visible: " + info);
				 new Handler().postDelayed(new Runnable() {
	
					 @Override
					 public void run() {
						 if (mediaRouteMenuItem.isVisible()) {
							 Log.d(TAG, "Cast Icon is visible: " + info.getName());
						 }
					 }
				 }, 1000);
			 }
		 }
	 };

	}




    private void setupActionBar(ActionBar actionBar) {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
       // getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

	
	/**
	 * Android voice recognition
	 */
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.message_to_cast));
		startActivityForResult(intent, REQUEST_CODE);
	}

	/*
	 * Handle the voice recognition response
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.d(TAG, "onActivityResult() was called1");
			if(requestCode==FIRST_REQUEST_CODE && resultCode==Activity.RESULT_FIRST_USER){
				if(data != null) {
					Log.d(TAG, "onActivityResult was call");
				}
			}
		}



	@Override
		protected void onResume() {
			Log.d(TAG, "onResume() was called");
			mCastManager = CastApplication.getCastManager(this);
			if (null != mCastManager) {
				mCastManager.addVideoCastConsumer(mCastConsumer);
				mCastManager.incrementUiCounter();
			}

			mAppConnected = mCastManager.isConnected();

			if(mAppConnected)
				Createbnt.setEnabled(true);
			else
				Createbnt.setEnabled(false);
	
			super.onResume();
		}


	@Override
	protected void onPause() {
        mCastManager.decrementUiCounter();
        mCastManager.removeVideoCastConsumer(mCastConsumer);

		super.onPause();
	}


	@Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			
			Log.d(TAG, "Run in Back Key ");
			onDestroy();
			
		 return true;
		}
		return super.onKeyDown(keyCode, event);
	  }


	

	@Override
	public void onDestroy() {
        if (null != mCastManager) {
            mCastManager.clearContext(this);
        }

		 finish();
         System.exit(0);

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Log.d(TAG, "onCreateOptionsMenu() was called");
		getMenuInflater().inflate(R.menu.main, menu);
		mediaRouteMenuItem = mCastManager.
                addMediaRouterButton(menu, R.id.media_route_menu_item);
		mAppConnected = mCastManager.isConnected();
		MenuItem actionRestart = menu.findItem(R.id.menu_restart);
		actionRestart.setVisible(false); 
		//MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
		//MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
		//		.getActionProvider(mediaRouteMenuItem);
		// Set the MediaRouteActionProvider selector for device discovery.
		//mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
		return true;
	}

@Override  
public boolean onOptionsItemSelected(MenuItem item) {  
	switch (item.getItemId()) {  
	case R.id.menu_setting:  
		Toast.makeText(this, "Menu Item Setting selected",	
				Toast.LENGTH_SHORT).show();  
		break;	
	case R.id.menu_exit:  
		finish(); 
		break;	
	default:  
		break;	
	}  
	return super.onOptionsItemSelected(item);  
}  	
	private void sendMessage(String message) {
		if (mCastManager!=null){			
			try {
				Log.d(TAG, "MainActivity send message"+message);
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
