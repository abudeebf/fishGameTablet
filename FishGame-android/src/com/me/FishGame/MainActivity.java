

	package com.me.FishGame;

	import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

	import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

	public class MainActivity extends AndroidApplication {
	    @SuppressLint("NewApi")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	   //     requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().getDecorView().setSystemUiVisibility(8);
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// to make the screen active all the time
	        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
	        cfg.useGL20 = false;
	       

	        initialize(new FishGame(), cfg);
	    }
	    @SuppressLint("NewApi")
		public void onResume(Bundle savedInstanceState) {
	    	 getWindow().getDecorView().setSystemUiVisibility(8);
	    }
	}


