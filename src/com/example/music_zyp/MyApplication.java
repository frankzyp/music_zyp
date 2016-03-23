package com.example.music_zyp;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.music_zyp.MusicPlayService;
import com.example.music_zyp.MusicPlayService.LocalBinder;

public class MyApplication extends Application {
	MusicPlayService mService;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = ((LocalBinder) service).getService();//用绑定方法启动service，就是从这里绑定并得到service，然后就可以操作service了
			System.out.println("1null?"+(null == mService));
			mService.setContext(getApplicationContext());
		}
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
//		Log.i("zyp", "MusicPlayService onCreate \n");
		Intent intent = new Intent(this, MusicPlayService.class);
		startService(intent);
		System.out.println("intent?"+(null == intent));
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public MusicPlayService getmService() {
//		Log.i("zyp", "MusicPlayService getmService \n");
		return mService;
	}

	public void setmService(MusicPlayService mService) {
//		Log.i("zyp", "MusicPlayService setmService \n");
		this.mService = mService;
	}

}
