package com.me.FishGame;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashScreen extends Activity implements OnCompletionListener
{public static String  init;
 public static String age;
  public static String mode;
  public static String equity;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = getIntent();
		init = intent.getStringExtra(User.INIT);
		 age=(intent.getStringExtra(User.AGE));
		 mode=intent.getStringExtra(User.MODE);
		 equity=intent.getStringExtra(User.EQUITY);
		// to make the screen active all the time
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   
		String fileName = "android.resource://"+  getPackageName() +"/raw/video";
		VideoView vv = (VideoView) this.findViewById(R.id.surface);
		vv.setVideoURI(Uri.parse(fileName));
		vv.setOnCompletionListener(this);
		vv.start();

	}


	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(init, init);
		intent.putExtra(age, age);
		intent.putExtra(mode, mode);
		startActivity(intent);      
		finish();
		
	}
}