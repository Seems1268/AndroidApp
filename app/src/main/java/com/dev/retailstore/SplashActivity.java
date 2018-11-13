package com.dev.retailstore;

/**
 * Splash screen of the application.
 * @author seemasavadi
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.dev.retailstore.main.MainActivity;

public class SplashActivity extends Activity {
	
	private static int SPLASH_TIME_OUT = 3000;
	private static final int SPLASH_STATE = 1;
	private static final int LANDING_STATE = 2;

	private static int state = SPLASH_STATE;
	
	private RelativeLayout splash_layout;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_screen);
		
		splash_layout = (RelativeLayout) findViewById(R.id.splash_layout);

		if (state == SPLASH_STATE) {
			showSplashScreen();		
		}else{
			startMainActivity();
		}
	}
	
	private void showSplashScreen(){
		splash_layout.setVisibility(View.VISIBLE);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
				state = LANDING_STATE;
				splash_layout.setVisibility(View.GONE);
				startMainActivity();
			}
		}, SPLASH_TIME_OUT);
	}
	
	private void startMainActivity(){
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
