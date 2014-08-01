package com.example.casthelloworld;

import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.Toast;
import android.view.GestureDetector; 

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.IVideoCastConsumer;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
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
    


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configgame);
		Log.d(TAG, "onCreate() was called");
		ActionBar actionBar = getSupportActionBar();
		mCastManager = CastApplication.getCastManager(this);
		setupActionBar(actionBar);

		mCastConsumer = new VideoCastConsumerImpl() {
			
			 @Override
			 public void onFailed(int resourceId, int statusCode) {
		
			 }
			 
			 @Override
			 public void onDataMessageReceived(String message) {
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

	

private void setupActionBar(ActionBar actionBar) {
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	//getSupportActionBar().setIcon(R.drawable.actionbar_logo_castvideos);
	getSupportActionBar().setDisplayShowTitleEnabled(false);
}

	
}
