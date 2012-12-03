package org.lukang.demo.map;

import org.lukang.demo.R;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;


public class ItemizedOverlayDemo extends MapActivity {
	
	LocationListener mLocationListener = null;
	
	MKLocationManager mLocationManager = null;
	
	MyLocationOverlay mLocationOverlay = null;	//定位图层
	
	ItemizedOverlayDemo instance = null;
	
	static View mPopView = null;	
	static MapView mMapView = null;
	int iZoom = 0;
	OverItemT overitem = null;
	
	GeoPoint mygp = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewdemo);
        
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BaiduMapApiApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        super.initMapActivity(app.mBMapMan);
        
        instance = this;
        
     
     // 注册定位事件
        mLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				if (location != null){
					GeoPoint pt = new GeoPoint((int)(location.getLatitude()*1e6),
							(int)(location.getLongitude()*1e6));
					mygp = pt;
					mMapView.getController().animateTo(pt);
					Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  
					marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
							.getIntrinsicHeight());   
					
					overitem = new OverItemT(marker, instance,mygp);
					mMapView.getOverlays().add(overitem); 
					
					mPopView=instance.getLayoutInflater().inflate(R.layout.popview, null);
					
					mMapView.addView( mPopView,
			                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
			                		null, MapView.LayoutParams.TOP_LEFT));
					mPopView.setVisibility(View.GONE);
					iZoom = mMapView.getZoomLevel();
				}
			}
        };
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setDrawOverlayWhenZooming(true);
        
     // 初始化Location模块
        mLocationManager = app.mBMapMan.getLocationManager();
        // 通过enableProvider和disableProvider方法，选择定位的Provider
        // mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
        // mLocationManager.disableProvider(MKLocationManager.MK_GPS_PROVIDER);
        // 添加定位图层
        mLocationOverlay = new MyLocationOverlay(this, mMapView);
        mLocationOverlay.enableMyLocation();
        
        mMapView.getOverlays().add(mLocationOverlay);

	}

	@Override
	protected void onPause() {
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		app.getmBMapMan().getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableCompass(); // 关闭指南针
		if(app.mBMapMan != null)
			app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		app.getmBMapMan().getLocationManager().requestLocationUpdates(mLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // 打开指南针
		app.mBMapMan.start();
		super.onResume();
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}


