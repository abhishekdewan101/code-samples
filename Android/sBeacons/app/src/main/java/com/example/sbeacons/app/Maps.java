package com.example.sbeacons.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by a.dewan on 6/12/14.
 */
public class Maps extends RelativeLayout {

    public Maps(Context context,AttributeSet attr) {
        super(context, attr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.maps, this);
    }
}
