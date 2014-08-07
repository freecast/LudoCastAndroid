package com.example.casthelloworld;

import java.io.IOException;
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
    
    String SendMsg;
    String playername;
    String usertype;
	private LudoProtocol protocol;
	static Boolean startgame;
	static Boolean ishost;
	RadioGroup RadioGroupRed;
	RadioGroup RadioGroupYellow;
	RadioGroup RadioGroupBlue;
	RadioGroup RadioGroupGreen;
	RadioGroup RadioGroupLevel;
	RadioButton RadioButtonRed1;
	RadioButton RadioButtonRed2;
	RadioButton RadioButtonRed3;
	RadioButton RadioButtonRed4;
	RadioButton RadioButtonBlue1;
	RadioButton RadioButtonBlue2;
	RadioButton RadioButtonBlue3;
	RadioButton RadioButtonBlue4;
	RadioButton RadioButtonYellow1;
	RadioButton RadioButtonYellow2;
	RadioButton RadioButtonYellow3;
	RadioButton RadioButtonYellow4;
	RadioButton RadioButtonGreen1;
	RadioButton RadioButtonGreen2;
	RadioButton RadioButtonGreen3;
	RadioButton RadioButtonGreen4;
	static Boolean RadioGroupRedLock;
	static Boolean RadioGroupBlueLock;
	static Boolean RadioGroupYellowLock;
	static Boolean RadioGroupGreenLock;
	static Boolean RadioGroupLevelLock;
	static Boolean updatestatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configgame);
		Log.d(TAG, "onCreate() was called");
		ActionBar actionBar = getSupportActionBar();
		mCastManager = CastApplication.getCastManager(this);
		setupActionBar(actionBar);
		
		startgame = false;

		ishost = true;

		updatestatus = false;

		protocol = new LudoProtocol();

		RadioButtonRed1 = (RadioButton)findViewById(R.id.RadioButtonRed1);
		RadioButtonRed2 = (RadioButton)findViewById(R.id.RadioButtonRed2);
		RadioButtonRed3 = (RadioButton)findViewById(R.id.RadioButtonRed3);
		RadioButtonRed4 = (RadioButton)findViewById(R.id.RadioButtonRed4);

		RadioButtonBlue1 = (RadioButton)findViewById(R.id.RadioButtonBlue1);
		RadioButtonBlue2 = (RadioButton)findViewById(R.id.RadioButtonBlue2);
		RadioButtonBlue3 = (RadioButton)findViewById(R.id.RadioButtonBlue3);
		RadioButtonBlue4 = (RadioButton)findViewById(R.id.RadioButtonBlue4);

		RadioButtonYellow1 = (RadioButton)findViewById(R.id.RadioButtonYellow1);
		RadioButtonYellow2 = (RadioButton)findViewById(R.id.RadioButtonYellow2);
		RadioButtonYellow3 = (RadioButton)findViewById(R.id.RadioButtonYellow3);
		RadioButtonYellow4 = (RadioButton)findViewById(R.id.RadioButtonYellow4);

		RadioButtonGreen1 = (RadioButton)findViewById(R.id.RadioButtonGreen1);
		RadioButtonGreen2 = (RadioButton)findViewById(R.id.RadioButtonGreen2);	
		RadioButtonGreen3 = (RadioButton)findViewById(R.id.RadioButtonGreen3);	
		RadioButtonGreen4 = (RadioButton)findViewById(R.id.RadioButtonGreen4);	

		RadioGroupRedLock = false;
		RadioGroupBlueLock = false;
		RadioGroupYellowLock = false;
		RadioGroupGreenLock = false;
		RadioGroupLevelLock = false;
		
		
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
		
		TextView textview =(TextView) findViewById(R.id.textView4);
		textview.setText(MainActivity.username) ;
		
		RadioButton RadioButtonRed1 = (RadioButton)findViewById(R.id.RadioButtonRed1);		
		RadioButton RadioButtonYellow1 = (RadioButton)findViewById(R.id.RadioButtonYellow1);			
		RadioButton RadioButtonBlue1 = (RadioButton)findViewById(R.id.RadioButtonBlue1);			
		RadioButton RadioButtonGreen1 = (RadioButton)findViewById(R.id.RadioButtonGreen1);			

		RadioButtonRed1.setText(MainActivity.username);
		RadioButtonYellow1.setText(MainActivity.username);
		RadioButtonBlue1.setText(MainActivity.username);
		RadioButtonGreen1.setText(MainActivity.username);

		InitLevel();
		InitRedPlayer();
		InitBluePlayer();
		InitYellowPlayer();
		InitGreenPlayer();
			
		
		
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

				if(updatestatus)
					{
						try {
							UpdatePlayer(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updatestatus = false;
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

@Override
protected void onDestroy() {
	Log.d(TAG, "onDestroy() is called");
	if (null != mCastManager) {
		mCastManager.clearContext(this);
		mCastConsumer = null;
	}

	finish();	

	super.onDestroy();
}

@Override
protected void onStop() {
	Log.d(TAG, "onStop() was called");
	super.onStop();
}


public boolean UpdatePlayer(String msg) throws JSONException {
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
		ConfigGame.SetHostvalue(ishost);

		JSONArray arr = obj.getJSONArray(KEY_PLAYER_STATUS);
		for (int i=0; i<arr.length(); i++) {
			JSONObject o = arr.getJSONObject(i);
			String color = o.getString(KEY_COLOR);
			String user_type = o.getString(KEY_USER_TYPE);
			boolean isready = o.getBoolean(KEY_ISREADY);
			String username = o.getString(KEY_USERNAME);
			Log.d(TAG, "player-"+i+"color"+color+
					"user_type"+user_type+"isready"+isready+"username"+username);
			UpdatePlayerStatus(color, user_type, username);
			
		}
	}else if(command.equals(COMMAND_STARTGAME_NOTIFY)){
		
		Log.d(TAG, "start game notify command = " + command);
		ConfigGame.startgame = true;
	}
	else if(command.equals(COMMAND_PICKUP_NOTIFY)){
				
					JSONObject o = obj.getJSONObject(KEY_PLAYER_STATUS);
					String color = o.getString(KEY_COLOR);
					String user_type = o.getString(KEY_USER_TYPE);
					boolean isready = o.getBoolean(KEY_ISREADY);
					String username = o.getString(KEY_USERNAME);
					Log.d(TAG, "player-"+"color"+color+
							"user_type"+user_type+"isready"+isready+"username"+username);
					UpdatePlayerStatus(color, user_type, username);
		
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
				int radioButtonId = arg00.getCheckedRadioButtonId();

				RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
				
				System.out.println("Get group select = "+rb00.getText());
				
				try {
					SendMsg = protocol.genMessage_pickup("red",rb00.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendMessage(SendMsg);
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
				int radioButtonId = arg00.getCheckedRadioButtonId();

				RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
				
				System.out.println("Get group select = "+rb00.getText());
				
				try {
					SendMsg = protocol.genMessage_pickup("blue",rb00.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendMessage(SendMsg);
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
				int radioButtonId = arg00.getCheckedRadioButtonId();

				RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
				
				System.out.println("Get group select = "+rb00.getText());
				
				try {
					SendMsg = protocol.genMessage_pickup("yellow",rb00.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendMessage(SendMsg);
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
				int radioButtonId = arg00.getCheckedRadioButtonId();

				RadioButton rb00 = (RadioButton)findViewById(radioButtonId);
				
				System.out.println("Get group select = "+rb00.getText());
				
				try {
					SendMsg = protocol.genMessage_pickup("green",rb00.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				sendMessage(SendMsg);
			}

	}
	});

	
}




private  void SetRedPlayerStatus(String user_type, String user_name){

	System.out.println("SetRedPlayerStatus = "+user_type);

	if(user_type.equals("computer"))
	{
		RadioButtonRed2.setChecked(true);
	}else if(user_type.equals("unavailable"))
	{
		RadioButtonRed4.setChecked(true);

	}else if(user_type.equals("nobody"))
	{
		RadioButtonRed3.setChecked(true);

	}else if(user_type.equals("human"))
	{
		RadioButtonRed1.setChecked(true);
		RadioButtonRed1.setText(user_name);

	}

	if(!ishost && !user_type.equals("nobody"))
		{
			RadioGroupRedLock = true;
		}else
		{
			RadioGroupRedLock = false;
		}


	
}

private  void SetGreenPlayerStatus(String user_type, String user_name){

		System.out.println("SetGreenPlayerStatus = "+user_type);
	
		if(user_type.equals("computer"))
		{
			RadioButtonGreen2.setChecked(true);
		}else if(user_type.equals("unavailable"))
		{
			RadioButtonGreen4.setChecked(true);
	
		}else if(user_type.equals("nobody"))
		{
			RadioButtonGreen3.setChecked(true);
	
		}else if(user_type.equals("human"))
		{
			RadioButtonGreen1.setChecked(true);
			RadioButtonGreen1.setText(user_name);
	
		}
	
		if(!ishost && !user_type.equals("nobody"))
			{
				RadioGroupGreenLock = true;
			}else
			{
				RadioGroupGreenLock = false;
			}

	
}		


private  void SetBluePlayerStatus(String user_type, String user_name){

		System.out.println("SetBluePlayerStatus = "+user_type);

		
		if(user_type.equals("computer"))
		{
			RadioButtonBlue2.setChecked(true);
		}else if(user_type.equals("unavailable"))
		{
			RadioButtonBlue4.setChecked(true);
	
		}else if(user_type.equals("nobody"))
		{
			RadioButtonBlue3.setChecked(true);
	
		}else if(user_type.equals("human"))
		{
			RadioButtonBlue1.setChecked(true);
			RadioButtonBlue1.setText(user_name);
	
		}
	
		if(!ishost && !user_type.equals("nobody"))
			{
				RadioGroupBlueLock = true;
			}else
			{
				RadioGroupBlueLock = false;
			}
	
}	


private void SetYellowPlayerStatus(String user_type, String user_name){

		System.out.println("SetYellowPlayerStatus = "+user_type);
		
		if(user_type.equals("computer"))
		{
			RadioButtonYellow2.setChecked(true);
		}else if(user_type.equals("unavailable"))
		{
			RadioButtonYellow4.setChecked(true);
	
		}else if(user_type.equals("nobody"))
		{
			RadioButtonYellow3.setChecked(true);
	
		}else if(user_type.equals("human"))
		{
			RadioButtonYellow1.setChecked(true);
			RadioButtonYellow1.setText(user_name);
	
		}
	
		if((!ishost) && (!user_type.equals("nobody")))
			{
				RadioGroupYellowLock = true;
			}else
			{
				RadioGroupYellowLock = false;
			}
	
}	


static void SetHostvalue(boolean hostvalue) {

		ishost = hostvalue;
		if(ishost == true)
			{
				System.out.println("SetHostvalue to True ");
			}
		else
			{
				System.out.println("SetHostvalue to false ");			
			}

}

static void NeedToUpdate() {

		updatestatus = true;

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
