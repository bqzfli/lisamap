package srs.Layer;

import java.text.DecimalFormat;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;
import srs.DataSource.DB.DBSourceManager;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.ELABELPOSITION;
import srs.Element.ITextElement;
import srs.Element.TextElement;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.Point;
import srs.Utility.sRSException;

/**
 * @ClassName: LabelDB
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Version: V1.0.0.0
 * @author lisa
 * @date 2016年12月29日 下午5:48:39
 ***********************************
 * @editor lisa 
 * @data 2016年12月29日 下午5:48:39
 * @todo TODO
 */
public class LabelDB extends Label {


	public void DrawLabel(DBSourceManager dbManager, 
			Bitmap canvas, 
			IEnvelope extent,
			List<Integer> sels,
			FromMapPointDelegate Delegate) throws sRSException{
		try{
			if(sels==null||sels.size()==0){
				return;
			}


			drawElements(canvas, sels, dbManager, Delegate);			
			Log.i("LEVEL-ROW-COLUMN", "在绘制Label个数"+ String.valueOf(sels.size()));
		}catch (java.lang.Exception e){
			Log.i("LEVEL-ROW-COLUMN", "在绘制Label过程中发生不可预知的错误.");
			throw new sRSException("在绘制Label过程中发生不可预知的错误.");
		}
	}

	/**若标注需要进行乘、除法运算，请设置倍数
	 * @param power
	 */
	public void setPower(double power){
		this.mPower = power;
	}

	/**若标注为浮点型，设置十进制样式
	 * @param decimalFormat
	 */
	public void setDecimalFormat(DecimalFormat decimalFormat){
		mDecimalFormat = decimalFormat;
	}


	/**绘制Element
	 * @param canvas 画布
	 * @param sels 要绘制的要素序号
	 * @param dbManager 数据
	 * @param Delegate 坐标点转换的代理
	 * @throws Exception
	 */
	private void drawElements(Bitmap canvas, 
			List<Integer> sels,
			DBSourceManager dbManager, 
			FromMapPointDelegate Delegate) throws Exception{
		for (int i = 0; i < sels.size(); i++){

			ITextElement element = new TextElement();
			element.setGeometry(setGeoPostion(
					dbManager.getGeoByIndex(sels.get(i)), 
					mLabelSymbol.getPosition()));
			element.setSymbol(mLabelSymbol);
			element.setScaleText(false);
			String labelValue = dbManager.getLabelValues().get(sels.get(i));
			if(mPower!=0&&mDecimalFormat!=null){
				double value = Double.valueOf(labelValue);
				value *= mPower;
				labelValue = mDecimalFormat.format(value); 
			}
			element.setText(labelValue);
			element.Draw(canvas, Delegate);		
		}
	}

	private IPoint setGeoPostion(IGeometry geo,ELABELPOSITION labelposition){
		IPoint point = geo.CenterPoint();
		switch (labelposition) {
		case CENTER:
			point = geo.CenterPoint();
			break;
		case LEFT_CENTER:
			point = new Point(geo.Extent().XMin(),point.Y());
			break;
		case RIGHT_CENTER:
			point = new Point(geo.Extent().XMax(),point.Y());
			break;
		case TOP_CENTER:
			point = new Point(point.X(),geo.Extent().YMax());
			break;
		case BOTTOM_CENTER:
			point = new Point(point.X(),geo.Extent().YMin());
			break;
		case BOTTOM_LEFT:
			point = geo.Extent().LowerLeft();
			break;
		case BOTTOM_RIGHT:
			point = geo.Extent().LowerRight();
			break;
		case TOP_LEFT:
			point = geo.Extent().UpperLeft();
			break;
		case TOP_RIGHT:
			point = geo.Extent().UpperRight();
			break;
		default:
			break;
		}
		return point;
	}
}
