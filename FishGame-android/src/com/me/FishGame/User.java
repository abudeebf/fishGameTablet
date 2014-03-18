package com.me.FishGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class User extends Activity {
	public final static String INIT = "initial";
	public final static String AGE = "age";
	public final static String MODE = "mode";
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
      
    }
	public void startPlaying(View view) {
		String init=((EditText) (findViewById(R.id.init))).getText().toString().trim();
		String age=((EditText) (findViewById(R.id.age))).getText().toString().trim();
if (init.length()>0 && age.length()>0)
{
	Intent intent = new Intent(this, SplashScreen.class);
	intent.putExtra(INIT, init);
	intent.putExtra(AGE, age);
	boolean checked = ((RadioButton) findViewById(R.id.radio_audio)).isChecked();
    if (checked)
	intent.putExtra(MODE,"Audio" );
    else
    	intent.putExtra("mode", "Visual");
        startActivity(intent);
        finish();

}
else{
AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

dlgAlert.setMessage("both initail and age are required");
dlgAlert.setTitle("Please fill all the information");
dlgAlert.setPositiveButton("OK", null);
dlgAlert.setCancelable(false);
dlgAlert.create().show();
}
	}

}
