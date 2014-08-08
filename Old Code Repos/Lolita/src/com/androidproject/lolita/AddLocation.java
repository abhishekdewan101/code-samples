package com.androidproject.lolita;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddLocation extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        
        /* 
         * Populate the category spinner 
         */
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.category_array, android.R.layout.simple_spinner_item);
        
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        
        /*
         * Populate the profile spinner
         */
        Spinner profileSpinner = (Spinner) findViewById(R.id.profile_spinner);
        
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
        R.array.profile_array, android.R.layout.simple_spinner_item);
        
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // Apply the adapter to the spinner
        profileSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_location, menu);
        return true;
    }
}
