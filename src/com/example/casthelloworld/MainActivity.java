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
	private ConnectionCallbacks mConnectionCallbacks;
	private ConnectionFailedListener mConnectionFailedListener;
	private HelloWorldChannel mHelloWorldChannel;
	private boolean mApplicationStarted;
	private boolean mWaitingForReconnect;
	private String mSessionId;
	private LudoProtocol protocol;
	
    final int RIGHT = 0;  
    final int LEFT = 1;  
    private GestureDetector gestureDetector;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		//actionBar.setBackgroundDrawable(new ColorDrawable(
		//		android.R.color.transparent));
		
		gestureDetector = new GestureDetector(MainActivity.this,onGestureListener); 

		mCastManager = CastApplication.getCastManager(this);

		// Configure Cast device discovery
//		mMediaRouter = MediaRouter.getInstance(getApplicationContext());
//		mMediaRouteSelector = new MediaRouteSelector.Builder()
//				.addControlCategory(
//						CastMediaControlIntent.categoryForCast(getResources()
//								.getString(R.string.app_id))).build();
//		mMediaRouterCallback = new MyMediaRouterCallback();


/*		mCastConsumer = new VideoCastConsumerImpl() {
		
			 @Override
			 public void onFailed(int resourceId, int statusCode) {
		
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
	*/	
		// setupActionBar(actionBar);
		//mCastManager.reconnectSessionIfPossible(this, false);

		protocol = new LudoProtocol();

	Button Createbnt = (Button) findViewById(R.id.Creat);
	Createbnt.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				String msg = protocol.genMessage_connect("alice");
				Log.d(TAG, "connect message: " + msg);
				mCastManager.sendDataMessage(msg);
				

				Intent it = new Intent(MainActivity.this, ConfigGame.class);

				startActivityForResult(it, 0);
				overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransientNetworkDisconnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	/**
	 * Callback for MediaRouter events
	 */
	private class MyMediaRouterCallback extends MediaRouter.Callback {

		@Override
		public void onRouteSelected(MediaRouter router, RouteInfo info) {
			Log.d(TAG, "onRouteSelected");
			// Handle the user route selection.
			mSelectedDevice = CastDevice.getFromBundle(info.getExtras());

			launchReceiver();

		}

		@Override
		public void onRouteUnselected(MediaRouter router, RouteInfo info) {
			Log.d(TAG, "onRouteUnselected: info=" + info);
			teardown();
			mSelectedDevice = null;
		}
	}

	/**
	 * Start the receiver app
	 */
	private void launchReceiver() {
		try {
			mCastListener = new Cast.Listener() {

				@Override
				public void onApplicationDisconnected(int errorCode) {
					Log.d(TAG, "application has stopped");
					teardown();
				}

			};
			// Connect to Google Play services
			mConnectionCallbacks = new ConnectionCallbacks();
			mConnectionFailedListener = new ConnectionFailedListener();
			Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
					.builder(mSelectedDevice, mCastListener);
			mApiClient = new GoogleApiClient.Builder(this)
					.addApi(Cast.API, apiOptionsBuilder.build())
					.addConnectionCallbacks(mConnectionCallbacks)
					.addOnConnectionFailedListener(mConnectionFailedListener)
					.build();

			mApiClient.connect();
		} catch (Exception e) {
			Log.e(TAG, "Failed launchReceiver", e);
		}
	}

	/**
	 * Google Play services callbacks
	 */
	private class ConnectionCallbacks implements
			GoogleApiClient.ConnectionCallbacks {
		@Override
		public void onConnected(Bundle connectionHint) {
			Log.d(TAG, "onConnected");

			if (mApiClient == null) {
				// We got disconnected while this runnable was pending
				// execution.
				return;
			}

			try {
				if (mWaitingForReconnect) {
					mWaitingForReconnect = false;

					// Check if the receiver app is still running
					if ((connectionHint != null)
							&& connectionHint
									.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
						Log.d(TAG, "App  is no longer running");
						teardown();
					} else {
						// Re-create the custom message channel
						try {
							Cast.CastApi.setMessageReceivedCallbacks(
									mApiClient,
									mHelloWorldChannel.getNamespace(),
									mHelloWorldChannel);
						} catch (IOException e) {
							Log.e(TAG, "Exception while creating channel", e);
						}
					}
				} else {
					// Launch the receiver app
					Cast.CastApi
							.launchApplication(mApiClient,
									getString(R.string.app_id), false)
							.setResultCallback(
									new ResultCallback<Cast.ApplicationConnectionResult>() {
										@Override
										public void onResult(
												ApplicationConnectionResult result) {
											Status status = result.getStatus();
											Log.d(TAG,
													"ApplicationConnectionResultCallback.onResult: statusCode"
															+ status.getStatusCode());
											if (status.isSuccess()) {
												ApplicationMetadata applicationMetadata = result
														.getApplicationMetadata();
												mSessionId = result
														.getSessionId();
												String applicationStatus = result
														.getApplicationStatus();
												boolean wasLaunched = result
														.getWasLaunched();
												Log.d(TAG,
														"application name: "
																+ applicationMetadata
																		.getName()
																+ ", status: "
																+ applicationStatus
																+ ", sessionId: "
																+ mSessionId
																+ ", wasLaunched: "
																+ wasLaunched);
												mApplicationStarted = true;

												// Create the custom message
												// channel
												mHelloWorldChannel = new HelloWorldChannel();
												try {
													Cast.CastApi
															.setMessageReceivedCallbacks(
																	mApiClient,
																	mHelloWorldChannel
																			.getNamespace(),
																	mHelloWorldChannel);
												} catch (IOException e) {
													Log.e(TAG,
															"Exception while creating channel",
															e);
												}

												// set the initial instructions
												// on the receiver
												//sendMessage(getString(R.string.instructions));
												System.out.println("onconnect to player");  
        										sendMessage("join");
			
											
											} else {
												Log.e(TAG,
														"application could not launch");
												teardown();
											}
										}
									});
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to launch application", e);
			}
			
        	System.out.println("onconnect to player");  
        	sendMessage("join");
		}

		@Override
		public void onConnectionSuspended(int cause) {
			Log.d(TAG, "onConnectionSuspended");
			mWaitingForReconnect = true;
		}
	}

	/**
	 * Google Play services callbacks
	 */
	private class ConnectionFailedListener implements
			GoogleApiClient.OnConnectionFailedListener {
		@Override
		public void onConnectionFailed(ConnectionResult result) {
			Log.e(TAG, "onConnectionFailed " + result);

			teardown();
		}
	}

	/**
	 * Tear down the connection to the receiver
	 */
	private void teardown() {
		Log.d(TAG, "teardown");
		if (mApiClient != null) {
			if (mApplicationStarted) {
				if (mApiClient.isConnected()) {
					try {
						Cast.CastApi.stopApplication(mApiClient, mSessionId);
						if (mHelloWorldChannel != null) {
							Cast.CastApi.removeMessageReceivedCallbacks(
									mApiClient,
									mHelloWorldChannel.getNamespace());
							mHelloWorldChannel = null;
						}
					} catch (IOException e) {
						Log.e(TAG, "Exception while removing channel", e);
					}
					mApiClient.disconnect();
				}
				mApplicationStarted = false;
			}
			mApiClient = null;
		}
		mSelectedDevice = null;
		mWaitingForReconnect = false;
		mSessionId = null;
	}

	/**
	 * Send a text message to the receiver
	 * 
	 * @param message
	 */
	private void sendMessage(String message) {
		if (mCastManager!=null){			
			try {
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

	/**
	 * Custom message channel
	 */
	class HelloWorldChannel implements MessageReceivedCallback {

		/**
		 * @return custom namespace
		 */
		public String getNamespace() {
			return getString(R.string.namespace);
		}

		/*
		 * Receive message from the receiver app
		 */
		@Override
		public void onMessageReceived(CastDevice castDevice, String namespace,
				String message) {
			Log.d(TAG, "onMessageReceived: " + message);
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT)
					.show();

			try {
				Log.d(TAG, "call protocol parseMessage");
				MainActivity.this.protocol.parseMessage(message);
				Log.d(TAG, "protocol parseMessage was called");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
