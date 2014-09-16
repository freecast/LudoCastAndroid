package com.example.casthelloworld;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
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

	private static final String KEY_MAGIC = "MAGIC";
    private static final String MAGIC = "ONLINE";

    private static final String KEY_VERSION = "prot_version";
    private static final int    VERSION = 1;

    private static final String KEY_COMMAND = "command";

    private static final String COMMAND_CONNECT = "connect";
    private static final String KEY_USERNAME = "username";
    private static final String COMMAND_CONNECT_REPLY = "connect_reply";
    private static final String COMMAND_STARTGAME_NOTIFY = "startgame_notify";
    private static final String KEY_RET = "ret";
    private static final String KEY_ISHOST = "ishost";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_PLAYER_STATUS = "player_status";
    private static final String KEY_COLOR = "color";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_ISREADY = "isready";
	private static final String COMMAND_PICKUP_NOTIFY = "pickup_notify";
	private final int SECOND_REQUEST_CODE = 2;
    
    String SendMsg;
    String playername = "";
	String playerStatus = "";
    String usertype;
	private LudoProtocol protocol;
	static Boolean startgame;
	RadioGroup RadioGroupRed;
	RadioGroup RadioGroupYellow;
	RadioGroup RadioGroupBlue;
	RadioGroup RadioGroupGreen;
	RadioGroup RadioGroupLevel;
	RadioButton RadioButtonRed1;
	RadioButton RadioButtonRed2;
	RadioButton RadioButtonRed3;
	RadioButton RadioButtonRed4;
	RadioButton RadioButtonRedCurrent;
	RadioButton RadioButtonBlue1;
	RadioButton RadioButtonBlue2;
	RadioButton RadioButtonBlue3;
	RadioButton RadioButtonBlue4;
	RadioButton RadioButtonBlueCurrent;
	RadioButton RadioButtonYellow1;
	RadioButton RadioButtonYellow2;
	RadioButton RadioButtonYellow3;
	RadioButton RadioButtonYellow4;
	RadioButton RadioButtonYellowCurrent;
	RadioButton RadioButtonGreen1;
	RadioButton RadioButtonGreen2;
	RadioButton RadioButtonGreen3;
	RadioButton RadioButtonGreen4;
	RadioButton RadioButtonGreenCurrent;
	ImageView RedLock;
	ImageView GreenLock;
	ImageView YellowLock;
	ImageView BlueLock;
	static Boolean RadioGroupRedLock;
	static Boolean RadioGroupBlueLock;
	static Boolean RadioGroupYellowLock;
	static Boolean RadioGroupGreenLock;
	static Boolean RadioGroupLevelLock;
	static Boolean begingame;
	static Boolean configgameishost;
	static Boolean configgameupdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configgame);
		Log.d(TAG, "onCreate() was called");
		ActionBar actionBar = getSupportActionBar();
		mCastManager = CastApplication.getCastManager(this);
		setupActionBar(actionBar);
		
		startgame = false;

		begingame = false;

		configgameishost = false;

		configgameupdate = false;

		protocol = new LudoProtocol();

		setupCastListener();

		RadioButtonRed1 = (RadioButton)findViewById(R.id.RadioButtonRed1);
		RadioButtonRed2 = (RadioButton)findViewById(R.id.RadioButtonRed2);
		RadioButtonRed3 = (RadioButton)findViewById(R.id.RadioButtonRed3);
		RadioButtonRed4 = (RadioButton)findViewById(R.id.RadioButtonRed4);
		RadioButtonRedCurrent = RadioButtonRed1;

		RadioButtonBlue1 = (RadioButton)findViewById(R.id.RadioButtonBlue1);
		RadioButtonBlue2 = (RadioButton)findViewById(R.id.RadioButtonBlue2);
		RadioButtonBlue3 = (RadioButton)findViewById(R.id.RadioButtonBlue3);
		RadioButtonBlue4 = (RadioButton)findViewById(R.id.RadioButtonBlue4);
		RadioButtonBlueCurrent = RadioButtonBlue1;

		RadioButtonYellow1 = (RadioButton)findViewById(R.id.RadioButtonYellow1);
		RadioButtonYellow2 = (RadioButton)findViewById(R.id.RadioButtonYellow2);
		RadioButtonYellow3 = (RadioButton)findViewById(R.id.RadioButtonYellow3);
		RadioButtonYellow4 = (RadioButton)findViewById(R.id.RadioButtonYellow4);
		RadioButtonYellowCurrent = RadioButtonYellow1;

		RadioButtonGreen1 = (RadioButton)findViewById(R.id.RadioButtonGreen1);
		RadioButtonGreen2 = (RadioButton)findViewById(R.id.RadioButtonGreen2);	
		RadioButtonGreen3 = (RadioButton)findViewById(R.id.RadioButtonGreen3);	
		RadioButtonGreen4 = (RadioButton)findViewById(R.id.RadioButtonGreen4);
		RadioButtonGreenCurrent = RadioButtonGreen1;

		RedLock = (ImageView)findViewById(R.id.imageView7);
		BlueLock = (ImageView)findViewById(R.id.imageView8);
		YellowLock = (ImageView)findViewById(R.id.imageView9);
		GreenLock = (ImageView)findViewById(R.id.imageView10);

		RadioGroupRedLock = false;
		RedLock.setVisibility(View.INVISIBLE);
		RadioGroupBlueLock = false;
		BlueLock.setVisibility(View.INVISIBLE);
		RadioGroupYellowLock = false;
		YellowLock.setVisibility(View.INVISIBLE);
		RadioGroupGreenLock = false;
		GreenLock.setVisibility(View.INVISIBLE);
		RadioGroupLevelLock = false;
		
		
        final Button getready=(Button)findViewById(R.id.button_getready);
		getready.setEnabled(true);
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
				getready.setEnabled(false);
                
            }  
        });  		
		
		RadioButton RadioButtonRed1 = (RadioButton)findViewById(R.id.RadioButtonRed1);		
		RadioButton RadioButtonYellow1 = (RadioButton)findViewById(R.id.RadioButtonYellow1);			
		RadioButton RadioButtonBlue1 = (RadioButton)findViewById(R.id.RadioButtonBlue1);			
		RadioButton RadioButtonGreen1 = (RadioButton)findViewById(R.id.RadioButtonGreen1);

        if (getIntent().getStringExtra("username from MainActivity") != null) {
        	playername = getIntent().getStringExtra("username from MainActivity");
        	System.out.println("ConfigGame get username = "+playername);
        }	

		RadioButtonRed1.setText(playername);
		RadioButtonYellow1.setText(playername);
		RadioButtonBlue1.setText(playername);
		RadioButtonGreen1.setText(playername);

		InitLevel();
		InitRedPlayer();
		InitBluePlayer();
		InitYellowPlayer();
		InitGreenPlayer();
		getOverflowMenu();
		
        if (getIntent().getStringExtra("playerstatus from MainActivity") != null) {
        	playerStatus = getIntent().getStringExtra("playerstatus from MainActivity");
        	System.out.println("ConfigGame get playerStatus = "+playerStatus);
			try {
				UpdatePlayer(playerStatus);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

    private void setupCastListener() {

		mCastConsumer = new VideoCastConsumerImpl() {
			
		    @Override
		    public void onDisconnected() {

				System.out.println("ConfigGame onDisconnected message ");
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
					UpdatePlayer(message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				
				 if(startgame == true)
				 {
						Intent it = new Intent(ConfigGame.this, PlayGame.class);
						it.putExtra("ishost from ConfigGame", configgameishost);
						startActivityForResult(it,0);
						overridePendingTransition(R.anim.push_left_in,
										R.anim.push_left_out);					
						startgame = false;
						begingame = true;
						finish();

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
        Log.d(TAG, "onCreateOptionsMenu() was called");
        getMenuInflater().inflate(R.menu.main, menu);
        mCastManager.addMediaRouterButton(menu, R.id.media_route_menu_item);
		MenuItem actionRestart = menu.findItem(R.id.menu_restart);
		actionRestart.setVisible(false); 
        return true;
    }

 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {	 
	 super.onActivityResult(requestCode, resultCode, data); 
	 Log.d(TAG, "onActivityResult() was called1");
		 setResult(Activity.RESULT_FIRST_USER, data);
		 Log.d(TAG, "onActivityResult() was called");
		 mCastManager.clearContext();
		 mCastConsumer = null;
		 finish();
 }


@Override  
public boolean onOptionsItemSelected(MenuItem item) {  
	switch (item.getItemId()) {  
	case R.id.menu_exit:  
		finish(); 
		break;	
	default:  
		break;	
	}  
	return super.onOptionsItemSelected(item);  
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
protected void onDestroy() {
	Log.d(TAG, "onDestroy() is called");
	if (null != mCastManager) {

		if(!begingame && mCastManager.isConnected())
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


public boolean UpdatePlayer(String msg) throws JSONException {
	System.out.println("UpdatePlayer  = "+msg);
	JSONObject obj = new JSONObject(msg);

	Log.d(TAG, "check " + KEY_MAGIC);
	if (obj.has(KEY_MAGIC) == false) {
		Log.d(TAG, "missing key: " + KEY_MAGIC);
		return false;
	}
	if (obj.getString(KEY_MAGIC).equals(MAGIC) == false) {
		Log.d(TAG, "wrong key[" + KEY_MAGIC + "]=" + obj.getString(KEY_MAGIC));
		return false;
	}

	Log.d(TAG, "check " + KEY_VERSION);
	if (obj.has(KEY_VERSION) == false) {
		Log.d(TAG, "missing key: " + KEY_VERSION);
		return false;
	}
	if (obj.getInt(KEY_VERSION) != VERSION) {
		Log.d(TAG, "wrong key[" + KEY_VERSION + "]=" + obj.getInt(KEY_VERSION));
		return false;
	}

	Log.d(TAG, "check " + KEY_COMMAND);
	if (obj.has(KEY_COMMAND) == false) {
		Log.d(TAG, "missing key: " + KEY_COMMAND);
		return false;
	}
	String command = obj.getString(KEY_COMMAND);
	if (command.equals(COMMAND_CONNECT_REPLY)) {
		boolean ret = obj.getBoolean(KEY_RET);
		boolean ishost = obj.getBoolean(KEY_ISHOST);
		String level = obj.getString(KEY_LEVEL);
		Log.d(TAG, "ret:" + ret + " ishost:" + ishost +
				" level:" + level);
		configgameishost = ishost;

		JSONArray arr = obj.getJSONArray(KEY_PLAYER_STATUS);
		for (int i=0; i<arr.length(); i++) {
			JSONObject o = arr.getJSONObject(i);
			String color = o.getString(KEY_COLOR);
			String user_type = o.getString(KEY_USER_TYPE);
			boolean isready = o.getBoolean(KEY_ISREADY);
			String username = o.getString(KEY_USERNAME);
			Log.d(TAG, "player-"+i+"color"+color+
					"user_type"+user_type+"isready"+isready+"username"+username);
			configgameupdate = true;
			UpdatePlayerStatus(color, user_type, username);
			configgameupdate = false;
			
		}
	}else if(command.equals(COMMAND_STARTGAME_NOTIFY)){
		
		Log.d(TAG, "start game notify command = " + command);
		startgame = true;
	}
	else if(command.equals(COMMAND_PICKUP_NOTIFY)){
				
					JSONObject o = obj.getJSONObject(KEY_PLAYER_STATUS);
					String color = o.getString(KEY_COLOR);
					String user_type = o.getString(KEY_USER_TYPE);
					boolean isready = o.getBoolean(KEY_ISREADY);
					String username = o.getString(KEY_USERNAME);
					Log.d(TAG, "player-"+"color"+color+
							"user_type"+user_type+"isready"+isready+"username"+username);
					configgameupdate = true;
					UpdatePlayerStatus(color, user_type, username);
					configgameupdate = false;
		
	}else {
		Log.d(TAG, "unsupported key[" + KEY_COMMAND + "]=" + command);
	}
	return true;
}


private void InitLevel(){

	RadioGroupLevel= (RadioGroup)this.findViewById(R.id.RadioGroupLevel);

	RadioGroupLevel.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	@Override
	public void onCheckedChanged(RadioGroup arg00, int arg01) {

		if(!RadioGroupLevelLock)
			{
				int radioButtonId = arg00.getCheckedRadioButtonId();

				RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
				
				System.out.println("Get group select = "+rb00.getText());
				
				try {
					SendMsg = protocol.genMessage_pickup("level",rb00.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendMessage(SendMsg);
			}

	}
	});
	
}



private void InitRedPlayer(){

	RadioGroupRed= (RadioGroup)this.findViewById(R.id.RadioGroupRed);

	RadioGroupRed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	@Override
	public void onCheckedChanged(RadioGroup arg00, int arg01) {


		if(!RadioGroupRedLock)
			{
				if(!configgameishost)
					{
						switch(arg00.getCheckedRadioButtonId())
							{
								case R.id.RadioButtonRed1:
									
									System.out.println("not host Get group select = "+RadioButtonRed1.getText());
									if(!configgameupdate){			
											try {
												SendMsg = protocol.genMessage_pickup("red",RadioButtonRed1.getText().toString());
											} catch (JSONException e) {
												e.printStackTrace();
											}
											sendMessage(SendMsg);
										}
											RadioButtonRedCurrent = RadioButtonRed1;
											
										break;
								case R.id.RadioButtonRed2:
									RadioButtonRedCurrent.setChecked(true);
										break;

								case R.id.RadioButtonRed3:
									
									System.out.println("not host Get group select = "+RadioButtonRed3.getText());
									if(!configgameupdate){			
											try {
												SendMsg = protocol.genMessage_pickup("red",RadioButtonRed3.getText().toString());
											} catch (JSONException e) {
												e.printStackTrace();
											}
											sendMessage(SendMsg);
										}
											RadioButtonRedCurrent = RadioButtonRed3;
											
										break;									
								case R.id.RadioButtonRed4:
									RadioButtonRedCurrent.setChecked(true);
										break;
							}
					
					}else
					{
				
							int radioButtonId = arg00.getCheckedRadioButtonId();

							RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
							
							System.out.println("host Get group select = "+rb00.getText());
							if(!configgameupdate){			
									try {
										SendMsg = protocol.genMessage_pickup("red",rb00.getText().toString());
									} catch (JSONException e) {
										e.printStackTrace();
									}
									sendMessage(SendMsg);
								}
							RadioButtonRedCurrent = rb00;
					}
			}else{

				System.out.println("RedPlayer has been locked! ");
				RadioButtonRedCurrent.setChecked(true);
			
				}

	}
	});
	
}

private void InitBluePlayer(){

	RadioGroupBlue= (RadioGroup)this.findViewById(R.id.RadioGroupBlue);

	RadioGroupBlue.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	@Override
	public void onCheckedChanged(RadioGroup arg00, int arg01) {

		

		if(!RadioGroupBlueLock)
			{
			if(!configgameishost)
				{
					switch(arg00.getCheckedRadioButtonId())
						{
							case R.id.RadioButtonBlue1:
								
								System.out.println("not host Get group select = "+RadioButtonBlue1.getText());
								if(!configgameupdate){			
										try {
											SendMsg = protocol.genMessage_pickup("blue",RadioButtonBlue1.getText().toString());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										sendMessage(SendMsg);
									}
										RadioButtonBlueCurrent = RadioButtonBlue1;
										
									break;
							case R.id.RadioButtonBlue2:
								RadioButtonBlueCurrent.setChecked(true);
									break;
			
							case R.id.RadioButtonBlue3:
								
								System.out.println("not host Get group select = "+RadioButtonBlue3.getText());
								if(!configgameupdate){			
										try {
											SendMsg = protocol.genMessage_pickup("blue",RadioButtonBlue3.getText().toString());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										sendMessage(SendMsg);
									}
										RadioButtonBlueCurrent = RadioButtonBlue3;
										
									break;
								
							case R.id.RadioButtonBlue4:
								RadioButtonBlueCurrent.setChecked(true);
									break;
						}
				
				}else
				{
			
						int radioButtonId = arg00.getCheckedRadioButtonId();
			
						RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
						
						System.out.println("host Get group select = "+rb00.getText());
						if(!configgameupdate){			
								try {
									SendMsg = protocol.genMessage_pickup("blue",rb00.getText().toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
								sendMessage(SendMsg);
							}
						RadioButtonBlueCurrent = rb00;
				}

			}else{

				System.out.println("BluePlayer has been locked! ");
				RadioButtonBlueCurrent.setChecked(true);

			
				}

	}
	});
	
}

private void InitYellowPlayer(){

	RadioGroupYellow= (RadioGroup)this.findViewById(R.id.RadioGroupYellow);

	RadioGroupYellow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	@Override
	public void onCheckedChanged(RadioGroup arg00, int arg01) {

		if(!RadioGroupYellowLock)
			{
			if(!configgameishost)
				{
					switch(arg00.getCheckedRadioButtonId())
						{
							case R.id.RadioButtonYellow1:
								
								System.out.println("not host Get group select = "+RadioButtonYellow1.getText());
								if(!configgameupdate){			
										try {
											SendMsg = protocol.genMessage_pickup("yellow",RadioButtonYellow1.getText().toString());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										sendMessage(SendMsg);
									}
										RadioButtonYellowCurrent = RadioButtonYellow1;
										
									break;
							case R.id.RadioButtonYellow2:
								RadioButtonYellowCurrent.setChecked(true);
									break;
			
							case R.id.RadioButtonYellow3:
								
								System.out.println("not host Get group select = "+RadioButtonYellow3.getText());
								if(!configgameupdate){			
										try {
											SendMsg = protocol.genMessage_pickup("yellow",RadioButtonYellow3.getText().toString());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										sendMessage(SendMsg);
									}
										RadioButtonYellowCurrent = RadioButtonYellow3;
										
									break;
								
							case R.id.RadioButtonYellow4:
								RadioButtonYellowCurrent.setChecked(true);
									break;
						}
				
				}else
				{
			
						int radioButtonId = arg00.getCheckedRadioButtonId();
			
						RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
						
						System.out.println("host Get group select = "+rb00.getText());
						if(!configgameupdate){			
								try {
									SendMsg = protocol.genMessage_pickup("yellow",rb00.getText().toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
								sendMessage(SendMsg);
							}
						RadioButtonYellowCurrent = rb00;
				}

			}else{

				System.out.println("YellowPlayer has been locked! ");
				
				RadioButtonYellowCurrent.setChecked(true);
			
				}

	}
	});

	
	
}

private void InitGreenPlayer(){

	RadioGroupGreen= (RadioGroup)this.findViewById(R.id.RadioGroupGreen);

	RadioGroupGreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	@Override
	public void onCheckedChanged(RadioGroup arg00, int arg01) {

		if(!RadioGroupGreenLock)
			{
			if(!configgameishost)
				{
					switch(arg00.getCheckedRadioButtonId())
						{
							case R.id.RadioButtonGreen1:
								
								System.out.println("not host Get group select = "+RadioButtonGreen1.getText());
								if(!configgameupdate){			
										try {
											SendMsg = protocol.genMessage_pickup("green",RadioButtonGreen1.getText().toString());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										sendMessage(SendMsg);
									}
										RadioButtonGreenCurrent = RadioButtonGreen1;
										
									break;
							case R.id.RadioButtonGreen2:
								RadioButtonGreenCurrent.setChecked(true);
									break;
			
							case R.id.RadioButtonGreen3:
								
								System.out.println("not host Get group select = "+RadioButtonGreen3.getText());
								if(!configgameupdate){			
										try {
											SendMsg = protocol.genMessage_pickup("green",RadioButtonGreen3.getText().toString());
										} catch (JSONException e) {
											e.printStackTrace();
										}
										sendMessage(SendMsg);
									}
										RadioButtonGreenCurrent = RadioButtonGreen3;
										
									break;
								
							case R.id.RadioButtonGreen4:
								RadioButtonGreenCurrent.setChecked(true);
									break;
						}
				
				}else
				{
			
						int radioButtonId = arg00.getCheckedRadioButtonId();
			
						RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
						
						System.out.println("host Get group select = "+rb00.getText());
						if(!configgameupdate){			
								try {
									SendMsg = protocol.genMessage_pickup("green",rb00.getText().toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
								sendMessage(SendMsg);
							}
						RadioButtonGreenCurrent = rb00;
				}

			}else{

				System.out.println("GreenPlayer has been locked! ");
				RadioButtonGreenCurrent.setChecked(true);

			
				}

	}
	});

	
}




private  void SetRedPlayerStatus(String user_type, String user_name){

	System.out.println("SetRedPlayerStatus = "+user_type);

	if(user_type.equals("computer"))
	{
		RadioButtonRedCurrent = RadioButtonRed2;
		RadioButtonRed2.setChecked(true);
		
		
	}else if(user_type.equals("unavailable"))
	{
		RadioButtonRedCurrent = RadioButtonRed4;	
		RadioButtonRed4.setChecked(true);
		

	}else if(user_type.equals("nobody"))
	{
		RadioButtonRedCurrent = RadioButtonRed3;	
		RadioButtonRed3.setChecked(true);
		

	}else if(user_type.equals("human"))
	{
		RadioButtonRedCurrent = RadioButtonRed1;	
		RadioButtonRed1.setChecked(true);
		RadioButtonRed1.setText(user_name);

	}

	if(!configgameishost )
		{
			if(user_type.equals("nobody"))
			{
				RadioGroupRedLock = false;
				RedLock.setVisibility(View.INVISIBLE);
			}else if(user_type.equals("human"))
				{
					if(user_name.equals(playername))
					{
						RadioGroupRedLock = false;
						RedLock.setVisibility(View.INVISIBLE);
					}else
					{
						RadioGroupRedLock = true;
						RedLock.setVisibility(View.VISIBLE);
					}
				}
			else
				{
					RadioGroupRedLock = true;
					RedLock.setVisibility(View.VISIBLE);
				}
		}else
		{
			RadioGroupRedLock = false;
			RedLock.setVisibility(View.INVISIBLE);
		}
	


	
}

private  void SetGreenPlayerStatus(String user_type, String user_name){

		System.out.println("SetGreenPlayerStatus = "+user_type);
	
		if(user_type.equals("computer"))
		{
			RadioButtonGreenCurrent = RadioButtonGreen2;		
			RadioButtonGreen2.setChecked(true);
			
		}else if(user_type.equals("unavailable"))
		{
			RadioButtonGreenCurrent = RadioButtonGreen4;		
			RadioButtonGreen4.setChecked(true);	
		}else if(user_type.equals("nobody"))
		{
			RadioButtonGreenCurrent = RadioButtonGreen3;		
			RadioButtonGreen3.setChecked(true);

	
		}else if(user_type.equals("human"))
		{
			RadioButtonGreenCurrent = RadioButtonGreen1;		
			RadioButtonGreen1.setChecked(true);
			RadioButtonGreen1.setText(user_name);
	
		}
	
	if(!configgameishost )
		{
			if(user_type.equals("nobody"))
			{
				RadioGroupGreenLock = false;
				GreenLock.setVisibility(View.INVISIBLE);
			}else if(user_type.equals("human"))
				{
					if(user_name.equals(playername))
					{
						RadioGroupGreenLock = false;
						GreenLock.setVisibility(View.INVISIBLE);
					}else
					{
						RadioGroupGreenLock = true;
						GreenLock.setVisibility(View.VISIBLE);
					}
				}
			else
				{
					RadioGroupGreenLock = true;
					GreenLock.setVisibility(View.VISIBLE);
				}
		}else
		{
			RadioGroupGreenLock = false;
			GreenLock.setVisibility(View.INVISIBLE);
		}

		

	
}		


private  void SetBluePlayerStatus(String user_type, String user_name){

		System.out.println("SetBluePlayerStatus = "+user_type);

		
		if(user_type.equals("computer"))
		{
			RadioButtonBlueCurrent = RadioButtonBlue2;		
			RadioButtonBlue2.setChecked(true);

		}else if(user_type.equals("unavailable"))
		{
			RadioButtonBlueCurrent = RadioButtonBlue4;		
			RadioButtonBlue4.setChecked(true);
	
		}else if(user_type.equals("nobody"))
		{
			RadioButtonBlueCurrent = RadioButtonBlue3;		
			RadioButtonBlue3.setChecked(true);
		}else if(user_type.equals("human"))
		{
			RadioButtonBlueCurrent = RadioButtonBlue1;		
			RadioButtonBlue1.setChecked(true);
			RadioButtonBlue1.setText(user_name);			
	
		}
	
	if(!configgameishost )
		{
			if(user_type.equals("nobody"))
			{
				RadioGroupBlueLock = false;
				BlueLock.setVisibility(View.INVISIBLE);
			}else if(user_type.equals("human"))
				{
					if(user_name.equals(playername))
					{
						RadioGroupBlueLock = false;
						BlueLock.setVisibility(View.INVISIBLE);
					}else
					{
						RadioGroupBlueLock = true;
						BlueLock.setVisibility(View.VISIBLE);
					}
				}
			else
				{
					RadioGroupBlueLock = true;
					BlueLock.setVisibility(View.VISIBLE);
				}
		}else
		{
			RadioGroupBlueLock = false;
			BlueLock.setVisibility(View.INVISIBLE);
		}

	
}	


private void SetYellowPlayerStatus(String user_type, String user_name){

		System.out.println("SetYellowPlayerStatus = "+user_type);
		
		if(user_type.equals("computer"))
		{
			RadioButtonYellowCurrent = RadioButtonYellow2;		
			RadioButtonYellow2.setChecked(true);
		}else if(user_type.equals("unavailable"))
		{
			RadioButtonYellowCurrent = RadioButtonYellow4;		
			RadioButtonYellow4.setChecked(true);
		}else if(user_type.equals("nobody"))
		{
			RadioButtonYellowCurrent = RadioButtonYellow3;		
			RadioButtonYellow3.setChecked(true);
	
		}else if(user_type.equals("human"))
		{
			RadioButtonYellowCurrent = RadioButtonYellow1;		
			RadioButtonYellow1.setChecked(true);
			RadioButtonYellow1.setText(user_name);
	
		}
	
	if(!configgameishost )
		{
			if(user_type.equals("nobody"))
			{
				RadioGroupYellowLock = false;
				YellowLock.setVisibility(View.INVISIBLE);
			}else if(user_type.equals("human"))
				{
					if(user_name.equals(playername))
					{
						RadioGroupYellowLock = false;
						YellowLock.setVisibility(View.INVISIBLE);
					}else
					{
						RadioGroupYellowLock = true;
						YellowLock.setVisibility(View.VISIBLE);
					}
				}
			else
				{
					RadioGroupYellowLock = true;
					YellowLock.setVisibility(View.VISIBLE);
				}
		}else
		{
			RadioGroupYellowLock = false;
			YellowLock.setVisibility(View.INVISIBLE);
		}

	
}	



void UpdatePlayerStatus(String color, String user_type,String user_name) {

		if(color.equals("red"))
			SetRedPlayerStatus(user_type,user_name);

		if(color.equals("blue"))
			SetBluePlayerStatus(user_type,user_name);

		if(color.equals("green"))
			SetGreenPlayerStatus(user_type,user_name);

		if(color.equals("yellow"))
			SetYellowPlayerStatus(user_type,user_name);

}


private void setupActionBar(ActionBar actionBar) {
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	//getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
	getSupportActionBar().setDisplayShowTitleEnabled(false);
}

private void sendMessage(String message) {
	if (mCastManager!=null){			
		try {
			System.out.println("configgame sendmessage = "+message);
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
