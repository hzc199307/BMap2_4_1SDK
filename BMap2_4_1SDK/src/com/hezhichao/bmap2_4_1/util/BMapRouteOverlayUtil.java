package com.hezhichao.bmap2_4_1.util;

import android.app.Activity;
import android.content.Context;

import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 百度地图 路线绘制 工具 （基于baiduMap SDK V2.4.1）
 * @ClassName: BMapRouteOverlayUtil 
 * @author 贺智超
 * @Description: TODO 
 * @date 2014年7月25日 下午2:38:40
 */
public class BMapRouteOverlayUtil {

	private RouteOverlay routeOverlay;
	private MapView mMapView;
	private MapController mMapController;
	
	public BMapRouteOverlayUtil(Context mContext,MapView mMapView) {
		routeOverlay = new RouteOverlay((Activity) mContext, mMapView);	
		this.mMapView = mMapView;
		mMapController = mMapView.getController();
		mMapView.refresh();
	}

	/**
	 * 设置数据
	 */
	public void setListData(GeoPoint[] geoPoints)
	{
		int size = geoPoints.length;
		MKRoute route = new MKRoute();
		route.customizeRoute(geoPoints[0], geoPoints[size-1], geoPoints);	
		routeOverlay.setData(route);
	}
	
	/**
	 * 将路线展示
	 */
	public void showAll()
	{
		mMapView.getOverlays().add(routeOverlay);
		mMapView.refresh();
	}
	
	/**
	 * 缩放地图到能容下指定的经纬度范围 并且指向所有点的中心位置
	 */
	public void showSpan()
	{
		mMapController.setCenter(routeOverlay.getCenter());//设置地图中心点
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//改变地图范围
		mMapController.zoomToSpanWithAnimation(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6(),1500);//1500ms时间来改变地图范围
	}
	
	/**
	 * 将路线移除
	 */
	public void removeAll()
	{
		mMapView.getOverlays().remove(routeOverlay);
		mMapView.refresh();
	}
}
