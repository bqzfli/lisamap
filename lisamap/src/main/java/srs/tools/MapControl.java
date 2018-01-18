package srs.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import srs.DataSource.Vector.IFeatureClass;
import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Layer.IElementContainer;
import srs.Layer.IFeatureLayer;
import srs.Layer.IGPSContainer;
import srs.Layer.TileLayer;
import srs.Layer.wmts.ImageDownLoader;
import srs.Map.ActiveView;
import srs.Map.Event.ContentChangedListener;
import srs.Map.IActiveView;
import srs.Map.IMap;
import srs.Map.Map;
import srs.Operation.SelectedFeatures;

public class MapControl extends BaseControl implements ContentChangedListener {


	/**绘制次数计数器
	 *
	 */
	private int mCount = 0;
	private IActiveView mActiveView;
	private ITool mZoomPan = null/*=new ZoomPan()*/;
	private ITool mGPSTool=null;
	private ITool mDrawTool=null;
	private int mwidthold=0;
	private int mheightold=0;
	private ProgressBar mProgressBar = null;
	private boolean IsDrawTrack=false;
	public Bitmap mBitScreen = null;
	private Handler myHandler;
	DisplayMetrics dm = new DisplayMetrics();
	private int densityDpi;
	/**
	 * 触摸状态 
	 */
	public int MODE = 0;
	private TextView mTVRules;

	/**开始进入编辑模式
	 *
	 */
	/*@Override
	public void StartEdit(){
		this.mActiveView.FocusMap().StartEdit();
	}

	 *//**结束编辑模式
	 *
	 *//*
	@Override
	public void StopEdit(){
		this.mActiveView.FocusMap().StopEdit();
	}*/

	private Paint mPaint = new Paint();

	/**是否为第一次刷新
	 *
	 */
	private boolean misFirst=true;

	IEnvelope menv=null;
	int mFid;
	String mfieldID;
	IFeatureLayer fLayer=null;

	public String IndexOfCheck = "";

	public void dispose() throws Exception{
		if(mBitScreen!=null&&!mBitScreen.isRecycled()){
			mBitScreen.recycle();
			mBitScreen = null;
		}
		mBitScreen = null;
		myHandler = null;
		mProgressBar = null;
		mPaint = null;
		menv = null;
		fLayer = null;
		mfieldID = null;
		mActiveView.dispose();mActiveView = null;
		mTVRules = null;

		((ZoomPan)mZoomPan).dispose();mZoomPan = null;
		mGPSTool=null;
		mDrawTool=null;
	}

	public MapControl(Context context) {
		super(context);
		mZoomPan=new ZoomPan();
		Initial();
		/*setWillNotDraw(false);*/
	}

	public MapControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		mZoomPan=new ZoomPan();
		Initial();
		/*setWillNotDraw(false);*/
	}

	/**初始化控件
	 * @author 李忠义 20121215
	 */
	@SuppressLint("HandlerLeak")
	private void Initial(){
		mActiveView=new ActiveView();
		this.mZoomPan.setBuddyControl(this);
		mProgressBar = new ProgressBar(this.getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mTVRules = new TextView(this.getContext());
		this.addView(mProgressBar,params);
		RelativeLayout.LayoutParams paramRules =new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramRules.addRule(RelativeLayout.ALIGN_TOP);
		paramRules.addRule(RelativeLayout.ALIGN_LEFT);
		this.addView(mTVRules,paramRules );
		dm = this.getResources().getDisplayMetrics();
		densityDpi = dm.densityDpi;
		myHandler=new Handler(){
			public void handleMessage(Message msg){
				try{
					super.handleMessage(msg);
					switch(msg.arg1){
						case 6:
							DrawTrackLayer();
							Map.INDEXDRAWLAYER ++;
							mActiveView.FocusMap().drawLayer(myHandler);
							break;
						case 0:
							//mProgressBar.setVisibility(View.VISIBLE);
							break;
						case 4:
							String key = msg.getData().getString("KEY");
							Log.i("LEVEL-ROW-COLUMN", "MSG = 4:"+"MapControl.myHandler 绘制瓦片："+key);
							if(key!=null){
								Log.i("LEVEL-ROW-COLUMN", "MapControl.myHandler 绘制瓦片："+key);
								DrawTileImage(key,myHandler);
								DrawTrackLayer();
							}
							if(TileLayer.IsDrawnEnd()){
								DrawTrackLayer();
								Log.i("LEVEL-ROW-COLUMN", "图层："
										+ String.valueOf(Map.INDEXDRAWLAYER)
										+ "   所有瓦片已经绘制完成，绘制下一层");
								Map.INDEXDRAWLAYER ++;
								mActiveView.FocusMap().drawLayer(myHandler);
							}
							break;
						case 3:
							Log.println(Log.ASSERT,"LEVEL-ROW-COLUMN", "3: 图层："+ String.valueOf(Map.INDEXDRAWLAYER)+" 绘制过程中,将部分'图层缓存'绘于屏幕 MapControl.DrawTrackLayer");
							DrawTrackLayer();
							break;
						case 1:
						case 2:
							DrawTrack();
							ImageDownLoader.cancelTask();
							ImageDownLoader.StopThread();
							mProgressBar.setVisibility(View.GONE);
							Log.e("LEVEL-ROW-COLUMN","MapControl刷新完成：进度条消失");
							break;
					}
				}catch(Exception e){
					Log.e("LEVEL-ROW-COLUMN","MapControl.myHandler.handleMessage:"+e.getMessage());
					/*String key = msg.getData().getString("KEY");*/
					Log.println(10256, "key", "e.getMessage()");
				}
			}
		};
	}

	/**清除控件上的所有绘图工具
	 *
	 */
	public void ClearDrawTool(){
		this.mDrawTool=null;
	}

	@Override
	protected void onFinishInflate(){
		super.onFinishInflate();
		IEnvelope en=new Envelope(-180,-90,90,180);
		int width=this.getWidth();
		int height=this.getHeight();

		if(mActiveView==null&&width>0&&height>0){
			en=new Envelope(0,0,width,height);
			//			mActiveView.getContentChanged().addListener(this);
			//				this.mZoomPan.BuddyControl(this);
		}

		mActiveView.FocusMap(new Map(en));
		mActiveView.getContentChanged().addListener(this);

		System.gc();
	}

	public ITool getDrawTool(){
		if(this.mDrawTool!=null){
			return this.mDrawTool;
		}
		else
			return null;
	}

	@Override
	public void setDrawTool(ITool value){
		if(value!=null){
			this.mDrawTool=value;
			this.mDrawTool.setBuddyControl(this);
			this.mDrawTool.setEnable(true);
		}
		else{
			this.mDrawTool=null;
		}
	}

	@Override
	public void setGPSTool(ITool value){
		if(value!=null){
			this.mGPSTool=value;
			this.mGPSTool.setBuddyControl(this);
			this.mGPSTool.setEnable(true);
		}
		else
			this.mGPSTool=null;
	}

	public void clearGPSTool(){
		if(this.mGPSTool!=null){
			this.mGPSTool.setEnable(false);
			this.mGPSTool=null;
		}
	}

	public ITool getGPSTool(){
		return mGPSTool;
	}

	public boolean onTouch(View v, MotionEvent event) {
		if(mDrawTool!=null&&mDrawTool.getEnable()){
			//edit by lzy 20131229 长点击选中
			boolean end = mDrawTool.onTouch(v, event);
			if(end){
				return end;
			}
		}
		if(mZoomPan!=null){
			if(mGPSTool!=null){
				mGPSTool.onTouch(v, event);
			}
			return mZoomPan.onTouch(v, event);
		}
		return true;
	}

	public IMap getMap(){
		return mActiveView.FocusMap();
	}

	public void setMap(IMap value){
		if(value==null
				||mActiveView.FocusMap()==value){
			return;
		}
		try {
			mActiveView.FocusMap().dispose();
			//			mActiveView = new ActiveView();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		mActiveView.FocusMap(value);
		this.misFirst=true;
		try {
			((ZoomPan)mZoomPan).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mZoomPan=new ZoomPan();
		mZoomPan.setBuddyControl(this);	
		/*IMap map = mActiveView.FocusMap();
		try {
			map.AddLayers(value.getLayers());
			value.ClearLayer();
		} catch (IOException e) {
			System.out.print("添加图层时计算图层面积出错！"+e.getMessage());
			e.printStackTrace();
		}*/
		/*设置地图了，mapControl就相当于重新初始化了*/
	}

	/* 返回控件上显示的视图
	 * @see tools.BaseControl#getBitmap()
	 */
	@Override
	public Bitmap getBitmap(){
		return mActiveView.FocusMap().ExportMap(false);
	}

	@Override
	public IElementContainer getElementContainer(){
		return mActiveView.FocusMap().getElementContainer();
	}

	@Override
	public IGPSContainer getGPSContainer(){
		return mActiveView.FocusMap().getGPSContainer();
	}

	public IActiveView getActiveView(){
		return mActiveView;
	}

	public void setActiveView(IActiveView value){
		if(!mActiveView.equals(value)){
			mActiveView=value;
			mActiveView.getContentChanged().addListener(this);
		}
	}

	/* (non-Javadoc)实现Map.Event.ContentChangedListener接口,灾情评估中的“mActiveView_ContentChanged”
	 * @see Map.Event.ContentChangedListener#doEvent(java.util.EventObject)
	 */
	@Override
	public void doEvent(EventObject event) {
		int width=this.getWidth();
		int height=this.getHeight();
		if(width!=0&&height!=0){
			mActiveView.FocusMap().setDeviceExtent(new Envelope(0,0,width,height));
		}
		else{
			mActiveView.FocusMap().setDeviceExtent(new Envelope(0,0,60,60));
		}
	}

	@Override
	public void Copy(BaseControl targetControl){
		if(targetControl.getActiveView().FocusMap()==null){
			targetControl.getActiveView().FocusMap(mActiveView.FocusMap());
		}

		if(!targetControl.getActiveView().equals(mActiveView)){
			mActiveView=targetControl.getActiveView();
			mActiveView.getContentChanged().addListener(this);
		}

		mActiveView.FocusMap().setDeviceExtent(new Envelope(0,0,this.getWidth(),this.getHeight()));
	}

	@Override
	public IPoint ToWorldPoint(PointF point){
		return mActiveView.FocusMap().ToMapPoint(point);
	}

	@Override
	public PointF FromWorldPoint(IPoint point){
		return mActiveView.FocusMap().FromMapPoint(point);
	}

	@Override
	public double FromWorldDistance(double worldDistance){
		return mActiveView.FocusMap().FromMapDistance(worldDistance);
	}

	@Override
	public double ToWorldDistance(double deviceDistance){
		return mActiveView.FocusMap().ToMapDistance(deviceDistance);
	}



	/**李忠义修改 20121215
	 *	停止绘图线程	 * 
	 */
	public void StopDraw(){
		ImageDownLoader.cancelTask();
		/*if(mDrawThread!=null&&mDrawThread.isAlive()){
			mDrawThread.StopThread();
			mDrawThread=null;
			isThreadDrawRun=false;
		}*/
		/*removed by lizhongyi
		 * 20150618
		 * mActiveView.FocusMap().getScreenDisplay().ResetPartCaches();*/
	}


	/**重绘全部缓存
	 *
	 */
	@Override
	public void Refresh(){
		/*try{
			mActiveView.FocusMap().Refresh();
			DrawTrack();
			//使用多线程的方式刷新底图
		 */			try {
//			 mProgressBar.setVisibility(View.VISIBLE);
			 /*if(!mActiveView.FocusMap().IsEditMode()||mActiveView.FocusMap().IsFirstEdit()){*/
			this.setDrawingCacheEnabled(true);
			try{
				if(!misFirst&&mActiveView.FocusMap().getHasWMTSBUTTOM()){
					this.mBitScreen = this.getDrawingCache().copy(Config.RGB_565, false);
					Log.i("RECYCLE","通过getDrawingCache获取了控件的截图，并copy后赋值给mBitScreen"+mBitScreen);
				}
				this.setDrawingCacheEnabled(false);
			}catch(Exception e){
				Log.e("LEVEL-ROW-COLUMN","MapControl.Refresh at 490"+e.getMessage());
			}
			if(mBitScreen!=null
					&&!mBitScreen.isRecycled()
					&&mActiveView.FocusMap().getHasWMTSBUTTOM()){
				Log.println(Log.ASSERT,"RECYCLE","通过界面截图作为底图，mBtiScreen："+mBitScreen);
				//FIXME 先不考虑用上一次缩放的界面做底图
				//      原因：当wmts数据无法获取时，上一次的矢量会可见，导致底图“画花”了
				//mActiveView.FocusMap().Refresh(myHandler,mBitScreen);
				mActiveView.FocusMap().Refresh(myHandler,null);
			}else{
				Log.println(Log.ASSERT,"RECYCLE","通过白图作为底图");
				mActiveView.FocusMap().Refresh(myHandler,null);
			}
			 /*}else{
					mActiveView.FocusMap().RefreshEdit(myHandler);
					StopEdit();
				}*/
			//				message=new Message();
			//				message.arg1=1;
			//				myHandler.sendMessage(message);
		}catch(InterruptedException e){
			Log.e("LEVEL-ROW-COLUMN","MapControl.Refresh at 512 InterruptedException"+e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			Log.e("LEVEL-ROW-COLUMN","MapControl.Refresh at 515"+e.getMessage());
			Message message=new Message();
			message.arg1=2;
			myHandler.sendMessage(message);
		}
		return;
		 /*}catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}*/
	}

	/**仅刷新编辑部分
	 *
	 */
	public void EditRefresh(){
	}

	/* (non-Javadoc)重绘除LayerCache外其他缓存
	 * @see Tools.BaseControl#PartialRefresh()
	 */
	@Override
	public void PartialRefresh() {
		try{
			mActiveView.FocusMap().PartialRefresh();
			DrawTrack();
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public void DrawTileImage(String key,Handler handler){
		TileLayer.DrawImageFromURL(key,handler);
	}

	/**将缓冲区中Layer部分的视图绘制到控件上    
	 * @author 李忠义  添加 20121216
	 */
	public void DrawTrackLayer(){
		//修改 李忠义 20121216 此处不需要copy， 因为不需要对底图数据进行绘制，并且copy bitmap消耗资源很严重
		BitmapDrawable bd = new BitmapDrawable(getResources(),
				mActiveView.FocusMap().ExportMapLayer());
		this.setBackgroundDrawable(bd);

		Log.i("LEVEL-ROW-COLUMN",
				"MapControl.DrawTrackLayer    "
						+"绘制图层："+ String.valueOf(Map.INDEXDRAWLAYER)+","
						+",将'图层缓存区内容'绘于屏幕 "
						+"\n\r-------------------------------" );
	}

	/**将缓冲区中的视图绘制到控件上     添加  by  李忠义 20120306
	 * @param
	 */
	@Override
	public void DrawTrack(){
		Bitmap bmp = mActiveView.FocusMap().ExportMap(false).copy(Config.RGB_565, true);
		BitmapDrawable bd= new BitmapDrawable(getResources(), bmp);
		this.setBackgroundDrawable(bd);
		bmp=null;
		Log.i("LEVEL-ROW-COLUMN", "地图刷新完成,将画布底图绘于屏幕 MapControl.DrawTrack");
		//	IsDrawTrack=true;
		//	this.setBackgroundColor(Color.BLACK);
		//	this.setImageBitmap(mActiveView.FocusMap().ExportMap().copy(Config.ARGB_8888, true));
	}

	/**将指定图片绘制到控件上     添加  by  李忠义 20120306
	 * @param
	 */
	@Override
	public void DrawTrack(Bitmap bit){
		//		mBitScreen=bit.copy(Config.ARGB_8888, true);
		//		IsDrawTrack=true;
		//		this.setBackgroundColor(Color.BLACK);
		if(bit!=null&&bit!=mActiveView.FocusMap().ExportMap(false)){
			BitmapDrawable bd= new BitmapDrawable(getResources(), bit);
			this.setBackgroundDrawable(bd);
			bit=null;
		}
	}

	public void setdata(IMap map,int fid,IFeatureLayer layer,IEnvelope env){
		fLayer=layer;
		mFid=fid;
		menv=new Envelope(env.XMin()-env.Width()*0.1,env.YMin()-env.Height()*0.1,
				env.XMax()+env.Width()*0.1,env.YMax()+env.Height()*0.1);
		if(mFid>-1){
			IFeatureClass fClass = fLayer.getFeatureClass();
			try {
				map.getSelectionSet().ClearSelection();
				this.getActiveView().FocusMap().getSelectionSet().ClearSelection();
				List<Integer> fidSelect=new ArrayList<Integer>();
				fidSelect.add(mFid);
				fClass.setSelectionSet(fidSelect);
				//StaticConfig.LoadRegions();
				this.setMap(map);
				this.getActiveView().FocusMap().setExtent(menv);
				//this.Refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**全屏高亮显示选中记录    
	 * 李忠义 20121120
	 * @param fids 选中样本单位区的顺序号
	 * @param layer 目标图层
	 * @param envs 选中样本单位去的范围
	 */
	public void setdata(List<Integer> fids,IFeatureLayer layer,List<IEnvelope> envs){
		fLayer = layer;
		menv= this.getAllSelectEnvelope(envs);
		SelectedFeatures s=new SelectedFeatures();
		s.FeatureClass=((IFeatureLayer)layer).getFeatureClass();
		s.FIDs=fids;
		layer.getFeatureClass().setSelectionSet(s.FIDs);
		this.getActiveView().FocusMap().getSelectionSet().AddFeatures(s);
		this.getActiveView().FocusMap().setExtent(menv);
		this.PartialRefresh();
	}

	private IEnvelope getAllSelectEnvelope(List<IEnvelope> envs) {
		menv=this.getActiveView().FocusMap().getExtent();
		IEnvelope env;
		double minx,miny,maxx,maxy;
		if(envs==null||envs.size()==0){
			return menv;
		}
		Iterator<IEnvelope> itenvs=envs.iterator();
		if(itenvs.hasNext()){
			menv=itenvs.next();
		}
		minx=menv.XMin();
		miny=menv.YMin();
		maxx=menv.XMax();
		maxy=menv.YMax();

		while(itenvs.hasNext()){
			env=itenvs.next();
			if(env.XMin()<minx)
				minx=env.XMin();
			if(env.YMin()<miny)
				miny=env.YMin();
			if(env.XMax()>maxx)
				maxx=env.XMax();
			if(env.YMax()>maxy)
				maxy=env.YMax();
		}

		menv=new Envelope(minx-(maxx-minx)*0.1,miny-(maxy-miny)*0.1,
				maxx+(maxx-minx)*0.1,maxy+(maxy-miny)*0.1);
		return menv;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(!changed){
			return;
		}
		int width = r - l;
		int height = b - t;
		//测量地图尺寸
		if(misFirst&&mwidthold!=width&&mheightold!=height){
			mwidthold=width;
			mheightold=height;
			Log.i("MapControl.onLayout", "width:"+ mwidthold +";height:"+mheightold+";");
			IMap mMap=mActiveView.FocusMap();
			mMap.setDeviceExtent(new Envelope(0,0,mwidthold,mheightold));
			/*bqzf 20150608
			 * 这一句会引起死循环
			 * 因为Refresh中使用了“this.BitScreen = this.getDrawingCache().copy(Config.RGB_565, false);”
			 * this.Refresh();*/
			misFirst = false;
			//FIXME 因为地图“图片尺寸”变更了，所以全部图层都要重新绘制
			mCount++;
			Log.i("MapControl.onLayout",String.valueOf(mCount));
			StopDraw();
			Refresh();
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/*Log.i("MapControl","onMeasure");*/
	}


	protected void onDraw(Canvas canvas){
		try{
			super.onDraw(canvas);
		}catch (Exception e) {
			Log.i("RECYCLE","MyIamgeView -> onDraw() Canvas:trying to use a recycled bitmap");
		}
		try {
			/*Log.i("onDraw", "重画屏幕");*/
			if(mZoomPan!=null&&((ZoomPan)mZoomPan).isMAGNIFY()){
				super.onDraw(canvas);
				((ZoomPan)mZoomPan).drawMagnify(canvas);
				return;
			}

			/*FIXME 貌似没有用到
			 * if(IsDrawTrack){
				if(mBitScreen==null){
					mBitScreen=mActiveView.FocusMap().ExportMap(false).copy(Config.RGB_565, true);
					Log.d("mBitScreen", mBitScreen+"");
				}
				canvas.drawBitmap(mBitScreen, 0,0, mPaint);
				IsDrawTrack=false;
				if(mBitScreen!=null&&!mBitScreen.isRecycled()){
					mBitScreen=null;
					Log.d("mBitScreen", mBitScreen+"");
				}
			}*/

			/*int d = (int) Math.round((this.getMap().getScale()*densityDpi/60*100)/100);
			this.mTVRules.setText("1:"+String.valueOf(d));*/

		} catch (Exception e1) {
			Log.e("mBitScreen", "MapControl onDraw :451    特殊异常");
			/*System.out.println("终于抓到你了！"); 
			e1.printStackTrace();*/
		}
	}


}
