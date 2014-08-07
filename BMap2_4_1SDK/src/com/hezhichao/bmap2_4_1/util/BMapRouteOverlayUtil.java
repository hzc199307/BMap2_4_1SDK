package com.hezhichao.bmap2_4_1.util;

import android.app.Activity;
import android.content.Context;

import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * �ٶȵ�ͼ ·�߻��� ���� ������baiduMap SDK V2.4.1��
 * @ClassName: BMapRouteOverlayUtil 
 * @author ���ǳ�
 * @Description: TODO 
 * @date 2014��7��25�� ����2:38:40
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
	 * ��������
	 */
	public void setListData(GeoPoint[] geoPoints)
	{
		int size = geoPoints.length;
		MKRoute route = new MKRoute();
		route.customizeRoute(geoPoints[0], geoPoints[size-1], geoPoints);	
		routeOverlay.setData(route);
	}
	
	/**
	 * ��·��չʾ
	 */
	public void showAll()
	{
		mMapView.getOverlays().add(routeOverlay);
		mMapView.refresh();
	}
	
	/**
	 * ���ŵ�ͼ��������ָ���ľ�γ�ȷ�Χ ����ָ�����е������λ��
	 */
	public void showSpan()
	{
		mMapController.setCenter(routeOverlay.getCenter());//���õ�ͼ���ĵ�
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//�ı��ͼ��Χ
		mMapController.zoomToSpanWithAnimation(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6(),1500);//1500msʱ�����ı��ͼ��Χ
	}
	
	/**
	 * ��·���Ƴ�
	 */
	public void removeAll()
	{
		mMapView.getOverlays().remove(routeOverlay);
		mMapView.refresh();
	}
}
