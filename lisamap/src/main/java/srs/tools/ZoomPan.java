package srs.tools;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import srs.Geometry.Envelope;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Map.IActiveView;
import srs.Map.IMap;
import srs.Utility.Log;

public class ZoomPan extends BaseTool implements ITool {
	private PointF pointOld1;
	private PointF pointOld2;
	private IPoint pointMidMap1;
	private IPoint pointOldMap1;
	private IPoint pointOldMap2;
	private PointF pointMid1Pic;
	private double dis1;
	/*private double disMap1;*/
	private Bitmap mBitmapCurrent;
	private IMap map;

	private long mStartTime;
	private long mEndTime;

	public static final int NONE=0;
	public static final int DRAG=1;
	public static final int ZOOM=2;
	/*放大镜
	 * public static final int MAGNIFY = 3;*/
	/**
	 * 下次摸我前不做设置
	 */
	public static final int NEVER = 999;
	/*int mode = NONE;*/
	/*private static long LONGTIME = 500;*/


	/**放大镜处图片
	 * 
	 */
	public Bitmap mBitmapShaderView = null;
	/**放大镜处绘图资源
	 * 
	 */
	public ShapeDrawable mDrawableShape = null;
	/**放大镜着色器
	 *  
	 */
	private BitmapShader mShader = null;

	/**手指触摸屏幕的时间
	 * 
	 */
	private long mDownTime = -1;

	public void dispose() throws Exception{
		pointOld1 = null;
		pointOld2 = null;
		pointMid1Pic = null;
		if(pointMidMap1!=null){
			pointMidMap1.dispose(); pointMidMap1= null;
		}
		if(pointOldMap1!=null){
			pointOldMap1.dispose();pointOldMap1 = null;
		}
		if(pointOldMap2!=null){
			pointOldMap2.dispose();pointOldMap2 = null;
		}
		map = null;
		mDrawableShape = null;
		mShader = null;
		if(mBitmapCurrent!=null){
			mBitmapCurrent.recycle();mBitmapCurrent = null;
		}
		if(mBitmapShaderView!=null){
			mBitmapShaderView.recycle(); mBitmapShaderView= null;
		}
		mBuddyControl = null;
	}


	/**是否为放大镜模式
	 * @return
	 */
	public boolean isMAGNIFY(){
		/*return ((MapControl)mBuddyControl).MODE == MAGNIFY;*/
		return false;
	}

	public ZoomPan(){
		super.setRate();
	}

	@Override
	public String getText() {
		return "放大";
	}


	@Override
	public Bitmap getBitmap() {
		//需要重写	
		return null;
	}

	@Override
	public BaseControl getBuddyControl() {
		return mBuddyControl;
	}


	/* 清空绘图控件中的缓存
	 * @see tools.ITool#DrawAgain()
	 */
	public void DrawAgain() {
		mBitmapCurrent=null;
	}

	@Override
	public void setBuddyControl(BaseControl basecontrol) {
		mBuddyControl=basecontrol;
		if(mBuddyControl!=null){
			super.mEnable=true;
		}
		else{
			super.mEnable=false;
		}
	}	

	@SuppressWarnings("deprecation")
	public boolean onTouch(View v, MotionEvent event) {
		try{
			IEnvelope enC;
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(mBitmapCurrent!=null&&!mBitmapCurrent.isRecycled()){
					/**
					 *  部分定制版本的系统必须增加此函数，否则会导致崩溃
					 * (v).setBackgroundDrawable(null);
					 * 增加此方法后，一定会造成平移后，第一次触屏时的白色闪烁
					 */
					Log.i("RECYCLE",
							"RECYCLE mBitmapCurrent（MapControl的BackGround）"+mBitmapCurrent);
					mBitmapCurrent.recycle();
					mBitmapCurrent = null;
				}
				((MapControl)mBuddyControl).StopDraw();
				if(event.getPointerCount()==1){
					if(map==null){
						IActiveView av=this.mBuddyControl.getActiveView();
						map=av.FocusMap();
					}				
					pointOld1 = new PointF(event.getX()*mRate,event.getY()*mRate);
					pointOldMap1 = map.ToMapPoint(pointOld1);
					((MapControl)mBuddyControl).MODE = NONE;

					this.mStartTime = System.currentTimeMillis();
					this.mDownTime = this.mStartTime;
				}
				break;
			case MotionEvent.ACTION_POINTER_2_DOWN:
				if(event.getPointerCount()==2){
					Log.i("MotionEvent",
							"MotionEvent.ACTION_POINTER_2_DOWN");
					if(map==null){
						map=this.mBuddyControl.getActiveView().FocusMap();
					}
					pointOld1 = new PointF(event.getX(0)*mRate,event.getY(0)*mRate);
					pointOld2 = new PointF(event.getX(1)*mRate,event.getY(1)*mRate);
					dis1 = Math.sqrt((pointOld1.x-pointOld2.x)*(pointOld1.x-pointOld2.x)
							+(pointOld1.y-pointOld2.y)*(pointOld1.y-pointOld2.y));
					pointMid1Pic = new PointF((pointOld1.x+pointOld2.x)/2,(pointOld1.y+pointOld2.y)/2);
					/*IEnvelope eC1=map.getExtent();*/

					pointOldMap1 = map.ToMapPoint(new PointF(event.getX(0)*mRate,event.getY(0)*mRate));
					pointOldMap2 = map.ToMapPoint(new PointF(event.getX(1)*mRate,event.getY(1)*mRate));

					/*disMap1=Math.sqrt((pointOldMap1.X()-pointOldMap2.X())*(pointOldMap1.X()-pointOldMap2.X())
						+(pointOldMap1.Y()-pointOldMap2.Y())*(pointOldMap1.Y()-pointOldMap2.Y()));*/
					//pointMid1为地图上的两点中点
					pointMidMap1 = new Point((pointOldMap1.X()+pointOldMap2.X())/2,(pointOldMap1.Y()+pointOldMap2.Y())/2);
					((MapControl)mBuddyControl).MODE  = ZOOM;

					this.mStartTime = System.currentTimeMillis();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(mBitmapCurrent == null){
					mBitmapCurrent = Bitmap.createBitmap(map.ExportMap(false).getWidth(), map.ExportMap(false).getHeight(), Config.RGB_565);				
					//				mBitmapCurrent = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Config.RGB_565);
					Log.i("RECYCLE","Create mBitmapCurrent"+mBitmapCurrent);					
				}
				if(map == null){
					map = this.mBuddyControl.getActiveView().FocusMap();
				}
				Canvas g = new Canvas(mBitmapCurrent);
				this.mEndTime = System.currentTimeMillis();
				if(((MapControl)mBuddyControl).MODE  == NONE
						/*&&mEndTime - mStartTime >= SETTINGSMAGNIFY.TIMEMAGNIFYMIN*/
						&&event.getPointerCount() == 1/*
						&&Math.abs(pointOld1.x-event.getX())*mRate >= SETTINGSMAGNIFY.DISSELECT
						&&Math.abs(pointOld1.y-event.getY())*mRate >= SETTINGSMAGNIFY.DISSELECT*/){
					((MapControl)mBuddyControl).MODE  = DRAG;
				}/*放大镜模式
				else if(((MapControl)mBuddyControl).MODE  == MAGNIFY){
					SETTINGSMAGNIFY.X = (int) event.getX();
					SETTINGSMAGNIFY.Y = (int) event.getY();
					 必须先切割然后再创建shader
				 * edit by bqzf 
				 * 20150620 
					int dBitmapShaderView = SETTINGSMAGNIFY.getRADIUS()*2/SETTINGSMAGNIFY.FACTOR;
					int xBitmapShaderView = -SETTINGSMAGNIFY.getRADIUS()/SETTINGSMAGNIFY.FACTOR + SETTINGSMAGNIFY.X;				
					SETTINGSMAGNIFY.X  = xBitmapShaderView<0?0:
						((xBitmapShaderView+dBitmapShaderView<map.ExportMap(false).getWidth())?xBitmapShaderView
								:(map.ExportMap(false).getWidth()-dBitmapShaderView));

					int yBitmapShaderView = -SETTINGSMAGNIFY.getRADIUS()/SETTINGSMAGNIFY.FACTOR + SETTINGSMAGNIFY.Y;
					SETTINGSMAGNIFY.Y = yBitmapShaderView<0?0:
						(yBitmapShaderView+dBitmapShaderView<map.ExportMap(false).getHeight()?yBitmapShaderView
								:(map.ExportMap(false).getHeight()-dBitmapShaderView));
					mBitmapShaderView = Bitmap.createBitmap(
							map.ExportMap(false),
							SETTINGSMAGNIFY.X,
							SETTINGSMAGNIFY.Y,
							dBitmapShaderView,
							dBitmapShaderView);
					mShader = new BitmapShader(Bitmap.createScaledBitmap(mBitmapShaderView, 
							mBitmapShaderView.getWidth()*SETTINGSMAGNIFY.FACTOR,
							mBitmapShaderView.getHeight()*SETTINGSMAGNIFY.FACTOR, true), 
							TileMode.CLAMP, 
							TileMode.CLAMP);
					//这个位置表示的是，画shader的起始位置,画在手指左上角
						SETTINGSMAGNIFY.MATRIXSHAPE.setTranslate(SETTINGSMAGNIFY.getRADIUS()-SETTINGSMAGNIFY.X*SETTINGSMAGNIFY.FACTOR,
						SETTINGSMAGNIFY.getRADIUS()-SETTINGSMAGNIFY.Y*SETTINGSMAGNIFY.FACTOR);
					mDrawableShape.getPaint().setShader(mShader);
					mDrawableShape.getPaint().getShader().setLocalMatrix(SETTINGSMAGNIFY.MATRIXSHAPE);
					//bounds，就是那个圆的外切矩形
					xBitmapShaderView = SETTINGSMAGNIFY.X - SETTINGSMAGNIFY.getRADIUS()*2;
					xBitmapShaderView = xBitmapShaderView>0.0?xBitmapShaderView:0;
					yBitmapShaderView = SETTINGSMAGNIFY.Y - SETTINGSMAGNIFY.getRADIUS()*2;
					yBitmapShaderView = yBitmapShaderView>0.0?yBitmapShaderView:0;
					mDrawableShape.setBounds(
							xBitmapShaderView,
							yBitmapShaderView,
							xBitmapShaderView + SETTINGSMAGNIFY.getRADIUS()*2,
							yBitmapShaderView + SETTINGSMAGNIFY.getRADIUS()*2);
					v.postInvalidate();
				}*/else if(mEndTime - mStartTime>10){
					if(((MapControl)mBuddyControl).MODE ==ZOOM){
						//缩放前原地图输出的图片
						Bitmap bitExMap = map.ExportMap(true);
						if(bitExMap!=null&&!bitExMap.isRecycled()){
							Log.i("MotionEvent",
									"MotionEvent.ACTION_MOVE 重置 缩放 时的缓存画面");
						}
						PointF p1Pic=new PointF(event.getX(0)*mRate,event.getY(0)*mRate);
						PointF p2Pic=new PointF(event.getX(1)*mRate,event.getY(1)*mRate);
						double dis2=Math.sqrt((p1Pic.x-p2Pic.x)*(p1Pic.x-p2Pic.x)+(p1Pic.y-p2Pic.y)*(p1Pic.y-p2Pic.y));
						double ratePic = dis1/dis2;	 			
						android.graphics.Point pCPic1=new android.graphics.Point(bitExMap.getWidth()/2,bitExMap.getHeight()/2);
						int xMove=(int)((pCPic1.x-pointMid1Pic.x)*((dis2-dis1)/dis1));
						int yMove=(int)((pCPic1.y-pointMid1Pic.y)*((dis2-dis1)/dis1));
						android.graphics.Point pCPicMoved=new android.graphics.Point(pCPic1.x+xMove,pCPic1.y+yMove);	
						float wPic=(float)(bitExMap.getWidth()/ratePic);
						float hPic=(float)(bitExMap.getHeight()/ratePic);
						float xdrawLPic=(float)(pCPicMoved.x-wPic/2);
						float ydrawTPic=(float)(pCPicMoved.y-hPic/2);		
						g.drawColor(Color.WHITE);	
						g.drawBitmap(bitExMap, 
								new Rect(0,0,bitExMap.getWidth(),bitExMap.getHeight()), 
								new RectF(xdrawLPic,ydrawTPic,xdrawLPic+wPic,ydrawTPic+hPic), 
								new Paint());
						BitmapDrawable bd= new BitmapDrawable(v.getContext().getResources(), mBitmapCurrent); 
						((MapControl)v).setBackgroundDrawable(bd);
					}
					if(((MapControl)mBuddyControl).MODE ==DRAG){
						Bitmap bitExMap = map.ExportMap(true);
						if(bitExMap!=null&&!bitExMap.isRecycled()){
							Log.i("MotionEvent",
									"MotionEvent.ACTION_MOVE 重置 平移 时的缓存画面");
						}
						g.drawColor(Color.WHITE);
						g.drawBitmap(map.ExportMap(false), 
								event.getX()*mRate - pointOld1.x, 
								event.getY()*mRate - pointOld1.y,
								null);
						BitmapDrawable bd= new BitmapDrawable(v.getContext().getResources(), mBitmapCurrent); 
						((MapControl)v).setBackgroundDrawable(bd);					
					}
					/*屏幕随手指移动不需要控制时间间隔，比较流畅
					 * this.mStartTime = System.currentTimeMillis();*/
				}
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
			case MotionEvent.ACTION_POINTER_2_UP:
				if(map==null){
					map=this.mBuddyControl.getActiveView().FocusMap();
				}
				if(((MapControl)mBuddyControl).MODE ==ZOOM){	
					PointF p1Pic=new PointF(event.getX(0)*mRate,event.getY(0)*mRate);
					PointF p2Pic=new PointF(event.getX(1)*mRate,event.getY(1)*mRate);
					double dis2=Math.sqrt((p1Pic.x-p2Pic.x)*(p1Pic.x-p2Pic.x)+(p1Pic.y-p2Pic.y)*(p1Pic.y-p2Pic.y));
					double ratePic=dis1/dis2;

					/*IPoint p1Map=map.ToMapPoint(new PointF(event.getX(0)*mRate,event.getY(0)*mRate));
				IPoint p2Map=map.ToMapPoint(new PointF(event.getX(1)*mRate,event.getY(1)*mRate));
				double disMap2=Math.sqrt((p1Map.X()-p2Map.X())*(p1Map.X()-p2Map.X())+(p1Map.Y()-p2Map.Y())*(p1Map.Y()-p2Map.Y()));
				double ratMap=disMap1/disMap2;			*/			
					//			
					IPoint pLL=map.getExtent().LowerLeft();
					IPoint pRT=map.getExtent().UpperRight();
					double Left=ratePic*(pLL.X()-pointMidMap1.X())+pointMidMap1.X();
					double R=ratePic*(pRT.X()-pointMidMap1.X())+pointMidMap1.X();
					double T=ratePic*(pRT.Y()-pointMidMap1.Y())+pointMidMap1.Y();
					double Low=ratePic*(pLL.Y()-pointMidMap1.Y())+pointMidMap1.Y();
					enC=new Envelope(Left,Low,R,T);

					map.setExtent(enC);
					mBuddyControl.Refresh();
					((MapControl)mBuddyControl).MODE =NEVER;
				}			
				break;
			case MotionEvent.ACTION_UP:
				this.mEndTime = System.currentTimeMillis();
				if(mEndTime - mDownTime<60){
					break;
				}
				if(map==null){
					map=this.mBuddyControl.getActiveView().FocusMap();
				}
				if(((MapControl)mBuddyControl).MODE ==DRAG){
					IPoint currentPointMap=map.ToMapPoint(new PointF(event.getX()*mRate,event.getY()*mRate));
					enC=(IEnvelope)(map.getExtent().Clone());
					enC.Move(this.pointOldMap1.X()-currentPointMap.X(), 
							pointOldMap1.Y()-currentPointMap.Y());
					map.setExtent(enC);
					//				mBuddyControl.getActiveView().FocusMap().ScreenDisplay().ResetCaches(mBuddyControl.getBitmap());
					mBuddyControl.Refresh();
					((MapControl)mBuddyControl).MODE =NONE; 
				}
				/*放大镜模式关闭
				 * if(((MapControl)mBuddyControl).MODE ==MAGNIFY){
					Bitmap bitExMap=map.ExportMap(false);
				BitmapDrawable bd= new BitmapDrawable(v.getContext().getResources(), bitExMap); 
				((MapControl)v).setBackgroundDrawable(bd);
					try {
						mBuddyControl.PartialRefresh();
					} catch (Exception e) {
						e.printStackTrace();
					}
					((MapControl)mBuddyControl).MODE =NONE;
				}*/
				break;
			}	

		}catch (Exception e) {
			Toast.makeText(mBuddyControl.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return true;
	}

	/**放大镜回调时使用此函数
	 * @param canvas
	 */
	public void drawMagnify(Canvas canvas){
		/*canvas.drawBitmap(mBitmapShaderView, 0, 0, null);*/
		mDrawableShape.draw(canvas);	
		if(SETTINGSMAGNIFY.getFDJDSZ()!=null){
			float x = SETTINGSMAGNIFY.X-2*SETTINGSMAGNIFY.getRADIUS();
			x = x<0?0:x;
			float y = SETTINGSMAGNIFY.Y-2*SETTINGSMAGNIFY.getRADIUS();
			y = y<0?0:y;
			canvas.drawBitmap(SETTINGSMAGNIFY.getFDJDSZ(),x,y,null);
		}
	}

}


