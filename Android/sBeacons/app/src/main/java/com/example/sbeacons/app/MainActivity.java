package com.example.sbeacons.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.sbeacons.app.custom.CustomRow;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    MyFragmentManger fragmentManger;
    CustomRow row1;
    CustomRow row2;
    GoogleMap maps;
    HashMap<Integer,CustomRow> beaconList = new HashMap<Integer,CustomRow>();

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManger = new MyFragmentManger(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(fragmentManger);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < fragmentManger.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(fragmentManger.getPageTitle(i))
                            .setTabListener(this));

        }
    }

    private void updateBeacons() {
        row1 = (CustomRow) findViewById(R.id.beacon1);
        row1.setBackgroundResource(R.drawable.pic1);
        row1.setBeaconFreq("Rapid");
        row1.setCustomID(1);
        beaconList.put(row1.getCustomID(),row1);
        row1.setBeaconName("Beacon 1");
        row1.setActivity(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    new Handler(getApplicationContext().getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            row1.setBeaconTime(Long.toString(new Date().getTime()));
//                        }
//                    });
//                    ;
//                }
//            }
//        }).start();


        row2 = (CustomRow) findViewById(R.id.beacon2);
        row2.setBeaconName("Beacon 2");
        row2.setCustomID(2);
        beaconList.put(row2.getCustomID(),row2);
        row2.setBackgroundResource(R.drawable.pic2);
        row2.setActivity(this);

//        WebView webView = (WebView) findViewById(R.id.webContent);
//        webView.setWebViewClient(new WebViewClient());
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        WebView.setWebContentsDebuggingEnabled(true);
//        webSettings.setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
//        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        //webView.loadUrl("http://beta.html5test.com");
//        webView.setVerticalScrollBarEnabled(true);
//        webView.setHorizontalScrollBarEnabled(true);
//        webView.setHorizontalScrollbarOverlay(true);
//        webView.setVerticalScrollbarOverlay(true);
//        //webView.loadData(htmlpage.getHTML(),"","utf-8");
//        webView.loadUrl("http://3ede8863.ngrok.com/example.html");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        updateBeacons();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

        //Home Tab

        //Map Tab
        if(tab.getText().toString().equals("Map")){
            maps = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsFrag)).getMap();
            maps.setMyLocationEnabled(true);
            maps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            maps.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).anchor(0.0f, 1.0f).position(new LatLng(41.889, -87.622)).title("This place rocks.... "));
            maps.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.889, -87.622), 16));

            maps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent graphIntent = new Intent(getApplicationContext(), Graph.class);
                    startActivity(graphIntent);
                    return false;
                }
            });
        }


       //Graph Tab
        if(tab.getText().toString().equals("Graph")){
            WebView webView = (WebView) findViewById(R.id.webContent);
           webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            WebView.setWebContentsDebuggingEnabled(true);
            webSettings.setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            //webView.loadUrl("http://beta.html5test.com");
            webView.setVerticalScrollBarEnabled(true);
            webView.setHorizontalScrollBarEnabled(true);
            webView.setHorizontalScrollbarOverlay(true);
            webView.setVerticalScrollbarOverlay(true);
            //webView.loadData(htmlpage.getHTML(),"","utf-8");
            webView.loadUrl("http://3ede8863.ngrok.com/example.html");
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){
            Bundle extras = data.getExtras();
            BitmapDrawable imageBitmap = new BitmapDrawable((Bitmap) extras.get("data"));
            beaconList.get(requestCode).setBackground(imageBitmap);
        }
    }

}
