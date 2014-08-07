package com.hezhichao.bmap2_4_1.util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;


/**
 * 百度地图 离线地图工具类 （基于baiduMap SDK V2.4.1）
 * @ClassName: BMapOfflineMapUtil 
 * @author 贺智超
 * @Description: TODO 
 * @date 2014年7月25日 下午2:41:44
 */

public class BMapOfflineMapUtil {

	private MKOfflineMap mOffline = null;
	private Context mContext = null;
	public MKOfflineMap getmOffline() {
		return mOffline;
	}

	public BMapOfflineMapUtil(Context mContext,MapView mMapView,MKOfflineMapListener mMKOfflineMapListener) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mOffline = new MKOfflineMap();  
		/**
		 * 初始化离线地图模块,MapControler可从MapView.getController()获取
		 */
		mOffline.init(mMapView.getController(), mMKOfflineMapListener);
	}

	/**
	 * 返回已下载的离线地图信息列表
	 */
	public ArrayList<MKOLUpdateElement> getAllUpdateInfo()
	{
		ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
		if ( localMapList == null ){
			localMapList = new ArrayList<MKOLUpdateElement>();	
		}
		return localMapList;
	}

	/**
	 * 返回某一个正在更新的 城市离线地图信息
	 */
	public MKOLUpdateElement getUpdateInfo(int cityID)
	{
		return mOffline.getUpdateInfo(cityID);
	}

	/**
	 * 搜索一个城市的地理坐标
	 */
	public GeoPoint searchGeoPoint(String cityName){
		ArrayList<MKOLUpdateElement> records = getAllUpdateInfo();
		for(int i=0;i<records.size();i++)
		{
			MKOLUpdateElement e = records.get(i);
			if(cityName.equals(e.cityName))
				return e.geoPt;
		}
		return null;
	}

	/**
	 * 不用时必须调用
	 */
	public void destroy()
	{
		if(mOffline!=null)
			mOffline.destroy();
	}

	/**
	 * 搜索离线城市的城市ID
	 */
	public int search(String cityName){
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityName);
		if (records == null || records.size() != 1)
			return -1;
		return records.get(0).cityID;
	}

	/**
	 * 开始(继续)下载
	 */
	public boolean start(int cityID){
		MKOLUpdateElement mMKOLUpdateElement;
		boolean isStart = false;
		if(cityID>0)
		{
			mMKOLUpdateElement= getUpdateInfo(cityID);
			Log.e("TAG","before start download status : "+(mMKOLUpdateElement==null?"null":mMKOLUpdateElement.status));
			//没有完成才下载
			if(mMKOLUpdateElement!=null&&mMKOLUpdateElement.status == MKOLUpdateElement.FINISHED)
				;
			else
			{
				mOffline.start(cityID);
				Toast.makeText( mContext,"开始下载离线地图. cityid: "+cityID, Toast.LENGTH_SHORT)
				.show();
				isStart = true;
			}
			mMKOLUpdateElement= getUpdateInfo(cityID);
			Log.e("TAG","after start download status : "+(mMKOLUpdateElement==null?"null":mMKOLUpdateElement.status));
		}
		return isStart;	
	}

	/**
	 * 暂停下载
	 */
	public boolean pause(int cityID){
		boolean isPause = false;
		MKOLUpdateElement mMKOLUpdateElement;
		Log.e("TAG","before stop download status : "+(getUpdateInfo(cityID)==null?"null":getUpdateInfo(cityID).status));
		mMKOLUpdateElement= getUpdateInfo(cityID);
		if(mMKOLUpdateElement!=null&&mMKOLUpdateElement.status != MKOLUpdateElement.FINISHED)//如果完成之后还暂停 会再次下载
		{
			mOffline.pause(cityID);
			Toast.makeText(mContext, "暂停下载离线地图. cityid: "+cityID, Toast.LENGTH_SHORT).show();
			isPause = true;
		}
		Log.e("TAG","after stop download status : "+(getUpdateInfo(cityID)==null?"null":getUpdateInfo(cityID).status));
		return isPause;
	}

	/**
	 * 删除离线地图
	 * @param view
	 */
	public boolean remove(int cityID){
		Toast.makeText(mContext, "删除离线地图. cityid: "+cityID, Toast.LENGTH_SHORT).show();
		return mOffline.remove(cityID);
	}

	/**
	 * 从SD卡导入离线地图安装包
	 */
	public void importFromSDCard(){
		int num = mOffline.scan();
		String msg = "";
		if ( num == 0){
			msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
		}
		else{
			msg = String.format("成功导入 %d 个离线包，可以在下载管理查看",num);	
		}
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}