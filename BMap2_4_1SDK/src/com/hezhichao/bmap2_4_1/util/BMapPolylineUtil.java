package com.hezhichao.bmap2_4_1.util;

import android.content.Context;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 百度地图 路线绘制(折线图) 工具 （基于baiduMap SDK V2.4.1）
 * @ClassName: BMapPolylineUtil 
 * @author 贺智超
 * @Description: TODO 
 * @date 2014年7月25日 下午3:07:54
 */
public class BMapPolylineUtil {

	private GraphicsOverlay graphicsOverlay;
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint[] geoPoints;
	
	public BMapPolylineUtil(Context mContext,MapView mMapView) {
		this.mMapView = mMapView;
		mMapController = mMapView.getController();
		graphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.refresh();
	}
	
	/**
	 * 设置数据  
	 */
	public void setListData(GeoPoint[] geoPoints)
	{
		this.geoPoints = geoPoints;
		graphicsOverlay.setData(drawLine(geoPoints));
	}
	
	/**
     * 绘制折线 TODO 样式需要调整
     * @return 折线对象
     */
    private Graphic drawLine(GeoPoint[] geoPoints){
	  
	    //构建线
  		Geometry lineGeometry = new Geometry();

  		lineGeometry.setPolyLine(geoPoints);
  		//设定样式
  		Symbol lineSymbol = new Symbol();
  		Symbol.Color lineColor = lineSymbol.new Color();
  		lineColor.red = 255;
  		lineColor.green = 0;
  		lineColor.blue = 0;
  		lineColor.alpha = 255;
  		lineSymbol.setLineSymbol(lineColor, 10);
  		//生成Graphic对象
  		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
  		return lineGraphic;
    }
    
    /**
	 * 将路线展示
	 */
	public void showAll()
	{
		mMapView.getOverlays().add(graphicsOverlay);
		mMapView.refresh();
	}
	
	/**
	 * 缩放地图到能容下指定的经纬度范围 并且指向所有点的中心位置
	 */
	public void showSpan()
	{
		GeoPoint[] gp = BMapUtil.getCenterAndSpan(geoPoints);
		mMapController.setCenter(gp[0]);//设置地图中心点
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//改变地图范围
		mMapController.zoomToSpanWithAnimation(gp[1].getLatitudeE6(), gp[1].getLongitudeE6(),1500);//1500ms时间来改变地图范围
	}
	
	/**
	 * 将路线移除
	 */
	public void removeAll()
	{
		mMapView.getOverlays().remove(graphicsOverlay);
		mMapView.refresh();
	}
}

