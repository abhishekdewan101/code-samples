package com.androidproject.lolita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.androidapplication.lolita.MESSAGE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        closeOptionsMenu();
        return true;
    }
    
    
    
    public void addLocation(View view)
    {
    	Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
    }
    
    public void showHomePage(View view)
    {
    	Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
