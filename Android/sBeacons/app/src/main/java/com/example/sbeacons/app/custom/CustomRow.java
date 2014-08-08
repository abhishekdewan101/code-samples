package com.example.sbeacons.app.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sbeacons.app.R;

/**
 * Created by a.dewan on 6/10/14.
 */
public class CustomRow extends RelativeLayout {
    private RelativeLayout mainLayout;
    private RelativeLayout backgroundLayout;
    private RelativeLayout detailLayout;
    private Button detailButton;
    private Button cameraButton;
    private TextView beaconName;
    private TextView beaconTime;
    private TextView beaconFreq;

    private int customID;

    Activity activity;
    public CustomRow(Context context,AttributeSet attr){
        super(context,attr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.row,this);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        detailLayout = (RelativeLayout) findViewById(R.id.detailLayout);
        detailButton = (Button) findViewById(R.id.detailButton);
        beaconName = (TextView) findViewById(R.id.beaconName);
        beaconTime = (TextView) findViewById(R.id.beaconTime);
        beaconFreq = (TextView) findViewById(R.id.beaconFreq);
        cameraButton =(Button) findViewById(R.id.cameraButton);
        update();
    }

    private void update() {
        detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detailButton.setVisibility(View.GONE);
                detailLayout.setVisibility(View.VISIBLE);
                mainLayout.setBackgroundResource(R.drawable.increase);
                AnimationDrawable borderAnimation = (AnimationDrawable) mainLayout.getBackground();
                borderAnimation.start();
            }
        });

        detailLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detailButton.setVisibility(View.VISIBLE);
                detailLayout.setVisibility(View.GONE);
                mainLayout.setBackgroundResource(R.drawable.border_00);
                return false;
            }
        });

        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    activity.startActivityForResult(takePictureIntent, customID);
                }
            }
        });


    }

    public String getBeaconName(){
        return beaconName.getText().toString();
    }

    public void setBeaconName(String beaconName){
        this.beaconName.setText(beaconName);
    }

    public void setBackground(int background){
        backgroundLayout.setBackgroundResource(background);
    }

    public BitmapDrawable getDrawable(Bitmap background){
      return new BitmapDrawable(background);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public String getBeaconTime(){
        return beaconTime.getText().toString();
    }

    public void setBeaconTime(String beaconTime){
        this.beaconTime.setText(beaconTime);

    }

    public int getCustomID() {
        return customID;
    }

    public void setCustomID(int customID) {
        this.customID = customID;
    }

    public String getBeaconFreq(){
        return beaconFreq.getText().toString();
    }

    public void setBeaconFreq(String beaconFreq){
        this.beaconFreq.setText(beaconFreq);
    }
}
