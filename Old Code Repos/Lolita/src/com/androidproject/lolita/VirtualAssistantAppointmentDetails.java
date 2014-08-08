package com.androidproject.lolita;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class VirtualAssistantAppointmentDetails extends Activity {
	public static final String appointmentName ="New Appointment";
	public static final String newAppointment = "true";
	public static final String date ="New Appointment";
	public static final String firstName ="New Appointment";
	public static final String secondName ="New Appointment";
	public static final String address1 ="New Appointment";
	public static final String address2 ="New Appointment";
	public static final String distance ="New Appointment";
	public static final String traffic = "New Appointment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_va_appointment_details);
		
		Bundle extra = getIntent().getExtras();
		if(extra.getBoolean(newAppointment)==false){
		findViewById(R.id.save).setVisibility(View.GONE);
		}
		TextView tv = (TextView)findViewById(R.id.title);
		tv.setText(extra.getString(appointmentName));
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_va_appointment_details, menu);
		return true;
	}

}
