package org.lukang.demo.map;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.*;

public class BaiduMapApiApp extends Application {
	
	static BaiduMapApiApp mDemoApp;
	
	//百度MapAPI的管理类
	public BMapManager mBMapMan = null;
	

	public BMapManager getmBMapMan() {
		return mBMapMan;
	}

	public void setmBMapMan(BMapManager mBMapMan) {
		this.mBMapMan = mBMapMan;
	}

	public String mStrKey = "78503B7D59702379DEABDD248E302CAB1C63641E";
	public String getmStrKey() {
		return mStrKey;
	}

	public void setmStrKey(String mStrKey) {
		this.mStrKey = mStrKey;
	}

	boolean m_bKeyRight = true;

	public static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(BaiduMapApiApp.mDemoApp.getApplicationContext(), "您的网络出错啦！",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(BaiduMapApiApp.mDemoApp.getApplicationContext(), 
						"您的key不正确",
						Toast.LENGTH_LONG).show();
				BaiduMapApiApp.mDemoApp.m_bKeyRight = false;
			}
		}
	}

	@Override
    public void onCreate() {
		Log.v("BMapApiDemoApp", "onCreate");
		mDemoApp = this;
		mBMapMan = new BMapManager(this);
		boolean isSuccess = mBMapMan.init(this.mStrKey, new MyGeneralListener());
		if (isSuccess) {
		    mBMapMan.getLocationManager().setNotifyInternal(10, 5);
		}
		else {
			
		}
		
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

}
