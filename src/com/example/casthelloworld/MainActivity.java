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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.text.TextWatcher;
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
	static Boolean connectstatus;
	static Boolean updatestatus;
	static Boolean Vibratorstatus;
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

		Vibratorstatus = true;

		ishost = false;

		updatestatus = false;

		connectstatus = false;
		
		editText=(EditText)findViewById(R.id.editText1); 		

		editText.addTextChangedListener(new TextWatcher() {  
				 
			   @Override  
			   public void onTextChanged(CharSequence text, int start, int before, int count) {  
				   writeFileData(fileName,editText.getText().toString());	
				 
			   }  
				 
			   @Override  
			   public void beforeTextChanged(CharSequence text, int start, int count,int after) {  

			   
			   }  
				 
			   @Override  
			   public void afterTextChanged(Editable edit) {  
			   		writeFileData(fileName,editText.getText().toString());			  
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

	Button Gamehelp = (Button) findViewById(R.id.GameHelp);
	Gamehelp.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "Start Game Help");
			
			Intent it = new Intent(MainActivity.this, MyGuideViewActivity.class);
			startActivityForResult(it, FIRST_REQUEST_CODE);

			overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);			
						
		}
	});

	

	Createbnt.setText("Disconnected");
	
		
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

	private void CreateConfigGame(String playerstatus){

			Intent it = new Intent(MainActivity.this, ConfigGame.class);
			it.putExtra("username from MainActivity", username);
			it.putExtra("playerstatus from MainActivity", playerstatus);
			startActivityForResult(it, FIRST_REQUEST_CODE);
			
			overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
		
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
			if(mAppConnected)
				{
					Createbnt.setEnabled(true);
					Createbnt.setText("Start Game");
				}
			else{
					Createbnt.setEnabled(false);
					Createbnt.setText("Disconnected");
				}

	
		}

		    @Override
    	public void onConnected() {

			System.out.println("MainActivity onConnected ");
			Createbnt.setText("Connecting");
		
    	}
	
	
		@Override
		public void onDisconnected() {
	
			System.out.println("onDisconnected message ");
			
			mAppConnected = false;

			if(mAppConnected)
				{
					Createbnt.setEnabled(true);
					Createbnt.setText("Start Game");
				}
			else{
					Createbnt.setEnabled(false);
					Createbnt.setText("Disconnected");
				}

	
		
		}			
	
		 @Override
		 public void onFailed(int resourceId, int statusCode) {
	
			System.out.println("onFailed message = "+statusCode);
			
			mAppConnected = false;

			 if(mAppConnected)
				 {
					 Createbnt.setEnabled(true);
					 Createbnt.setText("Start Game");
				 }
			 else{
					 Createbnt.setEnabled(false);
					 Createbnt.setText("Disconnected");
				 }


			
	
		 }
	
			@Override
			public void onApplicationDisconnected(int errorCode) {
	
			System.out.println("onApplicationDisconnected message = "+errorCode);
	
			mAppConnected = false;

			if(mAppConnected)
				{
					Createbnt.setEnabled(true);
					Createbnt.setText("Start Game");
				}
			else{
					Createbnt.setEnabled(false);
					Createbnt.setText("Disconnected");
				}			
	
			
			}
	
		 @Override
		 public boolean onApplicationConnectionFailed(int errorCode) {
		 
			System.out.println("onApplicationConnectionFailed message = "+errorCode);
	
			mAppConnected = false;

			 if(mAppConnected)
				 {
					 Createbnt.setEnabled(true);
					 Createbnt.setText("Start Game");
				 }
			 else{
					 Createbnt.setEnabled(false);
					 Createbnt.setText("Disconnected");
				 }


			
			 return true;
		 }
	
			
		 
		 @Override
		 public void onDataMessageReceived(String message) {

			 JSONObject obj = null;
			try {
				obj = new JSONObject(message);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			 String command = null;
			try {
				command = obj.getString("command");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 if (command.equals("connect_reply")) {
				 boolean ret = false;
				try {
					ret = obj.getBoolean("ret");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 if(ret == false)
					 {
						 String reason = null;
						try {
							reason = obj.getString("error");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 Log.d(TAG, "error info: "+reason);
						 Toast.makeText(MainActivity.this, "Fail to connect Ludo Server !!!!", Toast.LENGTH_SHORT).show();
	
					 }
				 else
				 	{
				 		connectstatus = true;
						CreateConfigGame(message);
				 	}
				 
			 	}
		 
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

			connectstatus = false;

			if(mAppConnected)
				{
					Createbnt.setEnabled(true);
					Createbnt.setText("Start Game");
				}
			else{
					Createbnt.setEnabled(false);
					Createbnt.setText("Disconnected");
				}
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
	case R.id.menu_exit:  
		finish(); 
		break;
	case R.id.menu_vibrator:  
		SetVibrator(); 
		break;			
	default:  
		break;	
	}  
	return super.onOptionsItemSelected(item);  
} 


	private void SetVibrator(){

		new AlertDialog.Builder(this).
		 setTitle("Enable/Disable Vibrator").
		 setMessage("Enable or Disable Vibrator ? ??").
		 setNegativeButton("Enable", new DialogInterface.OnClickListener() {
		   @Override
			   public void onClick(DialogInterface dialog, int which) {
									Vibratorstatus = true;
							   }
						   }).	 
			setPositiveButton("Disable", new DialogInterface.OnClickListener() {
		   @Override
			   public void onClick(DialogInterface dialog, int which) {
								   Vibratorstatus = false;
							   }
						   }).create().show();	 

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
