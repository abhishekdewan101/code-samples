package com.androidproject.lolita;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.widget.TextView;

public class VirtualAssistantWeather extends Activity implements TextToSpeech.OnInitListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_va_weather);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_va_weather, menu);
		return true;
	}

	@Override
	public void onInit(int status) {
	
		TextView tv= (TextView)findViewById(R.id.tempreture);
		String Text = "Its"+tv.getText().toString()+"degree celcius outside. Woah its hot!";
		
	}
	
	

}
