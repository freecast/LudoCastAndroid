package com.example.casthelloworld;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

enum STATE {
	EXP_HEADER(0),
	EXP_BODY(1);

	private final int state;
	private STATE(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
};

public class LudoProtocol {
	private static final String TAG = "LudoProtocol";

	private static final String KEY_MAGIC = "MAGIC";
    private static final String MAGIC = "ONLINE";

    private static final String KEY_VERSION = "prot_version";
    private static final int    VERSION = 1;

    private static final String KEY_COMMAND = "command";

    private static final String COMMAND_CONNECT = "connect";
    private static final String KEY_USERNAME = "username";
    private static final String COMMAND_CONNECT_REPLY = "connect_reply";
    private static final String COMMAND_STARTGAME_NOTIFY = "startgame_notify";
    private static final String COMMAND_PICKUP_NOTIFY = "pickup_notify";
	private static final String COMMAND_ENDOFGAME_NOTIFY = "endofgame_notify";
	private static final String COMMAND_RESET_NOTIFY = "reset_notify";
	private static final String COMMAND_STARTTURN_NOTIFY = "itsyourturn_notify";
	private static final String COMMAND_CLICK_REPLY = "click_reply";
	private static final String COMMAND_NEXT_REPLY = "next_reply";
	private static final String COMMAND_PREV_REPLY = "prev_reply";	
    private static final String KEY_RET = "ret";
    private static final String KEY_ISHOST = "ishost";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_PLAYER_STATUS = "player_status";
    private static final String KEY_COLOR = "color";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_ISREADY = "isready";
	private static final String KEY_ERROR = "error";

	STATE state;
	boolean ret;
	boolean isHost;
	int level;
	PlayerStatus player_status[];

	LudoProtocol() {
		player_status = new PlayerStatus[4];
	}

	public boolean parseMessage(String msg) throws JSONException {
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
			if(ret == false)
				{
					String reason = obj.getString(KEY_ERROR);
					Log.d(TAG, "error info: "+reason);
					return false;
				}
        	boolean ishost = obj.getBoolean(KEY_ISHOST);
        	String level = obj.getString(KEY_LEVEL);
        	Log.d(TAG, "ret:" + ret + " ishost:" + ishost +
        			" level:" + level);

        	JSONArray arr = obj.getJSONArray(KEY_PLAYER_STATUS);
        	for (int i=0; i<arr.length(); i++) {
        		JSONObject o = arr.getJSONObject(i);
        		String color = o.getString(KEY_COLOR);
        		String user_type = o.getString(KEY_USER_TYPE);
        		boolean isready = o.getBoolean(KEY_ISREADY);
        		String username = o.getString(KEY_USERNAME);
        		Log.d(TAG, "player-"+i+"color"+color+
        				"user_type"+user_type+"isready"+isready+"username"+username);

				
        	}
        }else if(command.equals(COMMAND_STARTGAME_NOTIFY)){
        	
        	Log.d(TAG, "start game notify command = " + command);

        }else if(command.equals(COMMAND_PICKUP_NOTIFY)){
        	
        		JSONObject o = obj.getJSONObject(KEY_PLAYER_STATUS);
        		String color = o.getString(KEY_COLOR);
        		String user_type = o.getString(KEY_USER_TYPE);
        		boolean isready = o.getBoolean(KEY_ISREADY);
        		String username = o.getString(KEY_USERNAME);
        		Log.d(TAG, "player-"+"color"+color+
        				"user_type"+user_type+"isready"+isready+"username"+username);

				        	       	
        }else if(command.equals(COMMAND_RESET_NOTIFY)){
        	
        		PlayGame.ResetGame = true;
        		Log.d(TAG, "Reset Game");
				        	       	
        }else if(command.equals(COMMAND_ENDOFGAME_NOTIFY)){
        	
        		PlayGame.EndofGame = true;
        		Log.d(TAG, "Restart Game");
		        	       	
        }else if(command.equals(COMMAND_STARTTURN_NOTIFY)){
        	
        		PlayGame.startturn = true;
        		Log.d(TAG, "clickstatus to true");
				        	       	
        }else if(command.equals(COMMAND_CLICK_REPLY)){
        	
        		PlayGame.clickstatus = true;
        		Log.d(TAG, "clickstatus to true");
				        	       	
        }else if(command.equals(COMMAND_NEXT_REPLY)){
        	
        		PlayGame.nextstatus = true;
        		Log.d(TAG, "nextstatus to true");

				        	       	
        }else if(command.equals(COMMAND_PREV_REPLY)){
        	
        		PlayGame.prevstatus = true;
        		Log.d(TAG, "prevstatus to true");

				        	       	
        }else {
			Log.d(TAG, "unsupported key[" + KEY_COMMAND + "]=" + command);
        }
		return true;
	}

	public String genMessage_connect(String username) throws JSONException {
		JSONObject json = new JSONObject();

		genMessageHeader(json);
		json.put(KEY_COMMAND, COMMAND_CONNECT);
		json.put(KEY_USERNAME, username);

		return json.toString();
	}

	public String genMessage_setlevel(String level) throws JSONException {
		JSONObject json = new JSONObject();

		genMessageHeader(json);
		json.put(KEY_COMMAND, "setlevel");
		json.put(KEY_USERNAME, level);

		return json.toString();
	}

    public String JudgeUserType(String user_type){
    	if(user_type.equals("Computer"))
    	{	
    		return "computer";
    	}else if(user_type.equals("Null"))
    	{
    		return "unavailable"; 
    	}else if(user_type.equals("Nobody"))
    	{
    		return "nobody"; 
    	}else{
    		return "human";
    	}
    }
	
	public String genMessage_pickup(String color, String user_type) throws JSONException {
		JSONObject json = new JSONObject();
		
		String server_type = JudgeUserType(user_type);

		genMessageHeader(json);
		json.put(KEY_COMMAND, "pickup");
		json.put(KEY_COLOR, color);
		json.put(KEY_USER_TYPE, server_type);

		return json.toString();
	}
	public String genMessage_getready() throws JSONException {
		JSONObject json = new JSONObject();

		genMessageHeader(json);
		json.put(KEY_COMMAND, "getready");

		return json.toString();
	}

	public String genMessage_reset() throws JSONException {
		JSONObject json = new JSONObject();

		genMessageHeader(json);
		json.put(KEY_COMMAND, "reset");

		return json.toString();
	}


	
	public String genMessage_disready() throws JSONException {
		JSONObject json = new JSONObject();

		genMessageHeader(json);
		json.put(KEY_COMMAND, "disready");

		return json.toString();
	}
	public String genMessage_disconnect() throws JSONException {
		JSONObject json = new JSONObject();

		genMessageHeader(json);
		json.put(KEY_COMMAND, "disconnect");

		return json.toString();
	}

	private void genMessageHeader(JSONObject json) throws JSONException {
		json.put(KEY_MAGIC, MAGIC);
		json.put(KEY_VERSION, VERSION);
	}
}
