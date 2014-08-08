package com.example.webview.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class MainActivity extends ActionBarActivity {
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        String customHTML = "<!DOCTYPE html>\n" +
                "<html lang =\"en\"><head><meta charset =\"utf-8\"><title>D3 Test</title><script type=\"text/javascript\" src=\"http://d3js.org/d3.v3.min.js\"></script></head><body><script type=\"text/javascript\">var svg = d3.select(\"body\").append(\"svg\").attr(\"width\",600).attr(\"height\",600);var square = svg.append(\"rect\").attr(\"x\",-600).attr(\"y\",60).attr(\"width\",600).attr(\"height\",60); square.transition().attr(\"x\",60).duration(1000).delay(100)</script></body></html>";
      //  String htnk = "<html><body><h1>Hello, WebView</h1></body></html>";
        webview.loadData(customHTML,"text/html","UTF-8");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
