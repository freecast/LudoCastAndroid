package com.example.casthelloworld;

import org.json.JSONException;

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PlayGame extends ActionBarActivity{
	
    private VideoCastManager mCastManager;
    private VideoCastConsumerImpl mCastConsumer;
    private static final String TAG = "PlayerGame";
    
    String SendMsg;
    String playername;
    String usertype;
	private LudoProtocol protocol;
    private GestureDetector gestureDetector; 
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playgame);
		Log.d(TAG, "onCreate() was called");
		ActionBar actionBar = getSupportActionBar();
		mCastManager = CastApplication.getCastManager(this);
		setupActionBar(actionBar);
		
		gestureDetector = new GestureDetector(PlayGame.this,onGestureListener); 

		mCastManager = CastApplication.getCastManager(this);


		mCastConsumer = new VideoCastConsumerImpl() {
		
		@Override
		public void onDisconnected() {
		
			System.out.println("PlayGame onDisconnected message ");
			
		
		}			
		
		 @Override
		 public void onFailed(int resourceId, int statusCode) {
		
			System.out.println("onFailed message = "+statusCode);
					 
		
		 }
		
			@Override
			public void onApplicationDisconnected(int errorCode) {
		
			System.out.println("onApplicationDisconnected message = "+errorCode);
		
			
			}
		
		 @Override
		 public boolean onApplicationConnectionFailed(int errorCode) {
		 
			System.out.println("onApplicationConnectionFailed message = "+errorCode);
	
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
				 System.out.println("Playgame receiver message = "+message);
			    }
		
			 @Override
			 public void onConnectionSuspended(int cause) {
				 Log.d(TAG, "onConnectionSuspended() was called with cause: " + cause);
				 com.example.casthelloworld.Utils.
						 showToast(PlayGame.this, R.string.connection_temp_lost);
			 }
		
			 @Override
			 public void onConnectivityRecovered() {
				 com.example.casthelloworld.Utils.
						 showToast(PlayGame.this, R.string.connection_recovered);
			 }
		
			 @Override
			 public void onCastDeviceDetected(final RouteInfo info) {
				 if (!CastPreference.isFtuShown(PlayGame.this)) {
					 CastPreference.setFtuShown(PlayGame.this);
		
					 Log.d(TAG, "Route is visible: " + info);
					 new Handler().postDelayed(new Runnable() {
		
						 @Override
						 public void run() {

						 }
					 }, 1000);
				 }
			 }
		 };

		protocol = new LudoProtocol();		
		
		
	}
	

	


	private void setupActionBar(ActionBar actionBar) {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	private void sendMessage(String message) {
		if (mCastManager!=null){			
			try {
				System.out.println("PlayGame sendmessage = "+message);
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
                    	sendMessage("prev");
                     
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                      && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
                    	sendMessage("next");
                     
                      }
                   } catch (Exception e) {
                    Log.e(TAG, "onFling error", e);
                   }
                   return false;
            }

            public boolean onSingleTapUp(MotionEvent event) {      
                // TODO Auto-generated method stub
                	sendMessage("click");
                   return false;      
                }              
         
        };  
      
        public boolean onTouchEvent(MotionEvent event) {
            return gestureDetector.onTouchEvent(event);  
        } 
		
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            Log.d(TAG, "onCreateOptionsMenu() was called");
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


	@Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			
			Log.d(TAG, "Run in Back Key ");

		 if (null != mCastManager) {
			 if(mCastManager.isConnected())
				 {
					 String msg = null;
					 try {
						 msg = protocol.genMessage_disconnect();
					 } catch (JSONException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					 Log.d(TAG, "disconnect message: " + msg);
					 sendMessage(msg);
				 }
		 
		 }
		 Intent i = new Intent();
		 i.putExtra("request_text_for_third", "从ThirdActivity再次传递到Main");
		 setResult(Activity.RESULT_FIRST_USER, i);
		 finish();

		 
		}
		return super.onKeyDown(keyCode, event);
	  }

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop() was called");
		super.onStop();
	}


	@Override
	protected void onPause() {
		Log.d(TAG, "onPause() was called");
        mCastManager.decrementUiCounter();
        mCastManager.removeVideoCastConsumer(mCastConsumer);

		super.onPause();
	}

	

	@Override
	protected void onDestroy() {
		Log.d(TAG, "PlayGame onDestroy() is called");
		if (null != mCastManager) {
			if(mCastManager.isConnected())
				{
					String msg = null;
					try {
						msg = protocol.genMessage_disconnect();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG, "disconnect message: " + msg);
					sendMessage(msg);
				}

			mCastManager.clearContext(this);
			mCastConsumer = null;
		}

		super.onDestroy();
	}


}
