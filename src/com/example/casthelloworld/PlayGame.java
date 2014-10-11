package com.example.casthelloworld;

import java.lang.reflect.Field;

import org.json.JSONException;

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class PlayGame extends ActionBarActivity{
	
    private VideoCastManager mCastManager;
    private VideoCastConsumerImpl mCastConsumer;
    private static final String TAG = "PlayerGame";
    
    String SendMsg;
    String playername;
    String usertype;
	static Boolean ResetGame;
	static Boolean ishost;
	static Boolean EndofGame;
	static Boolean startturn;
	static Boolean clickstatus;
	static Boolean nextstatus;
	static Boolean prevstatus;
	private LudoProtocol protocol;
    private GestureDetector gestureDetector; 
	private ImageView PlayerLogo;
	static String PlayerColor;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.playgame);
		Log.d(TAG, "onCreate() was called");
		ActionBar actionBar = getSupportActionBar();
		mCastManager = CastApplication.getCastManager(this);
		setupActionBar(actionBar);
		clickstatus = false;
		prevstatus = false;
		nextstatus = false;
		startturn = false;
		ResetGame = false;
		EndofGame = false;

		PlayerLogo =  (ImageView)this.findViewById(R.id.playerlogo);

		PlayerLogo.setVisibility(View.INVISIBLE); 		

		protocol = new LudoProtocol();		

		setupCastListener();		

        ishost = getIntent().getBooleanExtra("ishost from ConfigGame", false);
        System.out.println("ishost from ConfigGame = "+ishost);
		
		gestureDetector = new GestureDetector(PlayGame.this,onGestureListener); 

		getOverflowMenu();	
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
	public void onDisconnected() {
	
		System.out.println("PlayGame onDisconnected message ");
		finish();
		
	
	}			
	
	 @Override
	 public void onFailed(int resourceId, int statusCode) {
	
		System.out.println("onFailed message = "+statusCode);
		finish();		 
	
	 }
	
		@Override
		public void onApplicationDisconnected(int errorCode) {
	
		System.out.println("onApplicationDisconnected message = "+errorCode);
		finish();
	
		
		}
	
	 @Override
	 public boolean onApplicationConnectionFailed(int errorCode) {
	 
		System.out.println("onApplicationConnectionFailed message = "+errorCode);
		finish();
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
			 
			 if(clickstatus)
			 	{
			 		clickstatus = false;
					if(MainActivity.Vibratorstatus)
					{
						VibratorUtil.Vibrate(PlayGame.this, 100); 
					}
					
			 	}

			if(startturn)
			   {
				   startturn = false;
				   if(MainActivity.Vibratorstatus)
				   {
				   	   long [] pattern = {100,100,100,100}; 
					   VibratorUtil.Vibrate(PlayGame.this, pattern, false);
				   }
				    if(PlayerColor.equals("red"))
					 PlayerLogo.setImageDrawable(getResources().getDrawable(R.drawable.btn_option_sin_plane_red));

					if(PlayerColor.equals("yellow"))
					 PlayerLogo.setImageDrawable(getResources().getDrawable(R.drawable.btn_option_sin_plane_yellow));

					if(PlayerColor.equals("blue"))
					 PlayerLogo.setImageDrawable(getResources().getDrawable(R.drawable.btn_option_sin_plane_blue));

					if(PlayerColor.equals("green"))
					 PlayerLogo.setImageDrawable(getResources().getDrawable(R.drawable.btn_option_sin_plane_green));

				   
					PlayerLogo.setVisibility(View.VISIBLE); 
				   
			   }


			if(nextstatus)
			   {
				   nextstatus = false;
				   if(MainActivity.Vibratorstatus)
				   {
					   VibratorUtil.Vibrate(PlayGame.this, 100); 
				   }
				   
			   }

			 if(prevstatus)
			 	{
			 		prevstatus = false;
					if(MainActivity.Vibratorstatus)
					{
						VibratorUtil.Vibrate(PlayGame.this, 100); 
					}
					
			 	}			

			 

			if(ResetGame)
				{
					finish();
					ResetGame = false;
				}

			if(EndofGame)
				{

				  AlertDialog.Builder builder1=new AlertDialog.Builder(PlayGame.this);
				  builder1.setTitle("End Of Game").setMessage("Press Confirm Key to continue...").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					@Override
						public void onClick(DialogInterface dialog, int which) {
											finish();
										}
									}).create().show();				
				}
			 
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



	}


	private void setupActionBar(ActionBar actionBar) {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
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
    	  private static final int SWIPE_MIN_DISTANCE = 80;
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

			MenuItem actionRestart = menu.findItem(R.id.menu_restart);
			actionRestart.setVisible(false); 

			
			if(ishost)
				{
					actionRestart.setVisible(true); 
				}
            return true;
        }

	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
		switch (item.getItemId()) {  
		case R.id.menu_exit:  
			ExitGame();
			break;
		case R.id.menu_restart:  
			ResetGame(); 
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
										MainActivity.Vibratorstatus = true;
								   }
							   }).	 
				setPositiveButton("Disable", new DialogInterface.OnClickListener() {
			   @Override
				   public void onClick(DialogInterface dialog, int which) {
									   MainActivity.Vibratorstatus = false;
								   }
							   }).create().show();	 
	
		}


	private void ExitGame(){

		new AlertDialog.Builder(this).
		 setTitle("Exit this Game").
		 setMessage("Are you sure to Exit ? ? ?").
		 setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
		   @Override
			   public void onClick(DialogInterface dialog, int which) {
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
						   }).	 
			setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		   @Override
			   public void onClick(DialogInterface dialog, int which) {
								   return;
							   }
						   }).create().show();	 

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
			ExitGame();

		}
		return super.onKeyDown(keyCode, event);
	  }

	private void ResetGame(){
		if (null != mCastManager) {
			if(mCastManager.isConnected())
				{
					String msg = null;
					try {
						msg = protocol.genMessage_reset();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG, "disconnect message: " + msg);
					sendMessage(msg);
				}
		
		}
		Intent i = new Intent();
		setResult(Activity.RESULT_FIRST_USER, i);
		finish();

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
