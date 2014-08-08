package com.example.sbeacons.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by a.dewan on 6/10/14.
 */
public class MyFragments extends Fragment {

    public static final String SECTION_NUMBER = "section";
    public int tabSelection = 0;


    public int getTabSelection() {
        return tabSelection;
    }

    public void setTabSelection(int tabSelection) {
        this.tabSelection = tabSelection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       Log.w("tabselection",tabSelection+"");
       switch(tabSelection){
           case 0:   View homeView = inflater.inflate(R.layout.home, container, false);
//                   Bundle args = getArguments();
//                   ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                   getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
                     return homeView;

           case 1:   View mapView = inflater.inflate(R.layout.mapview, container, false);
//                   Bundle args = getArguments();
//                   ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                   getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
                     return mapView;

           case 2: View webView = inflater.inflate(R.layout.web,container,false);
                   return webView;
       }
        return null;
    }
}
