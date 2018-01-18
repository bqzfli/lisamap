package srs.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lisa.map.app.R;

import srs.DataSource.Vector.SearchType;
import srs.Display.IScreenDisplay;
import srs.Element.FillElement;
import srs.Element.IElement;
import srs.Element.IFillElement;
import srs.Element.IPicElement;
import srs.Element.PicElement;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.srsGeometryType;
import srs.Layer.IDBLayer;
import srs.tools.Event.MultipleItemChangedListener;

/**
 * @ClassName: TouchLongToolMultipleDB
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月26日 下午4:30:42
 ***********************************
 * @editor lisa
 * @data 2016年12月26日 下午4:30:42
 * @todo TODO
 */

public class TouchLongToolMultipleDB extends BaseTool {

    private PointF mCurrentPoint = null;
    /*private IPoint mPFT = null;*/
    private IScreenDisplay mScreenDisplay;
    private IDBLayer mLayer;
    /* private List<Integer> mAllfids; */
    private Paint mPaint;
    public static int currentIndex;
    /**选中的要素高亮显示时使用的
     *
     */

    private List<IElement> fillElements = null;
    private List<Integer> indexs = new ArrayList<Integer>();
    private List<IGeometry> geos = new ArrayList<IGeometry>();
    /**
     * 点选时间控制，手指在屏幕上停留最少多久，视为点选操作 可根据业务需要，手动设置此项属性
     * 此属性也可通过系统配置设置，系统设置修改时，调用此属性进行修改
     */
	/*private static long LONGTIME = 10;*/
    /**
     * 点选距离控制，触摸点与目标点最远相聚多少时，可视为选中 可根据业务需要，手动设置此项属性
     * 此属性也可通过系统配置设置，系统设置修改时，调用此属性进行修改
     */
    public static float DIS_DEFAULT = -1;
    private static float Dis = 10;
    /**
     * 长点击选择的范围，默认为60
     */
    public static float DisLongTouch = 60;
    private static String mTitleStr = "详细信息";
    /**
     * MapControl输出的底图
     *
     */
    Bitmap mBitExMap = null;

    /**
     * 在此添加选中目标后会触发的动作；
     *
     */
    public MultipleItemChangedListener zoom2Selected = null;

    /**
     * 判断定位辅助变量：记录“ACTION_DOWN”时手指的屏幕位置
     *
     */
	/*private int xposition = 0;
	private int yposition = 0;*/

    /**
     * 手指触摸屏幕的时刻
     *
     */
    private long mExtTime = -1;

    /**
     * 是否为选择单条记录
     *
     */
    public boolean IsOnlyOneSelect = true;

    /**
     * 是否为一次性选择
     *
     */
    public boolean IsOnlyOneTime = true;

    /**清空选中状态
     * @throws IOException
     */
    public void ClearSelect() throws IOException{
        mBuddyControl.getElementContainer().ClearElement();
        indexs.clear();
        geos.clear();
    }

    public TouchLongToolMultipleDB(IDBLayer TargetLayer) {
        zoom2Selected = null;
        mLayer = TargetLayer;
        indexs.clear();
        geos.clear();
        mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);

        super.setRate();
        if (mLayer != null) {
            if(DIS_DEFAULT>-1){
                Dis = DIS_DEFAULT;
            }else if (mLayer.getDBSourceManager().getGeoType() == srsGeometryType.Point) {
                Dis = 30; // 若目标类型为“点”，则缓冲距离为30
            } else {
                Dis = 10; // 若目标类型为“线、面”缓冲距离为10
            }
        }
    }

    @Override
    public String getText() {
        return mTitleStr;
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    public BaseControl getBuddyControl() {
        return this.mBuddyControl;
    }

    public void setBuddyControl(BaseControl value) {
        if (value != null) {
            this.mBuddyControl = value;
			/*mBuddyControl.getActiveView().FocusMap().getSelectionSet()
			.ClearSelection();*/
        }
    }

    @Override
    public void onClick(View v) {
        try {
            super.onClick(v);
            if (this.mBuddyControl instanceof BaseControl) {
                mScreenDisplay = this.mBuddyControl.getActiveView().FocusMap()
                        .getScreenDisplay();
            }
            mEnable = true;
            mBuddyControl.setDrawTool(this);
            mBuddyControl.PartialRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see srs.tools.BaseTool#onTouch(android.view.View, android.view.MotionEvent)
     */
	/* (non-Javadoc)
	 * @see srs.tools.BaseTool#onTouch(android.view.View, android.view.MotionEvent)
	 */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBitExMap = mBuddyControl.getActiveView().FocusMap()
                        .ExportMap(false);
                if (mExtTime == -1) {
                    mExtTime = System.currentTimeMillis();
                    mCurrentPoint = new PointF(event.getX() * mRate, event.getY()
                            * mRate);
                }
			/*xposition = (int) event.getX();
			yposition = (int) event.getY();*/
                break;
            case MotionEvent.ACTION_UP:
                try {
                    if (event.getPointerCount() == 1
						/* mExtTime!=-1&&System.currentTimeMillis()-mExtTime>LONGTIME
						 * &&Math.abs(event.getX()-xposition)<5
						 * &&Math.abs(event.getY()-yposition)<5
						 */) {
                        // 长按
                        super.onTouch(v, event);
                        mExtTime = -1;
                        PointF pF = new PointF(event.getX() * mRate, event.getY()
                                * mRate);
                        IPoint selGeo = mBuddyControl.ToWorldPoint(new PointF(pF.x,
                                pF.y));


                        if (mCurrentPoint != null
                                && Math.abs(mCurrentPoint.x - pF.x) < DisLongTouch
                                && Math.abs(mCurrentPoint.y - pF.y) < DisLongTouch) {

                            // 在一个点位，此时才算“长点击”
                            List<Integer> sels = mLayer.getDBSourceManager().selectOnly(
                                    selGeo,
                                    SearchType.Intersect,
                                    mScreenDisplay.ToMapDistance(Dis),
                                    null);
                            IFillElement SELECTEDElement = null;
                            Integer index = -1;
                            if (sels != null && sels.size() > 0) {
                                mExtTime = -1;
                                IGeometry geo  = null;
                                if (IsOnlyOneTime) {
                                    geos.clear();
                                    indexs.clear();
                                }
                                for(int i=0;i<sels.size();i++) {
                                    index = sels.get(i);
                                    // 查询图层中选中点所对应的geo
                                    geo = mLayer.getDBSourceManager().getGeoByIndex(index);
                                    if (indexs != null && indexs.size() > 0) {
                                        if (indexs.contains(sels.get(i))) {
                                            indexs.remove(sels.get(i));
                                            geos.remove(geo);
                                        } else {
                                            indexs.add(sels.get(i));
                                            geos.add(geo);
                                        }
                                    } else {
                                        indexs.add(sels.get(i));
                                        geos.add(geo);
                                    }
                                    if(IsOnlyOneSelect)
                                        break;//如果仅仅选择一个，则直接跳出
                                }
                                currentIndex = index;

                                mBuddyControl.getElementContainer().ClearElement();
                                if(geo!=null&&geo instanceof IPolygon) {
                                    for (int i = 0; i < indexs.size(); i++) {
                                        IFillElement fillElement = new FillElement();
                                        fillElement.setGeometry(geos.get(i));
                                        fillElement.setSymbol(srs.Display.Setting.SelectPolygonFeatureStyle);
                                        mBuddyControl.getElementContainer().AddElement(fillElement);
                                    }
                                }else if(geo!=null&&geo instanceof IPoint){
                                    Bitmap bit = BitmapFactory.decodeResource(
                                            mBuddyControl.getContext().getResources(),R.drawable.target);
                                    for (int i = 0; i < indexs.size(); i++) {
                                        IPicElement picElement = new PicElement();
                                        picElement.setGeometry(geos.get(i));
                                        picElement.setPic(bit,
                                                -bit.getWidth()/2,
                                                -bit.getHeight());
                                        mBuddyControl.getElementContainer().AddElement(picElement);
                                    }
                                }
                                if (zoom2Selected != null) {
                                    zoom2Selected.doEventSettingsChanged(indexs);
                                }
                                mBuddyControl.PartialRefresh();
                                /**
                                 * 搜索成功后不需要平移地图，返回true，销毁操作事件
                                 */
                                return true;
                            } /*else {
                                *//** 设置地图为未选中任何对象
                                 *//*
                                if(SELECTEDElement!=null
                                        &&mBuddyControl.getElementContainer().getElements().contains(SELECTEDElement)){
                                    mBuddyControl.getElementContainer().RemoveElementsE(fillElements);
                                }
                                mBuddyControl.PartialRefresh();
                            }*/
                        }
					    /*修改 20150820  李忠义
					    * 为了让地图能刷新，啥时候都要返回false*/
                        return false;
                    }
                } catch (Exception e) {
                    Log.e("MAP","点选DB中的目标，action_up:"+e.getMessage());
                    e.printStackTrace();
                }
                mExtTime = -1;
                mCurrentPoint = null;
                break;
        }
        return false;
    }


    public void dispose(){
        if(fillElements!=null) {
            fillElements.clear();
            fillElements = null;
        }
        if(geos!=null){
            geos.clear();
            geos = null;
        }
        if(indexs!=null){
            indexs.clear();
            indexs = null;
        }
        mCurrentPoint = null;
        mPaint = null;
        mScreenDisplay = null;
        zoom2Selected = null;

        mBuddyControl = null;
        mBitExMap = null;
        mLayer = null;
    }
}
