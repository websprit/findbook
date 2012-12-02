package org.lukang.demo.map;

import java.util.ArrayList;
import java.util.List;

import org.lukang.demo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.baidu.mapapi.*;


public class ItemizedOverlayDemo extends MapActivity {
	
	LocationListener mLocationListener = null;
	
	static View mPopView = null;	
	static MapView mMapView = null;
	int iZoom = 0;
	OverItemT overitem = null;
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

     // 注册定位事件
        mLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				if (location != null){
					GeoPoint pt = new GeoPoint((int)(location.getLatitude()*1e6),
							(int)(location.getLongitude()*1e6));
					mMapView.getController().animateTo(pt);
				}
			}
        };
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setDrawOverlayWhenZooming(true);

//        mMapView.getController().setCenter(pt);
  
		Drawable marker = getResources().getDrawable(R.drawable.iconmarka);  
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());   
		
		overitem = new OverItemT(marker, this, 3);
		mMapView.getOverlays().add(overitem); 
		
		mPopView=super.getLayoutInflater().inflate(R.layout.popview, null);
		mMapView.addView( mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		null, MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);
		iZoom = mMapView.getZoomLevel();
		
		

	}

	@Override
	protected void onPause() {
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		app.getmBMapMan().getLocationManager().removeUpdates(mLocationListener);
		if(app.mBMapMan != null)
			app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BaiduMapApiApp app = (BaiduMapApiApp)this.getApplication();
		app.getmBMapMan().getLocationManager().requestLocationUpdates(mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
class OverItemT extends ItemizedOverlay<OverlayItem> {

	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context mContext;

	private double mLat1 = 39.90923; // point1γ��
	private double mLon1 = 116.357428; // point1����

	private double mLat2 = 39.90923;
	private double mLon2 = 116.397428;

	private double mLat3 = 39.90923;
	private double mLon3 = 116.437428;

	public OverItemT(Drawable marker, Context context, int count) {
		super(boundCenterBottom(marker));

		this.marker = marker;
		this.mContext = context;

		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		
		mGeoList.add(new OverlayItem(p1, "P1", "point1"));
		mGeoList.add(new OverlayItem(p2, "P2", "point2"));
		if(count == 3)
		{
			GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
			mGeoList.add(new OverlayItem(p3, "P3", "point3"));
		}
		populate();  
	}

	public void updateOverlay()
	{
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { 
			OverlayItem overLayItem = getItem(index); 

			String title = overLayItem.getTitle();
			Point point = projection.toPixels(overLayItem.getPoint(), null); 

			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x-30, point.y, paintText);
		}

		super.draw(canvas, mapView, shadow);
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mGeoList.size();
	}
	@Override
	protected boolean onTap(int i) {
		setFocus(mGeoList.get(i));
		GeoPoint pt = mGeoList.get(i).getPoint();
		ItemizedOverlayDemo.mMapView.updateViewLayout( ItemizedOverlayDemo.mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		pt, MapView.LayoutParams.BOTTOM_CENTER));
		ItemizedOverlayDemo.mPopView.setVisibility(View.VISIBLE);
		Toast.makeText(this.mContext, mGeoList.get(i).getSnippet(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		ItemizedOverlayDemo.mPopView.setVisibility(View.GONE);
		return super.onTap(arg0, arg1);
	}
}

