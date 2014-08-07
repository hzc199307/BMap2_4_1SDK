package com.hezhichao.bmap2_4_1;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hezhichao.bmap2_4_1.fragment.BMapFragment;
import com.hezhichao.bmap2_4_1.util.BMapUtil;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	private String TAG = "MainActivity";

	private MapView mMapView;
	private TextView textView;
	private LinearLayout linearLayout;
	private BMapFragment bMapFragment;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BMapUtil.initBMapManager(getApplicationContext());

        bMapFragment = new BMapFragment();

        
//        SDKInitializer.initialize(getApplicationContext()); 
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.bmap_fragment, bMapFragment).commit();
//        mMapView = (MapView)findViewById(R.id.baiduMapView);
        textView = (TextView)findViewById(R.id.textView);
        
//        textView.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
//				linearParams.height +=100;
//				textView.setLayoutParams(linearParams);
//				return false;
//			}
//		});
//        BaiduMap mBaiduMap = mMapView.getMap();
//        final ScaleAnimation scaleAnimation =new ScaleAnimation(1.0f, 1.25f, 1.0f, 1.5f, 
//    			Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f); 
//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//			
//			@Override
//			public boolean onMapPoiClick(MapPoi arg0) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public void onMapClick(LatLng arg0) {
//				// TODO Auto-generated method stub
//				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)mMapView.getLayoutParams();
//				linearParams.height +=500;
//				mMapView.setLayoutParams(linearParams);
////				linearLayout.setAnimation(scaleAnimation);
////				scaleAnimation.setDuration(5000);//设置动画持续时间 
////				scaleAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态  
////				scaleAnimation.startNow();
//			}
//		});
//        bMapFragment.dealLocation(true);
        
        Log.v(TAG, "onCreate");
    }
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	Log.v(TAG, "onStart");
    	bMapFragment.setLocation(true);
		List<OverlayItem> listOverlayItems = new ArrayList<OverlayItem>();
		OverlayItem item1,item2,item3;
		item1 = new OverlayItem(new GeoPoint(23000000, 113000000), "1", "");
		item2 = new OverlayItem(new GeoPoint(23500000, 113500000), "2", "");
		item3 = new OverlayItem(new GeoPoint(23100000, 113100000), "3", "");
		listOverlayItems.add(item1);
		listOverlayItems.add(item2);
		listOverlayItems.add(item3);
    	bMapFragment.setOverlayData(listOverlayItems, null, true, null);
    	bMapFragment.showOverlaySpanWithAnimation();
    	//bMapFragment.removeOverlay();
    }

    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
