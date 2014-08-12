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

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

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
	static Boolean ishost;
	static Boolean updatestatus;
	
    final int RIGHT = 0;  
    final int LEFT = 1;  
    static String username = null;
    private GestureDetector gestureDetector;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));

		mAppConnected = false;

		ishost = false;

		updatestatus = false;
		
		final EditText editText=(EditText)findViewById(R.id.editText1); 
		

		 editText.setOnEditorActionListener(new OnEditorActionListener() {  
	            @Override  
	            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
	                Toast.makeText(MainActivity.this, String.valueOf(actionId), Toast.LENGTH_SHORT).show();  
	                return false;  
	            }  
	        });  		
		
		
		
		gestureDetector = new GestureDetector(MainActivity.this,onGestureListener); 

		mCastManager = CastApplication.getCastManager(this);


		mCastConsumer = new VideoCastConsumerImpl() {


		    @Override
    		public void onApplicationConnected(ApplicationMetadata appMetadata,
            String sessionId, boolean wasLaunched) {

				System.out.println("onApplicationConnected message = "+sessionId);

				Toast.makeText(MainActivity.this, "Device is Ready to Start Game!!!", Toast.LENGTH_SHORT).show();

				mAppConnected = true;
	
    		}


		    @Override
		    public void onDisconnected() {

				System.out.println("onDisconnected message ");
				
				mAppConnected = false;

			
		    }			
		
			 @Override
			 public void onFailed(int resourceId, int statusCode) {

				System.out.println("onFailed message = "+statusCode);
				
				mAppConnected = false;			 
		
			 }

			    @Override
			    public void onApplicationDisconnected(int errorCode) {

				System.out.println("onApplicationDisconnected message = "+errorCode);

				mAppConnected = false;

				
			    }

			 @Override
			 public boolean onApplicationConnectionFailed(int errorCode) {
			 
				System.out.println("onApplicationConnectionFailed message = "+errorCode);

				mAppConnected = false;
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

		protocol = new LudoProtocol();

	Button Createbnt = (Button) findViewById(R.id.Startgame);
	Createbnt.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

			username = editText.getText().toString();

			Log.d(TAG, "username =  " + username);

			if(username.equals(""))
				{
				
				Toast.makeText(MainActivity.this, "Please enter your name first !!!", Toast.LENGTH_SHORT).show();
				return;	
				}

			if(mAppConnected){
					try {
						String msg = protocol.genMessage_connect(username);
						Log.d(TAG, "connect message: " + msg);
						sendMessage(msg);
						

						Intent it = new Intent(MainActivity.this, ConfigGame.class);

						startActivityForResult(it, 0);
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


		
	}

    private void setupActionBar(ActionBar actionBar) {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
       // getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private GestureDetector.OnGestureListener onGestureListener =   
            new GestureDetector.SimpleOnGestureListener() { 
    	  private static final int SWIPE_MIN_DISTANCE = 120;
    	  private static final int SWIPE_MAX_OFF_PATH = 250;
    	  private static final int SWIPE_THRESHOLD_VELOCITY = 200;
            @Override  
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
                    float velocityY) {  
                
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                     return false;
                    // right to left swipe
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                      && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    	System.out.println("go left");  
                    	sendMessage("prev");
                     
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                      && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    	System.out.println("go right");  
                    	sendMessage("next");
                     
                      }
                   } catch (Exception e) {
                    Log.e(TAG, "onFling error", e);
                   }
                   return false;
            }

            public boolean onSingleTapUp(MotionEvent event) {      
                // TODO Auto-generated method stub
                	System.out.println("onclick @@@");
                	sendMessage("click");
                   return false;      
                }              
         
        };  
      
        public boolean onTouchEvent(MotionEvent event) {
            return gestureDetector.onTouchEvent(event);  
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if (matches.size() > 0) {
				Log.d(TAG, matches.get(0));
				sendMessage(matches.get(0));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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


	@Override
	protected void onPause() {
        mCastManager.decrementUiCounter();
        mCastManager.removeVideoCastConsumer(mCastConsumer);

		super.onPause();
	}

	@Override
	public void onDestroy() {
        if (null != mCastManager) {
            mMini.removeOnMiniControllerChangedListener(mCastManager);
            mCastManager.removeMiniController(mMini);
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
		//MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
		//MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
		//		.getActionProvider(mediaRouteMenuItem);
		// Set the MediaRouteActionProvider selector for device discovery.
		//mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
		return true;
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
