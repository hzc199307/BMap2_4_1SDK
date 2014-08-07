package com.hezhichao.bmap2_4_1.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;

/**
 * 百度地图 定位 工具类 （基于baiduMap SDK V2.4.1  、baidu loc sdk V4.1）
 * @ClassName: BMapLocationUtil 
 * @author 贺智超
 * @Description: TODO 
 * @date 2014年7月25日 下午2:45:19
 */
public class BMapLocationUtil {

	private final static String TAG = "BMapLocationUtil";
	private Context mContext;
	private LocationClient mLocClient;
	private LocationData locData = null;
	public BDLocationListener myListener;
	private LocationClientOption option = new LocationClientOption();
	private MapView mMapView;
	
	public BMapLocationUtil(Context mContext,BDLocationListener mListener)
	{
		this.mContext = mContext;
		//定位初始化
		mLocClient = new LocationClient(mContext);
		locData = new LocationData();
		myListener = mListener;
		mLocClient.registerLocationListener(myListener);
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setOpenGps(true);//打开gps
		option.setCoorType("bd09ll");     //设置坐标类型
		option.setScanSpan(2000);
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);
		mLocClient.setLocOption(option);	
	}

	private MyLocationOverlay myLocationOverlay = null;
	public BMapLocationUtil(Context mContext,BDLocationListener mListener,MapView mMapView)
	{
		this.mContext = mContext;
		this.mMapView = mMapView;
		initOrientation();
		//定位初始化
		mLocClient = new LocationClient(mContext);
		locData = new LocationData();
		myListener = mListener;
		mLocClient.registerLocationListener(myListener);
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setOpenGps(true);//打开gps
		option.setCoorType("bd09ll");     //设置坐标类型
		option.setScanSpan(2000);
		option.setNeedDeviceDirect(true);
		mLocClient.setLocOption(option);
		//定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		//添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();
	}
	
	/**
	 * 设置定位间隔时间（ms）  0为只定位一次
	 * @param ms_time
	 */
	public void setScanSpan(int ms_time)
	{
		option.setScanSpan(ms_time);
		mLocClient.setLocOption(option);
	}

	/**
	 * 只定位一次
	 */
	public void requestLocation()
	{
		setScanSpan(0);
		if(mLocClient.isStarted())
			mLocClient.requestLocation();
		else
		{
			mLocClient.start();
			registerOrientation();
		}
	}
	
	/**
	 * 定位每2秒一次
	 */
	public void requestLocationEvery2() {
		setScanSpan(2000);
		mLocClient.start();
		registerOrientation();
	}
	
	/**
	 * 返回是否定位开启ing
	 * @return
	 */
	public boolean isStarted()
	{
		return mLocClient.isStarted();
	}
	
	/**
	 * 开始定位
	 */
	public void start() {
		mLocClient.start();
		registerOrientation();
	}
	
	/**
	 * 暂停定位
	 */
	public void stop() {
		mLocClient.stop();
		unRegisterOrientation();
	}
	
	/**
	 * 销毁定位
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		mLocClient.unRegisterLocationListener(myListener);
		mLocClient.stop();
		mLocClient = null;
		unRegisterOrientation();
	}
	
	/**
	 * 更新地位的数据
	 * @param mBDLocation
	 */
	public void updateLocationData(BDLocation mBDLocation)
	{
		locData.latitude = mBDLocation.getLatitude();
		locData.longitude = mBDLocation.getLongitude();
		//如果不显示定位精度圈，将accuracy赋值为0即可
		locData.accuracy = mBDLocation.getRadius();
		// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
		//locData.direction = location.getDirection();
	}
	
	/**
	 * 让定位数据在地图上面显示
	 */
	public void setData()
	{
		myLocationOverlay.setData(locData);
		mMapView.refresh();
	}
	
	//方向相关
	private SensorManager sensorManager;
	private MySensorEventListener mySensorEventListener;
	/**
	 * 方向传感器
	 */
	private void initOrientation()
	{
		sensorManager= (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mySensorEventListener = new MySensorEventListener();
	}
	
	/**
	 * 注册方向传感器
	 */
	@SuppressWarnings({ "deprecation" })
	private void registerOrientation()
	{
		if(sensorManager!=null&&mySensorEventListener!=null)
		{
			Sensor sensor_orientation=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		    sensorManager.registerListener(mySensorEventListener,sensor_orientation, SensorManager.SENSOR_DELAY_UI);
		}
	}
	
	/**
	 * 注销方向传感器
	 */
	private void unRegisterOrientation()
	{
		if(sensorManager!=null&&mySensorEventListener!=null)
			sensorManager.unregisterListener(mySensorEventListener);
	}

	/**
	 * 方向传感器监听类
	 * @ClassName: MySensorEventListener 
	 */
	private class MySensorEventListener implements  SensorEventListener{

		@SuppressWarnings("deprecation")
		@Override
		//可以得到传感器实时测量出来的变化值
		public void onSensorChanged(SensorEvent event) {
			//方向传感器
			if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
				//x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
				float x = event.values[SensorManager.DATA_X];
				locData.direction = x;//优化百度地图方向不准的问题
				Log.v(TAG, "方向："+x);
			}
		}
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}

}
