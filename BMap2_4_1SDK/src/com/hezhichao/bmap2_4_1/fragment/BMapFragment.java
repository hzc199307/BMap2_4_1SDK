package com.hezhichao.bmap2_4_1.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hezhichao.bmap2_4_1.R;
import com.hezhichao.bmap2_4_1.util.BMapLocationUtil;
import com.hezhichao.bmap2_4_1.util.BMapOverlayUtil;
import com.hezhichao.bmap2_4_1.util.BMapOverlayUtil.OnTapListener;


public class BMapFragment extends Fragment implements OnClickListener{

	private final static String TAG = "BMapFragment";
	private SupportMapFragment bmapFragment;
	private MapView mMapView;
	private MapController mMapController;
	private ImageButton mapLocationBtn;

	private BMapLocationUtil mBMapLocationUtil;
	private boolean isLocating = false;//�Ƿ����ڶ�λ
	private boolean isFirstLocation = true;//�Ƿ��״ζ�λ
	private boolean locationOn = false;//�Ƿ����ö�λ  Ĭ��û��

	private BMapOverlayUtil mBMapOverlayUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Log.v(TAG, "onCreateView");
		View view  = inflater.inflate(R.layout.fragment_bmap, container, false);
		mMapView = (MapView)view.findViewById(R.id.mapView);
		mMapController = mMapView.getController();
//		bmapFragment = new SupportMapFragment();
//
//		getFragmentManager().beginTransaction().add(R.id.map_fragment, bmapFragment).commit();
		mapLocationBtn = (ImageButton)view.findViewById(R.id.location_button);

		Log.v(TAG, "onCreateView");
		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		if(mMapView==null)
//			mMapView = bmapFragment.getMapView();//������onCreateView֮���ȡ
//		if(mMapController==null)
//			mMapController = mMapView.getController();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
		if(isLocating==true)
			mBMapLocationUtil.start();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
		if(mBMapLocationUtil!=null)
			mBMapLocationUtil.stop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.destroy();
		if(mBMapLocationUtil!=null)
			mBMapLocationUtil.destroy();
		if(mBMapOverlayUtil!=null)
			mBMapOverlayUtil.destroy();
	}
	
	/**
	 * �����й�overlay������
	 * @param listOverlayItems  ��ע�б�
	 * @param onTapListener   ��ע����¼�
	 * @param PopupOverlayStatus  ��������
	 * @param popupClickListener   ��������¼�
	 */
	public void setOverlayData(List<OverlayItem> listOverlayItems,OnTapListener onTapListener,boolean PopupOverlayStatus,PopupClickListener popupClickListener)
	{
		if(mBMapOverlayUtil==null)
		{
			/**BMapOverlayUtil***/
			mBMapOverlayUtil = new BMapOverlayUtil(getActivity(), mMapView);
			
			/**BMapOverlayUtil***/
		}
//		List<OverlayItem> listOverlayItems = new ArrayList<OverlayItem>();
//		OverlayItem item1,item2,item3;
//		item1 = new OverlayItem(new GeoPoint(23000000, 113000000), "1", "");
//		item2 = new OverlayItem(new GeoPoint(23500000, 113500000), "2", "");
//		item3 = new OverlayItem(new GeoPoint(23100000, 113100000), "3", "");
//		listOverlayItems.add(item1);
//		listOverlayItems.add(item2);
//		listOverlayItems.add(item3);
		mBMapOverlayUtil.setData(listOverlayItems);
//		mBMapOverlayUtil.showAll();
//		mBMapOverlayUtil.showSpanWithAnimation(1500);
		
		mBMapOverlayUtil.setOnTapListener(onTapListener);
		mBMapOverlayUtil.setPopupOverlayStatus(PopupOverlayStatus);
		mBMapOverlayUtil.setPopupClickListener(popupClickListener);
	}
	
	/**
	 * ʹ��Ļ�պ�չʾ���е�overlay
	 * @param msTime  ����ʱ�� ����Ϊ��λ
	 */
	public void showOverlaySpanWithAnimation(int msTime)
	{
		mBMapOverlayUtil.showAll();
		mBMapOverlayUtil.showSpanWithAnimation(msTime);
	}
	/**
	 * ʹ��Ļ�պ�չʾ���е�overlay
	 * ����ʱ��1500 ����
	 */
	public void showOverlaySpanWithAnimation()
	{
		mBMapOverlayUtil.showAll();
		mBMapOverlayUtil.showSpanWithAnimation(1500);
	}
	
	/**
	 * �Ƴ����е�overlay
	 */
	public void removeOverlay()
	{
		mBMapOverlayUtil.removeAll();
	}
	
	/*************��λ���*********************************************************************/
	/**
	 * �����Ƿ����ö�λ���ܼ��䰴ť , ȷ����activity��onCreate��������֮���ٵ��á�
	 * @param on
	 */
	public void setLocation(boolean on)
	{
		if(on==locationOn)//�����֮ǰ״̬һ�� ��ִ��
			return;
		if(on)
		{
			mapLocationBtn.setVisibility(View.VISIBLE);
			mapLocationBtn.setOnClickListener(this);
			if(mBMapLocationUtil==null)
				mBMapLocationUtil = new BMapLocationUtil(getActivity(), new MyBDLocationListenner(),mMapView);
		}
		else
		{
			mapLocationBtn.setVisibility(View.GONE);
			if(mBMapLocationUtil!=null)
			{
				mBMapLocationUtil.destroy();
				mBMapLocationUtil=null;
			}
		}
		locationOn = on;
	}

	/**
	 * ��λSDK�������� ��TODO �Ҵ������û�ÿ�δ�����ͼ֮�󣬾Ͷ�λ��
	 */
	private class MyBDLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Toast.makeText(getActivity(), "onReceiveLocation", Toast.LENGTH_SHORT).show();
			if (location == null)
				return ;
			if(isFirstLocation)
			{
				isLocating = true;
				mapLocationBtn.setImageResource(R.drawable.location_button_return);//city_location_button.setText("����");
				Log.v(TAG, "��λ�� ");						
			}
			mBMapLocationUtil.updateLocationData(location);
			//���¶�λ����
			mBMapLocationUtil.setData();
			//���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if(isFirstLocation)
			{
				mMapController.animateTo(new GeoPoint((int)(location.getLatitude()* 1e6), (int)(location.getLongitude() *  1e6)));
				isFirstLocation = false;
			}	
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
		}
	}

	/**
	 * ��λ��ť
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG,"city_location_button");
		if(isLocating==false)
		{
			//					nowGeoPoint = mMapView.getMapCenter();//���涨λǰ��״̬
			//					nowZoomLevel = mMapView.getZoomLevel();
			mBMapLocationUtil.requestLocation();//һ�ζ�λ
			//mBMapLocationUtil.start();
			if(mBMapLocationUtil.isStarted())
			{
				Toast.makeText(getActivity(), "���ڶ�λ����", Toast.LENGTH_SHORT).show();
				isLocating = true;
				mapLocationBtn.setImageResource(R.drawable.location_button_return);
				//city_location_button.setText("����");
				Log.v(TAG, "��λ�� ");						
			}
			//mLocClient.requestLocation();	

		}
		else
		{
			mBMapLocationUtil.stop();
			isFirstLocation = true;
			if(mBMapLocationUtil.isStarted()==false)
			{
				isLocating = false;
				Toast.makeText(getActivity(), "���ؾ���λ�á���", Toast.LENGTH_SHORT).show();
				//				mOverlayUtil.showSpan();
				//						mMapController.setCenter(nowGeoPoint);//���õ�ͼ���ĵ㣺��һ�ε�λ��
				//						mMapController.setZoom(nowZoomLevel);//���õ�ͼ���ż���
				mapLocationBtn.setImageResource(R.drawable.location_button_loc);//city_location_button.setText("��λ");
				Log.v(TAG, "��λ�رգ����ؾ���λ�� ");
			}
			else
			{
				Toast.makeText(getActivity(), "����ʧ�ܣ������ԡ���", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
