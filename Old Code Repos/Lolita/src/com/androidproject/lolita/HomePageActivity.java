package com.androidproject.lolita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomePageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.id.add_reminders:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			return true;
		case R.id.add_profiles:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			return true;
		case R.id.add_friends:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			return true;
		case R.id.open_virtual_assistant:
			intent = new Intent(this, VirtualAssistantMain.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
