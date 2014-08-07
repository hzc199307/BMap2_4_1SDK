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
	private boolean isLocating = false;//是否正在定位
	private boolean isFirstLocation = true;//是否首次定位
	private boolean locationOn = false;//是否启用定位  默认没开

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
//			mMapView = bmapFragment.getMapView();//必须在onCreateView之后获取
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
	 * 设置有关overlay的内容
	 * @param listOverlayItems  标注列表
	 * @param onTapListener   标注点击事件
	 * @param PopupOverlayStatus  弹窗有无
	 * @param popupClickListener   弹窗点击事件
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
	 * 使屏幕刚好展示所有的overlay
	 * @param msTime  动画时间 毫秒为单位
	 */
	public void showOverlaySpanWithAnimation(int msTime)
	{
		mBMapOverlayUtil.showAll();
		mBMapOverlayUtil.showSpanWithAnimation(msTime);
	}
	/**
	 * 使屏幕刚好展示所有的overlay
	 * 动画时间1500 毫秒
	 */
	public void showOverlaySpanWithAnimation()
	{
		mBMapOverlayUtil.showAll();
		mBMapOverlayUtil.showSpanWithAnimation(1500);
	}
	
	/**
	 * 移除所有的overlay
	 */
	public void removeOverlay()
	{
		mBMapOverlayUtil.removeAll();
	}
	
	/*************定位相关*********************************************************************/
	/**
	 * 设置是否启用定位功能及其按钮 , 确保在activity的onCreate函数调用之后再调用。
	 * @param on
	 */
	public void setLocation(boolean on)
	{
		if(on==locationOn)//如果与之前状态一致 则不执行
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
	 * 定位SDK监听函数 （TODO 我打算在用户每次触碰地图之后，就定位）
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
				mapLocationBtn.setImageResource(R.drawable.location_button_return);//city_location_button.setText("返回");
				Log.v(TAG, "定位打开 ");						
			}
			mBMapLocationUtil.updateLocationData(location);
			//更新定位数据
			mBMapLocationUtil.setData();
			//是手动触发请求或首次定位时，移动到定位点
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
	 * 定位按钮
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG,"city_location_button");
		if(isLocating==false)
		{
			//					nowGeoPoint = mMapView.getMapCenter();//储存定位前的状态
			//					nowZoomLevel = mMapView.getZoomLevel();
			mBMapLocationUtil.requestLocation();//一次定位
			//mBMapLocationUtil.start();
			if(mBMapLocationUtil.isStarted())
			{
				Toast.makeText(getActivity(), "正在定位……", Toast.LENGTH_SHORT).show();
				isLocating = true;
				mapLocationBtn.setImageResource(R.drawable.location_button_return);
				//city_location_button.setText("返回");
				Log.v(TAG, "定位打开 ");						
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
				Toast.makeText(getActivity(), "返回景点位置……", Toast.LENGTH_SHORT).show();
				//				mOverlayUtil.showSpan();
				//						mMapController.setCenter(nowGeoPoint);//设置地图中心点：上一次的位置
				//						mMapController.setZoom(nowZoomLevel);//设置地图缩放级别
				mapLocationBtn.setImageResource(R.drawable.location_button_loc);//city_location_button.setText("定位");
				Log.v(TAG, "定位关闭，返回景点位置 ");
			}
			else
			{
				Toast.makeText(getActivity(), "返回失败，请重试……", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
