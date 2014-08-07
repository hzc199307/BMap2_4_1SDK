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
 * �ٶȵ�ͼ ·�߻���(����ͼ) ���� ������baiduMap SDK V2.4.1��
 * @ClassName: BMapPolylineUtil 
 * @author ���ǳ�
 * @Description: TODO 
 * @date 2014��7��25�� ����3:07:54
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
	 * ��������  
	 */
	public void setListData(GeoPoint[] geoPoints)
	{
		this.geoPoints = geoPoints;
		graphicsOverlay.setData(drawLine(geoPoints));
	}
	
	/**
     * �������� TODO ��ʽ��Ҫ����
     * @return ���߶���
     */
    private Graphic drawLine(GeoPoint[] geoPoints){
	  
	    //������
  		Geometry lineGeometry = new Geometry();

  		lineGeometry.setPolyLine(geoPoints);
  		//�趨��ʽ
  		Symbol lineSymbol = new Symbol();
  		Symbol.Color lineColor = lineSymbol.new Color();
  		lineColor.red = 255;
  		lineColor.green = 0;
  		lineColor.blue = 0;
  		lineColor.alpha = 255;
  		lineSymbol.setLineSymbol(lineColor, 10);
  		//����Graphic����
  		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
  		return lineGraphic;
    }
    
    /**
	 * ��·��չʾ
	 */
	public void showAll()
	{
		mMapView.getOverlays().add(graphicsOverlay);
		mMapView.refresh();
	}
	
	/**
	 * ���ŵ�ͼ��������ָ���ľ�γ�ȷ�Χ ����ָ�����е������λ��
	 */
	public void showSpan()
	{
		GeoPoint[] gp = BMapUtil.getCenterAndSpan(geoPoints);
		mMapController.setCenter(gp[0]);//���õ�ͼ���ĵ�
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//�ı��ͼ��Χ
		mMapController.zoomToSpanWithAnimation(gp[1].getLatitudeE6(), gp[1].getLongitudeE6(),1500);//1500msʱ�����ı��ͼ��Χ
	}
	
	/**
	 * ��·���Ƴ�
	 */
	public void removeAll()
	{
		mMapView.getOverlays().remove(graphicsOverlay);
		mMapView.refresh();
	}
}

