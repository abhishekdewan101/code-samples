package com.example.photosharedemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class MyActivity extends Activity {

    String USER_ID = Build.MODEL+Build.SERIAL;
    String SHARE_MANAGER ="http://4607d262.ngrok.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type != null){
            handleSingleFile(intent);
        }else if(Intent.ACTION_SEND_MULTIPLE.equals(action) && type !=null){
            handMultipleFile(intent);
        }
    }

    private void handleSingleFile(final Intent intent){
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                   if (imageUri != null) {
                       HttpClient httpClient = new DefaultHttpClient();

                       HttpPost httpPost = new HttpPost("http://4607d262.ngrok.com/users");
                       File file = new File(getPath(imageUri));

                       Log.e("File Value",file+"");

                       MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                       builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                       FileBody fileBody = new FileBody(file);
                       builder.addPart("users[image]", fileBody);
                       builder.addPart("users[username]", new StringBody("Abhishek"));
                       builder.addPart("users[major]", new StringBody("100"));
                       builder.addPart("users[minor]", new StringBody("1"));
                       builder.addPart("users[share]", new StringBody("true"));
                       builder.addPart("users[userid]", new StringBody(USER_ID));

                       HttpEntity entity = builder.build();

                       httpPost.setEntity(entity);
                       httpClient.execute(httpPost);
                   }
               }catch(IOException e){
                   e.printStackTrace();
               }
           }
       }).start();
    }

    private void handleSingleFile(final Uri uri){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Uri imageUri = uri;
                    if (imageUri != null) {
                        HttpClient httpClient = new DefaultHttpClient();

                        HttpPost httpPost = new HttpPost("http://4607d262.ngrok.com/users");
                        File file = new File(getPath(imageUri));

                        Log.e("File Value",file+"");

                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                        FileBody fileBody = new FileBody(file);
                        builder.addPart("users[image]", fileBody);
                        builder.addPart("users[username]", new StringBody("Abhishek"));
                        builder.addPart("users[major]", new StringBody("100"));
                        builder.addPart("users[minor]", new StringBody("1"));
                        builder.addPart("users[share]", new StringBody("true"));
                        builder.addPart("users[userid]", new StringBody(USER_ID));

                        HttpEntity entity = builder.build();

                        httpPost.setEntity(entity);
                        httpClient.execute(httpPost);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handMultipleFile(Intent intent){
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if(imageUris!=null){
           for(int i=0;i<imageUris.size();i++){
               Uri tempUri = imageUris.get(i);
               handleSingleFile(tempUri);
           }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
