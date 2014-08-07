package com.hezhichao.bmap2_4_1.util;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 百度地图 工具 （基于baiduMap SDK V2.4.1）
 * @ClassName: BMapUtil 
 * @author 贺智超
 * @Description: TODO 
 * @date 2014年7月25日 下午2:37:01
 */
public class BMapUtil {

	private final static String TAG = "BMapUtil";
	private static Context mContext;
	private static BMapManager mBMapManager;

	/*
	 * 初始化BMap 否则地图无法使用  上下文参数mContext请传入getApplicationContext()
	 */
	public static void initBMapManager(Context context) {
		mContext = context;
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(mContext);
			if (!mBMapManager.init(new MyGeneralListener())) {
				Toast.makeText(mContext, 
						"百度地图初始化错误!", Toast.LENGTH_LONG).show();
			}
			mBMapManager.start();
		}
		Log.v(TAG, "initEngineManager");
	}

	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(mContext, "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			}
			else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(mContext, "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			//非零值表示key验证未通过
			if (iError != 0) {
				//授权Key错误：
				Toast.makeText(mContext, 
						"请输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(mContext, 
						"Key认证成功", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 将View转成Bitmap
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;

	}
	
	/**
	 * 获取geoPoints中心点和经纬度范围
	 * @param geoPoints
	 * @return GeoPoint[0] geoPoints中心点的坐标     GeoPoint[1] geoPoints的经纬度范围
	 */
	public static GeoPoint[] getCenterAndSpan(GeoPoint[] geoPoints)
	{
		RouteOverlay routeOverlay = new RouteOverlay(null, null);
		MKRoute route = new MKRoute();
		route.customizeRoute(geoPoints[0], geoPoints[geoPoints.length-1], geoPoints);	
		routeOverlay.setData(route);
		GeoPoint[] center_span = new GeoPoint[2];
		center_span[0] = new GeoPoint(routeOverlay.getCenter().getLatitudeE6(), routeOverlay.getCenter().getLongitudeE6());
		center_span[1] = new GeoPoint(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
		return center_span;
	}

}
