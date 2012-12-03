package org.lukang.demo.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;

public class OverItemT extends ItemizedOverlay<OverlayItem> {

	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context mContext;

	public OverItemT(Drawable marker, Context context,GeoPoint mygp) {
		super(boundCenterBottom(marker));

		this.marker = marker;
		this.mContext = context;
		
		mGeoList.add(new OverlayItem(mygp, "我的地点", "我的书"));
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
