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
 * �ٶȵ�ͼ ���ߵ�ͼ������ ������baiduMap SDK V2.4.1��
 * @ClassName: BMapOfflineMapUtil 
 * @author ���ǳ�
 * @Description: TODO 
 * @date 2014��7��25�� ����2:41:44
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
		 * ��ʼ�����ߵ�ͼģ��,MapControler�ɴ�MapView.getController()��ȡ
		 */
		mOffline.init(mMapView.getController(), mMKOfflineMapListener);
	}

	/**
	 * ���������ص����ߵ�ͼ��Ϣ�б�
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
	 * ����ĳһ�����ڸ��µ� �������ߵ�ͼ��Ϣ
	 */
	public MKOLUpdateElement getUpdateInfo(int cityID)
	{
		return mOffline.getUpdateInfo(cityID);
	}

	/**
	 * ����һ�����еĵ�������
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
	 * ����ʱ�������
	 */
	public void destroy()
	{
		if(mOffline!=null)
			mOffline.destroy();
	}

	/**
	 * �������߳��еĳ���ID
	 */
	public int search(String cityName){
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityName);
		if (records == null || records.size() != 1)
			return -1;
		return records.get(0).cityID;
	}

	/**
	 * ��ʼ(����)����
	 */
	public boolean start(int cityID){
		MKOLUpdateElement mMKOLUpdateElement;
		boolean isStart = false;
		if(cityID>0)
		{
			mMKOLUpdateElement= getUpdateInfo(cityID);
			Log.e("TAG","before start download status : "+(mMKOLUpdateElement==null?"null":mMKOLUpdateElement.status));
			//û����ɲ�����
			if(mMKOLUpdateElement!=null&&mMKOLUpdateElement.status == MKOLUpdateElement.FINISHED)
				;
			else
			{
				mOffline.start(cityID);
				Toast.makeText( mContext,"��ʼ�������ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT)
				.show();
				isStart = true;
			}
			mMKOLUpdateElement= getUpdateInfo(cityID);
			Log.e("TAG","after start download status : "+(mMKOLUpdateElement==null?"null":mMKOLUpdateElement.status));
		}
		return isStart;	
	}

	/**
	 * ��ͣ����
	 */
	public boolean pause(int cityID){
		boolean isPause = false;
		MKOLUpdateElement mMKOLUpdateElement;
		Log.e("TAG","before stop download status : "+(getUpdateInfo(cityID)==null?"null":getUpdateInfo(cityID).status));
		mMKOLUpdateElement= getUpdateInfo(cityID);
		if(mMKOLUpdateElement!=null&&mMKOLUpdateElement.status != MKOLUpdateElement.FINISHED)//������֮����ͣ ���ٴ�����
		{
			mOffline.pause(cityID);
			Toast.makeText(mContext, "��ͣ�������ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT).show();
			isPause = true;
		}
		Log.e("TAG","after stop download status : "+(getUpdateInfo(cityID)==null?"null":getUpdateInfo(cityID).status));
		return isPause;
	}

	/**
	 * ɾ�����ߵ�ͼ
	 * @param view
	 */
	public boolean remove(int cityID){
		Toast.makeText(mContext, "ɾ�����ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT).show();
		return mOffline.remove(cityID);
	}

	/**
	 * ��SD���������ߵ�ͼ��װ��
	 */
	public void importFromSDCard(){
		int num = mOffline.scan();
		String msg = "";
		if ( num == 0){
			msg = "û�е������߰�������������߰�����λ�ò���ȷ�������߰��Ѿ������";
		}
		else{
			msg = String.format("�ɹ����� %d �����߰������������ع���鿴",num);	
		}
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}