package com.hezhichao.bmap2_4_1.util;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hezhichao.bmap2_4_1.R;

/**
 * 百度地图 标记 工具类 （基于baiduMap SDK V2.4.1 ）
 * @ClassName: BMapOverlayUtil 
 * @author 贺智超
 * @Description: 
 * @date 2014年7月28日 下午4:12:41
 */
public class BMapOverlayUtil {

	private final static String TAG = "BMapOverlayUtil";

	private MapView mMapView;
	private MapController mMapController;//地图控制
	private Context mContext;

	private ItemizedOverlay<OverlayItem> mOverlay = null;//地图标注管理
	private TextOverlay mTextOverlay = null;//地图标注下面的文字管理
	private PopupOverlay mPopupOverlay = null;//标注点击之后的弹出框
	private boolean popupOverlayStatus = false;//是否还有弹出框

	private View popupView = null;//弹出窗的view
	private View popupInfo = null;
	private TextView heightView = null;//有关弹窗pop与标注overlay距离的调节
	private TextView popupText = null;//弹出窗的文本内容的view

	private static int defaultIconID = R.drawable.icon_overlay_default;//默认的地图标注iconID
	private Drawable defaultIcon = null;                        //默认的地图标注icon

	private List<OverlayItem> listOverlayItems;
	int dataSize;
	private OverlayItem mCurentOverlayItem;//最近一次被点击的OverlayItem
	private OnTapListener onTapListener = null;

	public BMapOverlayUtil(Context context,MapView mapView) {

		this.mMapView = mapView;
		mMapController =mMapView.getController();
		this.mContext = context;

		//设置有关界面的内容
		popupView = LayoutInflater.from(mContext).inflate(R.layout.item_bmap_pop, null);
		popupInfo = (View) popupView.findViewById(R.id.popinfo);
		popupText =(TextView) popupView.findViewById(R.id.textcache);
		heightView = (TextView) popupView.findViewById(R.id.textheight);
		setDefaultIcon(this.defaultIconID);

		//将overlay对象 添加至MapView中
		mOverlay = new MyOverlayWithPop(defaultIcon,mMapView);
		mTextOverlay = new TextOverlay(mMapView);

	}

	/**
	 * 设置overlay的数据
	 * @param listOverlayItems
	 */
	public void setData(List<OverlayItem> listOverlayItems)
	{
		this.listOverlayItems = listOverlayItems;
		dataSize = listOverlayItems.size();Log.v(TAG, "listOverlayItems size = "+dataSize);
		/**
		 * 准备overlay 数据
		 */
		OverlayItem overlayItem;
		MyTextItem textItem;
		this.mOverlay.addItem(listOverlayItems);
		for(int i=0;i<dataSize;i++)
		{
			overlayItem = listOverlayItems.get(i);
			//			if(overlayItem.getMarker()==null)
			//			{
			//				overlayItem.setMarker(defaultIcon);//设置默认图标 （也可以不设置  因为null的时候用默认图标）
			//				//				int id = mContext.getResources().getIdentifier("bigscene_"+mBigScene.getBigScenePinyin() ,"drawable","com.ne.voiceguider");
			//				//				item.setMarker(mContext.getResources().getDrawable(id));
			//			}
			textItem=new MyTextItem(overlayItem.getTitle(),overlayItem.getPoint());
			mTextOverlay.addText(textItem);
		}
	}

	/**
	 * 设置默认的标注icon
	 * @param defaultIconID
	 */
	public void setDefaultIcon(int defaultIconID)
	{ 
		this.defaultIconID = defaultIconID;
		defaultIcon = mContext.getResources().getDrawable(defaultIconID);
		int height = defaultIcon.getMinimumHeight();//获取了地图标注icon的高度
		heightView.setHeight(height);//调节有关弹窗pop与标注overlay距离 优化显示
	}
	/**
	 * 设置默认的标注icon
	 * @param defaultIcon
	 */
	public void setDefaultIcon(Drawable defaultIcon)
	{ 
		this.defaultIcon = defaultIcon;
		int height = defaultIcon.getMinimumHeight();//获取了地图标注icon的高度
		heightView.setHeight(height);//调节有关弹窗pop与标注overlay距离 优化显示
	}

	/**
	 * 设置弹出框 的 有无,默认为没有
	 * @param popupOverlayStatus
	 */
	public void setPopupOverlayStatus(boolean popupOverlayStatus)
	{
		this.popupOverlayStatus = popupOverlayStatus;
		if(popupOverlayStatus)
			mPopupOverlay = new PopupOverlay(mMapView, new PopupClickListener() {

				@Override
				public void onClickedPopup(int arg0) {
					if(mPopupClickListener2!=null)//此处这样设计是因为添加listener只能在创建对象的时候
						mPopupClickListener2.onClickedPopup(arg0);
				}
			});
	}
	private PopupClickListener mPopupClickListener2 = null;

	/**
	 * 设置弹出框 的 点击监听  
	 * @param popupClickListener
	 * @return 返回是否添加监听起效（有弹出框 采用pop的点击事件）
	 */
	public boolean setPopupClickListener(PopupClickListener popupClickListener)
	{
		this.mPopupClickListener2 = popupClickListener;
		return popupOverlayStatus;
	}

	/**
	 * 标注的被触摸的Listener
	 * @ClassName: OnTapListener 
	 */
	public interface OnTapListener
	{
		public boolean onTap2(OverlayItem overlayItem);//传入被点击的OverlayItem
		public boolean onTap1(GeoPoint geoPoint , MapView mapView);//传入被点击的geoPoint坐标 和 被点击的mapView
	}
	
	/**
	 * 设置标注被点击的onTapListener
	 * @param onTapListener
	 */
	public void setOnTapListener(OnTapListener onTapListener)
	{
		this.onTapListener = onTapListener;
	}

	/**
	 * 继承自ItemizedOverlay<OverlayItem> 的一个自定义overlay管理类    包含了每个overlayItem的点击触发事件
	 * 目前这里有个bug pop的显示经常会出故障 显示了上一个内容 ，解决的办法是 点一次开  再点就关 TODO
	 * @ClassName: MyOverlayWithPop 
	 * @author 贺智超
	 */
	public class MyOverlayWithPop extends ItemizedOverlay<OverlayItem>{

		public MyOverlayWithPop(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		private boolean isPopShowed = false;

		/**
		 * 处理所有位置的点击事件 （最先执行）（ 如果按到上次相同的index 此事件不会触发）
		 */
		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			Log.v(TAG, "onTap1");
			if(popupOverlayStatus==true)
			{
				if (mPopupOverlay != null){
					isPopShowed = false;
					mPopupOverlay.hidePop();//触碰之后一律隐藏
				}
			}
			if(onTapListener!=null)
				onTapListener.onTap1(pt,mMapView);
			return false;
		}

		/**
		 * 处理所有item位置的点击事件  （最后执行）（ 如果按到上次相同的index 此事件不会触发）
		 */
		@Override
		public boolean onTap(int index){
			Log.v(TAG, "onTap2");
			if(popupOverlayStatus==true)
			{
				mCurentOverlayItem = this.getItem(index);

				popupText.setText(mCurentOverlayItem.getTitle());//设置弹出窗的文字显示

				//TODO 弹窗的页面显示 视图  需要更改
				//Bitmap bitMap= BMapUtil.getBitmapFromView((View) popupView.findViewById(R.id.popinfo));//弹窗view转换成bitmap
				if (mPopupOverlay != null){
					isPopShowed=true;
					//mPopupOverlay.showPopup(bitMap,mCurentOverlayItem.getPoint(),0);
					mPopupOverlay.hidePop();
					mPopupOverlay.showPopup(popupView,mCurentOverlayItem.getPoint(),0);
				}
				Log.v(TAG, "onTap 显示跳转pop 关闭景点名称text");
			}
			if(onTapListener!=null)
				onTapListener.onTap2(getItem(index));
			return true;
		}
	}

	/**
	 * 将所有overlay添加、并且显示
	 */
	public void showAll() {
		if(mMapView.getOverlays().contains(mOverlay)==false)
			mMapView.getOverlays().add(mOverlay);
		if(mMapView.getOverlays().contains(mTextOverlay)==false)
			mMapView.getOverlays().add(mTextOverlay);
		mMapView.refresh();
	}

	/**
	 * 缩放地图到能容下指定的经纬度范围 并且指向所有点的中心位置
	 */
	public void showSpan()
	{
		Log.v(TAG, mOverlay.getLatSpanE6()+" "+mOverlay.getLonSpanE6());
		mMapController.setCenter(mOverlay.getCenter());//设置地图中心点
		Thread thread = new Thread(){//晚0.5s显示span 解决地图界面无法显示错误
			@Override
			public void run(){
				try {
					Thread.currentThread().sleep(500);
					mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//改变地图范围
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//改变地图范围
	}

	/**
	 * 缩放地图到能容下指定的经纬度范围 并且指向所有点的中心位置 而且有msTime时间的动画
	 * @param msTime
	 */
	public void showSpanWithAnimation(final int msTime)
	{
		Log.v(TAG, mOverlay.getLatSpanE6()+" "+mOverlay.getLonSpanE6());
		mMapController.setCenter(mOverlay.getCenter());//设置地图中心点
		Thread thread = new Thread(){//晚0.5s显示span 解决地图界面无法显示错误
			@Override
			public void run(){
				try {
					Thread.currentThread().sleep(500);
					mMapController.zoomToSpanWithAnimation(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6(),msTime);//1500ms时间来改变地图范围
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		//mMapController.zoomToSpanWithAnimation(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6(),msTime);//1500ms时间来改变地图范围
	}

	/**
	 * 将所有overlay移除
	 */
	public boolean removeAll()
	{
		if(mMapView.getOverlays().contains(mOverlay)==true)
			mMapView.getOverlays().remove(mOverlay);
		if(mMapView.getOverlays().contains(mTextOverlay)==true)
			mMapView.getOverlays().remove(mTextOverlay);
		mMapView.refresh();
		return true;
	}

	private Symbol textSymbol = new Symbol(); 
	private Symbol.Color _fontColor = textSymbol.new Color(0xFF00FF00);
	private Symbol.Color _bgColor = textSymbol.new Color(0x0A505050);
	private int _fontSize = 30;
	private Typeface _typeface = null;
	/**
	 * 自定义的TextItem
	 * @ClassName: MyTextItem 
	 * @author 贺智超
	 * @Description: 默认的文字颜色 0xFF00FF00 背景颜色 0x0A505050
	 */
	public class MyTextItem extends TextItem
	{
		public MyTextItem(String text,GeoPoint pt) {

			this.align = ALIGN_TOP;
			this.fontColor = _fontColor;
			this.bgColor = _bgColor;
			this.fontSize = _fontSize;
			this.text = text;
			this.pt = pt;
			this.typeface = _typeface;
		}
	}
	/**
	 * 设置文本item的样式
	 * @param fontColor 文字颜色 -1代表不设置
	 * @param fontSize 字号大小 小于0代表不设置
	 * @param bgColor 背景颜色 -1代表不设置
	 * @param typeface 文字字体 null表示默认字体
	 */
	public void setTextStyle(int fontColor,int fontSize,int bgColor,Typeface typeface)
	{
		List<TextItem> listTextItems = mTextOverlay.getAllText();
		if(fontColor!=-1)
			this._fontColor = textSymbol.new Color(fontColor);
		if(fontSize>=0)
			this._fontSize = fontSize;
		if(bgColor!=-1)
			this._bgColor = textSymbol.new Color(bgColor);
		this._typeface = typeface;
		TextItem textItem;
		if(listTextItems!=null)//只有列表有数据的时候 再讲每一个textItem更新一遍样式
		{
			for(int i=0;i<dataSize;i++)
			{
				textItem = listTextItems.get(i);
				textItem.fontColor = _fontColor;
				textItem.bgColor = _bgColor;
				textItem.fontSize = _fontSize;
				textItem.typeface = _typeface;
			}
			mMapView.refresh();
		}
	}
	
	public void destroy() {
		listOverlayItems.clear();
		listOverlayItems = null;
		onTapListener = null;
		mPopupClickListener2 = null;
	}
}
