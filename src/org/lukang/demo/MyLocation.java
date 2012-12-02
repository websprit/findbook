package org.lukang.demo;

import org.lukang.demo.map.BaiduMapApiApp;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocation extends Activity {
	
	LocationListener mLocationListener = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mylocation);
        
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		if (app.getmBMapMan() == null) {
			app.setmBMapMan(new BMapManager(getApplication()));
			app.getmBMapMan().init(app.getmStrKey(), new BaiduMapApiApp.MyGeneralListener());
		}
		app.getmBMapMan().start();

        mLocationListener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					String strLog = String.format("您当前的位置:\r\n" +
							"纬度:%f\r\n" +
							"经度:%f",
							location.getLongitude(), location.getLatitude());

					TextView mainText = (TextView)findViewById(R.id.textview);
			        mainText.setText(strLog);
				}
			}
        };
	}

	@Override
	protected void onPause() {
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		app.getmBMapMan().getLocationManager().removeUpdates(mLocationListener);
		app.getmBMapMan().stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
        app.getmBMapMan().getLocationManager().requestLocationUpdates(mLocationListener);
		app.getmBMapMan().start();
		super.onResume();
	}
}
