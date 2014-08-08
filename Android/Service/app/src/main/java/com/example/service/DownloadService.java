package com.example.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by a.dewan on 7/23/14.
 */
public class DownloadService extends IntentService implements IBeaconConsumer {

    private int result = Activity.RESULT_CANCELED;
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";
    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    Boolean somethingFound = false;
    public DownloadService(){
        super("DownloadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
             Log.e("Error","Service Started");
             iBeaconManager.bind(this);
//           String status = intent.getExtras().getString("status");
//           Calendar cal = Calendar.getInstance();
//           status = "Service Added Componenet at"+cal.getTime();
//           publishResults(status);
            while(!somethingFound){

            }
    }

    @Override
    public void onDestroy(){
            Log.e("Error","On Destroy called");
            super.onDestroy();
            iBeaconManager.unBind(this);
    }

    @Override
    public void onIBeaconServiceConnect() {
        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                publishResults(iBeacons);
            }
        });

        try{
            iBeaconManager.startRangingBeaconsInRegion(new Region("myUniqueID",null,null,null));
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void publishResults(Collection<IBeacon> iBeacons){
        Intent intent = new Intent(NOTIFICATION);
        String status ="";
        Iterator<IBeacon> iBeaconIterator = iBeacons.iterator();
        while(iBeaconIterator.hasNext()){
            IBeacon tempBeacon = iBeaconIterator.next();
            status = status+tempBeacon.getMajor()+" - "+tempBeacon.getMinor()+" - "+tempBeacon.getAccuracy()+"\n";
        }
        intent.putExtra("status",status);
        Log.e("Error", "sendingBroadcast");
        somethingFound =true;
        sendBroadcast(intent);

    }
}
