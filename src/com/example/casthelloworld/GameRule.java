package com.example.casthelloworld;

import java.lang.reflect.Field;

import org.json.JSONException;

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.cast.exceptions.NoConnectionException;
import com.google.sample.castcompanionlibrary.cast.exceptions.TransientNetworkDisconnectionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameRule extends ActionBarActivity{
	
    private static final String TAG = "GameRule";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_rule);
		Log.d(TAG, "onCreate() was called");
		TextView textView = (TextView)findViewById(R.id.GameRule_Text);   
		textView.setMovementMethod(ScrollingMovementMethod.getInstance()); 

	}
	
    @Override
	protected void onResume() {
		Log.d(TAG, "onResume() was called");
	
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop() was called");
		super.onStop();
	}


	@Override
	protected void onPause() {
		Log.d(TAG, "onPause() was called");
		super.onPause();
	}

	

	@Override
	protected void onDestroy() {
		Log.d(TAG, "PlayGame onDestroy() is called");
		super.onDestroy();
	}

}
