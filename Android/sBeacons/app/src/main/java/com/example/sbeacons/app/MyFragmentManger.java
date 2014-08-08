package com.example.sbeacons.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by a.dewan on 6/10/14.
 */
public class MyFragmentManger extends FragmentPagerAdapter {

    public MyFragmentManger(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
         MyFragments fragment = new MyFragments();
         fragment.setTabSelection(position);
//                     Bundle args = new Bundle();
//                     args.putInt(MyFragments.ARG_SECTION_NUMBER, i + 1);
//                     fragment.setArguments(args);
         return fragment;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
       switch (position){
           case 0: return "Home";
           case 1: return "Map";
           case 2: return "Graph";
       }
        return null;
    }
}
