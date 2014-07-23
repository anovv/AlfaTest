package com.example.alphatest;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/*
 * service to update db*/

public class UpdaterService extends Service{

	private static Thread updaterThread;
	private static volatile boolean isServiceRunning;
	private static final long REFRESH_TIME_MILLIS = 5*60*1000; 
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		update(this);
	    return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		isServiceRunning = false;
		if(updaterThread != null){
			updaterThread.interrupt();
		}
		super.onDestroy();
	}

	public IBinder onBind(Intent intent) {
	    return null;
	}
	
	private void update(final Context context){
		isServiceRunning = true;
		updaterThread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(isServiceRunning){
					List<Item> items = Utils.getItemsFromServer();
					ItemDao.getInstance(context).insertIfNotExists(items);
					
					try {
						Thread.sleep(REFRESH_TIME_MILLIS);
					} catch (InterruptedException e) {
						isServiceRunning = false;
					}
				}
			}
		});
		
		updaterThread.start();
	}
}
