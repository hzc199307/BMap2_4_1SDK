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
 * �ٶȵ�ͼ ��� ������ ������baiduMap SDK V2.4.1 ��
 * @ClassName: BMapOverlayUtil 
 * @author ���ǳ�
 * @Description: 
 * @date 2014��7��28�� ����4:12:41
 */
public class BMapOverlayUtil {

	private final static String TAG = "BMapOverlayUtil";

	private MapView mMapView;
	private MapController mMapController;//��ͼ����
	private Context mContext;

	private ItemizedOverlay<OverlayItem> mOverlay = null;//��ͼ��ע����
	private TextOverlay mTextOverlay = null;//��ͼ��ע��������ֹ���
	private PopupOverlay mPopupOverlay = null;//��ע���֮��ĵ�����
	private boolean popupOverlayStatus = false;//�Ƿ��е�����

	private View popupView = null;//��������view
	private View popupInfo = null;
	private TextView heightView = null;//�йص���pop���עoverlay����ĵ���
	private TextView popupText = null;//���������ı����ݵ�view

	private static int defaultIconID = R.drawable.icon_overlay_default;//Ĭ�ϵĵ�ͼ��עiconID
	private Drawable defaultIcon = null;                        //Ĭ�ϵĵ�ͼ��עicon

	private List<OverlayItem> listOverlayItems;
	int dataSize;
	private OverlayItem mCurentOverlayItem;//���һ�α������OverlayItem
	private OnTapListener onTapListener = null;

	public BMapOverlayUtil(Context context,MapView mapView) {

		this.mMapView = mapView;
		mMapController =mMapView.getController();
		this.mContext = context;

		//�����йؽ��������
		popupView = LayoutInflater.from(mContext).inflate(R.layout.item_bmap_pop, null);
		popupInfo = (View) popupView.findViewById(R.id.popinfo);
		popupText =(TextView) popupView.findViewById(R.id.textcache);
		heightView = (TextView) popupView.findViewById(R.id.textheight);
		setDefaultIcon(this.defaultIconID);

		//��overlay���� �����MapView��
		mOverlay = new MyOverlayWithPop(defaultIcon,mMapView);
		mTextOverlay = new TextOverlay(mMapView);

	}

	/**
	 * ����overlay������
	 * @param listOverlayItems
	 */
	public void setData(List<OverlayItem> listOverlayItems)
	{
		this.listOverlayItems = listOverlayItems;
		dataSize = listOverlayItems.size();Log.v(TAG, "listOverlayItems size = "+dataSize);
		/**
		 * ׼��overlay ����
		 */
		OverlayItem overlayItem;
		MyTextItem textItem;
		this.mOverlay.addItem(listOverlayItems);
		for(int i=0;i<dataSize;i++)
		{
			overlayItem = listOverlayItems.get(i);
			//			if(overlayItem.getMarker()==null)
			//			{
			//				overlayItem.setMarker(defaultIcon);//����Ĭ��ͼ�� ��Ҳ���Բ�����  ��Ϊnull��ʱ����Ĭ��ͼ�꣩
			//				//				int id = mContext.getResources().getIdentifier("bigscene_"+mBigScene.getBigScenePinyin() ,"drawable","com.ne.voiceguider");
			//				//				item.setMarker(mContext.getResources().getDrawable(id));
			//			}
			textItem=new MyTextItem(overlayItem.getTitle(),overlayItem.getPoint());
			mTextOverlay.addText(textItem);
		}
	}

	/**
	 * ����Ĭ�ϵı�עicon
	 * @param defaultIconID
	 */
	public void setDefaultIcon(int defaultIconID)
	{ 
		this.defaultIconID = defaultIconID;
		defaultIcon = mContext.getResources().getDrawable(defaultIconID);
		int height = defaultIcon.getMinimumHeight();//��ȡ�˵�ͼ��עicon�ĸ߶�
		heightView.setHeight(height);//�����йص���pop���עoverlay���� �Ż���ʾ
	}
	/**
	 * ����Ĭ�ϵı�עicon
	 * @param defaultIcon
	 */
	public void setDefaultIcon(Drawable defaultIcon)
	{ 
		this.defaultIcon = defaultIcon;
		int height = defaultIcon.getMinimumHeight();//��ȡ�˵�ͼ��עicon�ĸ߶�
		heightView.setHeight(height);//�����йص���pop���עoverlay���� �Ż���ʾ
	}

	/**
	 * ���õ����� �� ����,Ĭ��Ϊû��
	 * @param popupOverlayStatus
	 */
	public void setPopupOverlayStatus(boolean popupOverlayStatus)
	{
		this.popupOverlayStatus = popupOverlayStatus;
		if(popupOverlayStatus)
			mPopupOverlay = new PopupOverlay(mMapView, new PopupClickListener() {

				@Override
				public void onClickedPopup(int arg0) {
					if(mPopupClickListener2!=null)//�˴������������Ϊ���listenerֻ���ڴ��������ʱ��
						mPopupClickListener2.onClickedPopup(arg0);
				}
			});
	}
	private PopupClickListener mPopupClickListener2 = null;

	/**
	 * ���õ����� �� �������  
	 * @param popupClickListener
	 * @return �����Ƿ���Ӽ�����Ч���е����� ����pop�ĵ���¼���
	 */
	public boolean setPopupClickListener(PopupClickListener popupClickListener)
	{
		this.mPopupClickListener2 = popupClickListener;
		return popupOverlayStatus;
	}

	/**
	 * ��ע�ı�������Listener
	 * @ClassName: OnTapListener 
	 */
	public interface OnTapListener
	{
		public boolean onTap2(OverlayItem overlayItem);//���뱻�����OverlayItem
		public boolean onTap1(GeoPoint geoPoint , MapView mapView);//���뱻�����geoPoint���� �� �������mapView
	}
	
	/**
	 * ���ñ�ע�������onTapListener
	 * @param onTapListener
	 */
	public void setOnTapListener(OnTapListener onTapListener)
	{
		this.onTapListener = onTapListener;
	}

	/**
	 * �̳���ItemizedOverlay<OverlayItem> ��һ���Զ���overlay������    ������ÿ��overlayItem�ĵ�������¼�
	 * Ŀǰ�����и�bug pop����ʾ����������� ��ʾ����һ������ ������İ취�� ��һ�ο�  �ٵ�͹� TODO
	 * @ClassName: MyOverlayWithPop 
	 * @author ���ǳ�
	 */
	public class MyOverlayWithPop extends ItemizedOverlay<OverlayItem>{

		public MyOverlayWithPop(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		private boolean isPopShowed = false;

		/**
		 * ��������λ�õĵ���¼� ������ִ�У��� ��������ϴ���ͬ��index ���¼����ᴥ����
		 */
		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			Log.v(TAG, "onTap1");
			if(popupOverlayStatus==true)
			{
				if (mPopupOverlay != null){
					isPopShowed = false;
					mPopupOverlay.hidePop();//����֮��һ������
				}
			}
			if(onTapListener!=null)
				onTapListener.onTap1(pt,mMapView);
			return false;
		}

		/**
		 * ��������itemλ�õĵ���¼�  �����ִ�У��� ��������ϴ���ͬ��index ���¼����ᴥ����
		 */
		@Override
		public boolean onTap(int index){
			Log.v(TAG, "onTap2");
			if(popupOverlayStatus==true)
			{
				mCurentOverlayItem = this.getItem(index);

				popupText.setText(mCurentOverlayItem.getTitle());//���õ�������������ʾ

				//TODO ������ҳ����ʾ ��ͼ  ��Ҫ����
				//Bitmap bitMap= BMapUtil.getBitmapFromView((View) popupView.findViewById(R.id.popinfo));//����viewת����bitmap
				if (mPopupOverlay != null){
					isPopShowed=true;
					//mPopupOverlay.showPopup(bitMap,mCurentOverlayItem.getPoint(),0);
					mPopupOverlay.hidePop();
					mPopupOverlay.showPopup(popupView,mCurentOverlayItem.getPoint(),0);
				}
				Log.v(TAG, "onTap ��ʾ��תpop �رվ�������text");
			}
			if(onTapListener!=null)
				onTapListener.onTap2(getItem(index));
			return true;
		}
	}

	/**
	 * ������overlay��ӡ�������ʾ
	 */
	public void showAll() {
		if(mMapView.getOverlays().contains(mOverlay)==false)
			mMapView.getOverlays().add(mOverlay);
		if(mMapView.getOverlays().contains(mTextOverlay)==false)
			mMapView.getOverlays().add(mTextOverlay);
		mMapView.refresh();
	}

	/**
	 * ���ŵ�ͼ��������ָ���ľ�γ�ȷ�Χ ����ָ�����е������λ��
	 */
	public void showSpan()
	{
		Log.v(TAG, mOverlay.getLatSpanE6()+" "+mOverlay.getLonSpanE6());
		mMapController.setCenter(mOverlay.getCenter());//���õ�ͼ���ĵ�
		Thread thread = new Thread(){//��0.5s��ʾspan �����ͼ�����޷���ʾ����
			@Override
			public void run(){
				try {
					Thread.currentThread().sleep(500);
					mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//�ı��ͼ��Χ
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//�ı��ͼ��Χ
	}

	/**
	 * ���ŵ�ͼ��������ָ���ľ�γ�ȷ�Χ ����ָ�����е������λ�� ������msTimeʱ��Ķ���
	 * @param msTime
	 */
	public void showSpanWithAnimation(final int msTime)
	{
		Log.v(TAG, mOverlay.getLatSpanE6()+" "+mOverlay.getLonSpanE6());
		mMapController.setCenter(mOverlay.getCenter());//���õ�ͼ���ĵ�
		Thread thread = new Thread(){//��0.5s��ʾspan �����ͼ�����޷���ʾ����
			@Override
			public void run(){
				try {
					Thread.currentThread().sleep(500);
					mMapController.zoomToSpanWithAnimation(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6(),msTime);//1500msʱ�����ı��ͼ��Χ
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		//mMapController.zoomToSpanWithAnimation(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6(),msTime);//1500msʱ�����ı��ͼ��Χ
	}

	/**
	 * ������overlay�Ƴ�
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
	 * �Զ����TextItem
	 * @ClassName: MyTextItem 
	 * @author ���ǳ�
	 * @Description: Ĭ�ϵ�������ɫ 0xFF00FF00 ������ɫ 0x0A505050
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
	 * �����ı�item����ʽ
	 * @param fontColor ������ɫ -1��������
	 * @param fontSize �ֺŴ�С С��0��������
	 * @param bgColor ������ɫ -1��������
	 * @param typeface �������� null��ʾĬ������
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
		if(listTextItems!=null)//ֻ���б������ݵ�ʱ�� �ٽ�ÿһ��textItem����һ����ʽ
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
